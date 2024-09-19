CREATE SCHEMA `TimeLess`;

CREATE TABLE `TimeLess`.`Agenda` (
                                     `id` int NOT NULL AUTO_INCREMENT,
                                     `fk_linea_atencion` int,
                                     PRIMARY KEY (`id`)
);
CREATE TABLE `TimeLess`.`Usuario` (
                                      `id` int NOT NULL AUTO_INCREMENT,
                                      `correo` varchar(50) NOT NULL,
                                      `clave` varchar(100) NOT NULL,
                                      `fh_creacion` datetime NOT NULL,
                                      `habilitado` bit NOT NULL,
                                      `fk_datos_personales` int ,
                                      `fk_config_usuario_general` int ,
                                      `fk_tipo_usuario` int NOT NULL,
                                      PRIMARY KEY (`id`)
);
CREATE TABLE `TimeLess`.`Turnos` (
                                     `id` int NOT NULL AUTO_INCREMENT,
                                     `uuid` varchar(36) NOT NULL,
                                     `fk_agenda` int NOT NULL,
                                     `fk_estado_turno` int NOT NULL,
                                     `fk_medio_págos` int NOT NULL,
                                     `fk_usuario` int ,
                                     `fh_reserva` datetime,
                                     `fh_inicio` datetime NOT NULL,
                                     `fh_fin` datetime NOT NULL,
                                     `lokedTime` TIME,
                                     `locked` TINYINT NOT NULL default false,
                                     PRIMARY KEY (`id`)
);


CREATE TABLE `TimeLess`.`Calendario` (
                                         `id` int NOT NULL AUTO_INCREMENT,
                                         `h_inicio` time(0) NOT NULL,
                                         `h_fin` time(0) NOT NULL,
                                         `lista_dias_laborables` varchar(50) NOT NULL,
                                         PRIMARY KEY (`id`)
);

CREATE TABLE `TimeLess`.`Ausencias` (
                                        `id` int NOT NULL AUTO_INCREMENT,
                                        `desde` datetime NOT NULL,
                                        `hasta` datetime NOT NULL,
                                        `descripcion` varchar(50) NOT NULL,
                                        `fk_calendario` int NOT NULL,
                                        PRIMARY KEY (`id`)
);





CREATE TABLE `TimeLess`.`Config_Sistema` (
                                             `id` int NOT NULL AUTO_INCREMENT,
                                             `detalle` varchar(50),
                                             `clave` varchar(50),
                                             `valor` varchar(500),
                                             PRIMARY KEY (`id`)
);

CREATE TABLE `TimeLess`.`Config_usuario_general` (
                                                     `id` int NOT NULL AUTO_INCREMENT,
                                                     `sms` bit NOT NULL,
                                                     `wpp` bit NOT NULL,
                                                     `email` bit NOT NULL,
                                                     PRIMARY KEY (`id`)
);

CREATE TABLE `TimeLess`.`Datos_Fiscales` (
                                             `id` int NOT NULL AUTO_INCREMENT,
                                             `razon_social` varchar(50),
                                             `nombre_fantasia` varchar(50),
                                             `cuit` varchar(50),
                                             `fk_direccion_empresa` int,
                                             PRIMARY KEY (`id`)
);


CREATE TABLE `TimeLess`.`Datos_Personales` (
                                               `id` int NOT NULL AUTO_INCREMENT,
                                               `fk_usuario` int,
                                               `nombre` varchar(50),
                                               `apellido` varchar(50),
                                               `fk_tipo_documento` int,
                                               `numero_documento` varchar(50),
                                               `telefono_celular` varchar(50),
                                               `f_nacimiento` varchar(10),
                                               `fk_domicilio` int,
                                               PRIMARY KEY (`id`)
);



CREATE TABLE `TimeLess`.`Domicilio` (
                                        `id` int NOT NULL AUTO_INCREMENT,
                                        `calle` varchar(50) NOT NULL,
                                        `numero` varchar(50) NOT NULL,
                                        `ciudad` varchar(50) NOT NULL,
                                        `localidad` varchar(50) NOT NULL,
                                        `provincia` varchar(50) NOT NULL,
                                        `pais` varchar(50) NOT NULL,
                                        PRIMARY KEY (`id`)
);

CREATE TABLE `TimeLess`.`Domicilio_Fiscal` (
                                               `id` int NOT NULL AUTO_INCREMENT,
                                               `calle` varchar(50),
                                               `numero` varchar(50),
                                               `ciudad` varchar(50),
                                               `localidad` varchar(50),
                                               `provincia` varchar(50),
                                               `pais` varchar(50),
                                               PRIMARY KEY (`id`)
);

CREATE TABLE `TimeLess`.`Empresa` (
                                      `id` int NOT NULL AUTO_INCREMENT,
                                      `fh_creacion` datetime NOT NULL,
                                      `fk_usuario_propietario` int NOT NULL,
                                      `fk_datos_empresa` int NOT NULL,
                                      `fk_membresia` int NOT NULL,
                                      `fk_calendario` int NOT NULL,
                                      PRIMARY KEY (`id`)
);




