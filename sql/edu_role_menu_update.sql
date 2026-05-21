-- 云课教学平台角色与菜单补丁
-- 已有数据库可直接执行本文件

update sys_role set role_name = '教师', role_key = 'teacher', role_sort = 3, status = '0', del_flag = '0' where role_id = 3;
update sys_role set role_name = '学生', role_key = 'student', role_sort = 4, status = '0', del_flag = '0' where role_id = 4;

insert into sys_role (role_id, role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, update_by, update_time, remark)
select 3, '教师', 'teacher', 3, 1, 1, 1, '0', '0', 'admin', sysdate(), '', null, '教师角色'
where not exists (select 1 from sys_role where role_id = 3);

insert into sys_role (role_id, role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, update_by, update_time, remark)
select 4, '学生', 'student', 4, 2, 1, 1, '0', '0', 'admin', sysdate(), '', null, '学生角色'
where not exists (select 1 from sys_role where role_id = 4);

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select '2100', '教学管理', '0', '5', 'teaching', '', '', '', 0, 0, 'M', '0', '0', '', 'education', 'admin', sysdate(), '', null, '教师教学管理目录'
where not exists (select 1 from sys_menu where menu_id = '2100');
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select '2101', '课程管理', '2100', '1', 'course', 'teaching/course/index', '', '', 0, 0, 'C', '0', '0', 'teaching:course:list', 'form', 'admin', sysdate(), '', null, '课程管理菜单'
where not exists (select 1 from sys_menu where menu_id = '2101');
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select '2102', '学生管理', '2100', '2', 'student', 'account/student/index', '', '', 0, 0, 'C', '0', '0', 'account:student:list', 'peoples', 'admin', sysdate(), '', null, '学生管理菜单'
where not exists (select 1 from sys_menu where menu_id = '2102');
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select '2103', '试卷管理', '2100', '3', 'paper', 'teaching/paper/index', '', '', 0, 0, 'C', '0', '0', 'teaching:paper:list', 'documentation', 'admin', sysdate(), '', null, '试卷管理菜单'
where not exists (select 1 from sys_menu where menu_id = '2103');
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select '2104', '成绩统计', '2100', '4', 'score', 'teaching/score/index', '', '', 0, 0, 'C', '0', '0', 'teaching:score:list', 'chart', 'admin', sysdate(), '', null, '成绩统计菜单'
where not exists (select 1 from sys_menu where menu_id = '2104');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select '2200', '学习中心', '0', '6', 'learning', '', '', '', 0, 0, 'M', '0', '0', '', 'guide', 'admin', sysdate(), '', null, '学生学习中心目录'
where not exists (select 1 from sys_menu where menu_id = '2200');
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select '2201', '我的课程', '2200', '1', 'my-course', 'learning/my-course/index', '', '', 0, 0, 'C', '0', '0', 'learning:course:list', 'dashboard', 'admin', sysdate(), '', null, '我的课程菜单'
where not exists (select 1 from sys_menu where menu_id = '2201');
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select '2202', '在线学习', '2200', '2', 'online', 'learning/online/index', '', '', 0, 0, 'C', '0', '0', 'learning:online:list', 'build', 'admin', sysdate(), '', null, '在线学习菜单'
where not exists (select 1 from sys_menu where menu_id = '2202');
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select '2203', '我的考试', '2200', '3', 'exam', 'learning/exam/index', '', '', 0, 0, 'C', '0', '0', 'learning:exam:list', 'documentation', 'admin', sysdate(), '', null, '我的考试菜单'
where not exists (select 1 from sys_menu where menu_id = '2203');
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select '2204', '我的错题', '2200', '4', 'wrong', 'learning/wrong/index', '', '', 0, 0, 'C', '0', '0', 'learning:wrong:list', 'bug', 'admin', sysdate(), '', null, '我的错题菜单'
where not exists (select 1 from sys_menu where menu_id = '2204');
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select '2205', '收藏', '2200', '5', 'favorite', 'learning/favorite/index', '', '', 0, 0, 'C', '0', '0', 'learning:favorite:list', 'star', 'admin', sysdate(), '', null, '收藏菜单'
where not exists (select 1 from sys_menu where menu_id = '2205');
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select '2206', '笔记', '2200', '6', 'note', 'learning/note/index', '', '', 0, 0, 'C', '0', '0', 'learning:note:list', 'edit', 'admin', sysdate(), '', null, '笔记菜单'
where not exists (select 1 from sys_menu where menu_id = '2206');

