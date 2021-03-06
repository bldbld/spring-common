drop table IF EXISTS user;

create TABLE user
(
    id BIGINT(20) NOT NULL comment '主键ID',
    name VARCHAR(30) NULL DEFAULT NULL comment '姓名',
    age INT(11) NULL DEFAULT NULL comment '年龄',
    email VARCHAR(50) NULL DEFAULT NULL comment '邮箱',
    PRIMARY KEY (id)
);

drop table IF EXISTS log;

create TABLE log
(
    USER_ID VARCHAR(50) COMMENT '用户ID',
    USER_NICKNAME VARCHAR(50) COMMENT '用户名',
    OPERATION_INFO VARCHAR(50) COMMENT '操作信息',
    INTERFACE_NAME VARCHAR(200) COMMENT '接口名',
    APPLICATION_NAME VARCHAR(100) COMMENT '应用名',
    CREATE_TIME VARCHAR(100) COMMENT '时间',
    CLIENT_IP VARCHAR(50) COMMENT 'CLIENTIP'
);
