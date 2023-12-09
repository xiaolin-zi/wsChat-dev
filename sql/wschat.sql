/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50729
 Source Host           : localhost:3306
 Source Schema         : wschat

 Target Server Type    : MySQL
 Target Server Version : 50729
 File Encoding         : 65001

 Date: 09/12/2023 11:09:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_follow
-- ----------------------------
DROP TABLE IF EXISTS `t_follow`;
CREATE TABLE `t_follow`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `friend_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关注人id',
  `is_deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_follow
-- ----------------------------
INSERT INTO `t_follow` VALUES ('1', '1', '2', 0, '2023-12-07 19:48:51', '2023-12-06 21:43:56');
INSERT INTO `t_follow` VALUES ('10', '6', '1', 0, '2023-12-07 19:48:51', '2023-12-07 19:48:51');
INSERT INTO `t_follow` VALUES ('11', '2', '3', 0, '2023-12-07 19:48:51', '2023-12-07 19:48:51');
INSERT INTO `t_follow` VALUES ('12', '3', '2', 0, '2023-12-07 19:48:51', '2023-12-07 19:48:51');
INSERT INTO `t_follow` VALUES ('13', '2', '4', 0, '2023-12-07 19:48:51', '2023-12-07 19:48:51');
INSERT INTO `t_follow` VALUES ('14', '4', '2', 0, '2023-12-07 19:48:51', '2023-12-07 19:48:51');
INSERT INTO `t_follow` VALUES ('15', '2', '5', 0, '2023-12-07 19:48:51', '2023-12-07 19:48:51');
INSERT INTO `t_follow` VALUES ('16', '5', '2', 0, '2023-12-07 19:48:51', '2023-12-07 19:48:51');
INSERT INTO `t_follow` VALUES ('17', '3', '4', 0, '2023-12-07 19:48:51', '2023-12-07 19:48:51');
INSERT INTO `t_follow` VALUES ('18', '4', '3', 0, '2023-12-07 19:48:51', '2023-12-07 19:48:51');
INSERT INTO `t_follow` VALUES ('19', '5', '3', 0, '2023-12-07 19:48:51', '2023-12-07 19:48:51');
INSERT INTO `t_follow` VALUES ('2', '2', '1', 0, '2023-12-06 21:44:03', '2023-12-06 21:44:07');
INSERT INTO `t_follow` VALUES ('20', '7', '5', 0, '2023-12-07 19:48:51', '2023-12-07 19:48:51');
INSERT INTO `t_follow` VALUES ('21', '5', '6', 0, '2023-12-07 19:48:51', '2023-12-07 19:48:51');
INSERT INTO `t_follow` VALUES ('22', '6', '3', 0, '2023-12-07 19:48:51', '2023-12-07 19:48:51');
INSERT INTO `t_follow` VALUES ('3', '1', '3', 0, '2023-12-07 19:48:51', '2023-12-07 19:48:51');
INSERT INTO `t_follow` VALUES ('4', '1', '4', 0, '2023-12-07 19:48:51', '2023-12-07 19:48:51');
INSERT INTO `t_follow` VALUES ('5', '1', '5', 0, '2023-12-07 19:48:51', '2023-12-07 19:48:51');
INSERT INTO `t_follow` VALUES ('6', '1', '6', 0, '2023-12-07 19:48:51', '2023-12-07 19:48:51');
INSERT INTO `t_follow` VALUES ('7', '3', '1', 0, '2023-12-07 19:48:51', '2023-12-07 19:48:51');
INSERT INTO `t_follow` VALUES ('8', '4', '1', 0, '2023-12-07 19:48:51', '2023-12-07 19:48:51');
INSERT INTO `t_follow` VALUES ('9', '5', '1', 0, '2023-12-07 19:48:51', '2023-12-07 19:48:51');

