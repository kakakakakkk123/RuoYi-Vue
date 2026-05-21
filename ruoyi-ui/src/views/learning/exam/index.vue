<template>
  <div class="app-container">
    <el-row :gutter="16">
      <el-col :xs="24" :md="12">
        <el-card shadow="never">
          <div slot="header">待参加考试</div>
          <el-table :data="upcoming" border>
            <el-table-column prop="name" label="考试名称" />
            <el-table-column prop="time" label="时间" width="160" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="12">
        <el-card shadow="never">
          <div slot="header">最近成绩</div>
          <el-table :data="history" border>
            <el-table-column prop="name" label="考试名称" />
            <el-table-column prop="score" label="成绩" width="100" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "MyExam",
  data() {
    return {
      upcoming: [
        { name: "数据库阶段测试", time: "2026-05-28 19:30" },
        { name: "软件测试随堂测验", time: "2026-06-02 14:00" }
      ],
      history: [
        { name: "Java Web 单元测验", score: 92 },
        { name: "需求分析章节测试", score: 88 }
      ]
    }
  }
}
</script>
