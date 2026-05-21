<template>
  <div class="app-container">
    <el-card shadow="never">
      <div slot="header">试卷管理</div>
      <el-table :data="paperList" border>
        <el-table-column prop="name" label="试卷名称" />
        <el-table-column prop="course" label="所属课程" />
        <el-table-column prop="questions" label="题目数量" width="100" />
        <el-table-column prop="publish" label="发布时间" width="160" />
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "TeachingPaper",
  data() {
    return {
      paperList: [
        { name: "Java Web 期中测试", course: "Java Web 开发基础", questions: 25, publish: "2026-05-10 10:00" },
        { name: "数据库章节测验", course: "数据库系统原理", questions: 20, publish: "2026-05-15 14:00" }
      ]
    }
  }
}
</script>
