/*
 Navicat Premium Data Transfer

 Source Server         : Lei的Mysql数据库
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : localhost:3306
 Source Schema         : handicraft_community

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 09/05/2024 00:30:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for comment_operation_num
-- ----------------------------
DROP TABLE IF EXISTS `comment_operation_num`;
CREATE TABLE `comment_operation_num`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `operation_type` enum('up','down') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'up:点赞 ; down:点踩',
  `comment_id` int(0) NULL DEFAULT NULL COMMENT '评论id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '评论操作数量表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for follower
-- ----------------------------
DROP TABLE IF EXISTS `follower`;
CREATE TABLE `follower`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `poster` int(0) NULL DEFAULT NULL COMMENT '发布者id',
  `fans` int(0) NULL DEFAULT NULL COMMENT '粉丝id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '关注信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for interest
-- ----------------------------
DROP TABLE IF EXISTS `interest`;
CREATE TABLE `interest`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `interest_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '兴趣圈子名称',
  `describe` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '兴趣圈子实体表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for interest_post_rel
-- ----------------------------
DROP TABLE IF EXISTS `interest_post_rel`;
CREATE TABLE `interest_post_rel`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `interest_id` int(0) NULL DEFAULT NULL COMMENT '兴趣圈子id',
  `post_id` int(0) NULL DEFAULT NULL COMMENT '发布者id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '兴趣圈子和发帖关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for personal_message
-- ----------------------------
DROP TABLE IF EXISTS `personal_message`;
CREATE TABLE `personal_message`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `send_user_id` int(0) NULL DEFAULT NULL COMMENT '发送消息用户id',
  `send_message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送消息',
  `send_time` datetime(0) NULL DEFAULT NULL COMMENT '发送时间',
  `is_read` bit(1) NULL DEFAULT NULL COMMENT '是否可读',
  `recipient_id` int(0) NULL DEFAULT NULL COMMENT '接受消息用户id',
  `receive_time` datetime(0) NULL DEFAULT NULL COMMENT '接收时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '私信信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for post
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面',
  `video_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '视频url',
  `tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签',
  `look_num` int(0) NULL DEFAULT NULL COMMENT '观看数量',
  `comment_num` int(0) NULL DEFAULT NULL COMMENT '评论数量',
  `like_num` int(0) NULL DEFAULT NULL COMMENT '点赞数量',
  `dislike_num` int(0) NULL DEFAULT NULL COMMENT '不喜欢数量',
  `coin_num` int(0) NULL DEFAULT NULL COMMENT '投币数量',
  `collect_num` int(0) NULL DEFAULT NULL COMMENT '收藏数量',
  `share_num` int(0) NULL DEFAULT NULL COMMENT '分享数量',
  `status` enum('publish','agree','disagree','remove') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'publish：已发布\r\nagree：同意\r\ndisagree：不同意\r\nremove：冻结/删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '帖子信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for post_attachment
-- ----------------------------
DROP TABLE IF EXISTS `post_attachment`;
CREATE TABLE `post_attachment`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `post_id` int(0) NULL DEFAULT NULL COMMENT '帖子id',
  `attachment_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件url',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '帖子附件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for post_comment
-- ----------------------------
DROP TABLE IF EXISTS `post_comment`;
CREATE TABLE `post_comment`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `post_id` int(0) NULL DEFAULT NULL COMMENT '帖子id',
  `comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评论',
  `to_user_id` int(0) NULL DEFAULT NULL COMMENT '回复的用户id',
  `up_num` int(0) NULL DEFAULT NULL COMMENT '点赞数量',
  `down_num` int(0) NULL DEFAULT NULL COMMENT '点踩数量',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '帖子评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for post_operation_num
-- ----------------------------
DROP TABLE IF EXISTS `post_operation_num`;
CREATE TABLE `post_operation_num`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `operation_type` enum('like','coin','share','collect','dislike','look') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '点赞，投币，分享，收藏',
  `post_id` int(0) NULL DEFAULT NULL COMMENT '帖子id',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '帖子操作数量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `role_name` enum('admin','user') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户角色',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `sex` enum('man','woman','unknow') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '性别',
  `status` enum('available','unusable') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
  `country` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '国家',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `coin_num` int(0) NULL DEFAULT NULL COMMENT '硬币数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'test1', 'cafe5b9a578b701a1110cb22f733d17d', 'user', NULL, NULL, NULL, 'available', NULL, '2024-04-24 00:15:13', '2024-04-24 00:15:13', NULL);

-- ----------------------------
-- Table structure for user_post_rel
-- ----------------------------
DROP TABLE IF EXISTS `user_post_rel`;
CREATE TABLE `user_post_rel`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `post_id` int(0) NULL DEFAULT NULL COMMENT '帖子id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户发帖关系表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
