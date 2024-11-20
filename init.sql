-- MariaDB dump 10.19  Distrib 10.4.21-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: TimeLess
-- ------------------------------------------------------
-- Server version	11.5.2-MariaDB-ubu2404

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Agenda`
--

DROP TABLE IF EXISTS `Agenda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Agenda` (
                          `id` int(11) NOT NULL AUTO_INCREMENT,
                          `fk_linea_atencion` int(11) DEFAULT NULL,
                          PRIMARY KEY (`id`),
                          KEY `fk_linea_atencion` (`fk_linea_atencion`),
                          CONSTRAINT `agenda_ibfk_1` FOREIGN KEY (`fk_linea_atencion`) REFERENCES `Linea_Atencion` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


DROP TABLE IF EXISTS `Auditoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Auditoria` (
                             `id` int(11) NOT NULL AUTO_INCREMENT,
                             `usuario` int(11) NOT NULL,
                             `usuarioEmpresa` int(11) NOT NULL,
                             `fh_turno` datetime DEFAULT NULL,
                             `fh_event` datetime NOT NULL DEFAULT current_timestamp(),
                             `canceledBy` varchar(45) DEFAULT NULL,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

DROP TABLE IF EXISTS `Ausencias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Ausencias` (
                             `id` int(11) NOT NULL AUTO_INCREMENT,
                             `desde` datetime NOT NULL,
                             `hasta` datetime NOT NULL,
                             `descripcion` varchar(50) NOT NULL,
                             `fk_calendario` int(11) NOT NULL,
                             PRIMARY KEY (`id`),
                             KEY `fk_calendario` (`fk_calendario`),
                             CONSTRAINT `ausencias_ibfk_1` FOREIGN KEY (`fk_calendario`) REFERENCES `Calendario` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


DROP TABLE IF EXISTS `Calendario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Calendario` (
                              `id` int(11) NOT NULL AUTO_INCREMENT,
                              `h_inicio` time NOT NULL,
                              `h_fin` time NOT NULL,
                              `lista_dias_laborables` varchar(50) NOT NULL,
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


DROP TABLE IF EXISTS `Config_Sistema`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Config_Sistema` (
                                  `id` int(11) NOT NULL AUTO_INCREMENT,
                                  `detalle` varchar(50) DEFAULT NULL,
                                  `clave` varchar(50) DEFAULT NULL,
                                  `valor` varchar(500) DEFAULT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Config_Sistema`
--


DROP TABLE IF EXISTS `Config_usuario_general`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Config_usuario_general` (
                                          `id` int(11) NOT NULL AUTO_INCREMENT,
                                          `sms` bit(1) NOT NULL,
                                          `wpp` bit(1) NOT NULL,
                                          `email` bit(1) NOT NULL,
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `Datos_Fiscales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Datos_Fiscales` (
                                  `id` int(11) NOT NULL AUTO_INCREMENT,
                                  `razon_social` varchar(50) DEFAULT NULL,
                                  `nombre_fantasia` varchar(50) DEFAULT NULL,
                                  `cuit` varchar(50) DEFAULT NULL,
                                  `fk_direccion_empresa` int(11) DEFAULT NULL,
                                  PRIMARY KEY (`id`),
                                  KEY `fk_direccion_empresa` (`fk_direccion_empresa`),
                                  CONSTRAINT `datos_fiscales_ibfk_1` FOREIGN KEY (`fk_direccion_empresa`) REFERENCES `Domicilio_Fiscal` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Datos_Fiscales`
--


--
-- Table structure for table `Datos_Personales`
--

DROP TABLE IF EXISTS `Datos_Personales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Datos_Personales` (
                                    `id` int(11) NOT NULL AUTO_INCREMENT,
                                    `fk_usuario` int(11) DEFAULT NULL,
                                    `nombre` varchar(50) DEFAULT NULL,
                                    `apellido` varchar(50) DEFAULT NULL,
                                    `fk_tipo_documento` int(11) DEFAULT NULL,
                                    `numero_documento` varchar(50) DEFAULT NULL,
                                    `telefono_celular` varchar(50) DEFAULT NULL,
                                    `f_nacimiento` varchar(10) DEFAULT NULL,
                                    `fk_domicilio` int(11) DEFAULT NULL,
                                    PRIMARY KEY (`id`),
                                    KEY `fk_usuario` (`fk_usuario`),
                                    KEY `fk_tipo_documento` (`fk_tipo_documento`),
                                    KEY `fk_domicilio` (`fk_domicilio`),
                                    CONSTRAINT `datos_personales_ibfk_1` FOREIGN KEY (`fk_usuario`) REFERENCES `Usuario` (`id`),
                                    CONSTRAINT `datos_personales_ibfk_2` FOREIGN KEY (`fk_tipo_documento`) REFERENCES `Tipo_Documento` (`id`),
                                    CONSTRAINT `datos_personales_ibfk_3` FOREIGN KEY (`fk_domicilio`) REFERENCES `Domicilio` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


DROP TABLE IF EXISTS `Domicilio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Domicilio` (
                             `id` int(11) NOT NULL AUTO_INCREMENT,
                             `calle` varchar(50) NOT NULL,
                             `numero` varchar(50) NOT NULL,
                             `ciudad` varchar(50) NOT NULL,
                             `localidad` varchar(50) NOT NULL,
                             `provincia` varchar(50) NOT NULL,
                             `pais` varchar(50) NOT NULL,
                             `latitud` decimal(9,6) DEFAULT NULL,
                             `longitud` decimal(9,6) DEFAULT NULL,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


DROP TABLE IF EXISTS `Domicilio_Fiscal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Domicilio_Fiscal` (
                                    `id` int(11) NOT NULL AUTO_INCREMENT,
                                    `calle` varchar(50) DEFAULT NULL,
                                    `numero` varchar(50) DEFAULT NULL,
                                    `ciudad` varchar(50) DEFAULT NULL,
                                    `localidad` varchar(50) DEFAULT NULL,
                                    `provincia` varchar(50) DEFAULT NULL,
                                    `pais` varchar(50) DEFAULT NULL,
                                    `latitud` decimal(9,6) DEFAULT NULL,
                                    `longitud` decimal(9,6) DEFAULT NULL,
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


DROP TABLE IF EXISTS `Empresa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Empresa` (
                           `id` int(11) NOT NULL AUTO_INCREMENT,
                           `fh_creacion` datetime NOT NULL,
                           `fk_usuario_propietario` int(11) NOT NULL,
                           `fk_datos_empresa` int(11) NOT NULL,
                           `fk_membresia` int(11) NOT NULL,
                           `fk_calendario` int(11) NOT NULL,
                           `fk_rubro` int(11) NOT NULL,
                           PRIMARY KEY (`id`),
                           KEY `fk_usuario_propietario` (`fk_usuario_propietario`),
                           KEY `fk_datos_empresa` (`fk_datos_empresa`),
                           KEY `fk_membresia` (`fk_membresia`),
                           KEY `fk_calendario` (`fk_calendario`),
                           KEY `fk_rubro` (`fk_rubro`),
                           CONSTRAINT `empresa_ibfk_1` FOREIGN KEY (`fk_usuario_propietario`) REFERENCES `Usuario` (`id`),
                           CONSTRAINT `empresa_ibfk_2` FOREIGN KEY (`fk_datos_empresa`) REFERENCES `Datos_Fiscales` (`id`),
                           CONSTRAINT `empresa_ibfk_3` FOREIGN KEY (`fk_membresia`) REFERENCES `Membresia` (`id`),
                           CONSTRAINT `empresa_ibfk_4` FOREIGN KEY (`fk_calendario`) REFERENCES `Calendario` (`id`),
                           CONSTRAINT `empresa_ibfk_5` FOREIGN KEY (`fk_rubro`) REFERENCES `Rubro` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;





DROP TABLE IF EXISTS `Estado_Turno`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Estado_Turno` (
                                `id` int(11) NOT NULL AUTO_INCREMENT,
                                `detalle` varchar(50) NOT NULL,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Estado_Turno`
--


INSERT INTO `Estado_Turno` VALUES ('1','OTORGADO'),('2','GENERADO'),('3','PRE_SELECCIONADO'),('4','CANCELADO'),('5','ELIMINADO');


--
-- Table structure for table `Linea_Atencion`
--

DROP TABLE IF EXISTS `Linea_Atencion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Linea_Atencion` (
                                  `id` int(11) NOT NULL AUTO_INCREMENT,
                                  `fk_empresa` int(11) NOT NULL,
                                  `descripccion` varchar(50) NOT NULL,
                                  `duracion_turno` int(11) NOT NULL,
                                  `habilitado` bit(1) NOT NULL,
                                  PRIMARY KEY (`id`),
                                  KEY `fk_empresa` (`fk_empresa`),
                                  CONSTRAINT `linea_atencion_ibfk_2` FOREIGN KEY (`fk_empresa`) REFERENCES `Empresa` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


DROP TABLE IF EXISTS `Medios_Pagos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Medios_Pagos` (
                                `id` int(11) NOT NULL AUTO_INCREMENT,
                                `detalle` varchar(50) DEFAULT NULL,
                                `habilitado` bit(1) DEFAULT NULL,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Medios_Pagos`
--


INSERT INTO `Medios_Pagos` VALUES ('1','Acordar con el prestador',true);


--
-- Table structure for table `Membresia`
--

DROP TABLE IF EXISTS `Membresia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Membresia` (
                             `id` int(11) NOT NULL AUTO_INCREMENT,
                             `detalle` varchar(50) NOT NULL,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;





DROP TABLE IF EXISTS `Parametrizacion_Empresa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Parametrizacion_Empresa` (
                                           `id` int(11) NOT NULL AUTO_INCREMENT,
                                           `fk_empresa` int(11) NOT NULL,
                                           `detalle` varchar(50) NOT NULL,
                                           `clave` varchar(50) NOT NULL,
                                           `valor` varchar(500) NOT NULL,
                                           PRIMARY KEY (`id`),
                                           KEY `fk_empresa` (`fk_empresa`),
                                           CONSTRAINT `parametrizacion_empresa_ibfk_1` FOREIGN KEY (`fk_empresa`) REFERENCES `Empresa` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;



DROP TABLE IF EXISTS `Rubro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Rubro` (
                         `id` int(11) NOT NULL AUTO_INCREMENT,
                         `detalle` varchar(50) DEFAULT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;




DROP TABLE IF EXISTS `Tipo_Documento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Tipo_Documento` (
                                  `id` int(11) NOT NULL AUTO_INCREMENT,
                                  `detalle` varchar(50) DEFAULT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;



INSERT INTO `Tipo_Documento` VALUES ('1','DNI'),('2','CUIL'),('3','LE'),('4','LC');






DROP TABLE IF EXISTS `Tipo_Usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Tipo_Usuario` (
                                `id` int(11) NOT NULL AUTO_INCREMENT,
                                `detalle` varchar(50) NOT NULL,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;




INSERT INTO `Tipo_Usuario` VALUES ('1','GENERAL'),('2','ADMINISTRADOR'),('3','EMPRESA');



DROP TABLE IF EXISTS `Turnos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Turnos` (
                          `id` int(11) NOT NULL AUTO_INCREMENT,
                          `uuid` varchar(36) NOT NULL,
                          `fk_agenda` int(11) NOT NULL,
                          `fk_estado_turno` int(11) NOT NULL,
                          `fk_medio_pagos` int(11) NOT NULL,
                          `fk_usuario` int(11) DEFAULT NULL,
                          `fh_reserva` datetime DEFAULT NULL,
                          `fh_inicio` datetime NOT NULL,
                          `fh_fin` datetime NOT NULL,
                          `lokedTime` time DEFAULT NULL,
                          `locked` tinyint(4) NOT NULL DEFAULT 0,
                          PRIMARY KEY (`id`),
                          KEY `fk_agenda` (`fk_agenda`),
                          KEY `fk_estado_turno` (`fk_estado_turno`),
                          KEY `fk_medio_pagos` (`fk_medio_pagos`),
                          KEY `fk_usuario` (`fk_usuario`),
                          CONSTRAINT `turnos_ibfk_1` FOREIGN KEY (`fk_agenda`) REFERENCES `Agenda` (`id`),
                          CONSTRAINT `turnos_ibfk_2` FOREIGN KEY (`fk_estado_turno`) REFERENCES `Estado_Turno` (`id`),
                          CONSTRAINT `turnos_ibfk_3` FOREIGN KEY (`fk_medio_pagos`) REFERENCES `Medios_Pagos` (`id`),
                          CONSTRAINT `turnos_ibfk_4` FOREIGN KEY (`fk_usuario`) REFERENCES `Usuario` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


DROP TABLE IF EXISTS `Usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Usuario` (
                           `id` int(11) NOT NULL AUTO_INCREMENT,
                           `correo` varchar(50) NOT NULL,
                           `clave` varchar(100) NOT NULL,
                           `fh_creacion` datetime NOT NULL,
                           `habilitado` bit(1) NOT NULL,
                           `fk_datos_personales` int(11) DEFAULT NULL,
                           `fk_config_usuario_general` int(11) DEFAULT NULL,
                           `fk_tipo_usuario` int(11) NOT NULL,
                           PRIMARY KEY (`id`),
                           KEY `fk_tipo_usuario` (`fk_tipo_usuario`),
                           KEY `fk_datos_personales` (`fk_datos_personales`),
                           KEY `fk_config_usuario_general` (`fk_config_usuario_general`),
                           CONSTRAINT `usuario_ibfk_1` FOREIGN KEY (`fk_tipo_usuario`) REFERENCES `Tipo_Usuario` (`id`),
                           CONSTRAINT `usuario_ibfk_2` FOREIGN KEY (`fk_datos_personales`) REFERENCES `Datos_Personales` (`id`),
                           CONSTRAINT `usuario_ibfk_3` FOREIGN KEY (`fk_config_usuario_general`) REFERENCES `Config_usuario_general` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;







INSERT INTO `TimeLess`.`Rubro`(`id`,`detalle`)VALUES(1,'Barberia');
INSERT INTO `TimeLess`.`Rubro`(`id`,`detalle`)VALUES(2,'Peluqueria canina');
INSERT INTO `TimeLess`.`Rubro`(`id`,`detalle`)VALUES(3,'Peluqueria');
INSERT INTO `TimeLess`.`Rubro`(`id`,`detalle`)VALUES(4,'Manicura');
INSERT INTO `TimeLess`.`Rubro`(`id`,`detalle`)VALUES(5,'Futbol');
INSERT INTO `TimeLess`.`Rubro`(`id`,`detalle`)VALUES(6,'Taller Automotor');
INSERT INTO `TimeLess`.`Rubro`(`id`,`detalle`)VALUES(7,'Masajista');
INSERT INTO `TimeLess`.`Rubro`(`id`,`detalle`)VALUES(8,'Padel');
INSERT INTO `TimeLess`.`Rubro`(`id`,`detalle`)VALUES(9,'Psicologia');
INSERT INTO `TimeLess`.`Rubro`(`id`,`detalle`)VALUES(10,'Tarot');
INSERT INTO `TimeLess`.`Rubro`(`id`,`detalle`)VALUES(11,'Taller ceramica');
INSERT INTO `TimeLess`.`Rubro`(`id`,`detalle`)VALUES(12,'Taller dibujo');
INSERT INTO `TimeLess`.`Rubro`(`id`,`detalle`)VALUES(13,'Apoyo escolar');
INSERT INTO `TimeLess`.`Rubro`(`id`,`detalle`)VALUES(14,'Lavadero de autos');
INSERT INTO `TimeLess`.`Rubro`(`id`,`detalle`)VALUES(15,'Sala de ensayo');
INSERT INTO `TimeLess`.`Rubro`(`id`,`detalle`)VALUES(16,'Otros');

INSERT INTO `TimeLess`.`Membresia`(`id`,`detalle`)VALUES(1,'FREE');



INSERT INTO `TimeLess`.`Config_Sistema`(`id`,`detalle`,`clave`,`valor`)VALUES(1,'Permitir Alta de empresas','altaEmpresa','false');
