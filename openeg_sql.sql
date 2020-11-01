-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: secolab
-- ------------------------------------------------------
-- Server version	8.0.19

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
