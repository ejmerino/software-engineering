-- =============================================
-- Seleccionar la base de datos
-- Se usa exactamente el mismo modelo de DDL
-- =============================================

USE Eurekabank;
GO

-- =============================================
-- Vaciar las tablas antes de la inserción
-- =============================================

DELETE FROM Movimientos;
DELETE FROM Cuentas;
DELETE FROM Clientes;
GO

-- =============================================
-- Insertar Clientes
-- =============================================

-- Cliente 1
INSERT INTO Clientes (CLI_CEDULA, CLI_NOMBRE, CLI_APELLIDO, CLI_CORREO, CLI_TELEFONO)
VALUES ('1726786823', 'Paul', 'Sanchez', 'pasanchez13@espe.edu.ec', '0963208402');

-- Cliente 2
INSERT INTO Clientes (CLI_CEDULA, CLI_NOMBRE, CLI_APELLIDO, CLI_CORREO, CLI_TELEFONO)
VALUES ('1723456789', 'Juan', 'Pérez', 'juan.perez@example.com', '0987654321');

-- Cliente 3
INSERT INTO Clientes (CLI_CEDULA, CLI_NOMBRE, CLI_APELLIDO, CLI_CORREO, CLI_TELEFONO)
VALUES ('1734567890', 'Laura', 'Martínez', 'laura.martinez@example.com', '0976543210');

-- Cliente 4
INSERT INTO Clientes (CLI_CEDULA, CLI_NOMBRE, CLI_APELLIDO, CLI_CORREO, CLI_TELEFONO)
VALUES ('1745678901', 'Carlos', 'Hernández', 'carlos.hernandez@example.com', '0965432109');

-- Cliente 5
INSERT INTO Clientes (CLI_CEDULA, CLI_NOMBRE, CLI_APELLIDO, CLI_CORREO, CLI_TELEFONO)
VALUES ('1756789012', 'Maria', 'Lopez', 'maria.lopez@example.com', '0954321098');
GO

-- =============================================
-- Insertar Cuentas
-- =============================================

-- Cuentas del Cliente 1
INSERT INTO Cuentas (CLI_ID, CU_NUMERO, CU_TIPO, CU_SALDO)
VALUES (1, '0000200001', 'AHORROS', 500.00);  -- Cuenta de Ahorros
INSERT INTO Cuentas (CLI_ID, CU_NUMERO, CU_TIPO, CU_SALDO)
VALUES (1, '0000200002', 'CORRIENTE', 1000.00); -- Cuenta Corriente

-- Cuentas del Cliente 2
INSERT INTO Cuentas (CLI_ID, CU_NUMERO, CU_TIPO, CU_SALDO)
VALUES (2, '0000200003', 'INVERSIÓN', 2000.00); -- Cuenta de Inversión

-- Cuentas del Cliente 3
INSERT INTO Cuentas (CLI_ID, CU_NUMERO, CU_TIPO, CU_SALDO)
VALUES (3, '0000200004', 'AHORROS', 1500.00);  -- Cuenta de Ahorros
INSERT INTO Cuentas (CLI_ID, CU_NUMERO, CU_TIPO, CU_SALDO)
VALUES (3, '0000200005', 'CORRIENTE', 2500.00); -- Cuenta Corriente

-- Cuentas del Cliente 4
INSERT INTO Cuentas (CLI_ID, CU_NUMERO, CU_TIPO, CU_SALDO)
VALUES (4, '0000200006', 'INVERSIÓN', 3000.00); -- Cuenta de Inversión

-- Cuentas del Cliente 5
INSERT INTO Cuentas (CLI_ID, CU_NUMERO, CU_TIPO, CU_SALDO)
VALUES (5, '0000200007', 'AHORROS', 700.00);   -- Cuenta de Ahorros
INSERT INTO Cuentas (CLI_ID, CU_NUMERO, CU_TIPO, CU_SALDO)
VALUES (5, '0000200008', 'CORRIENTE', 1200.00); -- Cuenta Corriente
GO

-- =============================================
-- Insertar Movimientos
-- =============================================

