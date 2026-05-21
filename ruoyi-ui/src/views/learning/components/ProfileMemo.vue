<template>
  <div class="app-container">
    <el-card shadow="never">
      <div slot="header" class="memo-header">
        <div>
          <div class="memo-title">{{ title }}</div>
          <div class="memo-desc">{{ description }}</div>
        </div>
        <el-button type="primary" size="mini" @click="submit">保存内容</el-button>
      </div>
      <el-input
        v-model="form[field]"
        type="textarea"
        :rows="14"
        :placeholder="placeholder"
      />
    </el-card>
  </div>
</template>

<script>
import { getStudentProfile, updateStudentProfile } from "@/api/system/user"

export default {
  name: "ProfileMemo",
  props: {
    field: {
      type: String,
      required: true
    },
    title: {
      type: String,
      required: true
    },
    description: {
      type: String,
      default: ""
    },
    placeholder: {
      type: String,
      default: "请输入内容"
    }
  },
  data() {
    return {
      form: {}
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      getStudentProfile().then(res => {
        this.form = res.data || {}
      })
    },
    submit() {
      updateStudentProfile(this.form).then(() => {
        this.$modal.msgSuccess("保存成功")
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.memo-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.memo-title {
  font-size: 18px;
  font-weight: 700;
  color: #0f172a;
}

.memo-desc {
  margin-top: 6px;
  color: #64748b;
  font-size: 13px;
}
</style>
