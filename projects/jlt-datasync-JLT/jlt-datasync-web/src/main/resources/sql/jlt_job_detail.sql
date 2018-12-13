/*
Navicat MySQL Data Transfer

Source Server         : 192.168.0.173
Source Server Version : 50621
Source Host           : 192.168.0.173:3306
Source Database       : 4.1_dev_jlt_pms

Target Server Type    : MYSQL
Target Server Version : 50621
File Encoding         : 65001

Date: 2018-09-20 11:29:26
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for jlt_job_detail
-- ----------------------------
DROP TABLE IF EXISTS `jlt_job_detail`;
CREATE TABLE `jlt_job_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `department_id` bigint(20) DEFAULT NULL COMMENT '部门主键',
  `job_id` bigint(20) NOT NULL COMMENT '任务名称',
  `job_type` varchar(1) NOT NULL DEFAULT 'R' COMMENT '任务类型：R：读，W：写',
  `schema_name` varchar(255) DEFAULT NULL COMMENT '库名(sass/upm/pms)',
  `table_name` varchar(50) DEFAULT NULL COMMENT '表名称',
  `is_exclude_time` char(1) DEFAULT 'N' COMMENT '抽取时，是否忽略时间字段，Y:是；N：否，不根据创建时间和修改时间抽取',
  `is_exclude_tenant_id` char(1) DEFAULT 'N' COMMENT '抽取时，是否忽略tenant_id，比如字典表等；Y:是，N:否',
  `tenant_id_condition` longtext COMMENT '需要过虑的tenant_id数据条件，以and开头',
  `is_direct_replace_into` varchar(1) DEFAULT 'Y' COMMENT '是否直接根据主键和唯一约束replace into',
  `unique_key_column` varchar(100) DEFAULT NULL COMMENT '插入或更新时，可以找到唯一数据的字段',
  `last_exec_success_time` datetime DEFAULT NULL COMMENT '上一次成功执行的时间，如果失败，不更新此字段',
  `last_exec_result` char(1) DEFAULT NULL COMMENT '上一次执行结果，Y:成功，N：失败',
  `err_msg` longtext,
  `extend_where_condition` longtext COMMENT '扩展的where条件，以and开头',
  `order_id` int(11) DEFAULT NULL COMMENT '写入时若需要删除数据，可能涉及删除时的先后关系，按序号从小到大删除',
  `rec_ver` int(8) DEFAULT NULL COMMENT '版本号',
  `rec_status` int(1) DEFAULT NULL COMMENT '逻辑删除：0-正常，1-删除',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_name` varchar(32) DEFAULT NULL COMMENT '创建人名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modifier` varchar(32) DEFAULT NULL COMMENT '修改人',
  `modify_name` varchar(32) DEFAULT NULL COMMENT '修改人名称',
  `modify_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_1` (`schema_name`,`table_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='晶链通数据同步作业';

-- ----------------------------
-- Records of jlt_job_detail
-- ----------------------------
INSERT INTO `jlt_job_detail` VALUES ('1', '1001707793419464705', null, '1', 'R', 'saas', 'et_waybill_event_info', 'N', 'N', 'select carrier_id from cd_carrier_config where bind_type=2 and tenant_id =  tenant_id_replace', 'Y', null, null, null, null, null, null, '0', '0', 'admin', 'sys', '2018-09-11 16:52:09', '', '', '2018-09-10 20:23:04');
INSERT INTO `jlt_job_detail` VALUES ('2', '1001707793419464705', null, '1', 'R', 'saas', 'et_waybill_exception', 'N', 'N', 'select carrier_id from cd_carrier_config where bind_type=2 and tenant_id =  tenant_id_replace', 'Y', null, null, null, null, null, null, '0', '0', 'admin', 'sys', '2018-09-11 16:52:09', '', '', '2018-09-10 20:23:04');
INSERT INTO `jlt_job_detail` VALUES ('3', '1001707793419464705', null, '1', 'R', 'saas', 'et_waybill_exception_detail', 'N', 'N', 'select carrier_id from cd_carrier_config where bind_type=2 and tenant_id =  tenant_id_replace', 'Y', null, null, null, null, null, null, '0', '0', 'admin', 'sys', '2018-09-11 16:52:09', '', '', '2018-09-10 20:23:04');
INSERT INTO `jlt_job_detail` VALUES ('4', '1001707793419464705', null, '1', 'R', 'saas', 'et_waybill_exception_handle', 'N', 'N', 'select carrier_id from cd_carrier_config where bind_type=2 and tenant_id =  tenant_id_replace', 'Y', null, null, null, null, null, null, '0', '0', 'admin', 'sys', '2018-09-11 16:52:09', '', '', '2018-09-10 20:23:04');
INSERT INTO `jlt_job_detail` VALUES ('5', '1001707793419464705', null, '1', 'R', 'saas', 'et_waybill_info', 'N', 'N', 'select carrier_id from cd_carrier_config where bind_type=2 and tenant_id =  tenant_id_replace', 'Y', null, null, null, null, 'and dispatch_no !=\'\' and dispatch_no is not null', null, '0', '0', 'admin', 'sys', '2018-09-11 16:52:09', '', '', '2018-09-10 20:23:04');
INSERT INTO `jlt_job_detail` VALUES ('6', '1001707793419464705', null, '1', 'R', 'saas', 'et_waybill_info_ext', 'N', 'N', 'select carrier_id from cd_carrier_config where bind_type=2 and tenant_id =  tenant_id_replace', 'Y', null, null, null, null, null, null, '0', '0', 'admin', 'sys', '2018-09-11 16:52:09', '', '', '2018-09-10 20:23:04');
INSERT INTO `jlt_job_detail` VALUES ('7', '1001707793419464705', null, '1', 'R', 'saas', 'et_waybill_item', 'N', 'N', 'select carrier_id from cd_carrier_config where bind_type=2 and tenant_id =  tenant_id_replace', 'Y', null, null, null, '', '', null, '0', '0', 'admin', 'sys', '2018-09-11 16:52:09', '', '', '2018-09-10 20:23:04');
INSERT INTO `jlt_job_detail` VALUES ('9', '1001707793419464705', null, '1', 'W', 'saas', 'mon_alert_rule', 'N', 'N', null, 'N', 'alert_rule_type', null, null, '', 'and alert_rule_code = \'TEMPERATURE_EXCEEDED_ALARM\'', '3', '0', '0', 'admin', 'sys', '2018-09-11 16:52:09', '', '', '2018-09-10 20:23:04');
INSERT INTO `jlt_job_detail` VALUES ('10', '1001707793419464705', null, '1', 'W', 'saas', 'mon_alert_rule_notify', 'N', 'N', '', 'N', 'alert_rule_pm_code', null, null, '', 'and alert_rule_pm_code =(select pm_code from  mon_alert_rule where tenant_id = tenant_id_replace and alert_rule_code = \'TEMPERATURE_EXCEEDED_ALARM\')', '2', '0', '0', 'admin', 'sys', '2018-09-11 16:52:09', '', '', '2018-09-10 20:23:04');
