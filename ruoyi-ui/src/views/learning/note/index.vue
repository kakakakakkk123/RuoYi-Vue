<template>
  <profile-memo
    field="learningNotes"
    title="学习笔记"
    description="记录课堂重点、实验步骤和个人总结。"
    placeholder="请输入本周学习笔记、重要知识点和复习总结"
  />
</template>

<script>
import ProfileMemo from "../components/ProfileMemo"

export default {
  name: "LearningNote",
  components: { ProfileMemo }
}
</script>
