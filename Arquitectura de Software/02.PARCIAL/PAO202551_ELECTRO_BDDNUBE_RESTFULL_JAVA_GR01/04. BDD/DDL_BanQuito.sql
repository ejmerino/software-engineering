CREATE DATABASE db_banquito;

USE db_banquito;

CREATE TABLE `CLIENTE` (
  `CEDULA` varchar(10) PRIMARY KEY,
  `NOMBRE` varchar(100) NOT NULL,
  `FECHA_NACIMIENTO` date NOT NULL,
  `ESTADO_CIVIL` varchar(1) NOT NULL COMMENT 'Ej: S=Soltero, C=Casado'
);

CREATE TABLE `CUENTA` (
  `NUM_CUENTA` varchar(8) PRIMARY KEY,
  `CEDULA` varchar(10) NOT NULL,
  `SALDO` decimal(10,2) NOT NULL
);

CREATE TABLE `MOVIMIENTO` (
  `COD_MOVIMIENTO` int PRIMARY KEY AUTO_INCREMENT,
  `NUM_CUENTA` varchar(8) NOT NULL,
  `TIPO` varchar(3) NOT NULL COMMENT 'DEP=Depósito, RET=Retiro',
  `VALOR` decimal(10,2) NOT NULL,
  `FECHA` date NOT NULL
);

CREATE TABLE `Credito` (
  `id_credito` int PRIMARY KEY AUTO_INCREMENT,
  `cedula_cliente` varchar(10) NOT NULL,
  `monto_prestamo` decimal(10,2) NOT NULL,
  `tasa_interes_anual` decimal(5,4) NOT NULL COMMENT 'Ej: 0.16 para 16%',
  `numero_cuotas` int NOT NULL,
  `valor_cuota_fija` decimal(10,2) NOT NULL,
  `fecha_aprobacion` date NOT NULL,
  `estado` varchar(10) NOT NULL COMMENT 'Activo, Pagado'
);

CREATE TABLE `Amortizacion_Detalle` (
  `id_amortizacion` int PRIMARY KEY AUTO_INCREMENT,
  `id_credito` int NOT NULL,
  `numero_cuota` int NOT NULL,
  `fecha_pago_programada` date NOT NULL,
  `valor_cuota` decimal(10,2) NOT NULL,
  `interes_pagado` decimal(10,2) NOT NULL,
  `capital_pagado` decimal(10,2) NOT NULL,
  `saldo_capital` decimal(10,2) NOT NULL
);

ALTER TABLE `CUENTA` ADD FOREIGN KEY (`CEDULA`) REFERENCES `CLIENTE` (`CEDULA`);

ALTER TABLE `MOVIMIENTO` ADD FOREIGN KEY (`NUM_CUENTA`) REFERENCES `CUENTA` (`NUM_CUENTA`);

ALTER TABLE `Credito` ADD FOREIGN KEY (`cedula_cliente`) REFERENCES `CLIENTE` (`CEDULA`);

ALTER TABLE `Amortizacion_Detalle` ADD FOREIGN KEY (`id_credito`) REFERENCES `Credito` (`id_credito`);
