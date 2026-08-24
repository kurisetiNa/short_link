CREATE DATABASE IF NOT EXISTS `link`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;

USE `link`;

CREATE TABLE IF NOT EXISTS `t_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username` VARCHAR(256) NULL COMMENT '用户名',
    `password` VARCHAR(512) NULL COMMENT '密码',
    `real_name` VARCHAR(256) NULL COMMENT '真实姓名',
    `phone` VARCHAR(128) NULL COMMENT '手机号',
    `mail` VARCHAR(512) NULL COMMENT '邮箱',
    `deletion_time` BIGINT NULL COMMENT '注销时间戳',
    `create_time` DATETIME NULL COMMENT '创建时间',
    `update_time` DATETIME NULL COMMENT '修改时间',
    `del_flag` TINYINT(1) NULL COMMENT '删除标识 0：未删除 1：已删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB
  DEFAULT CHARACTER SET=utf8mb4
  COLLATE=utf8mb4_general_ci
  COMMENT='用户表';