-- Movimientos de la Cuenta 1 (Cliente 1)
INSERT INTO Movimientos (CU_ID, MOV_FECHA, MOV_TIPO, MOV_VALOR, MOV_SALDO_FINAL)
VALUES (1, '2024-07-01', 'CRÉDITO', 100.00, 600.00);
INSERT INTO Movimientos (CU_ID, MOV_FECHA, MOV_TIPO, MOV_VALOR, MOV_SALDO_FINAL)
VALUES (1, '2024-07-02', 'DÉBITO', 50.00, 550.00);
INSERT INTO Movimientos (CU_ID, MOV_FECHA, MOV_TIPO, MOV_VALOR, MOV_SALDO_FINAL)
VALUES (2, '2024-07-01', 'CRÉDITO', 200.00, 1200.00);
INSERT INTO Movimientos (CU_ID, MOV_FECHA, MOV_TIPO, MOV_VALOR, MOV_SALDO_FINAL)
VALUES (2, '2024-07-03', 'DÉBITO', 150.00, 1050.00);

-- Movimientos de la Cuenta 3 (Cliente 2)
INSERT INTO Movimientos (CU_ID, MOV_FECHA, MOV_TIPO, MOV_VALOR, MOV_SALDO_FINAL)
VALUES (3, '2024-07-01', 'CRÉDITO', 500.00, 2500.00);
INSERT INTO Movimientos (CU_ID, MOV_FECHA, MOV_TIPO, MOV_VALOR, MOV_SALDO_FINAL)
VALUES (3, '2024-07-05', 'DÉBITO', 100.00, 2400.00);

-- Movimientos de la Cuenta 4 (Cliente 3)
INSERT INTO Movimientos (CU_ID, MOV_FECHA, MOV_TIPO, MOV_VALOR, MOV_SALDO_FINAL)
VALUES (4, '2024-07-02', 'CRÉDITO', 200.00, 1700.00);
INSERT INTO Movimientos (CU_ID, MOV_FECHA, MOV_TIPO, MOV_VALOR, MOV_SALDO_FINAL)
VALUES (4, '2024-07-04', 'DÉBITO', 50.00, 1650.00);
INSERT INTO Movimientos (CU_ID, MOV_FECHA, MOV_TIPO, MOV_VALOR, MOV_SALDO_FINAL)
VALUES (5, '2024-07-01', 'CRÉDITO', 300.00, 2800.00);
INSERT INTO Movimientos (CU_ID, MOV_FECHA, MOV_TIPO, MOV_VALOR, MOV_SALDO_FINAL)
VALUES (5, '2024-07-06', 'DÉBITO', 200.00, 2600.00);

-- Movimientos de la Cuenta 6 (Cliente 4)
INSERT INTO Movimientos (CU_ID, MOV_FECHA, MOV_TIPO, MOV_VALOR, MOV_SALDO_FINAL)
VALUES (6, '2024-07-01', 'CRÉDITO', 1000.00, 4000.00);
INSERT INTO Movimientos (CU_ID, MOV_FECHA, MOV_TIPO, MOV_VALOR, MOV_SALDO_FINAL)
VALUES (6, '2024-07-07', 'DÉBITO', 500.00, 3500.00);

-- Movimientos de la Cuenta 7 (Cliente 5)
INSERT INTO Movimientos (CU_ID, MOV_FECHA, MOV_TIPO, MOV_VALOR, MOV_SALDO_FINAL)
VALUES (7, '2024-07-02', 'CRÉDITO', 300.00, 1000.00);
INSERT INTO Movimientos (CU_ID, MOV_FECHA, MOV_TIPO, MOV_VALOR, MOV_SALDO_FINAL)
VALUES (7, '2024-07-08', 'DÉBITO', 100.00, 900.00);
INSERT INTO Movimientos (CU_ID, MOV_FECHA, MOV_TIPO, MOV_VALOR, MOV_SALDO_FINAL)
VALUES (8, '2024-07-02', 'CRÉDITO', 400.00, 1600.00);
INSERT INTO Movimientos (CU_ID, MOV_FECHA, MOV_TIPO, MOV_VALOR, MOV_SALDO_FINAL)
VALUES (8, '2024-07-09', 'DÉBITO', 200.00, 1400.00);
GO


-- Insertar datos de ejemplo en la tabla Usuarios
INSERT INTO Usuarios (USU_NOMBRE, USU_APELLIDO, USU_CORREO, USU_TELEFONO, USU_CEDULA, USU_ROL,USU_CONTRASENA)
VALUES ('Paul', 'Sanchez', 'paul.sanchez@example.com', '0963208402', '1726786823', 'Administrador', 'Paulfriki55');
GO
-- Actualizar USU_ID en CLIENTES basado en la cédula
UPDATE CLIENTES
SET USU_ID = U.USU_ID
FROM CLIENTES C
INNER JOIN USUARIOS U
ON C.CLI_CEDULA = U.USU_CEDULA;
GO
