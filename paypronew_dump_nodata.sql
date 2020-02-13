-- MySQL dump 10.13  Distrib 5.7.21, for Linux (x86_64)
--
-- Host: localhost    Database: paypronew
-- ------------------------------------------------------
-- Server version	5.7.21

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Blockchain`
--

DROP TABLE IF EXISTS `Blockchain`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Blockchain` (
  `idBlockchain` int(11) NOT NULL,
  `siglaBlockchain` varchar(10) DEFAULT NULL,
  `descripcion` varchar(45) DEFAULT NULL,
  `gasPrice` int(11) DEFAULT NULL,
  `gasLimit` int(11) DEFAULT NULL,
  `rutaKeyStore` varchar(150) DEFAULT NULL,
  `direccion` varchar(20) DEFAULT NULL,
  `puertoRPC` int(11) DEFAULT NULL,
  `urlWebDetail` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`idBlockchain`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Campana`
--

DROP TABLE IF EXISTS `Campana`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Campana` (
  `idCampana` int(11) NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(45) DEFAULT NULL,
  `fechaInicio` datetime DEFAULT NULL,
  `fechaFin` datetime DEFAULT NULL,
  `estado` char(50) DEFAULT NULL,
  PRIMARY KEY (`idCampana`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Cuenta`
--

DROP TABLE IF EXISTS `Cuenta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Cuenta` (
  `idBlockchain` int(11) NOT NULL,
  `idUsuario` varchar(120) NOT NULL,
  `descripcion` varchar(45) DEFAULT NULL,
  `cuenta` varchar(50) DEFAULT NULL,
  `fechaCreacion` datetime DEFAULT NULL,
  `gasPrice` int(11) DEFAULT NULL,
  `gasLimit` int(11) DEFAULT NULL,
  PRIMARY KEY (`idBlockchain`,`idUsuario`),
  KEY `fk_Cuenta_blockchain1_idx` (`idBlockchain`),
  KEY `fk_Cuenta_Usuario1_idx` (`idUsuario`),
  CONSTRAINT `fk_Cuenta_Usuario1` FOREIGN KEY (`idUsuario`) REFERENCES `Usuario` (`idUsuario`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Cuenta_blockchain1` FOREIGN KEY (`idBlockchain`) REFERENCES `Blockchain` (`idBlockchain`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Sistema`
--

DROP TABLE IF EXISTS `Sistema`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Sistema` (
  `idParametro` int(11) NOT NULL,
  `descripcion` varchar(45) DEFAULT NULL,
  `valor` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idParametro`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Token`
--

DROP TABLE IF EXISTS `Token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Token` (
  `contractAddress` varchar(255) NOT NULL,
  `idBlockchain` int(11) NOT NULL,
  `idUsuario` char(120) NOT NULL,
  `decimals` int(11) NOT NULL,
  `fechaCreacion` datetime NOT NULL,
  `tokenName` varchar(255) NOT NULL,
  `tokenSymbol` varchar(255) NOT NULL,
  PRIMARY KEY (`contractAddress`,`idBlockchain`,`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Usuario`
--

DROP TABLE IF EXISTS `Usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Usuario` (
  `idUsuario` varchar(120) NOT NULL,
  `telefono` varchar(20) CHARACTER SET big5 NOT NULL,
  `nombre` varchar(150) DEFAULT NULL,
  `estado` char(1) DEFAULT NULL,
  `password` varchar(512) DEFAULT NULL,
  `fechaCreacion` datetime DEFAULT NULL,
  `fechaUltimoAcceso` datetime DEFAULT NULL,
  `alias` varchar(45) DEFAULT NULL,
  `codPais` varchar(7) DEFAULT NULL,
  `imagen` blob,
  `numIntentos` int(11) DEFAULT '0',
  PRIMARY KEY (`idUsuario`),
  UNIQUE KEY `idUsuario_UNIQUE` (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-02-12 18:07:06
