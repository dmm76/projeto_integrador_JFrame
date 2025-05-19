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
-- Table structure for table `pedido`
--

DROP TABLE IF EXISTS `pedido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedido` (
  `idPedido` int NOT NULL AUTO_INCREMENT,
  `dataPedido` date NOT NULL,
  `statusPedido` varchar(45) NOT NULL COMMENT 'aprovado\nreprovado\nem espera\nconcluido',
  `idFormaPagamento` int NOT NULL,
  `idCliente` int NOT NULL,
  `valorTotalPedido` decimal(10,2) NOT NULL,
  PRIMARY KEY (`idPedido`),
  KEY `fk_pedido_formaPagamento_idx` (`idFormaPagamento`),
  KEY `fk_pedido_cliente_idx` (`idCliente`),
  CONSTRAINT `fk_pedido_cliente` FOREIGN KEY (`idCliente`) REFERENCES `cliente` (`idCliente`),
  CONSTRAINT `fk_pedido_formaPagamento` FOREIGN KEY (`idFormaPagamento`) REFERENCES `formapagamento` (`idFormaPagamento`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedido`
--

LOCK TABLES `pedido` WRITE;
/*!40000 ALTER TABLE `pedido` DISABLE KEYS */;
INSERT INTO `pedido` VALUES (1,'2025-05-16','Pedido',1,1,740.00),(2,'2025-05-16','Pedido',6,2,85.00),(3,'2025-05-16','Pedido',12,1,85.00),(4,'2025-05-16','Pedido',5,2,356.00),(7,'2025-05-16','Pedido',6,3,169.00),(8,'2025-05-16','Em Aberto',1,1,59.50),(9,'2025-05-16','Em Aberto',1,1,0.00),(10,'2025-05-16','Em Aberto',1,1,7.00),(11,'2025-05-17','Finalizado',1,1,53.50),(12,'2025-05-17','Finalizado',1,3,137.00),(13,'2025-05-17','Finalizado',2,3,9.50),(14,'2025-05-17','Finalizado',1,1,19.00),(15,'2025-05-17','Finalizado',2,1,23.50),(16,'2025-05-18','Finalizado',1,1,72.00),(17,'2025-05-18','Finalizado',2,4,30.00),(18,'2025-05-18','Finalizado',1,5,20.00),(19,'2025-05-18','Finalizado',1,5,113.50),(20,'2025-05-18','Finalizado',1,1,9.50),(21,'2025-05-19','Finalizado',12,6,13.50),(22,'2025-05-19','Finalizado',1,1,4.00);
/*!40000 ALTER TABLE `pedido` ENABLE KEYS */;
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
