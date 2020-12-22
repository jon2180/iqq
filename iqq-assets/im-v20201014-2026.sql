/*
Navicat MySQL Data Transfer

Source Server         : fakeqq
Source Server Version : 50547
Source Host           : 127.0.0.1:3306
Source Database       : fqq

Target Server Type    : MYSQL
Target Server Version : 50547
File Encoding         : 65001

Date: 2019-12-14 22:25:07
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for accounts
-- ----------------------------
DROP TABLE IF EXISTS `accounts`;
CREATE TABLE `accounts`
(
    `account_id`        int(11) unsigned   NOT NULL AUTO_INCREMENT COMMENT '用户唯一id',
    `qnumber`   varchar(10) UNIQUE NOT NULL COMMENT 'qq号码',
    `nickname`  varchar(64)        NOT NULL COMMENT '用户昵称',
    `signature` varchar(140) DEFAULT NULL COMMENT '签名',
    `tel`       varchar(16)  DEFAULT NULL COMMENT '电话号码',
    `ip`        varchar(64)  DEFAULT NULL COMMENT 'ip address',
    `port`      int(11)      DEFAULT NULL COMMENT 'port',
    `avatar`    varchar(256) DEFAULT NULL COMMENT '用户头像的Url',
    `email`     varchar(128) DEFAULT NULL COMMENT '用户的邮箱',
    `password`  varchar(128) DEFAULT NULL COMMENT '用户密码',
    `salt`      varchar(64)  DEFAULT NULL COMMENT '密码盐,通过盐与用户输入密码组合然后md5加密形成密码',
    `birthday`  date         DEFAULT NULL COMMENT '生日',
    `status`    tinyint(4)   DEFAULT '0' COMMENT '用户账号状态',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户账户表';

-- ----------------------------
-- Records of accounts
-- ----------------------------
INSERT INTO `accounts`
VALUES ('1', 1234567, '1111', null, null, null, null, null, null, '1111', null, null, '0');
INSERT INTO `accounts`
VALUES ('2', 2345678, 'LISHENG', null, null, null, null, null, null, '123456', null, null, '0');
INSERT INTO `accounts`
VALUES ('3', 3456789, 'hah', null, null, null, null, null, null, 'ainiya', null, null, '0');
INSERT INTO `accounts`
VALUES ('4', 4567890, 'hah1', null, null, null, null, null, null, 'ainiya', null, null, '0');


-- ----------------------------
-- Table structure for groups
-- ----------------------------
DROP TABLE IF EXISTS `groups`;
CREATE TABLE `groups`
(
    `group_id`             int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '群组唯一id',
    `group_desc` varchar(256)      NOT NULL COMMENT '群描述',
    `group_tags` varchar(256)      NOT NULL COMMENT '群标签',
    `group_name`     varchar(32) DEFAULT NULL COMMENT '所在组名',
    `group_owner_id` int(11) unsigned NOT NULL COMMENT '群主的用户id'
    `create_time`           int(10) unsigned not null comment '创建时间',
    `status`           int(10) unsigned NOT NULL COMMENT '类型',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='群组列表';



-- ----------------------------
-- Table structure for friends
-- ----------------------------
DROP TABLE IF EXISTS `friends`;
CREATE TABLE `friends`
(
    `id`             int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户唯一id',
    `origin_account` varchar(10)      NOT NULL COMMENT '源账号',
    `target_account` varchar(10)      NOT NULL COMMENT '目标账号',
    `group_name`     varchar(32) DEFAULT NULL COMMENT '所在组名',
    `time`           int(10) unsigned not null comment '添加好友时间',
    `type`           int(10) unsigned NOT NULL COMMENT '好友类型，正常为0，异常为1',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='朋友关系表';

insert into friends(origin_account, target_account, group_name, time, type)
values ('1234567', '2345678', 'test-demo', 1576482028, 0),
       ('2345678', '1234567', 'test-demo', 1576482028, 0),
       ('1234567', '3456789', 'test-demo', 1576482029, 0),
       ('3456789', '1234567', 'test-demo', 1576482029, 0);

-- ----------------------------
-- Records of friends
-- ----------------------------

-- ----------------------------
-- Table structure for messages
-- ----------------------------
DROP TABLE IF EXISTS `messages`;
CREATE TABLE `messages`
(
    `id`             int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户唯一id',
    `origin_account` varchar(10)      NOT NULL COMMENT '源账号',
    `target_account` varchar(10)      NOT NULL COMMENT '目标账号',
    `type`           int(10) unsigned NOT NULL COMMENT '消息类型',
    `content`        text COMMENT '消息内容',
    `time`           int(10) comment '消息发送时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='消息内容表';

-- ----------------------------
-- Records of messages
-- ----------------------------
