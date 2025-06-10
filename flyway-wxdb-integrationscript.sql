CREATE DATABASE  IF NOT EXISTS `wxdb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `wxdb`;
-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: wxdb
-- ------------------------------------------------------
-- Server version	8.0.42

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
-- Table structure for table `imputation_log`
--

DROP TABLE IF EXISTS `imputation_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `imputation_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `imputation_zusammenfassung_id` bigint NOT NULL,
  `wert_name` varchar(255) DEFAULT NULL,
  `wert` double DEFAULT NULL,
  `source_station_id` int DEFAULT NULL,
  `source_name` varchar(255) DEFAULT NULL,
  `information` longtext,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `triggered_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `imputation_zusammenfassung_id` (`imputation_zusammenfassung_id`),
  CONSTRAINT `imputation_log_ibfk_1` FOREIGN KEY (`imputation_zusammenfassung_id`) REFERENCES `imputation_zusammenfassung` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `imputation_zusammenfassung`
--

DROP TABLE IF EXISTS `imputation_zusammenfassung`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `imputation_zusammenfassung` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `triggered_by` varchar(255) DEFAULT NULL,
  `information` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `prozess_log`
--

DROP TABLE IF EXISTS `prozess_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prozess_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `prozess_name` varchar(255) NOT NULL,
  `status` varchar(50) NOT NULL,
  `beschreibung` tinytext,
  `zeitstempel` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `wetterdaten`
--

DROP TABLE IF EXISTS `wetterdaten`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wetterdaten` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `time` datetime DEFAULT NULL,
  `temperature` double DEFAULT NULL,
  `wind_direction` double DEFAULT NULL,
  `wind_speed` double DEFAULT NULL,
  `global_radiation` double DEFAULT NULL,
  `datasource` varchar(255) DEFAULT NULL,
  `weather_station_source` varchar(255) DEFAULT NULL,
  `station_source_id` bigint DEFAULT NULL,
  `last_changed_by` varchar(255) DEFAULT NULL,
  `last_changed_time` datetime DEFAULT NULL,
  `version` int DEFAULT NULL,
  `is_realtime` bit(1) NOT NULL,
  `imputed` tinyint(1) DEFAULT '0',
  `imputation_zusammenfassung_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `imputation_zusammenfassung_id` (`imputation_zusammenfassung_id`),
  CONSTRAINT `wetterdaten_ibfk_1` FOREIGN KEY (`imputation_zusammenfassung_id`) REFERENCES `imputation_zusammenfassung` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9107 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-10 12:52:43