-- ----------------------------
-- Table structure for t_group
-- ----------------------------
DROP TABLE IF EXISTS `t_group`;
CREATE TABLE `t_group`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '群名称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '群头像',
  `master_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人id',
  `is_deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_group
-- ----------------------------
INSERT INTO `t_group` VALUES ('GP1', '测试群聊', 'https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312062137298.webp', '1', 0, '2023-12-06 21:38:19', '2023-12-06 21:38:21');
INSERT INTO `t_group` VALUES ('GP1732425017943670786', '小黑子', 'https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312062137300.webp', '2', 0, '2023-12-06 23:41:26', '2023-12-06 23:41:26');
INSERT INTO `t_group` VALUES ('GP2', '爱坤真爱粉', 'https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312071944121.webp', '3', 0, '2023-12-07 19:52:53', '2023-12-07 19:52:57');
INSERT INTO `t_group` VALUES ('GP2fa9731ecc9b4c48', '香翅煎鱼', 'https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312072343138.webp', '1', 0, '2023-12-07 23:45:08', '2023-12-07 23:45:08');

-- ----------------------------
-- Table structure for t_message
-- ----------------------------
DROP TABLE IF EXISTS `t_message`;
CREATE TABLE `t_message`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '聊天名称',
  `type` tinyint(1) NULL DEFAULT NULL COMMENT '群聊还是私聊（1私聊，2群聊）',
  `accept_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收人或群组id',
  `is_read` tinyint(1) NULL DEFAULT NULL COMMENT '是否已读',
  `is_show` tinyint(1) NULL DEFAULT NULL COMMENT '是否展示',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `last_mess` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后的消息',
  `is_deleted` tinyint(1) NULL DEFAULT NULL COMMENT '逻辑删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_message
-- ----------------------------
INSERT INTO `t_message` VALUES ('1', '1', '珍德食泥鸭', 1, '6', 0, 1, 'https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312071944908.webp', '鸡你太美', 0, '2023-12-07 20:14:03', '2023-12-07 20:14:05');
INSERT INTO `t_message` VALUES ('10', '3', '小黑子', 2, 'GP1732425017943670786', 0, 1, 'https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312062137300.webp', NULL, 0, '2023-12-09 11:07:19', '2023-12-09 11:07:23');
INSERT INTO `t_message` VALUES ('11', '4', '小黑子', 2, 'GP1732425017943670786', 0, 1, 'https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312062137300.webp', NULL, 0, '2023-12-09 11:07:19', '2023-12-09 11:07:23');
INSERT INTO `t_message` VALUES ('12', '5', '小黑子', 2, 'GP1732425017943670786', 0, 1, 'https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312062137300.webp', NULL, 0, '2023-12-09 11:07:19', '2023-12-09 11:07:23');
INSERT INTO `t_message` VALUES ('13', '6', '小黑子', 2, 'GP1732425017943670786', 0, 1, 'https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312062137300.webp', NULL, 0, '2023-12-09 11:07:19', '2023-12-09 11:07:23');
INSERT INTO `t_message` VALUES ('2', '1', '爱坤真爱粉', 2, 'GP2', 0, 1, 'https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312071944121.webp', '大家好我是练习时长两年半的偶像练习生', 0, '2023-12-07 20:37:29', '2023-12-07 20:37:31');
INSERT INTO `t_message` VALUES ('3', '2', '爱坤真爱粉', 2, 'GP2', 0, 1, 'https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312071944121.webp', '大家好我是练习时长两年半的偶像练习生', 0, '2023-12-09 11:01:24', '2023-12-09 11:01:27');
INSERT INTO `t_message` VALUES ('4', '3', '爱坤真爱粉', 2, 'GP2', 0, 1, 'https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312071944121.webp', '大家好我是练习时长两年半的偶像练习生', 0, '2023-12-09 11:01:24', '2023-12-09 11:01:27');
INSERT INTO `t_message` VALUES ('5', '4', '爱坤真爱粉', 2, 'GP2', 0, 1, 'https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312071944121.webp', '大家好我是练习时长两年半的偶像练习生', 0, '2023-12-09 11:01:24', '2023-12-09 11:01:27');
INSERT INTO `t_message` VALUES ('6', '5', '爱坤真爱粉', 2, 'GP2', 0, 1, 'https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312071944121.webp', '大家好我是练习时长两年半的偶像练习生', 0, '2023-12-09 11:01:24', '2023-12-09 11:01:27');
INSERT INTO `t_message` VALUES ('7', '6', '爱坤真爱粉', 2, 'GP2', 0, 1, 'https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312071944121.webp', '大家好我是练习时长两年半的偶像练习生', 0, '2023-12-09 11:01:24', '2023-12-09 11:01:27');
INSERT INTO `t_message` VALUES ('8', '1', '小黑子', 2, 'GP1732425017943670786', 0, 1, 'https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312062137300.webp', NULL, 0, '2023-12-09 11:07:19', '2023-12-09 11:07:23');
INSERT INTO `t_message` VALUES ('9', '2', '小黑子', 2, 'GP1732425017943670786', 0, 1, 'https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312062137300.webp', NULL, 0, '2023-12-09 11:07:19', '2023-12-09 11:07:23');

-- ----------------------------
-- Table structure for t_message_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_message_detail`;
CREATE TABLE `t_message_detail`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
  `send_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送人id',
  `accept_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接受者id（可能是人也可能是群组的id)',
  `send_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送人头像',
  `send_nickname` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送人昵称',
  `send_time` datetime NULL DEFAULT NULL COMMENT '发送时间',
  `content_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送内容类型',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '发送内容',
  `is_deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_message_detail
-- ----------------------------
INSERT INTO `t_message_detail` VALUES ('1', '1', '6', 'https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312062137299.webp', '管理员', '2023-12-07 20:14:05', 'message', '鸡你太美', 0, '2023-12-07 20:14:05', '2023-12-07 20:14:05');
INSERT INTO `t_message_detail` VALUES ('2', '6', '1', 'https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312071944908.webp', '珍德食泥鸭', '2023-12-07 20:32:23', 'message', '泥杆嘛哎哟', 0, '2023-12-07 20:32:23', '2023-12-07 20:32:25');
INSERT INTO `t_message_detail` VALUES ('3', '1', 'GP2', 'https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312062137299.webp', '管理员', '2023-12-07 21:02:13', 'message', '泰裤辣！', 0, '2023-12-07 21:02:13', '2023-12-07 21:02:15');
INSERT INTO `t_message_detail` VALUES ('4', '6', 'GP2', 'https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312071944908.webp', '珍德食泥鸭', '2023-12-07 21:03:24', 'message', '大家好我是练习时长两年半的偶像练习生', 0, '2023-12-07 21:03:24', '2023-12-07 21:03:27');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
  `account` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户账号',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `nickname` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像url',
  `email` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `is_disabled` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '是否禁用',
  `is_deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', 'admin', '96e79218965eb72c92a549dd5a330112', '管理员', 'https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312062137299.webp', '1849569695@qq.com', '2023-12-05 11:22:53', '2023-12-05 11:22:57', 0, 0, 'admin', 'admin');
