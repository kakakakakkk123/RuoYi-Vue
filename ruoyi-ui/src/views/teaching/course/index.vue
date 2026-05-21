<template>
  <div class="app-container">
    <el-card shadow="never">
      <div slot="header">课程管理</div>
      <el-table :data="courseList" border>
        <el-table-column prop="name" label="课程名称" />
        <el-table-column prop="teacher" label="授课教师" width="120" />
        <el-table-column prop="students" label="选课人数" width="100" />
        <el-table-column prop="status" label="状态" width="100" />
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "TeachingCourse",
  data() {
    return {
      courseList: [
        { name: "Java Web 开发基础", teacher: "张老师", students: 86, status: "进行中" },
        { name: "数据库系统原理", teacher: "李老师", students: 74, status: "进行中" },
        { name: "软件测试与质量保证", teacher: "王老师", students: 63, status: "已结课" }
      ]
    }
  }
}
</script>
