use db_comercializadora;

-- 1. Clientes de la Tienda (Puedes tener clientes que no estén en el banco y viceversa)


INSERT INTO `Cliente` (`cedula`, `nombres`, `apellidos`, `direccion`, `telefono`, `email`) VALUES
('1000000001', 'Juan Pérez', '(Datos Tienda)', 'Av. Siempre Viva 123', '0991234567', 'juan@tienda.com'),
('1000000003', 'Carlos Sánchez', '(Datos Tienda)', 'Calle Falsa 456', '0987654321', 'carlos@tienda.com'),
('1150000001', 'Patricia Vera', '(Datos Tienda)', 'El Condado', '0971234567', 'paty@tienda.com');

-- 2. Electrodomesticos (Catálogo para Puntos 8 y 9)
INSERT INTO `Electrodomestico` (`nombre`, `descripcion`, `precio_venta`) VALUES
('Refrigeradora 300L', 'Refrigeradora No Frost, color cromo', 650.00),
('Smart TV 55" 4K', 'Televisor LED 4K UHD con Android TV', 820.00),
('Lavadora 18kg', 'Lavadora automática carga superior', 550.00),
('Microondas 1.1cu', 'Microondas digital con grill', 120.00),
('Licuadora Pro', 'Licuadora de 8 velocidades vaso de vidrio', 85.00);