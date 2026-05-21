<template>
  <div class="app-container">
    <el-row :gutter="16">
      <el-col :xs="24" :sm="12" :lg="6" v-for="card in cards" :key="card.title">
        <el-card class="score-card" shadow="hover">
          <div class="score-card__title">{{ card.title }}</div>
          <div class="score-card__value">{{ card.value }}</div>
        </el-card>
      </el-col>
    </el-row>
    <el-card shadow="never">
      <div slot="header">班级成绩概览</div>
      <el-table :data="tableData" border>
        <el-table-column prop="clazz" label="班级" />
        <el-table-column prop="avg" label="平均分" width="100" />
        <el-table-column prop="passRate" label="及格率" width="100" />
        <el-table-column prop="excellentRate" label="优秀率" width="100" />
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "TeachingScore",
  data() {
    return {
      cards: [
        { title: "本周考试数", value: 4 },
        { title: "已录入成绩", value: 182 },
        { title: "平均分", value: 86.4 },
        { title: "最高分", value: 98 }
      ],
      tableData: [
        { clazz: "软件工程 1 班", avg: 87.2, passRate: "96%", excellentRate: "42%" },
        { clazz: "软件工程 2 班", avg: 85.6, passRate: "94%", excellentRate: "38%" }
      ]
    }
  }
}
</script>

<style lang="scss" scoped>
.score-card {
  margin-bottom: 16px;
}

.score-card__title {
  color: #64748b;
  margin-bottom: 10px;
}

.score-card__value {
  font-size: 28px;
  font-weight: 700;
  color: #0f172a;
}
</style>