delete from sys_role_menu where role_id in (3, 4) and menu_id in (2000,2001,2002,2003,2004,2005,2100,2101,2102,2103,2104,2200,2201,2202,2203,2204,2205,2206);

insert into sys_role_menu (role_id, menu_id) select '3', '2100' where not exists (select 1 from sys_role_menu where role_id = '3' and menu_id = '2100');
insert into sys_role_menu (role_id, menu_id) select '3', '2101' where not exists (select 1 from sys_role_menu where role_id = '3' and menu_id = '2101');
insert into sys_role_menu (role_id, menu_id) select '3', '2102' where not exists (select 1 from sys_role_menu where role_id = '3' and menu_id = '2102');
insert into sys_role_menu (role_id, menu_id) select '3', '2103' where not exists (select 1 from sys_role_menu where role_id = '3' and menu_id = '2103');
insert into sys_role_menu (role_id, menu_id) select '3', '2104' where not exists (select 1 from sys_role_menu where role_id = '3' and menu_id = '2104');
insert into sys_role_menu (role_id, menu_id) select '4', '2200' where not exists (select 1 from sys_role_menu where role_id = '4' and menu_id = '2200');
insert into sys_role_menu (role_id, menu_id) select '4', '2201' where not exists (select 1 from sys_role_menu where role_id = '4' and menu_id = '2201');
insert into sys_role_menu (role_id, menu_id) select '4', '2202' where not exists (select 1 from sys_role_menu where role_id = '4' and menu_id = '2202');
insert into sys_role_menu (role_id, menu_id) select '4', '2203' where not exists (select 1 from sys_role_menu where role_id = '4' and menu_id = '2203');
insert into sys_role_menu (role_id, menu_id) select '4', '2204' where not exists (select 1 from sys_role_menu where role_id = '4' and menu_id = '2204');
insert into sys_role_menu (role_id, menu_id) select '4', '2205' where not exists (select 1 from sys_role_menu where role_id = '4' and menu_id = '2205');
insert into sys_role_menu (role_id, menu_id) select '4', '2206' where not exists (select 1 from sys_role_menu where role_id = '4' and menu_id = '2206');

insert into sys_user (user_id, dept_id, user_name, nick_name, student_no, email, phonenumber, sex, avatar, password, status, del_flag, login_ip, login_date, pwd_update_date, create_by, create_time, update_by, update_time, remark)
select 3, 103, 'teacher', '教师', 'TS001', 'teacher@school.edu.cn', '13800000000', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', sysdate(), sysdate(), 'admin', sysdate(), '', null, '教师账号'
where not exists (select 1 from sys_user where user_id = 3);

insert into sys_user_role (user_id, role_id)
select 3, 3 where not exists (select 1 from sys_user_role where user_id = 3 and role_id = 3);

insert into sys_user (user_id, dept_id, user_name, nick_name, student_no, email, phonenumber, sex, avatar, password, status, del_flag, login_ip, login_date, pwd_update_date, create_by, create_time, update_by, update_time, remark)
select 4, 103, 'student01', '学生一号', 'ST001', 'student01@school.edu.cn', '13900000001', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', sysdate(), sysdate(), 'admin', sysdate(), '', null, '示例学生账号'
where not exists (select 1 from sys_user where user_id = 4);

insert into sys_user_role (user_id, role_id)
select 4, 4 where not exists (select 1 from sys_user_role where user_id = 4 and role_id = 4);

insert into edu_student_profile (user_id, signature, todo_items, learning_history, learning_notes, favorites, wrong_questions, discussions, create_by, create_time)
select 4, '认真学习，持续进步', '完成 Java Web 课程实验和数据库作业', '已完成 Java Web 第 1~6 章学习', '本周重点复习过滤器和拦截器', '收藏：Spring Security 登录流程图', '错题：数据库范式综合题', '讨论：期末项目模块分工', 'admin', sysdate()
where not exists (select 1 from edu_student_profile where user_id = 4);
