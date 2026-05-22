<template>
  <div class="app-container">
    <el-card class="policy-card" shadow="never">
      <div slot="header" class="policy-header">
        <span>注册与安全策略</span>
        <el-button type="text" @click="refreshPolicies">刷新</el-button>
      </div>
      <el-form label-width="130px" size="small">
        <el-form-item label="学生自主注册">
          <el-switch
            v-model="registerEnabled"
            active-text="开放注册"
            inactive-text="关闭注册"
            @change="handleRegisterToggle"
          />
        </el-form-item>
        <el-form-item label="登录 IP 黑名单">
          <el-input
            v-model="securitySettings.blackIpList"
            type="textarea"
            :rows="3"
            placeholder="支持单个 IP、* 通配符和 IP 段，多个条目可用逗号或换行分隔"
          />
        </el-form-item>
        <el-form-item label="终端关键字黑名单">
          <el-input
            v-model="securitySettings.blockedUserAgentKeywords"
            type="textarea"
            :rows="3"
            placeholder="输入浏览器、系统或设备关键字，例如 Android、iPhone、MicroMessenger"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="mini" @click="handleSaveSecuritySettings">保存安全策略</el-button>
          <span class="policy-tip">密码错误锁定仍沿用系统配置：连续输错 5 次后锁定 10 分钟。</span>
        </el-form-item>
      </el-form>
    </el-card>

    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="账号" prop="userName">
        <el-input v-model="queryParams.userName" placeholder="请输入账号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="学号" prop="studentNo">
        <el-input v-model="queryParams.studentNo" placeholder="请输入学号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-upload2" size="mini" @click="handleImport">导入名单</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain size="mini" :disabled="multiple" @click="handleBatchEnable">批量启用</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain size="mini" :disabled="multiple" @click="handleBatchDisable">批量停用</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" plain size="mini" :disabled="multiple" @click="handleBatchResetPwd">批量重置密码</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain size="mini" :disabled="multiple" @click="handleBatchDelete">批量删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="studentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="账号" prop="userName" />
      <el-table-column label="学号" prop="studentNo" />
      <el-table-column label="昵称" prop="nickName" />
      <el-table-column label="邮箱" prop="email" />
      <el-table-column label="状态" prop="status">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="0"
            inactive-value="1"
            @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime" width="170">
        <template slot-scope="scope">{{ parseTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="220" align="center">
        <template slot-scope="scope">
          <el-button type="text" size="mini" @click="handleResetPwd(scope.row)">重置密码</el-button>
          <el-button type="text" size="mini" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <excel-import-dialog
      ref="importStudentRef"
      title="学生名单导入"
      action="/account/students/importData"
      template-action="/account/students/importTemplate"
      template-file-name="student_template"
      update-support-label="是否更新已经存在的学生数据"
      extra-support-label="导入后停用名单外学生"
      extra-support-param="disableMissing"
      @success="handleImportSuccess"
    />
  </div>
</template>

<script>
import {
  listStudents,
  resetStudentPassword,
  resetStudentPasswords,
  changeStudentStatus,
  changeStudentStatuses,
  deleteStudents,
  getRegisterEnabled,
  setRegisterEnabled,
  getSecuritySettings,
  updateSecuritySettings
} from "@/api/account"
import ExcelImportDialog from "@/components/ExcelImportDialog"

export default {
  name: "StudentAccount",
  dicts: ["sys_normal_disable"],
  components: { ExcelImportDialog },
  data() {
    return {
      loading: false,
      showSearch: true,
      total: 0,
      studentList: [],
      ids: [],
      selectedRows: [],
      registerEnabled: false,
      securitySettings: {
        blackIpList: "",
        blockedUserAgentKeywords: ""
      },
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        studentNo: undefined,
        status: undefined
      }
    }
  },
  computed: {
    multiple() {
      return this.ids.length === 0
    }
  },
  created() {
    this.getList()
    this.refreshPolicies()
  },
  methods: {
    getList() {
      this.loading = true
      listStudents(this.queryParams).then(res => {
        this.studentList = res.rows || []
        this.total = res.total || 0
      }).finally(() => {
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.selectedRows = selection
      this.ids = selection.map(item => item.userId)
    },
    handleResetPwd(row) {
      this.$prompt(`请输入 ${row.userName} 的新密码`, "重置密码", { closeOnClickModal: false }).then(({ value }) => {
        resetStudentPassword(row.userId, value).then(() => {
          this.$modal.msgSuccess("重置成功")
        })
      }).catch(() => {})
    },
    handleStatusChange(row) {
      const text = row.status === "0" ? "启用" : "停用"
      this.$modal.confirm(`确认要${text}用户 ${row.userName} 吗？`).then(() => {
        return changeStudentStatus(row.userId, row.status)
      }).then(() => {
        this.$modal.msgSuccess(text + "成功")
      }).catch(() => {
        row.status = row.status === "0" ? "1" : "0"
      })
    },
    handleDelete(row) {
      const userIds = row.userId ? [row.userId] : this.ids
      const studentText = row.studentNo || "所选学生"
      this.$modal.confirm(`确认删除 ${studentText} 吗？`).then(() => deleteStudents(userIds)).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleBatchResetPwd() {
      this.$prompt(`请输入所选 ${this.ids.length} 个学生的新密码`, "批量重置密码", { closeOnClickModal: false }).then(({ value }) => {
        resetStudentPasswords(this.ids, value).then(() => {
          this.$modal.msgSuccess("批量重置成功")
        })
      }).catch(() => {})
    },
    handleBatchEnable() {
      this.$modal.confirm(`确认批量启用所选 ${this.ids.length} 个学生吗？`).then(() => {
        return changeStudentStatuses(this.ids, "0")
      }).then(() => {
        this.$modal.msgSuccess("批量启用成功")
        this.getList()
      }).catch(() => {})
    },
    handleBatchDisable() {
      this.$modal.confirm(`确认批量停用所选 ${this.ids.length} 个学生吗？`).then(() => {
        return changeStudentStatuses(this.ids, "1")
      }).then(() => {
        this.$modal.msgSuccess("批量停用成功")
        this.getList()
      }).catch(() => {})
    },
    handleBatchDelete() {
      this.$modal.confirm(`确认删除所选 ${this.ids.length} 个学生吗？`).then(() => deleteStudents(this.ids)).then(() => {
        this.getList()
        this.$modal.msgSuccess("批量删除成功")
      }).catch(() => {})
    },
    handleImport() {
      this.$refs.importStudentRef.open()
    },
    handleImportSuccess() {
      this.getList()
    },
    refreshPolicies() {
      getRegisterEnabled().then(res => {
        this.registerEnabled = !!res.data
      })
      getSecuritySettings().then(res => {
        this.securitySettings = res.data || {
          blackIpList: "",
          blockedUserAgentKeywords: ""
        }
      })
    },
    handleRegisterToggle(val) {
      setRegisterEnabled(val ? "0" : "1").then(() => {
        this.$modal.msgSuccess(val ? "已开放注册" : "已关闭注册")
      }).catch(() => {
        this.registerEnabled = !val
      })
    },
    handleSaveSecuritySettings() {
      updateSecuritySettings(this.securitySettings).then(() => {
        this.$modal.msgSuccess("安全策略已保存")
      })
    }
  }
}
</script>

<style scoped lang="scss">
.policy-card {
  margin-bottom: 16px;
}

.policy-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.policy-tip {
  margin-left: 12px;
  color: #909399;
  font-size: 12px;
}
</style>
