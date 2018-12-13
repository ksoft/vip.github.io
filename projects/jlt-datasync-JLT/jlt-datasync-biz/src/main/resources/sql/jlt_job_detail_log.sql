/*
Navicat MySQL Data Transfer

Source Server         : 晶链通&国药-uat
Source Server Version : 50634
Source Host           : rm-uf6h5m5e6j59v169bo.mysql.rds.aliyuncs.com:3306
Source Database       : uat_jlt_pms

Target Server Type    : MYSQL
Target Server Version : 50634
File Encoding         : 65001

Date: 2018-12-13 11:15:46
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
  `msg` longtext COMMENT '成功或失败消息',
  `rec_ver` int(8) DEFAULT NULL COMMENT '版本号',
  `rec_status` int(1) DEFAULT NULL COMMENT '逻辑删除：0-正常，1-删除',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_name` varchar(32) DEFAULT NULL COMMENT '创建人名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modifier` varchar(32) DEFAULT NULL COMMENT '修改人',
  `modify_name` varchar(32) DEFAULT NULL COMMENT '修改人名称',
  `modify_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='晶链通数据同步作业日志';
