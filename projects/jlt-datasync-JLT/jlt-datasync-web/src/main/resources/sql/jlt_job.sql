/*
Navicat MySQL Data Transfer

Source Server         : 192.168.0.173
Source Server Version : 50621
Source Host           : 192.168.0.173:3306
Source Database       : 4.1_dev_jlt_pms

Target Server Type    : MYSQL
Target Server Version : 50621
File Encoding         : 65001

Date: 2018-09-20 11:29:20
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for jlt_job
-- ----------------------------
DROP TABLE IF EXISTS `jlt_job`;
CREATE TABLE `jlt_job` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `department_id` bigint(20) DEFAULT NULL COMMENT '部门主键',
  `job_name` varchar(50) DEFAULT NULL COMMENT '任务名称',
  `is_enable_saas` char(1) DEFAULT 'Y' COMMENT '是否启用SaaS',
  `is_enable_upm` char(1) DEFAULT 'N',
  `is_enable_pms` char(1) DEFAULT 'Y',
  `last_exec_success_time` datetime DEFAULT NULL COMMENT '上一次成功执行的时间，如果失败，不更新此字段',
  `last_exec_result` char(1) DEFAULT NULL COMMENT '上一次执行结果，Y:成功，N：失败',
  `rec_ver` int(8) DEFAULT NULL COMMENT '版本号',
  `rec_status` int(1) DEFAULT NULL COMMENT '逻辑删除：0-正常，1-删除',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_name` varchar(32) DEFAULT NULL COMMENT '创建人名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modifier` varchar(32) DEFAULT NULL COMMENT '修改人',
  `modify_name` varchar(32) DEFAULT NULL COMMENT '修改人名称',
  `modify_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='晶链通数据同步作业';

-- ----------------------------
-- Records of jlt_job
-- ----------------------------
INSERT INTO `jlt_job` VALUES ('1', '1001707793419464705', null, 'SAAS表同步', 'Y', 'N', 'Y', '2018-09-18 23:49:45', 'Y', '0', '0', 'admin', '系统', '2018-09-10 19:36:36', null, null, '2018-09-10 19:37:11');
