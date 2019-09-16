--  用户表
DROP TABLE IF EXISTS users;
CREATE TABLE users
(
  id                      BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户id',
  username                VARCHAR(100) NOT NULL COMMENT '用户名',
  password                VARCHAR(100) NOT NULL COMMENT '用户密码密文',
  name                    VARCHAR(200) COMMENT '用户姓名',
  mobile                  VARCHAR(20) COMMENT '用户手机',
  description             VARCHAR(500) COMMENT '简介',
  deleted                 VARCHAR(1)   NOT NULL DEFAULT 'N' COMMENT '是否已删除Y：已删除，N：未删除',
  enabled                 BOOLEAN COMMENT '是否有效用户',
  account_non_expired     BOOLEAN COMMENT '账号是否未过期',
  credentials_non_expired BOOLEAN COMMENT '密码是否未过期',
  account_non_locked      BOOLEAN COMMENT '是否未锁定',
  created_time            DATETIME     NOT NULL DEFAULT now() COMMENT '创建时间',
  updated_time            DATETIME     NOT NULL DEFAULT now() COMMENT '更新时间',
  created_by              VARCHAR(100) NOT NULL COMMENT '创建人',
  updated_by              VARCHAR(100) NOT NULL COMMENT '更新人'
) COMMENT '用户表';
CREATE UNIQUE INDEX ux_users_username
  ON users (username);
CREATE UNIQUE INDEX ux_users_mobile
  ON users (mobile);