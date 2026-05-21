drop table if exists edu_student_profile;
create table edu_student_profile (
  profile_id bigint(20) not null auto_increment comment '档案ID',
  user_id bigint(20) not null comment '用户ID',
  signature varchar(200) default '' comment '个性签名',
  todo_items text comment '待办事项',
  learning_history text comment '学习历史',
  learning_notes text comment '学习笔记',
  favorites text comment '收藏内容',
  wrong_questions text comment '错题内容',
  discussions text comment '讨论内容',
  create_by varchar(64) default '' comment '创建者',
  create_time datetime comment '创建时间',
  update_by varchar(64) default '' comment '更新者',
  update_time datetime comment '更新时间',
  remark varchar(500) default null comment '备注',
  primary key (profile_id),
  unique key uk_edu_student_profile_user_id (user_id)
) engine=innodb auto_increment=1 comment = '学生档案表';