INSERT INTO `t_user` VALUES ('2', 'test', '96e79218965eb72c92a549dd5a330112', '测试', 'https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312062137300.webp', '2371119023@qq.com', '2023-12-05 11:24:34', '2023-12-05 11:24:38', 0, 0, 'admin', 'admin');
INSERT INTO `t_user` VALUES ('3', 'xiaolin', '96e79218965eb72c92a549dd5a330112', '小林子', 'https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312071940273.webp', 'xiaolin_zii@163.com', '2023-12-07 19:41:45', '2023-12-07 19:41:48', 0, 0, 'admin', 'admin');
INSERT INTO `t_user` VALUES ('4', 'haoxiaoma', '96e79218965eb72c92a549dd5a330112', '嚎笑马', 'https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312071943164.webp', 'hxm@qq.com', '2023-12-07 21:33:38', '2023-12-07 19:43:39', 0, 0, 'admin', 'admin');
INSERT INTO `t_user` VALUES ('5', 'niganma', '96e79218965eb72c92a549dd5a330112', '泥干马', 'https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312071944499.webp', 'ngm@qq.com', '2023-12-07 19:45:02', '2023-12-07 19:45:04', 0, 0, 'admin', 'admin');
INSERT INTO `t_user` VALUES ('6', 'zdsny', '96e79218965eb72c92a549dd5a330112', '珍德食泥鸭', 'https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312071944908.webp', 'zdsny@qq.com', '2023-12-07 19:46:17', '2023-12-07 19:46:21', 0, 0, 'admin', 'admin');

