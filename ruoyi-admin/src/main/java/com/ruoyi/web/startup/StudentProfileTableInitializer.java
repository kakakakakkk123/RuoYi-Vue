package com.ruoyi.web.startup;

import java.sql.Connection;
import java.sql.Statement;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 启动时确保学生档案表存在，兼容旧库升级场景。
 */
@Component
public class StudentProfileTableInitializer implements ApplicationRunner
{
    private static final Logger log = LoggerFactory.getLogger(StudentProfileTableInitializer.class);

    private final DataSource dataSource;

    public StudentProfileTableInitializer(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception
    {
        String sql = """
                create table if not exists edu_student_profile (
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
                ) engine=innodb auto_increment=1 comment = '学生档案表'
                """;

        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement())
        {
            statement.execute(sql);
            log.info("student profile table checked");
        }
    }
}
