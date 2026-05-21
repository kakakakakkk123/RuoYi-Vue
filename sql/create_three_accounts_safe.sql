-- 只往现有 ruoyi 库里插入 3 个账号
-- 默认密码：admin123

insert into sys_role (role_id, role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, update_by, update_time, remark)
select 3, '教师', 'teacher', 3, 1, 1, 1, '0', '0', 'admin', sysdate(), '', null, '教师角色'
where not exists (select 1 from sys_role where role_id = 3);

insert into sys_role (role_id, role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, update_by, update_time, remark)
select 4, '学生', 'student', 4, 2, 1, 1, '0', '0', 'admin', sysdate(), '', null, '学生角色'
where not exists (select 1 from sys_role where role_id = 4);

insert into sys_user (
  user_id, dept_id, user_name, nick_name, student_no, email, phonenumber, sex, avatar,
  password, status, del_flag, login_ip, login_date, pwd_update_date, create_by, create_time,
  update_by, update_time, remark
)
select 1001, 103, 'admin02', '系统管理员', 'AD001', 'admin02@school.edu.cn', '13800000010', '1', '',
       '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2',
       '0', '0', '127.0.0.1', sysdate(), sysdate(), 'admin', sysdate(), '', null, '系统管理员账号'
where not exists (select 1 from sys_user where user_name = 'admin02');

insert into sys_user (
  user_id, dept_id, user_name, nick_name, student_no, email, phonenumber, sex, avatar,
  password, status, del_flag, login_ip, login_date, pwd_update_date, create_by, create_time,
  update_by, update_time, remark
)
select 1002, 103, 'teacher02', '教师二号', 'TS002', 'teacher02@school.edu.cn', '13800000011', '1', '',
       '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2',
       '0', '0', '127.0.0.1', sysdate(), sysdate(), 'admin', sysdate(), '', null, '教师账号'
where not exists (select 1 from sys_user where user_name = 'teacher02');

insert into sys_user (
  user_id, dept_id, user_name, nick_name, student_no, email, phonenumber, sex, avatar,
  password, status, del_flag, login_ip, login_date, pwd_update_date, create_by, create_time,
  update_by, update_time, remark
)
select 1003, 103, 'student02', '学生二号', 'ST002', 'student02@school.edu.cn', '13800000012', '0', '',
       '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2',
       '0', '0', '127.0.0.1', sysdate(), sysdate(), 'admin', sysdate(), '', null, '学生账号'
where not exists (select 1 from sys_user where user_name = 'student02');

insert into sys_user_role (user_id, role_id)
select 1001, 1 where not exists (select 1 from sys_user_role where user_id = 1001 and role_id = 1);

insert into sys_user_role (user_id, role_id)
select 1002, 3 where not exists (select 1 from sys_user_role where user_id = 1002 and role_id = 3);

insert into sys_user_role (user_id, role_id)
select 1003, 4 where not exists (select 1 from sys_user_role where user_id = 1003 and role_id = 4);
