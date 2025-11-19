CREATE TABLE `Electrodomestico` (
  `id_electrodomestico` int PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `descripcion` varchar(255),
  `precio_venta` decimal(10,2) NOT NULL
);

CREATE TABLE `Cliente` (
  `cedula` varchar(10) PRIMARY KEY,
  `nombres` varchar(100) NOT NULL,
  `apellidos` varchar(100) NOT NULL,
  `direccion` varchar(255),
  `telefono` varchar(20),
  `email` varchar(100)
);

CREATE TABLE `Factura` (
  `id_factura` int PRIMARY KEY AUTO_INCREMENT,
  `cedula_cliente` varchar(10) NOT NULL,
  `fecha` date NOT NULL,
  `forma_pago` varchar(20) NOT NULL COMMENT 'Efectivo o Crédito',
  `subtotal` decimal(10,2) NOT NULL,
  `descuento` decimal(10,2) DEFAULT 0,
  `total` decimal(10,2) NOT NULL,
  `id_credito_banco` int COMMENT 'ID del crédito en BanQuito, si aplica'
);

CREATE TABLE `Factura_Detalle` (
  `id_detalle` int PRIMARY KEY AUTO_INCREMENT,
  `id_factura` int NOT NULL,
  `id_electrodomestico` int NOT NULL,
  `cantidad` int NOT NULL,
  `precio_unitario_venta` decimal(10,2) NOT NULL,
  `subtotal_linea` decimal(10,2) NOT NULL
);

ALTER TABLE `Factura` ADD FOREIGN KEY (`cedula_cliente`) REFERENCES `Cliente` (`cedula`);

ALTER TABLE `Factura_Detalle` ADD FOREIGN KEY (`id_factura`) REFERENCES `Factura` (`id_factura`);

ALTER TABLE `Factura_Detalle` ADD FOREIGN KEY (`id_electrodomestico`) REFERENCES `Electrodomestico` (`id_electrodomestico`);
