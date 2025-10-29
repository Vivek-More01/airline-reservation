
USE `railway`;
-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: airline_db
-- ------------------------------------------------------
-- Server version	8.0.43

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
-- Table structure for table `aircraft`
--

DROP TABLE IF EXISTS `aircraft`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `aircraft` (
  `aircraft_id` int NOT NULL AUTO_INCREMENT,
  `aircraft_model` varchar(100) NOT NULL,
  `total_seats` int NOT NULL,
  `seat_layout_json` json DEFAULT NULL,
  PRIMARY KEY (`aircraft_id`),
  UNIQUE KEY `aircraft_model` (`aircraft_model`),
  CONSTRAINT `chk_valid_json` CHECK (((`seat_layout_json` is null) or json_valid(`seat_layout_json`)))
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aircraft`
--

LOCK TABLES `aircraft` WRITE;
/*!40000 ALTER TABLE `aircraft` DISABLE KEYS */;
INSERT INTO `aircraft` VALUES (1,'Airbus A320',150,'{\"seatmap\": {\"rows\": 25, \"columns\": [\"A\", \"B\", \"C\", \"AISLE\", \"D\", \"E\", \"F\"], \"structures\": [{\"row\": 0, \"type\": \"LAVATORY\", \"position\": \"LEFT\"}, {\"row\": 0, \"type\": \"GALLEY\", \"position\": \"RIGHT\"}, {\"row\": 25, \"type\": \"LAVATORY\", \"position\": \"LEFT\"}, {\"row\": 25, \"type\": \"LAVATORY\", \"position\": \"REAR_RIGHT\"}], \"seatTypeMap\": {\"1A\": \"Business\", \"1B\": \"Business\", \"1C\": \"Business\", \"1D\": \"Business\", \"1E\": \"Business\", \"1F\": \"Business\", \"2A\": \"Business\", \"2B\": \"Business\", \"2C\": \"Business\", \"2D\": \"Business\", \"2E\": \"Business\", \"2F\": \"Business\", \"3A\": \"Business\", \"3B\": \"Business\", \"3C\": \"Business\", \"3D\": \"Business\", \"3E\": \"Business\", \"3F\": \"Business\", \"4A\": \"Business\", \"4B\": \"Business\", \"4C\": \"Business\", \"4D\": \"Business\", \"4E\": \"Business\", \"4F\": \"Business\", \"9A\": \"Exit Row\", \"9B\": \"Exit Row\", \"9C\": \"Exit Row\", \"9D\": \"Exit Row\", \"9E\": \"Exit Row\", \"9F\": \"Exit Row\", \"10A\": \"Exit Row\", \"10B\": \"Exit Row\", \"10C\": \"Exit Row\", \"10D\": \"Exit Row\", \"10E\": \"Exit Row\", \"10F\": \"Exit Row\"}}}'),(2,'Boeing 777',350,'{\"seatmap\": {\"rows\": 48, \"columns\": [\"A\", \"B\", \"C\", \"AISLE\", \"D\", \"E\", \"F\", \"G\", \"AISLE\", \"H\", \"J\", \"K\"], \"structures\": [{\"row\": 0, \"type\": \"GALLEY\", \"position\": \"FRONT\"}, {\"row\": 0, \"type\": \"LAVATORY\", \"position\": \"FRONT\"}, {\"row\": 9, \"type\": \"GALLEY\", \"position\": \"MID_FRONT\"}, {\"row\": 9, \"type\": \"LAVATORY\", \"position\": \"MID_FRONT\"}, {\"row\": 29, \"type\": \"GALLEY\", \"position\": \"MID_REAR\"}, {\"row\": 29, \"type\": \"LAVATORY\", \"position\": \"MID_REAR\"}, {\"row\": 48, \"type\": \"GALLEY\", \"position\": \"REAR\"}, {\"row\": 48, \"type\": \"LAVATORY\", \"position\": \"REAR\"}], \"seatTypeMap\": {\"1A\": \"Business\", \"1D\": \"Business\", \"1G\": \"Business\", \"1K\": \"Business\", \"2A\": \"Business\", \"2D\": \"Business\", \"2G\": \"Business\", \"2K\": \"Business\", \"3A\": \"Business\", \"3D\": \"Business\", \"3G\": \"Business\", \"3K\": \"Business\", \"4A\": \"Business\", \"4D\": \"Business\", \"4G\": \"Business\", \"4K\": \"Business\", \"5A\": \"Business\", \"5D\": \"Business\", \"5G\": \"Business\", \"5K\": \"Business\", \"6A\": \"Business\", \"6D\": \"Business\", \"6G\": \"Business\", \"6K\": \"Business\", \"7A\": \"Business\", \"7D\": \"Business\", \"7G\": \"Business\", \"7K\": \"Business\", \"8A\": \"Business\", \"8D\": \"Business\", \"8G\": \"Business\", \"8K\": \"Business\", \"9A\": \"Premium Economy\", \"9B\": \"Premium Economy\", \"9C\": \"Premium Economy\", \"9D\": \"Premium Economy\", \"9E\": \"Premium Economy\", \"9F\": \"Premium Economy\", \"9G\": \"Premium Economy\", \"9H\": \"Premium Economy\", \"9J\": \"Premium Economy\", \"9K\": \"Premium Economy\", \"10A\": \"Premium Economy\", \"10B\": \"Premium Economy\", \"10C\": \"Premium Economy\", \"10D\": \"Premium Economy\", \"10E\": \"Premium Economy\", \"10F\": \"Premium Economy\", \"10G\": \"Premium Economy\", \"10H\": \"Premium Economy\", \"10J\": \"Premium Economy\", \"10K\": \"Premium Economy\", \"11A\": \"Premium Economy\", \"11B\": \"Premium Economy\", \"11C\": \"Premium Economy\", \"11D\": \"Premium Economy\", \"11E\": \"Premium Economy\", \"11F\": \"Premium Economy\", \"11G\": \"Premium Economy\", \"11H\": \"Premium Economy\", \"11J\": \"Premium Economy\", \"11K\": \"Premium Economy\", \"12A\": \"Premium Economy\", \"12B\": \"Premium Economy\", \"12C\": \"Premium Economy\", \"12D\": \"Premium Economy\", \"12E\": \"Premium Economy\", \"12F\": \"Premium Economy\", \"12G\": \"Premium Economy\", \"12H\": \"Premium Economy\", \"12J\": \"Premium Economy\", \"12K\": \"Premium Economy\", \"13A\": \"Premium Economy\", \"13B\": \"Premium Economy\", \"13C\": \"Premium Economy\", \"13D\": \"Premium Economy\", \"13E\": \"Premium Economy\", \"13F\": \"Premium Economy\", \"13G\": \"Premium Economy\", \"13H\": \"Premium Economy\", \"13J\": \"Premium Economy\", \"13K\": \"Premium Economy\", \"14A\": \"Premium Economy\", \"14B\": \"Premium Economy\", \"14C\": \"Premium Economy\", \"14D\": \"Premium Economy\", \"14E\": \"Premium Economy\", \"14F\": \"Premium Economy\", \"14G\": \"Premium Economy\", \"14H\": \"Premium Economy\", \"14J\": \"Premium Economy\", \"14K\": \"Premium Economy\", \"16A\": \"Exit Row\", \"16K\": \"Exit Row\", \"28A\": \"Exit Row\", \"28K\": \"Exit Row\", \"47D\": \"Limited Recline\", \"47E\": \"Limited Recline\", \"47F\": \"Limited Recline\", \"47G\": \"Limited Recline\", \"48A\": \"Limited Recline\", \"48B\": \"Limited Recline\", \"48C\": \"Limited Recline\", \"48H\": \"Limited Recline\", \"48J\": \"Limited Recline\", \"48K\": \"Limited Recline\"}}}'),(3,'Boeing 737-800',180,'{\"seatmap\": {\"rows\": 30, \"columns\": [\"A\", \"B\", \"C\", \"AISLE\", \"D\", \"E\", \"F\"], \"structures\": [{\"row\": 0, \"type\": \"GALLEY\", \"position\": \"FRONT\"}, {\"row\": 0, \"type\": \"LAVATORY\", \"position\": \"FRONT\"}, {\"row\": 30, \"type\": \"LAVATORY\", \"position\": \"REAR_LEFT\"}, {\"row\": 30, \"type\": \"LAVATORY\", \"position\": \"REAR_RIGHT\"}, {\"row\": 30, \"type\": \"GALLEY\", \"position\": \"REAR\"}], \"seatTypeMap\": {\"1A\": \"Business\", \"1B\": \"Business\", \"1D\": \"Business\", \"1E\": \"Business\", \"2A\": \"Business\", \"2B\": \"Business\", \"2D\": \"Business\", \"2E\": \"Business\", \"3A\": \"Business\", \"3B\": \"Business\", \"3D\": \"Business\", \"3E\": \"Business\", \"7A\": \"Premium Economy\", \"7B\": \"Premium Economy\", \"7C\": \"Premium Economy\", \"7D\": \"Premium Economy\", \"7E\": \"Premium Economy\", \"7F\": \"Premium Economy\", \"8A\": \"Premium Economy\", \"8B\": \"Premium Economy\", \"8C\": \"Premium Economy\", \"8D\": \"Premium Economy\", \"8E\": \"Premium Economy\", \"8F\": \"Premium Economy\", \"15A\": \"Exit Row\", \"15B\": \"Exit Row\", \"15C\": \"Exit Row\", \"15D\": \"Exit Row\", \"15E\": \"Exit Row\", \"15F\": \"Exit Row\", \"16A\": \"Exit Row\", \"16B\": \"Exit Row\", \"16C\": \"Exit Row\", \"16D\": \"Exit Row\", \"16E\": \"Exit Row\", \"16F\": \"Exit Row\", \"29D\": \"Limited Recline\", \"29E\": \"Limited Recline\", \"29F\": \"Limited Recline\", \"30A\": \"Limited Recline\", \"30B\": \"Limited Recline\", \"30C\": \"Limited Recline\"}}}'),(4,'Airbus 380',550,'{\"seatmap\": {\"rows\": 55, \"columns\": [\"A\", \"B\", \"C\", \"AISLE\", \"D\", \"E\", \"F\", \"G\", \"AISLE\", \"H\", \"J\", \"K\"], \"structures\": [{\"row\": 0, \"type\": \"STAIRS\", \"position\": \"FRONT\"}, {\"row\": 0, \"type\": \"GALLEY\", \"position\": \"FRONT\"}, {\"row\": 0, \"type\": \"LAVATORY\", \"position\": \"FRONT\"}, {\"row\": 42, \"type\": \"GALLEY\", \"position\": \"MID_FRONT\"}, {\"row\": 42, \"type\": \"LAVATORY\", \"position\": \"MID_FRONT\"}, {\"row\": 55, \"type\": \"STAIRS\", \"position\": \"REAR\"}, {\"row\": 55, \"type\": \"GALLEY\", \"position\": \"REAR\"}, {\"row\": 55, \"type\": \"LAVATORY\", \"position\": \"REAR\"}], \"seatTypeMap\": {\"31A\": \"Premium Economy\", \"31B\": \"Premium Economy\", \"31C\": \"Premium Economy\", \"31D\": \"Premium Economy\", \"31E\": \"Premium Economy\", \"31F\": \"Premium Economy\", \"31G\": \"Premium Economy\", \"31H\": \"Premium Economy\", \"31J\": \"Premium Economy\", \"31K\": \"Premium Economy\", \"32A\": \"Premium Economy\", \"32B\": \"Premium Economy\", \"32C\": \"Premium Economy\", \"32D\": \"Premium Economy\", \"32E\": \"Premium Economy\", \"32F\": \"Premium Economy\", \"32G\": \"Premium Economy\", \"32H\": \"Premium Economy\", \"32J\": \"Premium Economy\", \"32K\": \"Premium Economy\", \"43A\": \"Exit Row\", \"43K\": \"Exit Row\", \"50A\": \"Exit Row\", \"50K\": \"Exit Row\", \"67A\": \"Exit Row\", \"67K\": \"Exit Row\", \"68A\": \"Exit Row\", \"68K\": \"Exit Row\", \"80D\": \"Limited Recline\", \"80E\": \"Limited Recline\", \"80F\": \"Limited Recline\", \"80G\": \"Limited Recline\", \"81A\": \"Limited Recline\", \"81K\": \"Limited Recline\"}}}'),(5,'Boeing 787',360,'{\"seatmap\": {\"rows\": 40, \"columns\": [\"A\", \"B\", \"C\", \"AISLE\", \"D\", \"E\", \"F\", \"AISLE\", \"G\", \"H\", \"J\"], \"structures\": [{\"row\": 0, \"type\": \"GALLEY\", \"position\": \"FRONT\"}, {\"row\": 0, \"type\": \"LAVATORY\", \"position\": \"FRONT\"}, {\"row\": 15, \"type\": \"GALLEY\", \"position\": \"MID_FRONT\"}, {\"row\": 15, \"type\": \"LAVATORY\", \"position\": \"MID_FRONT\"}, {\"row\": 29, \"type\": \"GALLEY\", \"position\": \"MID_REAR\"}, {\"row\": 29, \"type\": \"LAVATORY\", \"position\": \"MID_REAR\"}, {\"row\": 40, \"type\": \"GALLEY\", \"position\": \"REAR\"}, {\"row\": 40, \"type\": \"LAVATORY\", \"position\": \"REAR\"}], \"seatTypeMap\": {\"1A\": \"Business\", \"1C\": \"Business\", \"1D\": \"Business\", \"1F\": \"Business\", \"1G\": \"Business\", \"1J\": \"Business\", \"2A\": \"Business\", \"2C\": \"Business\", \"2D\": \"Business\", \"2F\": \"Business\", \"2G\": \"Business\", \"2J\": \"Business\", \"3A\": \"Business\", \"3B\": \"Business\", \"3C\": \"Business\", \"3D\": \"Business\", \"3E\": \"Business\", \"3F\": \"Business\", \"3G\": \"Business\", \"3H\": \"Business\", \"3J\": \"Business\", \"4A\": \"Business\", \"4B\": \"Business\", \"4C\": \"Business\", \"4D\": \"Business\", \"4E\": \"Business\", \"4F\": \"Business\", \"4G\": \"Business\", \"4H\": \"Business\", \"4J\": \"Business\", \"5A\": \"Business\", \"5B\": \"Business\", \"5C\": \"Business\", \"5D\": \"Business\", \"5E\": \"Business\", \"5F\": \"Business\", \"5G\": \"Business\", \"5H\": \"Business\", \"5J\": \"Business\", \"6A\": \"Business\", \"6B\": \"Business\", \"6C\": \"Business\", \"6D\": \"Business\", \"6E\": \"Business\", \"6F\": \"Business\", \"6G\": \"Business\", \"6H\": \"Business\", \"6J\": \"Business\", \"7A\": \"Business\", \"7B\": \"Business\", \"7C\": \"Business\", \"7D\": \"Business\", \"7E\": \"Business\", \"7F\": \"Business\", \"7G\": \"Business\", \"7H\": \"Business\", \"7J\": \"Business\", \"8A\": \"Business\", \"8B\": \"Business\", \"8C\": \"Business\", \"8D\": \"Business\", \"8E\": \"Business\", \"8F\": \"Business\", \"8G\": \"Business\", \"8H\": \"Business\", \"8J\": \"Business\", \"9A\": \"Business\", \"9B\": \"Business\", \"9C\": \"Business\", \"9D\": \"Business\", \"9E\": \"Business\", \"9F\": \"Business\", \"9G\": \"Business\", \"9H\": \"Business\", \"9J\": \"Business\", \"10A\": \"Premium Economy\", \"10B\": \"Premium Economy\", \"10C\": \"Premium Economy\", \"10D\": \"Premium Economy\", \"10E\": \"Premium Economy\", \"10F\": \"Premium Economy\", \"10G\": \"Premium Economy\", \"10H\": \"Premium Economy\", \"10J\": \"Premium Economy\", \"11A\": \"Premium Economy\", \"11B\": \"Premium Economy\", \"11C\": \"Premium Economy\", \"11D\": \"Premium Economy\", \"11E\": \"Premium Economy\", \"11F\": \"Premium Economy\", \"11G\": \"Premium Economy\", \"11H\": \"Premium Economy\", \"11J\": \"Premium Economy\", \"12A\": \"Premium Economy\", \"12B\": \"Premium Economy\", \"12C\": \"Premium Economy\", \"12D\": \"Premium Economy\", \"12E\": \"Premium Economy\", \"12F\": \"Premium Economy\", \"12G\": \"Premium Economy\", \"12H\": \"Premium Economy\", \"12J\": \"Premium Economy\", \"13A\": \"Premium Economy\", \"13B\": \"Premium Economy\", \"13C\": \"Premium Economy\", \"13D\": \"Premium Economy\", \"13E\": \"Premium Economy\", \"13F\": \"Premium Economy\", \"13G\": \"Premium Economy\", \"13H\": \"Premium Economy\", \"13J\": \"Premium Economy\", \"14A\": \"Premium Economy\", \"14B\": \"Premium Economy\", \"14C\": \"Premium Economy\", \"14D\": \"Premium Economy\", \"14E\": \"Premium Economy\", \"14F\": \"Premium Economy\", \"14G\": \"Premium Economy\", \"14H\": \"Premium Economy\", \"14J\": \"Premium Economy\", \"15A\": \"Premium Economy\", \"15B\": \"Premium Economy\", \"15C\": \"Premium Economy\", \"15D\": \"Premium Economy\", \"15E\": \"Premium Economy\", \"15F\": \"Premium Economy\", \"15G\": \"Premium Economy\", \"15H\": \"Premium Economy\", \"15J\": \"Premium Economy\", \"16A\": \"Exit Row\", \"16C\": \"Exit Row\", \"16D\": \"Exit Row\", \"16F\": \"Exit Row\", \"16G\": \"Exit Row\", \"16J\": \"Exit Row\", \"28A\": \"Exit Row\", \"28C\": \"Exit Row\", \"28D\": \"Exit Row\", \"28F\": \"Exit Row\", \"28G\": \"Exit Row\", \"28J\": \"Exit Row\", \"39D\": \"Limited Recline\", \"39E\": \"Limited Recline\", \"39F\": \"Limited Recline\", \"40A\": \"Limited Recline\", \"40B\": \"Limited Recline\", \"40C\": \"Limited Recline\", \"40G\": \"Limited Recline\", \"40H\": \"Limited Recline\", \"40J\": \"Limited Recline\"}}}'),(6,'ATR72',72,'{\"seatmap\": {\"rows\": 18, \"columns\": [\"A\", \"B\", \"AISLE\", \"C\", \"D\"], \"structures\": [{\"row\": 0, \"type\": \"GALLEY\", \"position\": \"FRONT\"}, {\"row\": 0, \"type\": \"LAVATORY\", \"position\": \"FRONT\"}, {\"row\": 18, \"type\": \"DOOR/STAIRS\", \"position\": \"REAR\"}], \"seatTypeMap\": {\"1A\": \"Exit Row\", \"1B\": \"Exit Row\", \"1C\": \"Exit Row\", \"1D\": \"Exit Row\", \"17A\": \"Limited Recline\", \"17B\": \"Limited Recline\", \"18C\": \"Limited Recline\", \"18D\": \"Limited Recline\"}}}'),(7,'Airbus A330',360,'{\"seatmap\": {\"rows\": 45, \"columns\": [\"A\", \"C\", \"AISLE\", \"D\", \"E\", \"F\", \"G\", \"AISLE\", \"H\", \"K\"], \"structures\": [{\"row\": 0, \"type\": \"GALLEY\", \"position\": \"FRONT\"}, {\"row\": 0, \"type\": \"LAVATORY\", \"position\": \"FRONT\"}, {\"row\": 19, \"type\": \"GALLEY\", \"position\": \"MID\"}, {\"row\": 19, \"type\": \"LAVATORY\", \"position\": \"MID\"}, {\"row\": 45, \"type\": \"GALLEY\", \"position\": \"REAR\"}, {\"row\": 45, \"type\": \"LAVATORY\", \"position\": \"REAR\"}], \"seatTypeMap\": {\"1A\": \"Business\", \"1C\": \"Business\", \"1H\": \"Business\", \"1K\": \"Business\", \"2A\": \"Business\", \"2C\": \"Business\", \"2D\": \"Business\", \"2E\": \"Business\", \"2F\": \"Business\", \"2G\": \"Business\", \"2H\": \"Business\", \"2K\": \"Business\", \"3A\": \"Business\", \"3C\": \"Business\", \"3D\": \"Business\", \"3E\": \"Business\", \"3F\": \"Business\", \"3G\": \"Business\", \"3H\": \"Business\", \"3K\": \"Business\", \"4A\": \"Business\", \"4C\": \"Business\", \"4D\": \"Business\", \"4E\": \"Business\", \"4F\": \"Business\", \"4G\": \"Business\", \"4H\": \"Business\", \"4K\": \"Business\", \"5A\": \"Business\", \"5C\": \"Business\", \"5D\": \"Business\", \"5E\": \"Business\", \"5F\": \"Business\", \"5G\": \"Business\", \"5H\": \"Business\", \"5K\": \"Business\", \"6A\": \"Business\", \"6C\": \"Business\", \"6D\": \"Business\", \"6E\": \"Business\", \"6F\": \"Business\", \"6G\": \"Business\", \"6H\": \"Business\", \"6K\": \"Business\", \"20A\": \"Exit Row\", \"20C\": \"Exit Row\", \"20H\": \"Exit Row\", \"20K\": \"Exit Row\", \"35D\": \"Exit Row\", \"35E\": \"Exit Row\", \"35F\": \"Exit Row\", \"35G\": \"Exit Row\", \"44D\": \"Limited Recline\", \"44E\": \"Limited Recline\", \"44F\": \"Limited Recline\", \"44G\": \"Limited Recline\", \"45A\": \"Limited Recline\", \"45C\": \"Limited Recline\", \"45H\": \"Limited Recline\", \"45K\": \"Limited Recline\"}}}'),(8,'Airbus A350',378,'{\"seatmap\": {\"rows\": 42, \"columns\": [\"A\", \"B\", \"C\", \"AISLE\", \"D\", \"E\", \"F\", \"AISLE\", \"G\", \"H\", \"J\"], \"structures\": [{\"row\": 0, \"type\": \"GALLEY\", \"position\": \"FRONT\"}, {\"row\": 0, \"type\": \"LAVATORY\", \"position\": \"FRONT\"}, {\"row\": 15, \"type\": \"GALLEY\", \"position\": \"MID_FRONT\"}, {\"row\": 15, \"type\": \"LAVATORY\", \"position\": \"MID_FRONT\"}, {\"row\": 30, \"type\": \"GALLEY\", \"position\": \"MID_REAR\"}, {\"row\": 30, \"type\": \"LAVATORY\", \"position\": \"MID_REAR\"}, {\"row\": 42, \"type\": \"GALLEY\", \"position\": \"REAR\"}, {\"row\": 42, \"type\": \"LAVATORY\", \"position\": \"REAR\"}], \"seatTypeMap\": {\"1A\": \"Business\", \"1C\": \"Business\", \"1D\": \"Business\", \"1F\": \"Business\", \"1G\": \"Business\", \"1J\": \"Business\", \"2A\": \"Business\", \"2C\": \"Business\", \"2D\": \"Business\", \"2F\": \"Business\", \"2G\": \"Business\", \"2J\": \"Business\", \"3A\": \"Business\", \"3C\": \"Business\", \"3D\": \"Business\", \"3F\": \"Business\", \"3G\": \"Business\", \"3J\": \"Business\", \"4A\": \"Business\", \"4C\": \"Business\", \"4D\": \"Business\", \"4F\": \"Business\", \"4G\": \"Business\", \"4J\": \"Business\", \"5A\": \"Business\", \"5C\": \"Business\", \"5D\": \"Business\", \"5F\": \"Business\", \"5G\": \"Business\", \"5J\": \"Business\", \"6A\": \"Business\", \"6C\": \"Business\", \"6D\": \"Business\", \"6F\": \"Business\", \"6G\": \"Business\", \"6J\": \"Business\", \"10A\": \"Premium Economy\", \"10B\": \"Premium Economy\", \"10C\": \"Premium Economy\", \"10D\": \"Premium Economy\", \"10E\": \"Premium Economy\", \"10F\": \"Premium Economy\", \"10G\": \"Premium Economy\", \"10H\": \"Premium Economy\", \"10J\": \"Premium Economy\", \"11A\": \"Premium Economy\", \"11B\": \"Premium Economy\", \"11C\": \"Premium Economy\", \"11D\": \"Premium Economy\", \"11E\": \"Premium Economy\", \"11F\": \"Premium Economy\", \"11G\": \"Premium Economy\", \"11H\": \"Premium Economy\", \"11J\": \"Premium Economy\", \"12A\": \"Premium Economy\", \"12B\": \"Premium Economy\", \"12C\": \"Premium Economy\", \"12D\": \"Premium Economy\", \"12E\": \"Premium Economy\", \"12F\": \"Premium Economy\", \"12G\": \"Premium Economy\", \"12H\": \"Premium Economy\", \"12J\": \"Premium Economy\", \"16A\": \"Exit Row\", \"16C\": \"Exit Row\", \"16D\": \"Exit Row\", \"16F\": \"Exit Row\", \"16G\": \"Exit Row\", \"16J\": \"Exit Row\", \"29A\": \"Exit Row\", \"29C\": \"Exit Row\", \"29D\": \"Exit Row\", \"29F\": \"Exit Row\", \"29G\": \"Exit Row\", \"29J\": \"Exit Row\", \"41D\": \"Limited Recline\", \"41E\": \"Limited Recline\", \"41F\": \"Limited Recline\", \"42A\": \"Limited Recline\", \"42B\": \"Limited Recline\", \"42C\": \"Limited Recline\", \"42G\": \"Limited Recline\", \"42H\": \"Limited Recline\", \"42J\": \"Limited Recline\"}}}'),(9,'Airbus A321',216,'{\"seatmap\": {\"rows\": 36, \"columns\": [\"A\", \"B\", \"C\", \"AISLE\", \"D\", \"E\", \"F\"], \"structures\": [{\"row\": 0, \"type\": \"LAVATORY\", \"position\": \"FRONT_LEFT\"}, {\"row\": 0, \"type\": \"GALLEY\", \"position\": \"FRONT_RIGHT\"}, {\"row\": 24, \"type\": \"LAVATORY\", \"position\": \"MID_LEFT\"}, {\"row\": 24, \"type\": \"LAVATORY\", \"position\": \"MID_RIGHT\"}, {\"row\": 36, \"type\": \"LAVATORY\", \"position\": \"REAR_LEFT\"}, {\"row\": 36, \"type\": \"GALLEY\", \"position\": \"REAR_RIGHT\"}], \"seatTypeMap\": {\"1A\": \"Business\", \"1C\": \"Business\", \"1D\": \"Business\", \"1F\": \"Business\", \"2A\": \"Business\", \"2C\": \"Business\", \"2D\": \"Business\", \"2F\": \"Business\", \"3A\": \"Business\", \"3C\": \"Business\", \"3D\": \"Business\", \"3F\": \"Business\", \"4A\": \"Business\", \"4C\": \"Business\", \"4D\": \"Business\", \"4F\": \"Business\", \"5A\": \"Business\", \"5C\": \"Business\", \"5D\": \"Business\", \"5F\": \"Business\", \"10A\": \"Exit Row\", \"10B\": \"Exit Row\", \"10C\": \"Exit Row\", \"10D\": \"Exit Row\", \"10E\": \"Exit Row\", \"10F\": \"Exit Row\", \"11A\": \"Exit Row\", \"11B\": \"Exit Row\", \"11C\": \"Exit Row\", \"11D\": \"Exit Row\", \"11E\": \"Exit Row\", \"11F\": \"Exit Row\", \"24A\": \"Exit Row\", \"24B\": \"Exit Row\", \"24C\": \"Exit Row\", \"24D\": \"Exit Row\", \"24E\": \"Exit Row\", \"24F\": \"Exit Row\", \"25A\": \"Exit Row\", \"25B\": \"Exit Row\", \"25C\": \"Exit Row\", \"25D\": \"Exit Row\", \"25E\": \"Exit Row\", \"25F\": \"Exit Row\", \"35D\": \"Limited Recline\", \"35E\": \"Limited Recline\", \"35F\": \"Limited Recline\", \"36A\": \"Limited Recline\", \"36B\": \"Limited Recline\", \"36C\": \"Limited Recline\"}}}'),(10,'E190',100,'{\"seatmap\": {\"rows\": 25, \"columns\": [\"A\", \"C\", \"AISLE\", \"D\", \"F\"], \"structures\": [{\"row\": 0, \"type\": \"GALLEY\", \"position\": \"FRONT\"}, {\"row\": 0, \"type\": \"LAVATORY\", \"position\": \"FRONT\"}, {\"row\": 25, \"type\": \"LAVATORY\", \"position\": \"REAR\"}, {\"row\": 25, \"type\": \"GALLEY\", \"position\": \"REAR\"}], \"seatTypeMap\": {\"1A\": \"Business\", \"1C\": \"Business\", \"1D\": \"Business\", \"1F\": \"Business\", \"2A\": \"Business\", \"2C\": \"Business\", \"2D\": \"Business\", \"2F\": \"Business\", \"10A\": \"Exit Row\", \"10C\": \"Exit Row\", \"10D\": \"Exit Row\", \"10F\": \"Exit Row\", \"11A\": \"Exit Row\", \"11C\": \"Exit Row\", \"11D\": \"Exit Row\", \"11F\": \"Exit Row\", \"24D\": \"Limited Recline\", \"24F\": \"Limited Recline\", \"25A\": \"Limited Recline\", \"25C\": \"Limited Recline\"}}}'),(11,'Q400',76,'{\"seatmap\": {\"rows\": 19, \"columns\": [\"A\", \"B\", \"AISLE\", \"C\", \"D\"], \"structures\": [{\"row\": 0, \"type\": \"GALLEY\", \"position\": \"FRONT\"}, {\"row\": 0, \"type\": \"LAVATORY\", \"position\": \"FRONT\"}, {\"row\": 0, \"type\": \"DOOR\", \"position\": \"FRONT\"}, {\"row\": 19, \"type\": \"LAVATORY\", \"position\": \"REAR\"}, {\"row\": 19, \"type\": \"GALLEY\", \"position\": \"REAR\"}, {\"row\": 19, \"type\": \"DOOR\", \"position\": \"REAR\"}], \"seatTypeMap\": {\"1A\": \"Premium Economy\", \"1B\": \"Premium Economy\", \"1C\": \"Premium Economy\", \"1D\": \"Premium Economy\", \"2A\": \"Exit Row\", \"2B\": \"Exit Row\", \"2C\": \"Exit Row\", \"2D\": \"Exit Row\", \"18C\": \"Limited Recline\", \"18D\": \"Limited Recline\", \"19A\": \"Limited Recline\", \"19B\": \"Limited Recline\"}}}'),(12,'Airbus A220',125,'{\"seatmap\": {\"rows\": 25, \"columns\": [\"A\", \"C\", \"AISLE\", \"D\", \"E\", \"F\"], \"structures\": [{\"row\": 0, \"type\": \"GALLEY\", \"position\": \"FRONT\"}, {\"row\": 0, \"type\": \"LAVATORY\", \"position\": \"FRONT\"}, {\"row\": 25, \"type\": \"LAVATORY\", \"position\": \"REAR_LEFT\"}, {\"row\": 25, \"type\": \"GALLEY\", \"position\": \"REAR_RIGHT\"}], \"seatTypeMap\": {\"1A\": \"Business\", \"1C\": \"Business\", \"1D\": \"Business\", \"1E\": \"Business\", \"1F\": \"Business\", \"2A\": \"Business\", \"2C\": \"Business\", \"2D\": \"Business\", \"2E\": \"Business\", \"2F\": \"Business\", \"3A\": \"Business\", \"3C\": \"Business\", \"3D\": \"Business\", \"3E\": \"Business\", \"3F\": \"Business\", \"10A\": \"Exit Row\", \"10C\": \"Exit Row\", \"11D\": \"Exit Row\", \"11E\": \"Exit Row\", \"11F\": \"Exit Row\", \"24D\": \"Limited Recline\", \"24E\": \"Limited Recline\", \"24F\": \"Limited Recline\", \"25A\": \"Limited Recline\", \"25C\": \"Limited Recline\"}}}');
/*!40000 ALTER TABLE `aircraft` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `airlines`
--

DROP TABLE IF EXISTS `airlines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `airlines` (
  `airline_id` int NOT NULL AUTO_INCREMENT,
  `airline_name` varchar(100) NOT NULL,
  `iata_code` varchar(3) DEFAULT NULL,
  PRIMARY KEY (`airline_id`),
  UNIQUE KEY `airline_name` (`airline_name`),
  UNIQUE KEY `iata_code` (`iata_code`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `airlines`
--

LOCK TABLES `airlines` WRITE;
/*!40000 ALTER TABLE `airlines` DISABLE KEYS */;
INSERT INTO `airlines` VALUES (1,'Emirates','EK'),(2,'British Airways','BA'),(3,'Lufthansa','LH'),(4,'Delta','DL'),(5,'United Airlines','UA');
/*!40000 ALTER TABLE `airlines` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `booking`
--

DROP TABLE IF EXISTS `booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking` (
  `booking_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `passenger_id` int NOT NULL,
  `flight_id` int NOT NULL,
  `seat_no` varchar(10) NOT NULL,
  `seat_type_id` int NOT NULL,
  `calculated_price` decimal(10,2) NOT NULL,
  `status` varchar(20) NOT NULL,
  `booking_date` datetime NOT NULL,
  `pnr` varchar(20) NOT NULL,
  PRIMARY KEY (`booking_id`),
  UNIQUE KEY `pnr` (`pnr`),
  UNIQUE KEY `unique_flight_seat` (`flight_id`,`seat_no`),
  KEY `user_id` (`user_id`),
  KEY `passenger_id` (`passenger_id`),
  KEY `seat_type_id` (`seat_type_id`),
  CONSTRAINT `booking_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `booking_ibfk_2` FOREIGN KEY (`passenger_id`) REFERENCES `passengers` (`passenger_id`) ON DELETE CASCADE,
  CONSTRAINT `booking_ibfk_3` FOREIGN KEY (`flight_id`) REFERENCES `flight` (`flight_id`),
  CONSTRAINT `booking_ibfk_4` FOREIGN KEY (`seat_type_id`) REFERENCES `seat_types` (`seat_type_id`),
  CONSTRAINT `booking_chk_1` CHECK ((`status` in (_utf8mb4'CONFIRMED',_utf8mb4'CANCELLED',_utf8mb4'CHECKED-IN')))
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking`
--

LOCK TABLES `booking` WRITE;
/*!40000 ALTER TABLE `booking` DISABLE KEYS */;
INSERT INTO `booking` VALUES (1,1,1,1,'5A',3,2380.00,'CHECKED-IN','2025-10-20 10:00:00','EKABCDEF'),(2,2,2,2,'14C',1,650.00,'CANCELLED','2025-10-21 15:30:00','BAGHIJKL'),(3,12,3,5,'2A',3,1250.00,'CONFIRMED','2025-10-24 09:53:26','047D9CA0'),(4,12,4,5,'2B',3,1250.00,'CANCELLED','2025-10-24 09:53:26','B5FA5C93'),(5,12,5,5,'2C',3,1250.00,'CONFIRMED','2025-10-24 09:53:26','24258491'),(6,12,6,5,'19B',1,500.00,'CHECKED-IN','2025-10-24 18:22:38','37A974BF'),(7,12,7,5,'19C',1,500.00,'CONFIRMED','2025-10-24 18:22:38','B63879C1'),(8,12,8,5,'19A',1,500.00,'CONFIRMED','2025-10-24 18:22:39','E1AE849F');
/*!40000 ALTER TABLE `booking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flight`
--

DROP TABLE IF EXISTS `flight`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flight` (
  `flight_id` int NOT NULL AUTO_INCREMENT,
  `airline_id` int NOT NULL,
  `aircraft_id` int NOT NULL,
  `source` varchar(100) NOT NULL,
  `destination` varchar(100) NOT NULL,
  `departure` datetime NOT NULL,
  `arrival` datetime NOT NULL,
  `seats_available` int NOT NULL,
  `base_price` decimal(10,2) NOT NULL,
  `status` varchar(20) NOT NULL DEFAULT 'Scheduled',
  PRIMARY KEY (`flight_id`),
  KEY `airline_id` (`airline_id`),
  KEY `aircraft_id` (`aircraft_id`),
  CONSTRAINT `flight_ibfk_1` FOREIGN KEY (`airline_id`) REFERENCES `airlines` (`airline_id`),
  CONSTRAINT `flight_ibfk_2` FOREIGN KEY (`aircraft_id`) REFERENCES `aircraft` (`aircraft_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flight`
--

LOCK TABLES `flight` WRITE;
/*!40000 ALTER TABLE `flight` DISABLE KEYS */;
INSERT INTO `flight` VALUES (1,1,2,'New York','Dubai','2025-11-01 10:00:00','2025-11-02 07:00:00',350,850.00,'Scheduled'),(2,2,1,'London','New York','2025-11-01 14:30:00','2025-11-01 17:30:00',0,650.00,'Cancelled'),(3,3,2,'Frankfurt','Tokyo','2025-11-02 21:00:00','2025-11-03 16:00:00',350,950.00,'Scheduled'),(4,4,2,'Los Angeles','Sydney','2025-11-02 23:00:00','2025-11-04 06:00:00',350,1100.00,'Scheduled'),(5,3,1,'Kolkata','London','2025-10-25 07:28:00','2025-10-26 07:28:00',145,500.00,'Scheduled'),(6,3,2,'Kolkata','London','2025-10-25 09:57:00','2025-10-26 09:57:00',350,500.00,'Scheduled'),(7,2,4,'Kolkata','London','2025-10-25 14:39:00','2025-10-26 14:39:00',550,500.00,'Scheduled'),(8,1,6,'New York','London','2025-10-25 18:25:00','2025-10-26 18:25:00',72,800.00,'Scheduled'),(9,5,1,'New York','London','2025-10-29 18:42:00','2025-10-30 18:42:00',150,800.00,'Scheduled');
/*!40000 ALTER TABLE `flight` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `passengers`
--

DROP TABLE IF EXISTS `passengers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `passengers` (
  `passenger_id` int NOT NULL AUTO_INCREMENT,
  `full_name` varchar(255) NOT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `passport_number` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`passenger_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `passengers`
--

LOCK TABLES `passengers` WRITE;
/*!40000 ALTER TABLE `passengers` DISABLE KEYS */;
INSERT INTO `passengers` VALUES (1,'John Doe','123-456-7890','P12345'),(2,'Jane Smith','987-654-3210','P67890'),(3,'Pass1','01256846852','12456'),(4,'Suresh Wangjam','01256846852','12456'),(5,'Pass3','01256846852','12456'),(6,'Pass1','01256846852','12456'),(7,'Suresh Wangjam','01256846852','12456'),(8,'Pass3','01256846852','12456');
/*!40000 ALTER TABLE `passengers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment` (
  `payment_id` int NOT NULL AUTO_INCREMENT,
  `booking_id` int NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `payment_date` datetime NOT NULL,
  `transaction_id` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`payment_id`),
  KEY `booking_id` (`booking_id`),
  CONSTRAINT `payment_ibfk_1` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`booking_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment`
--

LOCK TABLES `payment` WRITE;
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
INSERT INTO `payment` VALUES (1,1,2380.00,'2025-10-20 10:05:00','txn_ek789'),(2,2,650.00,'2025-10-21 15:35:00','txn_ba101');
/*!40000 ALTER TABLE `payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pricing_rules`
--

DROP TABLE IF EXISTS `pricing_rules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pricing_rules` (
  `rule_id` int NOT NULL AUTO_INCREMENT,
  `airline_id` int DEFAULT NULL,
  `seat_type_id` int NOT NULL,
  `price_multiplier` decimal(5,2) NOT NULL DEFAULT '1.00',
  PRIMARY KEY (`rule_id`),
  UNIQUE KEY `unique_rule` (`airline_id`,`seat_type_id`),
  KEY `seat_type_id` (`seat_type_id`),
  CONSTRAINT `pricing_rules_ibfk_1` FOREIGN KEY (`airline_id`) REFERENCES `airlines` (`airline_id`),
  CONSTRAINT `pricing_rules_ibfk_2` FOREIGN KEY (`seat_type_id`) REFERENCES `seat_types` (`seat_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pricing_rules`
--

LOCK TABLES `pricing_rules` WRITE;
/*!40000 ALTER TABLE `pricing_rules` DISABLE KEYS */;
INSERT INTO `pricing_rules` VALUES (1,NULL,1,1.00),(2,NULL,4,1.15),(3,NULL,2,1.40),(4,NULL,3,2.50),(5,1,3,2.80);
/*!40000 ALTER TABLE `pricing_rules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seat_types`
--

DROP TABLE IF EXISTS `seat_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seat_types` (
  `seat_type_id` int NOT NULL AUTO_INCREMENT,
  `type_name` varchar(50) NOT NULL,
  `description` text,
  PRIMARY KEY (`seat_type_id`),
  UNIQUE KEY `type_name` (`type_name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seat_types`
--

LOCK TABLES `seat_types` WRITE;
/*!40000 ALTER TABLE `seat_types` DISABLE KEYS */;
INSERT INTO `seat_types` VALUES (1,'Economy','Standard economy seat'),(2,'Premium Economy','Economy seat with extra legroom and amenities'),(3,'Business','Business class seat with more space and features'),(4,'Exit Row','Seat located at an emergency exit row');
/*!40000 ALTER TABLE `seat_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `role` varchar(20) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `account_status` varchar(20) NOT NULL DEFAULT 'Active',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`),
  CONSTRAINT `users_chk_1` CHECK ((`role` in (_utf8mb4'Passenger',_utf8mb4'Admin',_utf8mb4'Staff')))
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Admin User','admin@example.com','Admin',NULL,'Active'),(2,'John Doe','john.doe@example.com','Passenger',NULL,'ACTIVE'),(3,'Jane Smith','jane.smith@example.com','Passenger',NULL,'ACTIVE'),(4,'Peter Jones','peter.jones@example.com','Passenger',NULL,'ACTIVE'),(5,'Mary Williams','mary.w@example.com','Passenger',NULL,'ACTIVE'),(6,'David Brown','d.brown@example.com','Passenger',NULL,'ACTIVE'),(7,'s','vivek789@gmail.com','Passenger',NULL,'BANNED'),(8,'whyneed youname','vivek759@gmail.com','Passenger','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92','ACTIVE'),(9,'daupl printout','rm457@gmail.com','Passenger','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92','ACTIVE'),(10,'Nitish Panse','nps564@gmail.com','Passenger','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92','ACTIVE'),(11,'Suresh S Wangjam','nps584@gmail.com','Passenger','$2a$10$6lwJJ5OyiOYo3/SqobtHr.yP.RwIJt8Wbh0XBIARNJk0xPz4DMV4a','ACTIVE'),(12,'Vivek','vmm1605@gmail.com','Admin','$2a$12$iYmHYfezLHt5woc3ywDI2uBt330VAVKxhJZKXWa9PKSWCyLyemqXO','Active'),(13,'Staff_Member','abc123@gmail.com','Staff','$2y$10$YS4OhMEuYqyM68ohk09O7OnrjsitPV2tWSmMHU2auFtMA5p2YyTlq','Active'),(17,'Rushikesh Mirashe','rushikesh2005@gmail.com','Staff','$2a$10$oMbtgwPFzuqWlBSQ7EG.rOzc/UesehXgjw0QrHnH0iMsNT.YJpLYm','ACTIVE');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-29 10:32:40