CREATE TABLE `TimeLess`.`Linea_Atencion` (
                                             `id` int NOT NULL AUTO_INCREMENT,
                                             `fk_empresa` int NOT NULL,
                                             `descripccion` varchar(50) NOT NULL,
                                             `duracion_turno` int NOT NULL,
                                             `habilitado` bit NOT NULL,
                                             `fk_rubro`  int not null,
                                             PRIMARY KEY (`id`)
);






CREATE TABLE `TimeLess`.`Notificacion` (
                                           `id` int NOT NULL AUTO_INCREMENT,
                                           `origen` varchar(50) NOT NULL,
                                           `destino` varchar(50) NOT NULL,
                                           `template_id` int NOT NULL,
                                           `fh_envio` datetime NOT NULL,
                                           `fh_creacion` datetime NOT NULL,
                                           `fk_estado` int NOT NULL,
                                           `fk_tipo_notificacion` int NOT NULL,
                                           PRIMARY KEY (`id`)
);

CREATE TABLE `TimeLess`.`Parametrizacion_Empresa` (
                                                      `id` int NOT NULL AUTO_INCREMENT,
                                                      `fk_empresa` int NOT NULL,
                                                      `detalle` varchar(50) NOT NULL,
                                                      `clave` varchar(50) NOT NULL,
                                                      `valor` varchar(500) NOT NULL,
                                                      PRIMARY KEY (`id`)
);


CREATE TABLE `TimeLess`.`Rubro` (
                                    `id` int NOT NULL AUTO_INCREMENT,
                                    `detalle` varchar(50),
                                    PRIMARY KEY (`id`)
);

INSERT INTO `TimeLess`.`Rubro`(`detalle`)VALUES('BARBERIA');
INSERT INTO `TimeLess`.`Rubro`(`detalle`)VALUES('MANICURA');
INSERT INTO `TimeLess`.`Rubro`(`detalle`)VALUES('MASAJES');



CREATE TABLE `TimeLess`.`Tipo_Documento` (
                                             `id` int NOT NULL AUTO_INCREMENT,
                                             `detalle` varchar(50),
                                             PRIMARY KEY (`id`)
);


INSERT INTO `TimeLess`.`Tipo_Documento`(`detalle`)VALUES('DNI');
INSERT INTO `TimeLess`.`Tipo_Documento`(`detalle`)VALUES('CUIL');
INSERT INTO `TimeLess`.`Tipo_Documento`(`detalle`)VALUES('LE');
INSERT INTO `TimeLess`.`Tipo_Documento`(`detalle`)VALUES('LC');


CREATE TABLE `TimeLess`.`Tipo_Notificacion` (
                                                `id` int NOT NULL AUTO_INCREMENT,
                                                `detalle` varchar(50),
                                                PRIMARY KEY (`id`)
);
INSERT INTO `TimeLess`.`Tipo_Notificacion`(`detalle`)VALUES('CONFIRMACION');
INSERT INTO `TimeLess`.`Tipo_Notificacion`(`detalle`)VALUES('CANCELACION');
INSERT INTO `TimeLess`.`Tipo_Notificacion`(`detalle`)VALUES('REPROGRAMACION');

CREATE TABLE `TimeLess`.`Tipo_Usuario` (
                                           `id` int NOT NULL AUTO_INCREMENT,
                                           `detalle` varchar(50) NOT NULL,
                                           PRIMARY KEY (`id`)
);
INSERT INTO `TimeLess`.`Tipo_Usuario`(`detalle`)VALUES('GENERAL');
INSERT INTO `TimeLess`.`Tipo_Usuario`(`detalle`)VALUES('ADMINISTRADOR');
INSERT INTO `TimeLess`.`Tipo_Usuario`(`detalle`)VALUES('EMPRESA');



CREATE TABLE `TimeLess`.`Medios_Pagos` (
                                           `id` int NOT NULL AUTO_INCREMENT,
                                           `detalle` varchar(50),
                                           `habilitado` bit,
                                           PRIMARY KEY (`id`)
);
INSERT INTO `TimeLess`.`Medios_Pagos`(detalle,habilitado )VALUES('Acordar con el prestador',1);

CREATE TABLE `TimeLess`.`Membresia` (
                                        `id` int NOT NULL AUTO_INCREMENT,
                                        `detalle` varchar(50) NOT NULL,
                                        PRIMARY KEY (`id`)
);
INSERT INTO `TimeLess`.`Membresia`(`detalle`)VALUES('FREE');

CREATE TABLE `TimeLess`.`Estado_Notificacion` (
                                                  `id` int NOT NULL AUTO_INCREMENT,
                                                  `detalle` varchar(50) NOT NULL,
                                                  PRIMARY KEY (`id`)
);

INSERT INTO `TimeLess`.`Estado_Notificacion`(`detalle`)VALUES('ENVIADA');
INSERT INTO `TimeLess`.`Estado_Notificacion`(`detalle`)VALUES('ERROR');
INSERT INTO `TimeLess`.`Estado_Notificacion`(`detalle`)VALUES('PENDIENTE');

