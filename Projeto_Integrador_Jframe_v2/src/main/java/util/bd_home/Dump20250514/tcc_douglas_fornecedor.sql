-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: tcc_douglas
-- ------------------------------------------------------
-- Server version	8.0.41

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
-- Table structure for table `fornecedor`
--

DROP TABLE IF EXISTS `fornecedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fornecedor` (
  `idFornecedor` int NOT NULL AUTO_INCREMENT,
  `nomeFornecedor` varchar(55) NOT NULL,
  `cnpjFornecedor` varchar(25) NOT NULL,
  `emailFornecedor` varchar(35) DEFAULT NULL,
  `telefoneFornecedor` varchar(25) NOT NULL,
  `enderecoFornecedor` varchar(60) NOT NULL,
  PRIMARY KEY (`idFornecedor`),
  UNIQUE KEY `cnpjFornecedor_UNIQUE` (`cnpjFornecedor`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fornecedor`
--

LOCK TABLES `fornecedor` WRITE;
/*!40000 ALTER TABLE `fornecedor` DISABLE KEYS */;
INSERT INTO `fornecedor` VALUES (1,'Maringá Bebidas Distribuidora','12.345.678/0001-01','contato@maringabebidas.com','(44) 3222-1000','Av. Colombo, 3500 - Maringá-PR'),(2,'Produtos Refrimix','23.456.789/0001-02','vendas@refrimix.com','(44) 3033-2211	','R. São João, 411 - Maringá-PR'),(3,'BarSul Distribuidora de Alimentos','34.567.890/0001-03','atendimento@barsul.com.br','(44) 3344-0099','Av. Brasil, 1500 - Maringá-PR'),(4,'Rei do Gelo Curitiba','45.678.901/0001-04','contato@reidogelo.com','(41) 3010-3030','R. Chile, 152 - Curitiba-PR'),(5,'Café Colonial Paraná','56.789.012/0001-05','comercial@cafecolonialpr.com','(41) 3222-4455','R. XV de Novembro, 800 - Curitiba'),(6,'Doces e Sobremesas do Vale','67.890.123/0001-06','doces@valesobremesas.com','(44) 3221-9988','R. das Rosas, 210 - Sarandi-PR'),(7,'Distribuidora Campo Belo','78.901.234/0001-07','pedidos@campobelo.com.br','(44) 99887-7788','R. Independência, 123 - Maringá'),(8,'Queijos e Laticínios Curitiba','89.012.345/0001-08','curitibaqueijos@gmail.com','(41) 99874-1200','R. São Francisco, 82 - Curitiba'),(9,'Frigorífico Bom Corte','90.123.456/0001-09','vendas@bomcorte.com','(44) 3030-9000','Av. Mandacaru, 970 - Maringá'),(10,'Verduras Paraná Agro','01.234.567/0001-10','contato@paranaagro.com','(41) 3555-2525','R. das Acácias, 71 - Curitiba'),(11,'Grãos e Cereais Iguaçu','02.345.678/0001-11','comercial@iguacucereais.com','(44) 3366-4040','R. São Jorge, 55 - Maringá'),(12,'Bebidas Geladas Curitiba','03.456.789/0001-12','vendas@bgcuritiba.com','(41) 99999-1212','Av. Curitiba, 900 - Curitiba'),(13,'Distribuidora Casa do Bar','04.567.890/0001-13','casadobar@distribuidora.com','(44) 3333-4141','R. do Comércio, 11 - Maringá'),(14,'Panificadora União - Fornecedores','05.678.901/0001-14','uniaopanificadora@gmail.com','(44) 3222-2020','Av. Kakogawa, 800 - Maringá'),(15,'Produtos Naturais Serra Verde','06.789.012/0001-15','atendimento@serraverde.com','(41) 3250-8888','R. das Palmeiras, 402 - Curitiba');
/*!40000 ALTER TABLE `fornecedor` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-14 17:05:20
