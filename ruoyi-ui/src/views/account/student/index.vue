<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="账号" prop="userName">
        <el-input v-model="queryParams.userName" placeholder="请输入账号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="学号" prop="studentNo">
        <el-input v-model="queryParams.studentNo" placeholder="请输入学号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="warning" plain size="mini" @click="refreshRegisterEnabled">刷新注册开关</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-switch v-model="registerEnabled" active-text="开放注册" inactive-text="关闭注册" @change="handleRegisterToggle" />
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
          <el-switch v-model="scope.row.status" active-value="0" inactive-value="1" @change="handleStatusChange(scope.row)" />
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

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script>
import { listStudents, resetStudentPassword, changeStudentStatus, deleteStudents, getRegisterEnabled, setRegisterEnabled } from "@/api/account"

export default {
  name: "StudentAccount",
  dicts: ["sys_normal_disable"],
  data() {
    return {
      loading: false,
      showSearch: true,
      total: 0,
      studentList: [],
      ids: [],
      registerEnabled: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        studentNo: undefined,
        status: undefined
      }
    }
  },
  created() {
    this.getList()
    this.refreshRegisterEnabled()
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
      this.ids = selection.map(i => i.userId)
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
      const userIds = row.userId || this.ids
      this.$modal.confirm(`确认删除学号为 ${row.studentNo} 的学生吗？`).then(() => deleteStudents(userIds)).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    refreshRegisterEnabled() {
      getRegisterEnabled().then(res => {
        this.registerEnabled = !!res.data
      })
    },
    handleRegisterToggle(val) {
      setRegisterEnabled(val ? "0" : "1").then(() => {
        this.$modal.msgSuccess(val ? "已开放注册" : "已关闭注册")
      }).catch(() => {
        this.registerEnabled = !val
      })
    }
  }
}
</script>