CREATE TABLE `TimeLess`.`Estado_Turno` (
                                           `id` int NOT NULL AUTO_INCREMENT,
                                           `detalle` varchar(50) NOT NULL,
                                           PRIMARY KEY (`id`)
);

INSERT INTO `TimeLess`.`Estado_Turno`(`detalle`)VALUES('OTORGADO');
INSERT INTO `TimeLess`.`Estado_Turno`(`detalle`)VALUES('GENERADO');
INSERT INTO `TimeLess`.`Estado_Turno`(`detalle`)VALUES('PRE_SELECCIONADO');
INSERT INTO `TimeLess`.`Estado_Turno`(`detalle`)VALUES('CANCELADO');
INSERT INTO `TimeLess`.`Estado_Turno`(`detalle`)VALUES('ELIMINADO');

ALTER TABLE `TimeLess`.`Linea_Atencion` ADD FOREIGN KEY (`fk_rubro`) REFERENCES `TimeLess`.`Rubro` (`id`);
ALTER TABLE `TimeLess`.`Ausencias` ADD FOREIGN KEY (`fk_calendario`) REFERENCES `TimeLess`.`Calendario` (`id`);
ALTER TABLE `TimeLess`.`Usuario` ADD FOREIGN KEY (`fk_tipo_usuario`) REFERENCES `TimeLess`.`Tipo_Usuario` (`id`);
ALTER TABLE `TimeLess`.`Usuario` ADD FOREIGN KEY (`fk_datos_personales`) REFERENCES `TimeLess`.`Datos_Personales` (`id`);
ALTER TABLE `TimeLess`.`Usuario` ADD FOREIGN KEY (`fk_config_usuario_general`) REFERENCES `TimeLess`.`Config_usuario_general` (`id`);
ALTER TABLE `TimeLess`.`Turnos` ADD FOREIGN KEY (`fk_agenda`) REFERENCES `TimeLess`.`Agenda` (`id`);
ALTER TABLE `TimeLess`.`Turnos` ADD FOREIGN KEY (`fk_estado_turno`) REFERENCES `TimeLess`.`Estado_Turno` (`id`);
ALTER TABLE `TimeLess`.`Turnos` ADD FOREIGN KEY (`fk_medio_págos`) REFERENCES `TimeLess`.`Medios_Pagos` (`id`);
ALTER TABLE `TimeLess`.`Turnos` ADD FOREIGN KEY (`fk_usuario`) REFERENCES `TimeLess`.`Usuario` (`id`);
ALTER TABLE `TimeLess`.`Notificacion` ADD FOREIGN KEY (`fk_estado`) REFERENCES `TimeLess`.`Estado_Notificacion` (`id`);
ALTER TABLE `TimeLess`.`Notificacion` ADD FOREIGN KEY (`fk_tipo_notificacion`) REFERENCES `TimeLess`.`Tipo_Notificacion` (`id`);
ALTER TABLE `TimeLess`.`Linea_Atencion` ADD FOREIGN KEY (`fk_empresa`) REFERENCES `TimeLess`.`Empresa` (`id`);
ALTER TABLE `TimeLess`.`Empresa` ADD FOREIGN KEY (`fk_usuario_propietario`) REFERENCES `TimeLess`.`Usuario` (`id`);
ALTER TABLE `TimeLess`.`Empresa` ADD FOREIGN KEY (`fk_datos_empresa`) REFERENCES `TimeLess`.`Datos_Fiscales` (`id`);
ALTER TABLE `TimeLess`.`Empresa` ADD FOREIGN KEY (`fk_membresia`) REFERENCES `TimeLess`.`Membresia` (`id`);
ALTER TABLE `TimeLess`.`Empresa` ADD FOREIGN KEY (`fk_calendario`) REFERENCES `TimeLess`.`Calendario` (`id`);
ALTER TABLE `TimeLess`.`Datos_Personales` ADD FOREIGN KEY (`fk_usuario`) REFERENCES `TimeLess`.`Usuario` (`id`);
ALTER TABLE `TimeLess`.`Datos_Personales` ADD FOREIGN KEY (`fk_tipo_documento`) REFERENCES `TimeLess`.`Tipo_Documento` (`id`);
ALTER TABLE `TimeLess`.`Datos_Personales` ADD FOREIGN KEY (`fk_domicilio`) REFERENCES `TimeLess`.`Domicilio` (`id`);
ALTER TABLE `TimeLess`.`Datos_Fiscales` ADD FOREIGN KEY (`fk_direccion_empresa`) REFERENCES `TimeLess`.`Domicilio_Fiscal` (`id`);
ALTER TABLE `TimeLess`.`Agenda` ADD FOREIGN KEY (`fk_linea_atencion`) REFERENCES `TimeLess`.`Linea_Atencion` (`id`);
ALTER TABLE `TimeLess`.`Parametrizacion_Empresa` ADD FOREIGN KEY (`fk_empresa`) REFERENCES `TimeLess`.`Empresa` (`id`);


