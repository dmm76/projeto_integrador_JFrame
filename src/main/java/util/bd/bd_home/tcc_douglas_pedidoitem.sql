-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: tcc_douglas
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
-- Table structure for table `pedidoitem`
--

DROP TABLE IF EXISTS `pedidoitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedidoitem` (
  `idPedidoItem` int NOT NULL AUTO_INCREMENT,
  `idItem` int NOT NULL,
  `idPedido` int NOT NULL,
  `quantidadeItem` int NOT NULL,
  `valorItem` decimal(7,2) NOT NULL,
  `valorTotalItem` decimal(10,2) GENERATED ALWAYS AS ((`quantidadeItem` * `valorItem`)) VIRTUAL,
  PRIMARY KEY (`idPedidoItem`),
  KEY `fk_item_produto_idx` (`idItem`),
  KEY `fk_item_pedido_idx` (`idPedido`),
  CONSTRAINT `fk_itemPedido_item` FOREIGN KEY (`idItem`) REFERENCES `item` (`idItem`),
  CONSTRAINT `fk_itemPedido_pedido` FOREIGN KEY (`idPedido`) REFERENCES `pedido` (`idPedido`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedidoitem`
--

LOCK TABLES `pedidoitem` WRITE;
/*!40000 ALTER TABLE `pedidoitem` DISABLE KEYS */;
INSERT INTO `pedidoitem` (`idPedidoItem`, `idItem`, `idPedido`, `quantidadeItem`, `valorItem`) VALUES (3,3,1,50,2.50),(4,10,1,50,8.00),(5,14,1,10,7.50),(6,1,2,10,8.50),(7,15,3,10,8.50),(8,15,4,10,5.00),(9,9,4,10,9.00),(10,12,4,10,8.00),(11,13,4,4,4.00),(12,5,4,10,5.00),(13,2,4,10,7.00),(19,7,7,1,42.00),(20,1,7,6,9.50),(21,2,7,10,7.00),(22,1,8,1,9.50),(23,2,8,1,7.00),(24,6,8,1,18.00),(25,16,8,1,25.00),(26,2,10,1,7.00),(27,1,11,1,9.50),(28,2,11,2,7.00),(29,4,11,5,6.00),(30,1,12,10,9.50),(31,2,12,6,7.00),(32,1,13,1,9.50),(33,3,14,2,2.00),(34,14,14,2,7.50),(35,1,15,1,9.50),(36,2,15,2,7.00),(37,9,1,10,14.00),(38,2,16,10,7.00),(39,3,16,1,2.00),(40,15,17,5,6.00),(41,3,18,10,2.00),(42,1,19,1,9.50),(43,2,19,2,7.00),(44,6,19,5,18.00),(45,1,20,1,9.50),(46,13,21,3,4.50),(47,3,22,2,2.00);
/*!40000 ALTER TABLE `pedidoitem` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-19 10:07:38
