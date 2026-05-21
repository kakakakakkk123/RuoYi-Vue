<template>
  <profile-memo
    field="wrongQuestions"
    title="我的错题"
    description="记录课堂练习、考试和在线答题中的典型错题，方便后续复习。"
    placeholder="请输入错题编号、题干摘要、错误原因和改正思路"
  />
</template>

<script>
import ProfileMemo from "../components/ProfileMemo"

export default {
  name: "WrongQuestions",
  components: { ProfileMemo }
}
</script>
