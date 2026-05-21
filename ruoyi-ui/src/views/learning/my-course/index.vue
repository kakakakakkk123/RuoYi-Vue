<template>
  <div class="app-container">
    <el-row :gutter="16">
      <el-col v-for="course in courses" :key="course.name" :xs="24" :sm="12" :lg="8">
        <el-card class="course-card" shadow="hover">
          <div class="course-card__tag">{{ course.status }}</div>
          <h3>{{ course.name }}</h3>
          <p>{{ course.teacher }}</p>
          <el-progress :percentage="course.progress" :stroke-width="10" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "MyCourse",
  data() {
    return {
      courses: [
        { name: "Java Web 开发基础", teacher: "任课教师：张老师", progress: 78, status: "进行中" },
        { name: "数据库系统原理", teacher: "任课教师：李老师", progress: 52, status: "进行中" },
        { name: "软件测试与质量保证", teacher: "任课教师：王老师", progress: 100, status: "已完成" }
      ]
    }
  }
}
</script>

<style lang="scss" scoped>
.course-card {
  margin-bottom: 16px;
  border-radius: 18px;
}

.course-card__tag {
  display: inline-block;
  margin-bottom: 12px;
  padding: 4px 10px;
  border-radius: 999px;
  background: #dbeafe;
  color: #1d4ed8;
  font-size: 12px;
  font-weight: 700;
}

h3 {
  margin: 0 0 10px;
}

p {
  color: #64748b;
}
</style>
