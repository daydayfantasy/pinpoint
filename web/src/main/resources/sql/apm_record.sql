/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : ppoint

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-05-25 17:35:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `apm_record`
-- ----------------------------
DROP TABLE IF EXISTS `apm_record`;
CREATE TABLE `apm_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `applicationId` varchar(255) DEFAULT NULL,
  `serviceType` varchar(255) DEFAULT NULL,
  `smsMessage` varchar(500) DEFAULT NULL,
  `checkName` varchar(255) DEFAULT NULL,
  `inTime` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of apm_record
-- ----------------------------
INSERT INTO `apm_record` VALUES ('1', 'Jacob', 'TOMCAT', '[PINPOINT Alarm - jacob] HEAP USAGE RATE is 5% (Threshold : 1%)', 'HEAP USAGE RATE', '2018-05-24');
INSERT INTO `apm_record` VALUES ('2', 'Jacob', 'TOMCAT', '[PINPOINT Alarm - jacob] HEAP USAGE RATE is 5% (Threshold : 1%)', 'HEAP USAGE RATE', '2018-05-24');
INSERT INTO `apm_record` VALUES ('3', 'Jacob', 'TOMCAT', '[PINPOINT Alarm - jacob] HEAP USAGE RATE is 8% (Threshold : 1%)', 'HEAP USAGE RATE', '2018-05-24');
INSERT INTO `apm_record` VALUES ('4', 'Jacob', 'TOMCAT', '[PINPOINT Alarm - jacob] HEAP USAGE RATE is 8% (Threshold : 1%)', 'HEAP USAGE RATE', '2018-05-24');
