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
-- Table structure for table `hinweise`
--

DROP TABLE IF EXISTS `hinweise`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hinweise` (
  `hinweisid` int NOT NULL AUTO_INCREMENT,
  `zeitstempel` datetime NOT NULL,
  `beschreibung` text NOT NULL,
  `typ` varchar(50) NOT NULL,
  PRIMARY KEY (`hinweisid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hinweise`
--

LOCK TABLES `hinweise` WRITE;
/*!40000 ALTER TABLE `hinweise` DISABLE KEYS */;
/*!40000 ALTER TABLE `hinweise` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `imputation_log`
--

LOCK TABLES `imputation_log` WRITE;
/*!40000 ALTER TABLE `imputation_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `imputation_log` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=2042 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `imputation_zusammenfassung`
--

LOCK TABLES `imputation_zusammenfassung` WRITE;
/*!40000 ALTER TABLE `imputation_zusammenfassung` DISABLE KEYS */;
/*!40000 ALTER TABLE `imputation_zusammenfassung` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prozess_log`
--

LOCK TABLES `prozess_log` WRITE;
/*!40000 ALTER TABLE `prozess_log` DISABLE KEYS */;
INSERT INTO `prozess_log` VALUES (1,'IMPORT-WeatherData','COMPLETED','Process Executed','2025-06-04 22:44:16'),(2,'IMPORT-WeatherData','COMPLETED','1008 Datensätze wurden erfolgreich importiert.','2025-06-06 15:57:47'),(3,'IMPORT-WeatherData','ERROR','org.hibernate.TransientObjectException: persistent instance references an unsaved transient instance of \'de.wxdb.wxdb_masterthesis.schema.ImputationSummary\' (save the transient instance before flushing)[Ljava.lang.StackTraceElement;@8c6292','2025-06-06 16:00:40'),(4,'IMPORT-WeatherData','COMPLETED','2247 Datensätze wurden erfolgreich importiert.','2025-06-06 16:03:27'),(5,'IMPORT-WeatherData','ERROR','invalid Daterange kalkulationsfehler in den Zeiträumen.','2025-06-10 13:23:03'),(6,'IMPORT-WeatherData','COMPLETED','2231 Datensätze wurden erfolgreich importiert.','2025-06-10 13:26:21'),(7,'IMPORT-WeatherData','COMPLETED','2233 Datensätze wurden erfolgreich importiert.','2025-06-10 13:41:48'),(8,'IMPORT-WeatherData','COMPLETED','2234 Datensätze wurden erfolgreich importiert.','2025-06-10 13:50:14'),(9,'IMPORT-WeatherData','COMPLETED','4320 Datensätze wurden erfolgreich importiert.','2025-06-13 11:41:41'),(10,'IMPORT-WeatherData','ERROR','invalid Daterange kalkulationsfehler in den Zeiträumen.','2025-06-13 11:47:24'),(11,'IMPORT-WeatherData','ERROR','invalid Daterange kalkulationsfehler in den Zeiträumen.','2025-06-13 11:49:35'),(12,'IMPORT-WeatherData','COMPLETED','1069 Datensätze wurden erfolgreich importiert.','2025-06-13 11:51:18'),(13,'IMPORT-WeatherData','COMPLETED','1069 Datensätze wurden erfolgreich importiert.','2025-06-13 11:53:51'),(14,'IMPORT-WeatherData','COMPLETED','1095 Datensätze wurden erfolgreich importiert.','2025-06-13 11:57:00'),(15,'IMPORT-WeatherData','ERROR','invalid Daterange kalkulationsfehler in den Zeiträumen.','2025-06-13 14:00:32'),(16,'IMPORT-WeatherData','ERROR','invalid Daterange kalkulationsfehler in den Zeiträumen.','2025-06-13 14:14:14'),(17,'IMPORT-WeatherData','ERROR','invalid Daterange kalkulationsfehler in den Zeiträumen.','2025-06-13 14:21:08'),(18,'IMPORT-WeatherData','ERROR','invalid Daterange kalkulationsfehler in den Zeiträumen.','2025-06-13 14:23:09'),(19,'IMPORT-WeatherData','ERROR','invalid Daterange kalkulationsfehler in den Zeiträumen.','2025-06-13 14:23:47'),(20,'IMPORT-WeatherData','ERROR','invalid Daterange kalkulationsfehler in den Zeiträumen.','2025-06-13 14:34:26'),(21,'IMPORT-WeatherData','ERROR','invalid Daterange kalkulationsfehler in den Zeiträumen.','2025-06-13 14:35:46'),(22,'IMPORT-WeatherData','ERROR','invalid Daterange kalkulationsfehler in den Zeiträumen.','2025-06-13 14:38:09'),(23,'IMPORT-WeatherData','COMPLETED','1038 Datensätze wurden erfolgreich importiert.','2025-06-13 14:40:57'),(24,'IMPORT-WeatherData','COMPLETED','1038 Datensätze wurden erfolgreich importiert.','2025-06-13 14:43:03'),(25,'IMPORT-WeatherData','COMPLETED','4398 Datensätze wurden erfolgreich importiert.','2025-06-13 14:46:37'),(26,'IMPORT-WeatherData','COMPLETED','4398 Datensätze wurden erfolgreich importiert.','2025-06-13 14:49:56'),(27,'IMPORT-WeatherData','ERROR','FeignClient-Fehlerhafte Anfrage','2025-06-13 14:52:45'),(28,'IMPORT-WeatherData','ERROR','FeignClient-Fehlerhafte Anfrage','2025-06-13 14:58:25'),(29,'IMPORT-WeatherData','COMPLETED','15788 Datensätze wurden erfolgreich importiert.','2025-06-13 15:01:56'),(30,'IMPORT-WeatherData','COMPLETED','20358 Datensätze wurden erfolgreich importiert.','2025-07-15 08:49:28'),(31,'IMPORT-WeatherData','COMPLETED','20365 Datensätze wurden erfolgreich importiert.','2025-07-15 09:52:13'),(32,'IMPORT-WeatherData','COMPLETED','20399 Datensätze wurden erfolgreich importiert.','2025-07-15 15:36:22'),(33,'IMPORT-WeatherData','COMPLETED','20550 Datensätze wurden erfolgreich importiert.','2025-07-16 16:49:17'),(34,'IMPORT-WeatherData','COMPLETED','20551 Datensätze wurden erfolgreich importiert.','2025-07-16 16:51:06'),(35,'IMPORT-WeatherData','STARTED','FeignClient-Fehlerhafte Anfrage','2025-07-16 17:29:52'),(36,'IMPORT-WeatherData','COMPLETED','12 Datensätze wurden erfolgreich importiert.','2025-07-16 17:35:41'),(37,'IMPORT-WeatherData','COMPLETED','20556 Datensätze wurden erfolgreich importiert.','2025-07-16 17:41:05'),(38,'IMPORT-WeatherData','COMPLETED','11 Datensätze wurden erfolgreich importiert.','2025-07-16 18:13:48'),(39,'IMPORT-WeatherData','COMPLETED','20560 Datensätze wurden erfolgreich importiert.','2025-07-16 18:29:04'),(40,'IMPORT-WeatherData','COMPLETED','153 Datensätze wurden erfolgreich importiert.','2025-07-17 18:38:06'),(41,'IMPORT-WeatherData','COMPLETED','20706 Datensätze wurden erfolgreich importiert.','2025-07-17 18:44:34'),(42,'IMPORT-WeatherData','COMPLETED','20706 Datensätze wurden erfolgreich importiert.','2025-07-17 18:46:50'),(43,'MANUAL-CSV-IMPORT','ERROR','Error in Mapping values min1 min2 min3','2025-07-17 18:58:27'),(44,'MANUAL-CSV-IMPORT','COMPLETED','43399 Datensätze wurden erfolgreich importiert.','2025-07-17 19:00:22'),(45,'MANUAL-CSV-IMPORT','COMPLETED','43399 Datensätze wurden erfolgreich importiert.','2025-07-17 19:03:42'),(46,'IMPORT-Realtime-Weatherdata','COMPLETED','9 Datensätze wurden erfolgreich importiert.','2025-07-17 20:09:45'),(47,'IMPORT-Realtime-Weatherdata','COMPLETED','1 Datensätze wurden erfolgreich importiert.','2025-07-17 20:10:00'),(48,'IMPORT-Realtime-Weatherdata','COMPLETED','1 Datensätze wurden erfolgreich importiert.','2025-07-17 20:10:15');
/*!40000 ALTER TABLE `prozess_log` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `wetterdaten_csv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wetterdaten_csv` (
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
  CONSTRAINT `wetterdaten_csv_ibfk_1` FOREIGN KEY (`imputation_zusammenfassung_id`) REFERENCES `imputation_zusammenfassung` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wetterdaten`
--

LOCK TABLES `wetterdaten` WRITE;
/*!40000 ALTER TABLE `wetterdaten` DISABLE KEYS */;
/*!40000 ALTER TABLE `wetterdaten` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'wxdb'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-01 13:06:51
