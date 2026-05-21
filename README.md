# 云课教学平台

基于 `Spring Boot + Vue` 的在线课程教学平台，适合小组期末作业展示与二次开发。

## 运行顺序

1. 启动 MySQL 和 Redis。
2. 导入 `sql/ry_20260417.sql`。
3. 先运行后端 `ruoyi-admin/src/main/java/com/ruoyi/RuoYiApplication.java`。
4. 再运行前端 `ruoyi-ui` 下的 `npm run dev`。

## 主要功能

- 学生自主注册与登录
- 教师学生账号管理
- 个人中心、收藏、笔记、错题、讨论
- 账号冻结、重置密码、删除
- 中文界面与接口文档
