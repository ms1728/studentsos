/*
Navicat MySQL Data Transfer

Source Server         : localhost_3307
Source Server Version : 50704
Source Host           : localhost:3306
Source Database       : studentsos

Target Server Type    : MYSQL
Target Server Version : 50704
File Encoding         : 65001

Date: 2016-04-15 21:00:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `book`
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `bookID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `author` varchar(50) NOT NULL,
  `publisher` varchar(50) NOT NULL,
  `introduction` varchar(200) DEFAULT NULL,
  `picPath` varchar(100) DEFAULT NULL,
  `path` varchar(50) NOT NULL,
  PRIMARY KEY (`bookID`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO `book` VALUES ('1', '全新版大学英语综合教程', '未知', '未知', 'qwe', 'keben_1.png', '');
INSERT INTO `book` VALUES ('2', '电力电子技术与MATLAB仿真', '周渊深', '中国电力出版社', 'aaa', 'keben_2.png', '');
INSERT INTO `book` VALUES ('3', '自动控制原理', '王永骥、王金城、王敏合著', '化学工业出版社', 'zxc', 'keben_3.png', '');
INSERT INTO `book` VALUES ('4', '大学英语快速阅读', '未知', '未知', 'qaz', 'keben_4.png', '');
INSERT INTO `book` VALUES ('5', '数字电子技术基础', '阎石', '高等教育出版社', 'sssss', 'keben_5.png', '');
INSERT INTO `book` VALUES ('6', '模拟电子技术基础', '未知', '未知', 'oooo', 'keben_6.png', '');
INSERT INTO `book` VALUES ('7', '汽车电路读图', '李春明', '北京理工大学出版社', 'kkkkkk', 'keben_7.png', '');
INSERT INTO `book` VALUES ('8', '模拟电路分析与设计基础', '吴援明、曲健', '科学出版社', 'rrrr', 'keben_8.png', '');
INSERT INTO `book` VALUES ('9', 'LED照明驱动电路设计与实例精选', '杨恒', '中国电力出版社', 'hhhhh', 'keben_9.png', '');
INSERT INTO `book` VALUES ('10', '电机学', '未知', '未知', 'jjjjjj', 'keben_10.png', '');
INSERT INTO `book` VALUES ('11', '新视野大学英语听说教程', '未知', '未知', 'zzzzzzz', 'keben_11.png', '');
INSERT INTO `book` VALUES ('12', 'Visual C++面向对象编程', '未知', '清华大学出版社', 'llllll', 'keben_12.png', '');

-- ----------------------------
-- Table structure for `bookcollection`
-- ----------------------------
DROP TABLE IF EXISTS `bookcollection`;
CREATE TABLE `bookcollection` (
  `bookCollectionID` int(11) NOT NULL AUTO_INCREMENT,
  `studentID` varchar(16) NOT NULL,
  `bookID` int(11) NOT NULL,
  PRIMARY KEY (`bookCollectionID`),
  UNIQUE KEY `uk_BookCollection_studentID_bookID` (`studentID`,`bookID`),
  KEY `fk_Book_BookCollection_bookID` (`bookID`),
  CONSTRAINT `fk_Book_BookCollection_bookID` FOREIGN KEY (`bookID`) REFERENCES `book` (`bookID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_Student_BookCollection_studentID` FOREIGN KEY (`studentID`) REFERENCES `student` (`studentID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of bookcollection
-- ----------------------------
INSERT INTO `bookcollection` VALUES ('43', '123456', '1');
INSERT INTO `bookcollection` VALUES ('42', '123456', '11');
INSERT INTO `bookcollection` VALUES ('51', '15766253678', '4');
INSERT INTO `bookcollection` VALUES ('52', '15766253678', '11');
INSERT INTO `bookcollection` VALUES ('36', '18825058480', '3');
INSERT INTO `bookcollection` VALUES ('40', '18825058480', '4');
INSERT INTO `bookcollection` VALUES ('38', '18825058480', '11');
INSERT INTO `bookcollection` VALUES ('41', '201313220421', '4');
INSERT INTO `bookcollection` VALUES ('44', 'asd', '2');
INSERT INTO `bookcollection` VALUES ('54', 'ms1728', '4');
INSERT INTO `bookcollection` VALUES ('19', 'rty', '1');
INSERT INTO `bookcollection` VALUES ('28', 'rty', '2');
INSERT INTO `bookcollection` VALUES ('22', 'rty', '4');
INSERT INTO `bookcollection` VALUES ('50', 'rty', '6');
INSERT INTO `bookcollection` VALUES ('24', 'rty', '7');
INSERT INTO `bookcollection` VALUES ('48', 'rty', '8');
INSERT INTO `bookcollection` VALUES ('26', 'rty', '10');
INSERT INTO `bookcollection` VALUES ('27', 'rty', '11');
INSERT INTO `bookcollection` VALUES ('49', 'rty', '12');
INSERT INTO `bookcollection` VALUES ('33', 'test1', '3');
INSERT INTO `bookcollection` VALUES ('23', 'test1', '4');

-- ----------------------------
-- Table structure for `booksectionpath`
-- ----------------------------
DROP TABLE IF EXISTS `booksectionpath`;
CREATE TABLE `booksectionpath` (
  `bookSectionPathID` int(11) NOT NULL AUTO_INCREMENT,
  `bookID` int(11) NOT NULL,
  `sectionName` varchar(20) NOT NULL,
  `sectionNum` int(11) DEFAULT NULL,
  `contentPath` varchar(100) NOT NULL,
  PRIMARY KEY (`bookSectionPathID`),
  KEY `fk_Book_BookSectionPath_bookID` (`bookID`),
  CONSTRAINT `fk_Book_BookSectionPath_bookID` FOREIGN KEY (`bookID`) REFERENCES `book` (`bookID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of booksectionpath
-- ----------------------------
INSERT INTO `booksectionpath` VALUES ('1', '4', '第四章:开发历程', '4', '1_4.doc');
INSERT INTO `booksectionpath` VALUES ('2', '4', '第二章', '2', '2_4.doc');
INSERT INTO `booksectionpath` VALUES ('3', '1', 'zxc', '3', 'zxc');
INSERT INTO `booksectionpath` VALUES ('4', '1', 'ghgh', '2', 'vfv');
INSERT INTO `booksectionpath` VALUES ('5', '1', 'gffvf', '1', 'rrrrr');
INSERT INTO `booksectionpath` VALUES ('6', '4', '第三章', '3', 'asdsa');
INSERT INTO `booksectionpath` VALUES ('7', '4', '第一章', '1', 'ww');

-- ----------------------------
-- Table structure for `department`
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `departmentID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`departmentID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO `department` VALUES ('1', '未知');
INSERT INTO `department` VALUES ('2', '海运学院');
INSERT INTO `department` VALUES ('3', '航务工程学院');
INSERT INTO `department` VALUES ('4', '艺术设计学院');
INSERT INTO `department` VALUES ('5', '信息与通信工程学院');

-- ----------------------------
-- Table structure for `departmentdetail`
-- ----------------------------
DROP TABLE IF EXISTS `departmentdetail`;
CREATE TABLE `departmentdetail` (
  `DepartmentDetailID` int(11) NOT NULL AUTO_INCREMENT,
  `schoolID` int(11) NOT NULL,
  `departmentID` int(11) NOT NULL,
  PRIMARY KEY (`DepartmentDetailID`),
  KEY `fk_School_DepartmentDetail_schoolID` (`schoolID`),
  KEY `fk_Department_DepartmentDetail_departmentID` (`departmentID`),
  CONSTRAINT `fk_Department_DepartmentDetail_departmentID` FOREIGN KEY (`departmentID`) REFERENCES `department` (`departmentID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_School_DepartmentDetail_schoolID` FOREIGN KEY (`schoolID`) REFERENCES `school` (`schoolID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of departmentdetail
-- ----------------------------
INSERT INTO `departmentdetail` VALUES ('1', '1', '1');
INSERT INTO `departmentdetail` VALUES ('2', '2', '2');
INSERT INTO `departmentdetail` VALUES ('3', '2', '3');
INSERT INTO `departmentdetail` VALUES ('4', '2', '4');
INSERT INTO `departmentdetail` VALUES ('6', '2', '5');

-- ----------------------------
-- Table structure for `major`
-- ----------------------------
DROP TABLE IF EXISTS `major`;
CREATE TABLE `major` (
  `majorID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`majorID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of major
-- ----------------------------
INSERT INTO `major` VALUES ('1', '未知');
INSERT INTO `major` VALUES ('2', '电子商务');
INSERT INTO `major` VALUES ('3', '航海技术');
INSERT INTO `major` VALUES ('4', '轮机工程');
INSERT INTO `major` VALUES ('5', '工程造价');
INSERT INTO `major` VALUES ('6', '电脑艺术设计');
INSERT INTO `major` VALUES ('7', '船舶电子电气工程');
INSERT INTO `major` VALUES ('8', '软件技术');

-- ----------------------------
-- Table structure for `majordetail`
-- ----------------------------
DROP TABLE IF EXISTS `majordetail`;
CREATE TABLE `majordetail` (
  `majorDetailID` int(11) NOT NULL AUTO_INCREMENT,
  `departmentDetailID` int(11) NOT NULL,
  `majorID` int(11) NOT NULL,
  `classCount` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`majorDetailID`),
  KEY `fk_DepartmentDetail_MajorDetail_departmentDetailID` (`departmentDetailID`),
  KEY `fk_Major_MajorDetail_majorID` (`majorID`),
  CONSTRAINT `fk_DepartmentDetail_MajorDetail_departmentDetailID` FOREIGN KEY (`departmentDetailID`) REFERENCES `departmentdetail` (`DepartmentDetailID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_Major_MajorDetail_majorID` FOREIGN KEY (`majorID`) REFERENCES `major` (`majorID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of majordetail
-- ----------------------------
INSERT INTO `majordetail` VALUES ('1', '1', '1', '1');
INSERT INTO `majordetail` VALUES ('2', '6', '2', '3');
INSERT INTO `majordetail` VALUES ('3', '2', '3', '5');
INSERT INTO `majordetail` VALUES ('4', '2', '4', '4');
INSERT INTO `majordetail` VALUES ('5', '3', '5', '5');
INSERT INTO `majordetail` VALUES ('6', '4', '6', '4');
INSERT INTO `majordetail` VALUES ('7', '3', '7', '2');
INSERT INTO `majordetail` VALUES ('8', '6', '8', '2');

-- ----------------------------
-- Table structure for `school`
-- ----------------------------
DROP TABLE IF EXISTS `school`;
CREATE TABLE `school` (
  `schoolID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`schoolID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of school
-- ----------------------------
INSERT INTO `school` VALUES ('1', '未知');
INSERT INTO `school` VALUES ('2', '广州航海学院');

-- ----------------------------
-- Table structure for `student`
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `studentID` varchar(16) NOT NULL,
  `majorDetailID` int(11) NOT NULL,
  `entryYear` int(10) DEFAULT NULL,
  `classNum` int(11) DEFAULT NULL,
  `studentName` varchar(50) NOT NULL,
  `password` varchar(16) NOT NULL,
  `email` varchar(30) NOT NULL,
  `headPath` varchar(100) DEFAULT NULL,
  `changeHeadTimes` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`studentID`),
  KEY `fk_MajorDetail_Student_majorDetailID` (`majorDetailID`),
  CONSTRAINT `fk_MajorDetail_Student_majorDetailID` FOREIGN KEY (`majorDetailID`) REFERENCES `majordetail` (`majorDetailID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('111', '3', '2010', '1', '111', '111', '1234567@qq.com', null, '0');
INSERT INTO `student` VALUES ('123456', '1', null, null, '123456', '123456', '123456789@qq.com', null, '0');
INSERT INTO `student` VALUES ('13226429680', '7', '2013', '1', '冬冬', '133011.dong', '78039242@qq.com', null, '0');
INSERT INTO `student` VALUES ('15766227430', '1', null, null, '启丽', '15766227430', '1049840129@qq.com', null, '0');
INSERT INTO `student` VALUES ('1576622743093', '1', null, null, '启丽99', '15766227430', '767048526@qq.com', null, '0');
INSERT INTO `student` VALUES ('15766253678', '1', '0', '0', '思念', '15766253678', '1455162515@qq.com', '15766253678_faceImage1.jpg', '1');
INSERT INTO `student` VALUES ('18825058480', '7', '2013', '2', '徐宏林', '13727712405', 'xhdbcb@xjr.xhdh', null, '1');
INSERT INTO `student` VALUES ('18825058670', '1', '0', '0', '', '741hzpwcshn', '2292423872@qq.com', null, '0');
INSERT INTO `student` VALUES ('201313220421', '7', '2013', '1', 'Xhl', '13727712405xhl', '1361682980@qq.com', '201313220421_faceImage4.jpg', '4');
INSERT INTO `student` VALUES ('330352', '1', '2010', '1', 'K', '330352', '1248993940@qq.com', null, '0');
INSERT INTO `student` VALUES ('asd', '1', null, null, 'asd', 'qweqwe', 'asd@asd.com', null, '0');
INSERT INTO `student` VALUES ('ms1728', '1', '0', '0', 'ms11', '123123', '172837016@qq.com', 'ms1728_faceImage1.jpg', '1');
INSERT INTO `student` VALUES ('rty', '1', '1', '1', '林cc', '123', '123@123.com', 'rty_faceImage19.jpg', '19');
INSERT INTO `student` VALUES ('test1', '7', '2013', '1', 'test', '123456', '123456@qq.com', null, '1');

-- ----------------------------
-- Table structure for `syllabus`
-- ----------------------------
DROP TABLE IF EXISTS `syllabus`;
CREATE TABLE `syllabus` (
  `syllabusID` int(11) NOT NULL AUTO_INCREMENT,
  `majorDetailID` int(11) NOT NULL,
  `schoolYear` varchar(15) NOT NULL,
  `term` bit(1) NOT NULL,
  `grade` int(10) NOT NULL,
  `classNum` int(11) NOT NULL,
  `lession1` varchar(100) DEFAULT NULL,
  `lession2` varchar(100) DEFAULT NULL,
  `lession3` varchar(100) DEFAULT NULL,
  `lession4` varchar(100) DEFAULT NULL,
  `lession5` varchar(100) DEFAULT NULL,
  `lession6` varchar(100) DEFAULT NULL,
  `lession7` varchar(100) DEFAULT NULL,
  `lession8` varchar(100) DEFAULT NULL,
  `lession9` varchar(100) DEFAULT NULL,
  `lession10` varchar(100) DEFAULT NULL,
  `lession11` varchar(100) DEFAULT NULL,
  `lession12` varchar(100) DEFAULT NULL,
  `lession13` varchar(100) DEFAULT NULL,
  `lession14` varchar(100) DEFAULT NULL,
  `lession15` varchar(100) DEFAULT NULL,
  `lession16` varchar(100) DEFAULT NULL,
  `lession17` varchar(100) DEFAULT NULL,
  `lession18` varchar(100) DEFAULT NULL,
  `lession19` varchar(100) DEFAULT NULL,
  `lession20` varchar(100) DEFAULT NULL,
  `lession21` varchar(100) DEFAULT NULL,
  `lession22` varchar(100) DEFAULT NULL,
  `lession23` varchar(100) DEFAULT NULL,
  `lession24` varchar(100) DEFAULT NULL,
  `lession25` varchar(100) DEFAULT NULL,
  `lession26` varchar(100) DEFAULT NULL,
  `lession27` varchar(100) DEFAULT NULL,
  `lession28` varchar(100) DEFAULT NULL,
  `lession29` varchar(100) DEFAULT NULL,
  `lession30` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`syllabusID`),
  UNIQUE KEY `uk_Syllabus_majorID_schoolyear_isleap_term` (`majorDetailID`,`schoolYear`,`term`,`classNum`),
  CONSTRAINT `fk_Major_Syllabus_majorDetailID` FOREIGN KEY (`majorDetailID`) REFERENCES `majordetail` (`majorDetailID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of syllabus
-- ----------------------------
INSERT INTO `syllabus` VALUES ('1', '1', '2013', '', '2', '1', '', '', '', '', '', '', '软件项目管理@教学楼504（双）&清宏燕', '职业资格认证课程（程序员）@教学楼507&秦宗蓉', '', '', '', '', '', '', '', '职业资格认证课程（程序员）@信息楼505&秦宗蓉', '', '', '', '软件项目管理@信息楼618&清宏燕', '', '', '', '', '', '', '', '', '', '');
INSERT INTO `syllabus` VALUES ('2', '7', '2013', '', '2', '4', 'PLC通信网络高级技术@教学楼607&蒋先平', '通信电子线路@教学楼202&李瑞', '传感器与检测技术@教学楼707&李春香', '船舶电站@轮机楼408&白明', '', '', '交流变频高速@教学楼201&王永祥', '船舶电站@轮机楼408&白明', '', '', '企业管理@文理楼302&杨国亮', '', 'PLC通信网络高级技术@教学楼706&蒋先平', '', '传感器与检测技术@教学楼705&李春香', '通信电子线路@教学楼705&李瑞', '企业战略管理@教学楼401&宋旭琴', '', '船舶电站@轮机楼408&白明', '', '', '', '', '', '', '交流变频高速@教学楼301&王永祥', '', '', '', '');

-- ----------------------------
-- View structure for `departmentdetail_vw`
-- ----------------------------
DROP VIEW IF EXISTS `departmentdetail_vw`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `departmentdetail_vw` AS select `dd`.`DepartmentDetailID` AS `departmentDetailID`,`sc`.`schoolID` AS `schoolID`,`sc`.`name` AS `schoolName`,`dp`.`departmentID` AS `departmentID`,`dp`.`name` AS `departmentName` from ((`departmentdetail` `dd` join `school` `sc`) join `department` `dp`) where ((`dd`.`schoolID` = `sc`.`schoolID`) and (`dd`.`departmentID` = `dp`.`departmentID`)) ;

-- ----------------------------
-- View structure for `majordetail_vw`
-- ----------------------------
DROP VIEW IF EXISTS `majordetail_vw`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `majordetail_vw` AS select `md`.`majorDetailID` AS `majorDetailID`,`dd`.`schoolID` AS `schoolID`,`dd`.`schoolName` AS `schoolName`,`dd`.`departmentID` AS `departmentID`,`dd`.`departmentName` AS `departmentName`,`mj`.`majorID` AS `majorID`,`mj`.`name` AS `majorName`,`md`.`classCount` AS `classCount` from ((`majordetail` `md` join `departmentdetail_vw` `dd`) join `major` `mj`) where ((`md`.`departmentDetailID` = `dd`.`departmentDetailID`) and (`md`.`majorID` = `mj`.`majorID`)) ;

-- ----------------------------
-- View structure for `studentdetail_vw`
-- ----------------------------
DROP VIEW IF EXISTS `studentdetail_vw`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `studentdetail_vw` AS select `s`.`studentID` AS `studentID`,`s`.`studentName` AS `studentName`,`s`.`email` AS `email`,`s`.`headPath` AS `headPath`,`s`.`changeHeadTimes` AS `changeHeadTimes`,`s`.`classNum` AS `classNum`,`m`.`majorDetailID` AS `majorDetailID`,`m`.`schoolName` AS `schoolName`,`m`.`departmentName` AS `departmentName`,`m`.`majorName` AS `majorName`,`s`.`password` AS `password`,`s`.`entryYear` AS `entryYear` from (`student` `s` join `majordetail_vw` `m` on((`s`.`majorDetailID` = `m`.`majorDetailID`))) ;

-- ----------------------------
-- Procedure structure for `pro_DepartmentDetailDel`
-- ----------------------------
DROP PROCEDURE IF EXISTS `pro_DepartmentDetailDel`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `pro_DepartmentDetailDel`(
	schoolID_t int,
	departmentID_t int
)
BEGIN
	DECLARE num int default 0;
	SELECT COUNT(*) into num FROM DepartmentDetail
	WHERE schoolID=schoolID_t AND departmentID=departmentID_t;
	IF num>0 then
		DELETE FROM DepartmentDetail WHERE schoolID=schoolID_t AND departmentID=departmentID_t;
 	end if;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `pro_DepartmentDetailInsert`
-- ----------------------------
DROP PROCEDURE IF EXISTS `pro_DepartmentDetailInsert`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `pro_DepartmentDetailInsert`(
	schoolID_t int,
	departmentID_t int
)
BEGIN
	DECLARE num int default 0;
	SELECT COUNT(*) into num FROM DepartmentDetail
	WHERE schoolID=schoolID_t AND departmentID=departmentID_t;
	IF num=0 then
		INSERT INTO DepartmentDetail(schoolID,departmentID) VALUES(schoolID_t,departmentID_t);
	end if;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `pro_departmentInsert`
-- ----------------------------
DROP PROCEDURE IF EXISTS `pro_departmentInsert`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `pro_departmentInsert`(
	name_t varchar(50)
)
begin
	declare num int default 0;
	select COUNT(*) into num from Department where name=name_t;
	if num=0 then
		insert into Department(departmentID,name) values(null,name_t);
	end if;
end
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `pro_MajorDetailDel`
-- ----------------------------
DROP PROCEDURE IF EXISTS `pro_MajorDetailDel`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `pro_MajorDetailDel`(
	departmentDetailID_t int,
	majorID_t int
)
BEGIN
	DECLARE num int default 0;
	SELECT COUNT(*) into num FROM MajorDetail
	WHERE departmentDetailID=departmentDetailID_t AND majorID=majorID_t;
	IF num>0 then
		DELETE FROM MajorDetail WHERE departmentDetailID=departmentDetailID_t AND majorID=majorID_t;
	end if;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `pro_MajorDetailInsert`
-- ----------------------------
DROP PROCEDURE IF EXISTS `pro_MajorDetailInsert`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `pro_MajorDetailInsert`(
	departmentDetailID_t int,
	majorID_t int
)
BEGIN
	DECLARE num int default 0;
	SELECT COUNT(*) into num FROM MajorDetail
	WHERE departmentDetailID=departmentDetailID_t AND majorID=majorID_t;
	IF num=0 then
		INSERT INTO MajorDetail(departmentDetailID,majorID) 
		VALUES(departmentDetailID_t,majorID_t);
	end if;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `pro_majorInsert`
-- ----------------------------
DROP PROCEDURE IF EXISTS `pro_majorInsert`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `pro_majorInsert`(
	name_t varchar(50)
)
begin
	declare num int default 0;
	select COUNT(*) into num from Major where name=name_t;
	if num=0 then
		insert into Major(majorID,name) values(null,name_t);
	end if;
end
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `pro_schoolInsert`
-- ----------------------------
DROP PROCEDURE IF EXISTS `pro_schoolInsert`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `pro_schoolInsert`(
	name_t varchar(50)
)
begin
	declare num int default 0;
	select COUNT(*) into num from School where name=name_t;
	if num=0 then
		insert into School(schoolID,name) values(null,name_t);
	end if;
end
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `temp`
-- ----------------------------
DROP PROCEDURE IF EXISTS `temp`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `temp`()
begin
 declare i int default 1;
 while i<=100 do
	insert into school(name) values(i);
	insert into department(name) values(i*10+1);
	insert into major(name) values(i*100+99);
 	set i=i+1;
 end while;
end
;;
DELIMITER ;
