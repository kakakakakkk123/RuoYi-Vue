<template>
  <div class="course-square">
    <div class="hero">
      <div>
        <p class="eyebrow">游客模式</p>
        <h1>在线课程广场</h1>
        <p class="summary">
          未登录用户可以浏览课程、搜索课程和查看课程介绍；收藏、学习、考试和评论需要登录学生账号。
        </p>
      </div>
      <div class="hero__actions">
        <el-button type="primary" @click="$router.push('/login')">登录学习</el-button>
        <el-button @click="$router.push('/register')">学生注册</el-button>
      </div>
    </div>

    <el-card class="search-card" shadow="never">
      <el-input
        v-model.trim="keyword"
        placeholder="搜索课程名称、老师或关键词"
        clearable
        prefix-icon="el-icon-search"
      />
    </el-card>

    <div class="course-grid">
      <el-card v-for="course in filteredCourses" :key="course.id" class="course-card" shadow="hover">
        <div class="course-card__head">
          <span class="course-tag">{{ course.category }}</span>
          <span class="teacher">{{ course.teacher }}</span>
        </div>
        <h3>{{ course.name }}</h3>
        <p>{{ course.intro }}</p>
        <div class="course-meta">
          <span>课时：{{ course.hours }}</span>
          <span>难度：{{ course.level }}</span>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  name: "CourseSquare",
  data() {
    return {
      keyword: "",
      courses: [
        { id: 1, name: "Java Web 开发基础", teacher: "张老师", category: "编程开发", level: "入门", hours: 32, intro: "面向在线教学平台的 Java Web 入门课程，覆盖 Servlet、JSP 与前后端联调。" },
        { id: 2, name: "数据库系统原理", teacher: "李老师", category: "数据库", level: "中级", hours: 28, intro: "学习关系数据库设计、SQL 优化与事务机制，适合教学系统数据建模。" },
        { id: 3, name: "软件测试与质量保证", teacher: "王老师", category: "软件工程", level: "中级", hours: 24, intro: "围绕测试用例、缺陷跟踪和质量保障流程展开，强调项目实战。" },
        { id: 4, name: "前端界面设计实践", teacher: "陈老师", category: "前端设计", level: "入门", hours: 20, intro: "学习 Vue、组件化界面设计与课程平台常见交互实现。" }
      ]
    }
  },
  computed: {
    filteredCourses() {
      if (!this.keyword) {
        return this.courses
      }
      const keyword = this.keyword.toLowerCase()
      return this.courses.filter(course =>
        [course.name, course.teacher, course.category, course.intro].some(item => item.toLowerCase().includes(keyword))
      )
    }
  }
}
</script>

<style lang="scss" scoped>
.course-square {
  min-height: 100vh;
  padding: 40px 6vw;
  background:
    radial-gradient(circle at top left, rgba(59, 130, 246, 0.18), transparent 28%),
    radial-gradient(circle at top right, rgba(16, 185, 129, 0.16), transparent 24%),
    linear-gradient(180deg, #f8fbff 0%, #eef7ff 100%);
}

.hero,
.search-card {
  margin-bottom: 24px;
}

.hero {
  display: flex;
  justify-content: space-between;
  gap: 24px;
  align-items: flex-end;
}

.eyebrow {
  margin: 0 0 10px;
  color: #2563eb;
  font-weight: 700;
  letter-spacing: 2px;
}

h1 {
  margin: 0;
  font-size: 40px;
  color: #0f172a;
}

.summary {
  max-width: 760px;
  margin: 14px 0 0;
  color: #475569;
  line-height: 1.8;
}

.hero__actions {
  display: flex;
  gap: 12px;
}

.search-card {
  border-radius: 18px;
}

.course-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 18px;
}

.course-card {
  border-radius: 18px;
}

.course-card__head,
.course-meta {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  color: #64748b;
  font-size: 13px;
}

.course-tag {
  color: #1d4ed8;
  font-weight: 700;
}

.course-card h3 {
  margin: 16px 0 10px;
  color: #0f172a;
}

.course-card p {
  min-height: 72px;
  line-height: 1.8;
  color: #475569;
}

@media (max-width: 768px) {
  .hero,
  .hero__actions {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