-- ----------------------------
-- Table structure for t_user_group
-- ----------------------------
DROP TABLE IF EXISTS `t_user_group`;
CREATE TABLE `t_user_group`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `group_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属群组id',
  `is_deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_group
-- ----------------------------
INSERT INTO `t_user_group` VALUES ('1', '1', 'GP1', 0, '2023-12-06 23:33:05', '2023-12-06 23:33:07');
INSERT INTO `t_user_group` VALUES ('10', '4', 'GP1', 0, '2023-12-07 19:51:54', '2023-12-07 19:51:49');
INSERT INTO `t_user_group` VALUES ('11', '1', 'GP2', 0, '2023-12-07 19:53:30', '2023-12-07 19:53:30');
INSERT INTO `t_user_group` VALUES ('12', '2', 'GP2', 0, '2023-12-07 19:53:30', '2023-12-07 19:53:30');
INSERT INTO `t_user_group` VALUES ('13', '3', 'GP2', 0, '2023-12-07 19:53:30', '2023-12-07 19:53:30');
INSERT INTO `t_user_group` VALUES ('14', '4', 'GP2', 0, '2023-12-07 19:53:30', '2023-12-07 19:53:30');
INSERT INTO `t_user_group` VALUES ('15', '5', 'GP2', 0, '2023-12-07 19:53:30', '2023-12-07 19:53:30');
INSERT INTO `t_user_group` VALUES ('1732788337477435393', '1', 'GP2fa9731ecc9b4c48', 0, '2023-12-07 23:45:08', '2023-12-07 23:45:08');
INSERT INTO `t_user_group` VALUES ('2', '2', 'GP1', 0, '2023-12-06 23:33:20', '2023-12-06 23:33:23');
INSERT INTO `t_user_group` VALUES ('3', '1', 'GP1732425017943670786', 0, '2023-12-07 19:50:19', '2023-12-07 19:50:22');
INSERT INTO `t_user_group` VALUES ('4', '2', 'GP1732425017943670786', 0, '2023-12-07 19:51:46', '2023-12-07 19:51:49');
INSERT INTO `t_user_group` VALUES ('5', '3', 'GP1732425017943670786', 0, '2023-12-07 19:51:52', '2023-12-07 19:51:49');
INSERT INTO `t_user_group` VALUES ('6', '4', 'GP1732425017943670786', 0, '2023-12-07 19:51:54', '2023-12-07 19:51:49');
INSERT INTO `t_user_group` VALUES ('7', '5', 'GP1732425017943670786', 0, '2023-12-07 19:51:54', '2023-12-07 19:51:49');
INSERT INTO `t_user_group` VALUES ('8', '6', 'GP1732425017943670786', 0, '2023-12-07 19:51:54', '2023-12-07 19:51:49');
INSERT INTO `t_user_group` VALUES ('9', '3', 'GP1', 0, '2023-12-07 19:51:54', '2023-12-07 19:51:49');

SET FOREIGN_KEY_CHECKS = 1;
