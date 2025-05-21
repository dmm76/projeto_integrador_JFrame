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
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente` (
  `idCliente` int NOT NULL AUTO_INCREMENT,
  `nomeCliente` varchar(55) NOT NULL,
  `cpfCliente` varchar(25) NOT NULL,
  `emailCliente` varchar(35) DEFAULT NULL,
  `telefoneCliente` varchar(25) NOT NULL,
  `enderecoCliente` varchar(160) NOT NULL,
  PRIMARY KEY (`idCliente`),
  UNIQUE KEY `cpfCliente_UNIQUE` (`cpfCliente`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente`
--

LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` VALUES (1,'Douglas Marcelo Monquero','021.852.259-27','douglasdev@gmail.com','(44) 99901-3434','Avenida Brasil, 2222 - Centro - Maringá - PR'),(2,'Patricia Alves','026.987.456-65','patyhgatinha@gmail.com','(44) 9 9852-4563','Rua das Encantadas, 3212 - Jd. das Flores - Maringá - PR'),(3,'Lucas Vinicius Monquero','123.456.789-32','lucas@gmail.com','(44) 9 965-6598','Rua do Morgado, 321 - Jd Novo Horizonte - Maringá - Pr'),(4,'Carol Alves','654.128.719-39','carol@gmail.com','(44) 9 9952-1445','Avenida Herval, 876 - Zona 07 - Maringá - PR'),(5,'Pedro Paulo de Carvalho','171.898.659-98','ppc@msn.com','(44) 3216-4567','Rua das Esmeraldas, 445 - Sarandi - PR'),(6,'Jonathan da Silva','123.456.999-99','jou@msn.com','(44) 9 9876-4512','Rua da Calma, 323 - Maringá - Pr'),(8,'Paulo de Almeida de Barros','123.456.789-99','pab@consultoria.com.br','(44) 3216-4567','Avenida Horacio Racanello Filho, 765 - sala 122 - Maringá - PR'),(9,'Claudia Rosa Tupan','789.456.123-98','crt@senac.pr.gov.br','(44) 3219-7899','Avenida Colombo, 111 - Maringá - PR');
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-21  9:39:18
