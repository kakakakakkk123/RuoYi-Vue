<template>
  <el-form ref="form" :model="form" :rules="rules" label-width="90px">
    <el-form-item label="个性签名" prop="signature">
      <el-input v-model="form.signature" maxlength="200" />
    </el-form-item>
    <el-form-item label="待办事项" prop="todoItems">
      <el-input v-model="form.todoItems" type="textarea" :rows="3" />
    </el-form-item>
    <el-form-item label="学习历史" prop="learningHistory">
      <el-input v-model="form.learningHistory" type="textarea" :rows="3" />
    </el-form-item>
    <el-form-item label="学习笔记" prop="learningNotes">
      <el-input v-model="form.learningNotes" type="textarea" :rows="3" />
    </el-form-item>
    <el-form-item label="我的收藏" prop="favorites">
      <el-input v-model="form.favorites" type="textarea" :rows="3" />
    </el-form-item>
    <el-form-item label="我的错题" prop="wrongQuestions">
      <el-input v-model="form.wrongQuestions" type="textarea" :rows="3" />
    </el-form-item>
    <el-form-item label="我的讨论" prop="discussions">
      <el-input v-model="form.discussions" type="textarea" :rows="3" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">保存</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { getStudentProfile, updateStudentProfile } from "@/api/system/user"

export default {
  data() {
    return {
      form: {},
      rules: {}
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
