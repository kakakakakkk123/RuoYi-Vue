<template>
  <div class="app-container">
    <el-card shadow="never">
      <div slot="header">在线学习</div>
      <el-timeline>
        <el-timeline-item v-for="item in lessons" :key="item.title" :timestamp="item.time" placement="top">
          <el-card shadow="hover">
            <h4>{{ item.title }}</h4>
            <p>{{ item.desc }}</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "OnlineLearning",
  data() {
    return {
      lessons: [
        { time: "第 1 周", title: "Servlet 生命周期", desc: "学习请求分发与参数处理，完成随堂练习。" },
        { time: "第 2 周", title: "MyBatis 数据映射", desc: "掌握 Mapper、XML 与实体类之间的映射关系。" },
        { time: "第 3 周", title: "Spring Security 登录流程", desc: "理解认证、授权、验证码与 Token 机制。" }
      ]
    }
  }
}
</script>
