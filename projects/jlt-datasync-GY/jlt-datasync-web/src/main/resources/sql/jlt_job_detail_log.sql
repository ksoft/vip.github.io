/*
Navicat MySQL Data Transfer

Source Server         : 192.168.0.173
Source Server Version : 50621
Source Host           : 192.168.0.173:3306
Source Database       : dev_gy_pms

Target Server Type    : MYSQL
Target Server Version : 50621
File Encoding         : 65001

Date: 2018-09-20 11:29:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for jlt_job_detail_log
-- ----------------------------
DROP TABLE IF EXISTS `jlt_job_detail_log`;
CREATE TABLE `jlt_job_detail_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `department_id` bigint(20) DEFAULT NULL COMMENT '部门主键',
  `job_detail_id` bigint(20) NOT NULL COMMENT '任务明细id',
  `schema_name` varchar(255) DEFAULT NULL COMMENT '库名(sass/upm/pms)',
  `table_name` varchar(50) DEFAULT NULL COMMENT '表名称',
  `msg` varchar(4000) DEFAULT NULL COMMENT '成功或失败消息',
  `rec_ver` int(8) DEFAULT NULL COMMENT '版本号',
  `rec_status` int(1) DEFAULT NULL COMMENT '逻辑删除：0-正常，1-删除',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_name` varchar(32) DEFAULT NULL COMMENT '创建人名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modifier` varchar(32) DEFAULT NULL COMMENT '修改人',
  `modify_name` varchar(32) DEFAULT NULL COMMENT '修改人名称',
  `modify_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6951 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='晶链通数据同步作业日志';
