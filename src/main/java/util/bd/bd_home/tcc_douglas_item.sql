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
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item` (
  `idItem` int NOT NULL AUTO_INCREMENT,
  `nomeProduto` varchar(65) NOT NULL,
  `descricaoProduto` varchar(75) DEFAULT NULL,
  `idMarca` int NOT NULL,
  `idCategoria` int NOT NULL,
  `idFornecedor` int NOT NULL,
  `valorUnitarioProduto` decimal(7,2) NOT NULL,
  PRIMARY KEY (`idItem`),
  KEY `fk_produto_fornecedor_idx` (`idFornecedor`),
  KEY `fk_produto_marca_idx` (`idMarca`),
  KEY `fk_produto_categoria_idx` (`idCategoria`),
  CONSTRAINT `fk_produto_categoria` FOREIGN KEY (`idCategoria`) REFERENCES `categoria` (`idCategoria`),
  CONSTRAINT `fk_produto_fornecedor` FOREIGN KEY (`idFornecedor`) REFERENCES `fornecedor` (`idFornecedor`),
  CONSTRAINT `fk_produto_marca` FOREIGN KEY (`idMarca`) REFERENCES `marca` (`idMarca`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item`
--

LOCK TABLES `item` WRITE;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
INSERT INTO `item` VALUES (1,'Cerveja Heineken 600ml','Cerveja Puro Malte',8,3,1,9.50),(2,'Refrigerante Coca-Cola 2L','Refrigerante tradicional',5,11,3,7.00),(3,'Água Mineral 500ml','Água sem gás',13,1,1,2.00),(4,'Pastel de Carne','Pastel frito recheado',14,16,14,6.00),(5,'Coxinha de Frango','Salgado frito tradicional',15,16,14,5.50),(6,'Pizza Calabresa P (4 fatias)','Massa fina, calabresa',16,10,2,18.00),(7,'Picanha Grelhada','Acompanha arroz e batata frita',17,10,9,42.00),(8,'Batata Frita P','Porção individual',18,9,7,10.00),(9,'Caipirinha de Limão','Cachaça, limão e açúcar',19,5,4,14.00),(10,'Suco Natural de Laranja','Suco sem conservantes',21,14,15,8.00),(11,'Porção de Queijo Coalho','Queijo assado com orégano',22,9,8,12.00),(12,'Hambúrguer Artesanal','160g carne, pão, queijo',23,16,14,20.00),(13,'Café Expresso','Café curto e forte',24,2,5,4.50),(14,'Chopp Pilsen 300ml','Chopp claro refrescante',3,3,12,7.50),(15,'Chocolate Quente 200ml','Com leite integral',25,2,6,6.00),(16,'Porção de Mandioca Frita','porção de 300 gramas de mandioca frita',15,9,3,25.00);
/*!40000 ALTER TABLE `item` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-19 10:07:36
