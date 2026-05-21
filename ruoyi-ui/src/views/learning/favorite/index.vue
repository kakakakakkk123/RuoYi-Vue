<template>
  <profile-memo
    field="favorites"
    title="我的收藏"
    description="收藏重要课程、知识点和资源链接，便于后续快速回看。"
    placeholder="请输入收藏课程名、章节标题或学习资源说明"
  />
</template>

<script>
import ProfileMemo from "../components/ProfileMemo"

export default {
  name: "FavoriteCourse",
  components: { ProfileMemo }
}
</script>
