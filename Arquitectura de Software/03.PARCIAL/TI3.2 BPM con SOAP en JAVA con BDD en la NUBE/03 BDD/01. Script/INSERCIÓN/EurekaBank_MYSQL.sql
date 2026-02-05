-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--

-- Volcado de datos para la tabla `cliente`
--

INSERT INTO `cliente` (`CODIGO`, `NOMBRES`, `APELLIDOS`, `CEDULA`, `CORREO`, `TELEFONO`) VALUES
(1, 'JUAN GABRIEL', 'LOPEZ LOOR', '1723551044', 'jglpl@gmail.com', '0985415234'),
(2, 'MARIA FERNANDA', 'VELEZ PEREZ', '1745998523', 'mafervelez@gmail.com', '0985626247');

-- --------------------------------------------------------

--
-- Volcado de datos para la tabla `cuenta`
--

INSERT INTO `cuenta` (`CODIGO`, `CODIGOCLIENTE`, `NUMERO`, `TIPO`, `SALDO`) VALUES
(1, 1, '11111111111', 'A', '201.00'),
(2, 2, '11111111112', 'A', '23.00'),
(3, 1, '11111111113', 'C', '200.00');

-- --------------------------------------------------------

--
-- Volcado de datos para la tabla `movimientos`
--

INSERT INTO `movimientos` (`CODIGO`, `CUENTAEMISOR`, `CUENTARECEPTOR`, `FECHA`, `TIPO`, `VALOR`, `SALDOFINAL`) VALUES
(1, 1, 2, '2021-01-01 17:52:57', 'D', '20.32', '123.30'),
(2, 2, 3, '2021-01-04 17:52:57', 'C', '12.30', '145.30'),
(3, 1, 3, '2021-01-05 17:53:51', 'C', '12.30', '231.10'),
(4, 1, 2, '2021-01-04 17:53:51', 'D', '142.30', '1222.30'),
(5, 1, 2, '2021-01-01 17:54:32', 'D', '20.32', '231.10');
