<template>
  <div class="home-page">
    <div class="hero">
      <div class="hero__text">
        <p class="eyebrow">云课教学平台</p>
        <h1>让课程、作业、笔记和账号管理都更清晰</h1>
        <p class="summary">
          这是你们小组的中文在线课程教学平台首页，覆盖学生注册、教师管理、个人中心与账户安全等核心功能。
        </p>
        <div class="stats">
          <div class="stat">
            <strong>学生</strong>
            <span>自主注册、登录、资料维护</span>
          </div>
          <div class="stat">
            <strong>教师</strong>
            <span>账号管理、重置、禁用、删除</span>
          </div>
          <div class="stat">
            <strong>系统</strong>
            <span>加盐哈希、冻结保护、验证码</span>
          </div>
        </div>
      </div>
      <div class="hero__card">
        <h3>平台定位</h3>
        <ul>
          <li>中文界面</li>
          <li>教学账户体系</li>
          <li>个人学习档案</li>
          <li>教师管理后台</li>
        </ul>
      </div>
    </div>

    <div class="feature-grid">
      <el-card v-for="item in features" :key="item.title" class="feature-card" shadow="hover">
        <div class="feature-card__title">{{ item.title }}</div>
        <div class="feature-card__desc">{{ item.desc }}</div>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Index',
  data() {
    return {
      features: [
        { title: '学生账户', desc: '支持自主注册、登录与个人资料修改。' },
        { title: '教师管理', desc: '可重置密码、禁用和删除学生账号。' },
        { title: '安全保护', desc: '密码加密存储，支持冻结和异常提示。' }
      ]
    }
  }
}
</script>

<style lang="scss" scoped>
.home-page {
  padding: 24px;
  color: #1f2d3d;
}

.hero {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 20px;
  margin-bottom: 24px;
}

.hero__text,
.hero__card,
.feature-card {
  background: #fff;
  border-radius: 18px;
  box-shadow: 0 12px 40px rgba(15, 23, 42, 0.08);
}

.hero__text {
  padding: 32px;
  background: linear-gradient(135deg, #eff6ff 0%, #ffffff 55%, #ecfeff 100%);
}

.eyebrow {
  margin: 0 0 12px;
  color: #1d4ed8;
  font-weight: 700;
  letter-spacing: 2px;
}

h1 {
  margin: 0;
  font-size: 34px;
  line-height: 1.2;
}

.summary {
  max-width: 720px;
  margin: 16px 0 0;
  color: #475569;
  line-height: 1.8;
}

.stats {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 14px;
  margin-top: 24px;
}

.stat {
  padding: 16px;
  border-radius: 14px;
  background: rgba(255, 255, 255, 0.8);
}

.stat strong,
.feature-card__title,
.hero__card h3 {
  display: block;
  margin-bottom: 8px;
}

.stat span,
.feature-card__desc,
.hero__card li {
  color: #64748b;
  line-height: 1.7;
}

.hero__card {
  padding: 28px;
}

.hero__card ul {
  margin: 0;
  padding-left: 18px;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 16px;
}

.feature-card {
  padding: 22px;
}

@media (max-width: 960px) {
  .hero,
  .feature-grid,
  .stats {
    grid-template-columns: 1fr;
  }
}
</style>
