/*
Navicat MySQL Data Transfer

Source Server         : sql
Source Server Version : 50734
Source Host           : 43.129.222.223:3306
Source Database       : picc

Target Server Type    : MYSQL
Target Server Version : 50734
File Encoding         : 65001

Date: 2021-07-16 16:47:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for picc_admin_manager
-- ----------------------------
DROP TABLE IF EXISTS `picc_admin_manager`;
CREATE TABLE `picc_admin_manager` (
  `mg_id` int(11) NOT NULL AUTO_INCREMENT,
  `mg_name` varchar(255) NOT NULL,
  `mg_pwd` varchar(255) NOT NULL,
  `mg_ctime` varchar(13) NOT NULL,
  `mg_phone` varchar(255) NOT NULL,
  `mg_email` varchar(255) NOT NULL,
  `mg_state` varchar(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`mg_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of picc_admin_manager
-- ----------------------------
INSERT INTO `picc_admin_manager` VALUES ('19', 'admin', '/Pl31Rb10nzd0ow0OCTDGQ==', '1621088807739', '15827133190', '2723166086@qq.com', '1');
INSERT INTO `picc_admin_manager` VALUES ('20', 'test', 'MdD4GQDVW3o=', '1621886414066', '15827133190', '2723166086@qq.com', '0');
INSERT INTO `picc_admin_manager` VALUES ('21', 'tet123', '30RtkokFvm4=', '1621887720279', '15827133190', '1234@163.com', '0');

-- ----------------------------
-- Table structure for picc_admin_menu
-- ----------------------------
DROP TABLE IF EXISTS `picc_admin_menu`;
CREATE TABLE `picc_admin_menu` (
  `id` int(11) NOT NULL,
  `menu_name` varchar(255) NOT NULL,
  `menu_pid` int(11) NOT NULL,
  `menu_level` int(11) NOT NULL,
  `menu_path` varchar(255) NOT NULL,
  `menu_order` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of picc_admin_menu
-- ----------------------------
INSERT INTO `picc_admin_menu` VALUES ('1', '用户管理', '0', '1', 'nurses', '1');
INSERT INTO `picc_admin_menu` VALUES ('3', '护士列表', '1', '2', 'nurses', '1');
INSERT INTO `picc_admin_menu` VALUES ('4', '患者列表', '1', '2', 'patients', '2');
INSERT INTO `picc_admin_menu` VALUES ('10', '身份审核', '0', '1', 'nurseCheck', '2');
INSERT INTO `picc_admin_menu` VALUES ('11', '护士身份', '10', '2', 'nurseCheck', '1');
INSERT INTO `picc_admin_menu` VALUES ('20', '绑定信息', '0', '1', 'patientBinding', '3');
INSERT INTO `picc_admin_menu` VALUES ('21', '患者绑定', '20', '2', 'patientBinding', '1');
INSERT INTO `picc_admin_menu` VALUES ('30', '排班管理', '0', '1', 'schedule', '4');
INSERT INTO `picc_admin_menu` VALUES ('31', '护士排班', '30', '2', 'schedule', '1');
INSERT INTO `picc_admin_menu` VALUES ('40', '记录查看', '0', '1', 'catheter', '5');
INSERT INTO `picc_admin_menu` VALUES ('50', '信息统计', '0', '1', 'statistic', '6');
INSERT INTO `picc_admin_menu` VALUES ('51', '统计报表', '50', '2', 'statistic', '1');
INSERT INTO `picc_admin_menu` VALUES ('60', '留言板管理', '0', '1', 'opinion', '7');
INSERT INTO `picc_admin_menu` VALUES ('61', '留言板管理', '60', '2', 'opinion', '1');
INSERT INTO `picc_admin_menu` VALUES ('5', '管理员列表', '1', '2', 'managers', '3');
INSERT INTO `picc_admin_menu` VALUES ('44', '记录查看', '40', '3', 'record', '4');

-- ----------------------------
-- Table structure for picc_department
-- ----------------------------
DROP TABLE IF EXISTS `picc_department`;
CREATE TABLE `picc_department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `department` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of picc_department
-- ----------------------------
INSERT INTO `picc_department` VALUES ('1', '科室1');
INSERT INTO `picc_department` VALUES ('2', '科室2');

-- ----------------------------
-- Table structure for picc_nurse
-- ----------------------------
DROP TABLE IF EXISTS `picc_nurse`;
CREATE TABLE `picc_nurse` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(100) NOT NULL COMMENT 'user_ID 参考user表',
  `job_number` varchar(64) NOT NULL COMMENT '工号',
  `name` varchar(16) NOT NULL COMMENT '姓名',
  `sex` varchar(2) NOT NULL COMMENT '性别',
  `phone` varchar(32) NOT NULL COMMENT '电话号码',
  `department` varchar(64) NOT NULL COMMENT '部门科室',
  `create_time` varchar(13) NOT NULL COMMENT '添加时间',
  `state` varchar(2) NOT NULL DEFAULT '0' COMMENT '是否已认证',
  PRIMARY KEY (`id`),
  KEY `job_number_INDEX` (`job_number`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of picc_nurse
-- ----------------------------
INSERT INTO `picc_nurse` VALUES ('9', 'oDVoB5CP6kYY5k9VPdn-UuWSxil4', '176253', '测试护士2', '男', '15726244178', '科室2', '1622257549600', '1');
INSERT INTO `picc_nurse` VALUES ('10', 'oDVoB5EtMbajsZ6eOB2TW9BvUydM', '1827363', '测试护士3', '女', '15827144190', '科室2', '1622257889526', '1');
INSERT INTO `picc_nurse` VALUES ('11', 'oDVoB5BhP3wX2bKD8jlnPbFf48jY', '1762435', '测试护士4', '男', '17265344190', '科室2', '1622258183943', '1');
INSERT INTO `picc_nurse` VALUES ('12', 'oDVoB5Mlk9tYxUrHCd3YN76curbI', '111133', '测试护士1', '男', '15727117891', '科室1', '1622288270252', '1');

-- ----------------------------
-- Table structure for picc_nurse_schedule
-- ----------------------------
DROP TABLE IF EXISTS `picc_nurse_schedule`;
CREATE TABLE `picc_nurse_schedule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nurse_name` varchar(255) NOT NULL,
  `job_number` varchar(255) NOT NULL,
  `appointment_date` varchar(13) NOT NULL,
  `forenoon` varchar(255) DEFAULT 'false',
  `forenoon_number` int(11) DEFAULT '0',
  `forenoon_limit` int(11) DEFAULT '6',
  `afternoon` varchar(255) DEFAULT 'false',
  `afternoon_number` int(11) DEFAULT '0',
  `afternoon_limit` int(11) DEFAULT '6',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=509 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of picc_nurse_schedule
-- ----------------------------
INSERT INTO `picc_nurse_schedule` VALUES ('468', '测试护士2', '176253', '1622304000000', 'true', '0', '6', 'false', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('469', '测试护士3', '1827363', '1622304000000', 'true', '0', '6', 'false', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('470', '测试护士4', '1762435', '1622304000000', 'true', '0', '6', 'false', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('471', '测试护士2', '176253', '1622390400000', 'true', '1', '6', 'false', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('472', '测试护士4', '1762435', '1622390400000', 'true', '0', '6', 'true', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('473', '测试护士2', '176253', '1622476800000', 'true', '0', '6', 'true', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('474', '测试护士4', '1762435', '1622476800000', 'true', '0', '6', 'true', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('475', '测试护士3', '1827363', '1622390400000', 'false', '0', '6', 'true', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('476', '测试护士2', '176253', '1622563200000', 'false', '0', '6', 'false', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('477', '测试护士3', '1827363', '1622563200000', 'false', '0', '6', 'false', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('478', '测试护士4', '1762435', '1622563200000', 'false', '0', '6', 'false', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('479', '测试护士1', '111133', '1622563200000', 'true', '1', '6', 'fasle', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('480', '测试护士3', '1827363', '1622476800000', 'false', '0', '6', 'true', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('481', '测试护士1', '111133', '1622476800000', 'false', '0', '6', 'true', '1', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('483', '测试护士2', '176253', '1622736000000', 'true', '0', '6', 'false', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('484', '测试护士3', '1827363', '1622736000000', 'true', '0', '6', 'false', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('485', '测试护士4', '1762435', '1622736000000', 'false', '0', '6', 'false', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('486', '测试护士1', '111133', '1622736000000', 'false', '0', '6', 'true', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('487', '测试护士1', '111133', '1622822400000', 'true', '1', '6', 'false', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('488', '测试护士4', '1762435', '1622822400000', 'true', '0', '6', 'false', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('489', '测试护士4', '1762435', '1622908800000', 'true', '0', '6', 'true', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('490', '测试护士1', '111133', '1622908800000', 'true', '0', '6', 'true', '-1', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('491', '测试护士4', '1762435', '1650816000000', 'true', '0', '6', 'false', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('492', '测试护士1', '111133', '1650816000000', 'true', '0', '6', 'false', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('493', '测试护士3', '1827363', '1650816000000', 'true', '0', '6', 'false', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('494', '测试护士2', '176253', '1624377600000', 'true', '0', '6', 'false', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('495', '测试护士2', '176253', '1622764800000', 'false', '0', '6', 'false', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('496', '测试护士3', '1827363', '1622764800000', 'false', '0', '6', 'false', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('497', '测试护士4', '1762435', '1622764800000', 'false', '0', '6', 'false', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('498', '测试护士1', '111133', '1622764800000', 'false', '0', '6', 'true', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('499', '测试护士2', '176253', '1622505600000', 'false', '0', '6', 'false', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('500', '测试护士3', '1827363', '1622505600000', 'false', '0', '6', 'false', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('501', '测试护士4', '1762435', '1622505600000', 'false', '0', '6', 'false', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('502', '测试护士1', '111133', '1622505600000', 'false', '0', '6', 'true', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('503', '测试护士2', '176253', '1622678400000', 'false', '0', '6', 'true', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('504', '测试护士3', '1827363', '1622678400000', 'false', '0', '6', 'true', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('505', '测试护士4', '1762435', '1622678400000', 'false', '0', '6', 'true', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('506', '测试护士1', '111133', '1622678400000', 'false', '0', '6', 'true', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('507', '测试护士4', '1762435', '1623772800000', 'true', '0', '6', 'false', '0', '6');
INSERT INTO `picc_nurse_schedule` VALUES ('508', '测试护士1', '111133', '1624550400000', 'false', '0', '6', 'true', '0', '6');

-- ----------------------------
-- Table structure for picc_opinion
-- ----------------------------
DROP TABLE IF EXISTS `picc_opinion`;
CREATE TABLE `picc_opinion` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(100) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `problem` varchar(255) DEFAULT NULL,
  `ctime` varchar(13) DEFAULT NULL,
  `whether_to_solve` varchar(255) DEFAULT NULL,
  `solution` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of picc_opinion
-- ----------------------------
INSERT INTO `picc_opinion` VALUES ('5', 'oDVoB5Mlk9tYxUrHCd3YN76curbI', '本院职工', '测试护士1', '测试', '1621674637217', '未回复', null);
INSERT INTO `picc_opinion` VALUES ('6', 'oDVoB5Mlk9tYxUrHCd3YN76curbI', '外院专家', '测试专家1', '1212', '1622205400874', '已回复', '12324');
INSERT INTO `picc_opinion` VALUES ('7', 'oDVoB5CP6kYY5k9VPdn-UuWSxil4', '患者', '测试患者2', '测试', '1622352841896', '未回复', null);
INSERT INTO `picc_opinion` VALUES ('8', 'oDVoB5CP6kYY5k9VPdn-UuWSxil4', '患者', '测试患者2', 'TEST', '1622354645575', '未回复', null);
INSERT INTO `picc_opinion` VALUES ('9', 'oDVoB5Mlk9tYxUrHCd3YN76curbI', '患者', '测试患者1', '121313', '1622957333580', '未回复', null);

-- ----------------------------
-- Table structure for picc_patient
-- ----------------------------
DROP TABLE IF EXISTS `picc_patient`;
CREATE TABLE `picc_patient` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID 参考wx_users表',
  `user_id` varchar(100) NOT NULL COMMENT 'user_ID 参考user表',
  `patient_num` varchar(16) DEFAULT NULL COMMENT '病人的病例号',
  `name` varchar(16) NOT NULL COMMENT '姓名',
  `sex` varchar(2) DEFAULT NULL COMMENT '性别',
  `birthday` varchar(20) DEFAULT '0' COMMENT '生日，默认是unix时间0',
  `culture` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `phone` varchar(16) DEFAULT NULL COMMENT '联系电话',
  `create_time` varchar(13) NOT NULL,
  `address` varchar(255) DEFAULT NULL COMMENT '居住地/详细地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='患者表';

-- ----------------------------
-- Records of picc_patient
-- ----------------------------
INSERT INTO `picc_patient` VALUES ('10', 'oDVoB5CP6kYY5k9VPdn-UuWSxil4', 'HZ21052701', '测试患者2', '男', '1999-1-1', '文盲', '洪山区', '15827133190', '1622038237010', '12121');
INSERT INTO `picc_patient` VALUES ('12', 'oDVoB5EtMbajsZ6eOB2TW9BvUydM', 'HZ21052901', '测试患者3', '男', '2015-09-01', '小学', '洪山区', '15827266190', '1622257754536', '121212');
INSERT INTO `picc_patient` VALUES ('13', 'oDVoB5BhP3wX2bKD8jlnPbFf48jY', 'HZ21052902', '测试患者4', '男', '2017-11-01', '小学', '洪山区', '13597537407', '1622258068691', '121212');
INSERT INTO `picc_patient` VALUES ('14', 'oDVoB5N4gXjHs7FQz6HlnFN9q8jo', 'HZ21052903', '测试患者5', '男', '1999-1-1', '高中', '洪山区', '15827144190', '1622265986828', '1212121');
INSERT INTO `picc_patient` VALUES ('16', 'oDVoB5Mlk9tYxUrHCd3YN76curbI', 'HZ21052900', '测试患者1', '男', '1999-1-1', '大专及以上', '洪山区', '15827155190', '1622287992471', '12121212');

-- ----------------------------
-- Table structure for picc_patient_appointment
-- ----------------------------
DROP TABLE IF EXISTS `picc_patient_appointment`;
CREATE TABLE `picc_patient_appointment` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `patient_num` varchar(255) DEFAULT NULL,
  `patient_name` varchar(255) DEFAULT NULL,
  `nurse_name` varchar(255) DEFAULT NULL,
  `job_number` varchar(255) DEFAULT NULL,
  `appointment_date` varchar(13) DEFAULT NULL,
  `appointment_time` varchar(255) DEFAULT NULL,
  `type` varchar(255) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `c_time` varchar(13) DEFAULT NULL,
  `record_id` int(10) DEFAULT NULL,
  `schedule_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of picc_patient_appointment
-- ----------------------------
INSERT INTO `picc_patient_appointment` VALUES ('31', 'HZ21052701', '测试患者2', '测试护士1', '111133', '1622476800000', '下午', '维护', '3', '1622352668806', '9', '481');
INSERT INTO `picc_patient_appointment` VALUES ('32', 'HZ21052701', '测试患者2', '测试护士1', '111133', '1622822400000', '上午', '维护', '3', '1622353250007', '10', '487');
INSERT INTO `picc_patient_appointment` VALUES ('33', 'HZ21052701', '测试患者2', '测试护士1', '111133', '1622563200000', '上午', '维护', '3', '1622353913800', '12', '479');
INSERT INTO `picc_patient_appointment` VALUES ('37', 'HZ21052701', '测试患者2', '测试护士1', '111133', '1622736000000', '下午', '维护', '3', '1622354528826', '13', '486');

-- ----------------------------
-- Table structure for picc_patient_binding
-- ----------------------------
DROP TABLE IF EXISTS `picc_patient_binding`;
CREATE TABLE `picc_patient_binding` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_number` varchar(64) NOT NULL,
  `nurse_name` varchar(16) NOT NULL,
  `patient_num` varchar(16) NOT NULL,
  `patient_name` varchar(16) NOT NULL,
  `create_time` varchar(13) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of picc_patient_binding
-- ----------------------------
INSERT INTO `picc_patient_binding` VALUES ('6', '111133', '测试护士1', 'HZ21052701', '测试患者2', '1622056037419');
INSERT INTO `picc_patient_binding` VALUES ('7', '111133', '测试护士1', 'HZ21052900', '测试患者1', '1622270220401');

-- ----------------------------
-- Table structure for picc_patient_catheter
-- ----------------------------
DROP TABLE IF EXISTS `picc_patient_catheter`;
CREATE TABLE `picc_patient_catheter` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `patient_name` varchar(255) NOT NULL,
  `patient_num` varchar(16) NOT NULL,
  `disease_type` varchar(255) DEFAULT NULL,
  `first_catheter` varchar(255) DEFAULT NULL,
  `catheter_time` varchar(13) DEFAULT NULL,
  `catheter_department` varchar(255) DEFAULT NULL,
  `catheter_opportunity` varchar(255) DEFAULT NULL,
  `catheter_model` varchar(255) DEFAULT NULL,
  `catheter_specification` varchar(255) DEFAULT NULL,
  `catheter_batchnumber` varchar(255) DEFAULT NULL,
  `catheter_length` double(255,0) DEFAULT NULL,
  `puncture_method` varchar(255) DEFAULT NULL,
  `catheter_site` varchar(255) DEFAULT NULL,
  `catheter_arm` varchar(255) DEFAULT NULL,
  `catheter_vein` varchar(255) DEFAULT NULL,
  `catheter_tip_position` varchar(255) DEFAULT NULL,
  `catheter_insertion_length` double(255,0) DEFAULT NULL,
  `catheter_exposed_length` double(255,0) DEFAULT NULL,
  `arm_circumference` varchar(255) DEFAULT NULL,
  `chemotherapy_number` int(11) DEFAULT NULL COMMENT '化疗次数',
  `catheter_brand` varchar(255) DEFAULT NULL COMMENT '导管品牌',
  `nurse_jobnumber` varchar(255) DEFAULT NULL COMMENT '置管护士工号',
  `catheter_num` varchar(255) DEFAULT NULL COMMENT '置管编号',
  `record_num` varchar(255) DEFAULT NULL COMMENT 'PICC档案编号',
  `catheter_hospital` varchar(255) DEFAULT NULL COMMENT '置管医院',
  `number_of_catheterization` varchar(255) DEFAULT NULL COMMENT '第几次置管',
  `create_time` varchar(13) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of picc_patient_catheter
-- ----------------------------
INSERT INTO `picc_patient_catheter` VALUES ('1', '测试患者1', 'HZ21052900', '肝癌', 'false', '1609430000000', '科室1', '首次化疗前', '双腔三向瓣模式', '双腔XZ', '170915', '20', '盲穿', '肘上', '左侧', '贵要静脉', '气管隆突上', '18', '2', '肘上10 cm', '3', '导管品牌', '111133', 'Z2021050701', 'BH2021050701', '本院', '2', null);
INSERT INTO `picc_patient_catheter` VALUES ('4', '测试患者2', 'HZ21052701', '胃癌', 'true', '1621099425033', '科室2', '第三次化疗及以上', '21', '21', '21', '21', '21', '21', '21', '21', '21', '21', '21', '21', '2', '导管品牌', '111133', 'Z2021050702', 'BH2021050702', '本院', '3', null);

-- ----------------------------
-- Table structure for picc_patient_extubation
-- ----------------------------
DROP TABLE IF EXISTS `picc_patient_extubation`;
CREATE TABLE `picc_patient_extubation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `patient_name` varchar(16) DEFAULT NULL,
  `patient_num` varchar(16) DEFAULT NULL,
  `extubation_date` varchar(13) DEFAULT NULL,
  `indwelling_time` varchar(255) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `end_intact` varchar(255) DEFAULT NULL COMMENT '导管末端是否完整',
  `process_smooth` varchar(255) DEFAULT NULL COMMENT '拔管过程是否顺利',
  `extubation_num` varchar(255) DEFAULT NULL COMMENT '拔管编号',
  `record_num` varchar(255) DEFAULT NULL COMMENT 'PICC档案编号',
  `nurse_jobnumber` varchar(255) DEFAULT NULL COMMENT '护士工号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of picc_patient_extubation
-- ----------------------------
INSERT INTO `picc_patient_extubation` VALUES ('1', '测试患者2', 'HZ21052701', '1617206490000', '28', '非计划拔管：伤口感染', '1', 'false', 'false', 'B2021060101', 'BH2021050702', '111133');
INSERT INTO `picc_patient_extubation` VALUES ('3', '测试患者1', 'HZ21052900', '1621101179453', '30', '正常拔管', '2', 'false', 'false', 'B2021060101', 'BH2021050701', '111133');

-- ----------------------------
-- Table structure for picc_patient_information
-- ----------------------------
DROP TABLE IF EXISTS `picc_patient_information`;
CREATE TABLE `picc_patient_information` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `patient_name` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `patient_number` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `next_time` varchar(13) CHARACTER SET utf8 DEFAULT NULL,
  `has_catheter` varchar(255) CHARACTER SET utf8 DEFAULT '0',
  `has_extubation` varchar(255) CHARACTER SET utf8 DEFAULT '0',
  `catheter_type` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `animal_heat` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `previous_history` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `drug_allergy_history` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `record_num` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of picc_patient_information
-- ----------------------------
INSERT INTO `picc_patient_information` VALUES ('1', '测试患者2', 'HZ21052701', '1622908800000', 'true', 'false', '输液港', '31', '高血压', '无', 'BH2021050701');
INSERT INTO `picc_patient_information` VALUES ('2', '测试患者1', 'HZ21052900', '1622908800000', 'true', 'false', 'PICC', '36', '无', '无', 'BH2021050702');

-- ----------------------------
-- Table structure for picc_patient_maintain
-- ----------------------------
DROP TABLE IF EXISTS `picc_patient_maintain`;
CREATE TABLE `picc_patient_maintain` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `patient_name` varchar(16) DEFAULT NULL,
  `patient_num` varchar(16) DEFAULT NULL,
  `maintain_date` varchar(13) DEFAULT NULL COMMENT '维护日期',
  `pipeline_unobstructed` varchar(255) DEFAULT NULL COMMENT '管路是否通畅',
  `change_dressing` varchar(255) DEFAULT NULL COMMENT '是否更换敷料',
  `replace_connector` varchar(255) DEFAULT NULL COMMENT '是否更换接头',
  `resistance` varchar(255) DEFAULT NULL COMMENT '有无阻力',
  `companion` varchar(255) DEFAULT NULL COMMENT '并发症',
  `treatment_process` varchar(255) DEFAULT NULL COMMENT '处理方法',
  `blood_return` varchar(255) DEFAULT NULL COMMENT '是否有回血',
  `catheter_exposed_length` int(11) DEFAULT NULL COMMENT '导管外露长度',
  `nurse_jobnumber` varchar(255) DEFAULT NULL COMMENT '维护护士工号',
  `maintain_num` varchar(255) DEFAULT NULL COMMENT '维护编号',
  `record_num` varchar(255) DEFAULT NULL COMMENT 'PICC档案编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of picc_patient_maintain
-- ----------------------------
INSERT INTO `picc_patient_maintain` VALUES ('1', '测试患者2', 'HZ21052701', '1232567820343', 'true', 'true', 'true', 'false', '无', null, 'false', '2', '111133', 'W2021051201', 'BH2021050702');
INSERT INTO `picc_patient_maintain` VALUES ('4', '测试患者1', 'HZ21052900', '1634567887654', 'true', 'false', 'false', 'false', '导管堵塞', '用生理盐水脉冲方式冲管', 'false', '3', '111133', 'W2021051202', 'BH2021050701');
INSERT INTO `picc_patient_maintain` VALUES ('5', '测试患者1', 'HZ21052900', '1621100927237', 'true', 'true', 'true', 'false', '无', '', 'false', '2', '111133', 'W2021051901', 'BH2021050701');
INSERT INTO `picc_patient_maintain` VALUES ('8', '测试患者1', 'HZ21052900', '1621694497354', 'true', 'true', 'true', 'false', '静脉炎', '局部热敷', 'false', '1', '111133', 'W2021052301', 'BH2021050701');
INSERT INTO `picc_patient_maintain` VALUES ('9', '测试患者2', 'HZ21052701', '1622304000000', 'true', 'true', 'true', 'false', '无', '', 'false', '2', '111133', 'W2021051701', 'BH2021050702');
INSERT INTO `picc_patient_maintain` VALUES ('10', '测试患者2', 'HZ21052701', '1622304000000', 'true', 'true', 'true', 'false', '无', '', 'false', '3', '111133', 'W2021052101', 'BH2021050702');
INSERT INTO `picc_patient_maintain` VALUES ('11', '测试患者2', 'HZ21052701', '1622353761956', 'true', 'true', 'true', 'false', '静脉炎', '局部热敷', 'false', '2', '111133', 'W2021052501', 'BH2021050702');
INSERT INTO `picc_patient_maintain` VALUES ('12', '测试患者2', 'HZ21052701', '1622353761956', 'true', 'true', 'true', 'false', '静脉炎', '局部热敷', 'false', '2', '111133', 'W2021052901', 'BH2021050702');
INSERT INTO `picc_patient_maintain` VALUES ('13', '测试患者2', 'HZ21052701', '1622304000000', 'true', 'true', 'true', 'false', '静脉炎', '局部热敷', 'false', '1', '111133', 'W2021053001', 'BH2021050702');

-- ----------------------------
-- Table structure for picc_patient_record
-- ----------------------------
DROP TABLE IF EXISTS `picc_patient_record`;
CREATE TABLE `picc_patient_record` (
  `id` int(11) DEFAULT NULL,
  `record_num` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `patient_num` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `patient_name` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `c_time` varchar(255) CHARACTER SET utf8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of picc_patient_record
-- ----------------------------
INSERT INTO `picc_patient_record` VALUES ('1', 'BH2021050701', 'HZ21052900', '测试患者1', '1621099425033');
INSERT INTO `picc_patient_record` VALUES ('2', 'BH2021050702', 'HZ21052701', '测试患者2', '1621099425033');

-- ----------------------------
-- Table structure for picc_specialist
-- ----------------------------
DROP TABLE IF EXISTS `picc_specialist`;
CREATE TABLE `picc_specialist` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) CHARACTER SET utf8 NOT NULL,
  `specialist_num` varchar(255) CHARACTER SET utf8 NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 NOT NULL,
  `sex` varchar(255) CHARACTER SET utf8 NOT NULL,
  `phone` varchar(255) CHARACTER SET utf8 NOT NULL,
  `work_unit` varchar(255) CHARACTER SET utf8 NOT NULL,
  `create_time` varchar(255) CHARACTER SET utf8 NOT NULL,
  `state` varchar(255) CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of picc_specialist
-- ----------------------------
INSERT INTO `picc_specialist` VALUES ('2', 'oDVoB5CP6kYY5k9VPdn-UuWSxil4', 'WY21052900', '测试专家2', '男', '15827155173', '测试地址2', '1622257571123', '1');
INSERT INTO `picc_specialist` VALUES ('3', 'oDVoB5EtMbajsZ6eOB2TW9BvUydM', 'WY21052901', '测试专家3', '女', '15271441711', '测试地址3', '1622257923540', '0');

-- ----------------------------
-- Table structure for picc_wx_user
-- ----------------------------
DROP TABLE IF EXISTS `picc_wx_user`;
CREATE TABLE `picc_wx_user` (
  `open_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'open_id',
  `skey` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'skey',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_visit_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后登录时间',
  `session_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'session_key',
  `city` varchar(255) DEFAULT NULL COMMENT '市',
  `province` varchar(255) DEFAULT NULL COMMENT '省',
  `country` varchar(255) DEFAULT NULL COMMENT '国',
  `avatar_url` varchar(255) DEFAULT NULL COMMENT '头像',
  `gender` tinyint(11) DEFAULT NULL COMMENT '性别',
  `nick_name` varchar(255) DEFAULT NULL COMMENT '网名',
  PRIMARY KEY (`open_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='微信用户信息';

-- ----------------------------
-- Records of picc_wx_user
-- ----------------------------
INSERT INTO `picc_wx_user` VALUES ('oDVoB5BhP3wX2bKD8jlnPbFf48jY', 'd84b0a31-7c28-4006-9d6a-bdba08f68a8b', '2021-05-29 03:12:41', '2021-05-29 03:12:41', 'suw/6XeqgoaEOVepy+idNw==', '', '', '', 'https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132', '0', '微信用户');
INSERT INTO `picc_wx_user` VALUES ('oDVoB5CP6kYY5k9VPdn-UuWSxil4', '58da7ece-761d-4e6a-91b7-b33be99680c8', '2021-05-26 14:10:06', '2021-06-01 07:01:17', '8kK4DL51nCPuGqqxZU+mMw==', '', '', '', 'https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132', '0', '微信用户');
INSERT INTO `picc_wx_user` VALUES ('oDVoB5EtMbajsZ6eOB2TW9BvUydM', '570c0eb7-e69c-4bf2-bcbc-aacff6643fc5', '2021-05-29 03:08:44', '2021-05-29 03:08:44', 'rwHbdyxCr1XKIBWhUMQ42A==', '', '', '', 'https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132', '0', '微信用户');
INSERT INTO `picc_wx_user` VALUES ('oDVoB5Mlk9tYxUrHCd3YN76curbI', 'b6f93dfe-5c12-4b7c-af53-4a96f5a3ac8a', '2021-04-11 15:20:16', '2021-06-02 06:58:44', 'QD+JDndh9IQsytNIjopqIQ==', '', '', '', 'https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132', '0', '微信用户');
INSERT INTO `picc_wx_user` VALUES ('oDVoB5N4gXjHs7FQz6HlnFN9q8jo', '0a7dfba1-a37e-430b-bead-fe74151d9569', '2021-05-29 05:25:09', '2021-05-29 05:25:09', 'CO8oGaLOWfhNcpw4BQTdmw==', '', '', '', 'https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132', '0', '微信用户');
