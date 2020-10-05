-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: secolab
-- ------------------------------------------------------
-- Server version	8.0.19

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `language_tb`
--

DROP TABLE IF EXISTS `language_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `language_tb` (
  `language_id` varchar(50) NOT NULL COMMENT '언어아이디',
  `language_type` varchar(32) NOT NULL COMMENT '언어종류',
  `language_reg_date` datetime NOT NULL COMMENT '언어등록날짜',
  PRIMARY KEY (`language_id`),
  UNIQUE KEY `UC_language_type` (`language_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `language_tb`
--

LOCK TABLES `language_tb` WRITE;
/*!40000 ALTER TABLE `language_tb` DISABLE KEYS */;
/*!40000 ALTER TABLE `language_tb` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project_tb`
--

DROP TABLE IF EXISTS `project_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `project_tb` (
  `project_id` varchar(100) NOT NULL,
  `user_id` varchar(100) NOT NULL,
  `project_name` varchar(100) NOT NULL,
  `project_type` varchar(100) NOT NULL,
  `project_created` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project_tb`
--

LOCK TABLES `project_tb` WRITE;
/*!40000 ALTER TABLE `project_tb` DISABLE KEYS */;
INSERT INTO `project_tb` VALUES ('4c572a95-7508-4a53-8509-879c35dafbf8','3674f6b0-8e9f-432b-9f80-64286d4883c2','Project Test Nodejs','Nodejs','2020-10-05 02:29:57'),('61fe4aa4-4f3d-4375-9877-b263be73c1dd','3674f6b0-8e9f-432b-9f80-64286d4883c2','Project Spring ','Java Spring','2020-10-05 02:31:09'),('ecdeb22e-8a06-441e-ab9c-88227e0b8043','3674f6b0-8e9f-432b-9f80-64286d4883c2','Project Test Python','Python','2020-10-05 02:30:22');
/*!40000 ALTER TABLE `project_tb` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project_tb_spring`
--

DROP TABLE IF EXISTS `project_tb_spring`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `project_tb_spring` (
  `id` varchar(100) NOT NULL,
  `project_id` varchar(100) DEFAULT NULL,
  `project_build_type` varchar(100) DEFAULT NULL,
  `project_language` varchar(100) DEFAULT NULL,
  `project_spring_boot_ver` varchar(50) DEFAULT NULL,
  `project_meta_group` varchar(100) DEFAULT NULL,
  `project_meta_desc` varchar(100) DEFAULT NULL,
  `project_meta_package` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project_tb_spring`
--

LOCK TABLES `project_tb_spring` WRITE;
/*!40000 ALTER TABLE `project_tb_spring` DISABLE KEYS */;
INSERT INTO `project_tb_spring` VALUES ('5b384e41-b31a-4053-b5c2-0d04b12ffb37','61fe4aa4-4f3d-4375-9877-b263be73c1dd','Maven','Java','2.4.0','group','des','package name');
/*!40000 ALTER TABLE `project_tb_spring` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `security_category_tb`
--

DROP TABLE IF EXISTS `security_category_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `security_category_tb` (
  `scategory_id` bigint NOT NULL AUTO_INCREMENT COMMENT '보안약점대분류아이디',
  `scategory_name` varchar(128) NOT NULL COMMENT '보안약점대분류명',
  `sc_reg_date` datetime DEFAULT NULL COMMENT '보안약점대분류등록날짜',
  PRIMARY KEY (`scategory_id`),
  UNIQUE KEY `UC_scategory_name` (`scategory_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='보안약점 대분류';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `security_category_tb`
--

LOCK TABLES `security_category_tb` WRITE;
/*!40000 ALTER TABLE `security_category_tb` DISABLE KEYS */;
/*!40000 ALTER TABLE `security_category_tb` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `security_tb`
--

DROP TABLE IF EXISTS `security_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `security_tb` (
  `sec_id` bigint NOT NULL AUTO_INCREMENT COMMENT '보안약점아이디',
  `scategory_id` bigint NOT NULL COMMENT '보안약점대분류아이디',
  `sec_name` varchar(128) NOT NULL COMMENT '보안약점명',
  `sec_pdf` varchar(256) DEFAULT NULL COMMENT '보안약점해설파일',
  `sec_video` varchar(256) DEFAULT NULL COMMENT '보안약점동영상',
  `sec_reg_date` datetime NOT NULL COMMENT '보안약점등록날짜',
  PRIMARY KEY (`sec_id`),
  KEY `FK_security_tb_scategory_id_security_category_tb_scategory_id` (`scategory_id`),
  CONSTRAINT `FK_security_tb_scategory_id_security_category_tb_scategory_id` FOREIGN KEY (`scategory_id`) REFERENCES `security_category_tb` (`scategory_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='보안약점 소분류';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `security_tb`
--

LOCK TABLES `security_tb` WRITE;
/*!40000 ALTER TABLE `security_tb` DISABLE KEYS */;
/*!40000 ALTER TABLE `security_tb` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `solved_code_tb`
--

DROP TABLE IF EXISTS `solved_code_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `solved_code_tb` (
  `solved_id` bigint NOT NULL AUTO_INCREMENT COMMENT '푼문제아이디',
  `user_id` varchar(50) NOT NULL COMMENT '유저아이디',
  `scode_id` varchar(50) NOT NULL COMMENT '출제문제아이디',
  `try_num_first` int NOT NULL DEFAULT '0' COMMENT '1단계문제시도횟수',
  `try_num_second` int NOT NULL DEFAULT '0' COMMENT '2단계문제시도횟수',
  `solved_date_first` datetime DEFAULT NULL COMMENT '1단계푼날짜',
  `solved_date_second` datetime DEFAULT NULL COMMENT '2단계푼날짜',
  `last_try_date` datetime DEFAULT NULL COMMENT '마지막시도날짜',
  PRIMARY KEY (`solved_id`),
  KEY `FK_solved_code_tb_user_id_user_tb_user_id` (`user_id`),
  KEY `FK_solved_code_tb_scode_id_submit_code_tb_scode_id` (`scode_id`),
  CONSTRAINT `FK_solved_code_tb_scode_id_submit_code_tb_scode_id` FOREIGN KEY (`scode_id`) REFERENCES `submit_code_tb` (`scode_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_solved_code_tb_user_id_user_tb_user_id` FOREIGN KEY (`user_id`) REFERENCES `user_tb` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `solved_code_tb`
--

LOCK TABLES `solved_code_tb` WRITE;
/*!40000 ALTER TABLE `solved_code_tb` DISABLE KEYS */;
/*!40000 ALTER TABLE `solved_code_tb` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `submit_code_tb`
--

DROP TABLE IF EXISTS `submit_code_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `submit_code_tb` (
  `scode_id` varchar(50) NOT NULL COMMENT '출제문제아이디',
  `scode_num` bigint NOT NULL COMMENT '문제번호',
  `user_id` varchar(50) NOT NULL COMMENT '유저아이디',
  `sec_id` bigint NOT NULL COMMENT '보안약점아이디',
  `language_id` varchar(50) NOT NULL COMMENT '언어아이디',
  `scode_vul_file` varchar(64) DEFAULT NULL COMMENT '진단대상코드파일명',
  `scode_vul_desc` varchar(2048) DEFAULT NULL COMMENT '진단대상코드해설',
  `scode_sec_file` varchar(64) DEFAULT NULL COMMENT '수정대상코드파일명',
  `scode_sec_desc` varchar(2048) DEFAULT NULL COMMENT '수정대상코드해설',
  `scode_line_num` varchar(64) NOT NULL COMMENT '정답라인번호',
  `scode_approval` tinyint NOT NULL DEFAULT '0' COMMENT '승인상태',
  `scode_reg_date` datetime NOT NULL COMMENT '등록날짜',
  `scode_try_num` int NOT NULL DEFAULT '0' COMMENT '총시도횟수',
  `scode_succ_num` int NOT NULL DEFAULT '0' COMMENT '총성공횟수',
  `scode_keyword` varchar(4096) DEFAULT NULL COMMENT '수정대상코드키워드',
  `reject_reason` int NOT NULL COMMENT '거절사유코드',
  PRIMARY KEY (`scode_id`),
  UNIQUE KEY `UC_scode_num` (`scode_num`),
  KEY `FK_submit_code_tb_user_id_user_tb_user_id` (`user_id`),
  KEY `FK_submit_code_tb_language_id_language_tb_language_id` (`language_id`),
  KEY `FK_submit_code_tb_sec_id_security_tb_sec_id` (`sec_id`),
  CONSTRAINT `FK_submit_code_tb_language_id_language_tb_language_id` FOREIGN KEY (`language_id`) REFERENCES `language_tb` (`language_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_submit_code_tb_sec_id_security_tb_sec_id` FOREIGN KEY (`sec_id`) REFERENCES `security_tb` (`sec_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_submit_code_tb_user_id_user_tb_user_id` FOREIGN KEY (`user_id`) REFERENCES `user_tb` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `submit_code_tb`
--

LOCK TABLES `submit_code_tb` WRITE;
/*!40000 ALTER TABLE `submit_code_tb` DISABLE KEYS */;
/*!40000 ALTER TABLE `submit_code_tb` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_tb`
--

DROP TABLE IF EXISTS `user_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_tb` (
  `user_id` varchar(50) NOT NULL COMMENT '유저아이디',
  `name` varchar(32) NOT NULL COMMENT '유저이름',
  `email` varchar(32) NOT NULL COMMENT '유저이메일',
  `password` varchar(256) DEFAULT NULL COMMENT '유저비밀번호',
  `type` tinyint NOT NULL COMMENT '유저타입',
  `reg_date` datetime NOT NULL COMMENT '가입날짜',
  `is_oauth` int DEFAULT NULL COMMENT 'OAuth유저',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UC_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_tb`
--

LOCK TABLES `user_tb` WRITE;
/*!40000 ALTER TABLE `user_tb` DISABLE KEYS */;
INSERT INTO `user_tb` VALUES ('3674f6b0-8e9f-432b-9f80-64286d4883c2','Huong','huong@gmail.com','$2a$10$n90su3Jl/dc/yuT7Py0hAuvsCpHg4JD5JJqiAxUSXM624Hk0v7.C2',1,'2020-09-28 13:52:01',0),('401dd279-b5c2-4b76-a168-d10ca720d5a5','Huong2','test@gmail.com','$2a$10$ScTMxlEze05Wld1M3BNPNOKCeZY/VF4dUWZNbWJaZy9TuTRYEkIy2',1,'2020-09-22 23:06:59',0),('97b4a1d4-3c9d-4322-8ee8-6838e12f3e03','Huong','user@gmail.com','$2a$10$jwLiu7gCVds9TOFAh.RvPugzkowc30uXw.43kN1wEUZAvP67rrdRK',1,'2020-09-22 18:24:59',1);
/*!40000 ALTER TABLE `user_tb` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-10-05 11:37:16
