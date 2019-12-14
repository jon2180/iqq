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

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for accounts
-- ----------------------------
DROP TABLE IF EXISTS `accounts`;
CREATE TABLE `accounts` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户唯一id',
  `qnumber` varchar(255) DEFAULT NULL COMMENT 'qq号码',
  `nickname` varchar(64) NOT NULL COMMENT '用户昵称',
  `signature` varchar(140) DEFAULT NULL COMMENT '签名',
  `tel` varchar(16) DEFAULT NULL COMMENT '电话号码',
  `ip` varchar(64) DEFAULT NULL COMMENT 'ip address',
  `port` int(11) DEFAULT NULL COMMENT 'port',
  `avatar` varchar(256) DEFAULT NULL COMMENT '用户头像的Url',
  `email` varchar(128) DEFAULT NULL COMMENT '用户的邮箱',
  `password` varchar(128) DEFAULT NULL COMMENT '用户密码',
  `salt` varchar(64) DEFAULT NULL COMMENT '密码盐,通过盐与用户输入密码组合然后md5加密形成密码',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `status` tinyint(4) DEFAULT '0' COMMENT '用户账号状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='用户账户表';

-- ----------------------------
-- Records of accounts
-- ----------------------------
INSERT INTO `accounts` VALUES ('1', null, '1111', null, null, null, null, null, null, '1111', null, null, '0');
INSERT INTO `accounts` VALUES ('2', null, 'LISHENG', null, null, null, null, null, null, '123456', null, null, '0');
INSERT INTO `accounts` VALUES ('3', null, 'hah', null, null, null, null, null, null, 'ainiya', null, null, '0');
INSERT INTO `accounts` VALUES ('4', null, 'hah1', null, null, null, null, null, null, 'ainiya', null, null, '0');

-- ----------------------------
-- Table structure for friends
-- ----------------------------
DROP TABLE IF EXISTS `friends`;
CREATE TABLE `friends` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户唯一id',
  `origin_account` int(11) unsigned NOT NULL COMMENT '源账号',
  `target_account` int(11) unsigned NOT NULL COMMENT '目标账号',
  `group_name` varchar(32) DEFAULT NULL COMMENT '所在组名',
  `type` int(10) unsigned NOT NULL COMMENT '好友类型，正常为0，异常为1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='朋友关系表';

-- ----------------------------
-- Records of friends
-- ----------------------------

-- ----------------------------
-- Table structure for messages
-- ----------------------------
DROP TABLE IF EXISTS `messages`;
CREATE TABLE `messages` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户唯一id',
  `origin_account` int(11) unsigned NOT NULL COMMENT '源账号',
  `target_account` int(11) unsigned NOT NULL COMMENT '目标账号',
  `type` int(10) unsigned NOT NULL COMMENT '消息类型',
  `content` text COMMENT '消息内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息内容表';

-- ----------------------------
-- Records of messages
-- ----------------------------
