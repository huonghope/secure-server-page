-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: secolab
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
-- Table structure for table `owasp_container`
--

DROP TABLE IF EXISTS `owasp_container`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `owasp_container` (
  `containerid` varchar(50) NOT NULL,
  `container_port` int DEFAULT NULL,
  `user_id` varchar(45) DEFAULT NULL,
  `container_name` varchar(100) DEFAULT NULL,
  `container_created` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`containerid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `project_diagnosis`
--

DROP TABLE IF EXISTS `project_diagnosis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `project_diagnosis` (
  `id` int NOT NULL AUTO_INCREMENT,
  `project_id` varchar(100) NOT NULL,
  `user_id` varchar(100) DEFAULT NULL,
  `time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `path` varchar(100) DEFAULT NULL,
  `result` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `project_path` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `project_meta_groupId` varchar(100) DEFAULT NULL,
  `project_meta_artifactId` varchar(100) DEFAULT NULL,
  `project_meta_desc` varchar(100) DEFAULT NULL,
  `project_meta_package` varchar(100) DEFAULT NULL,
  `project_dependencies` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
-- Table structure for table `user_container`
--

DROP TABLE IF EXISTS `user_container`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_container` (
  `containerid` varchar(50) NOT NULL COMMENT '컨테이너 ID',
  `project_id` varchar(50) DEFAULT NULL COMMENT '현재 컨테이너내에 실행 프로젝트 ID',
  `user_id` varchar(50) DEFAULT NULL COMMENT '유저 ID',
  `container_name` varchar(45) DEFAULT NULL,
  `vscode_port` int DEFAULT NULL COMMENT 'VS-CODE 에디터 포트',
  `node_port` int DEFAULT NULL COMMENT 'Node 프로젝트 포트',
  `java_port` int DEFAULT NULL COMMENT 'Java 프로젝트 포트',
  `python_port` int DEFAULT NULL COMMENT 'Python 프로젝트 포트',
  `state` int DEFAULT NULL COMMENT '컨테이너 상태',
  `container_created` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '컨테이너 생성 시간',
  PRIMARY KEY (`containerid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-12-03 15:05:04
