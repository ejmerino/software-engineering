/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     1/1/2021 19:48:24                            */
/*==============================================================*/


DROP TABLE IF EXISTS CLIENTE;

DROP TABLE IF EXISTS CUENTA;

DROP TABLE IF EXISTS MOVIMIENTOS;

/*==============================================================*/
/* Table: CLIENTE                                               */
/*==============================================================*/
CREATE TABLE CLIENTE
(
   CODIGO               INT NOT NULL AUTO_INCREMENT,
   NOMBRES              VARCHAR(100) NOT NULL,
   APELLIDOS            VARCHAR(100) NOT NULL,
   CEDULA               VARCHAR(10) NOT NULL,
   CORREO               VARCHAR(100),
   TELEFONO             VARCHAR(10),
   PRIMARY KEY (CODIGO)
);

ALTER TABLE CLIENTE COMMENT 'Tabla encargada de registrar la información del cliente';

/*==============================================================*/
/* Table: CUENTA                                                */
/*==============================================================*/
CREATE TABLE CUENTA
(
   CODIGO               INT NOT NULL AUTO_INCREMENT,
   CODIGOCLIENTE        INT NOT NULL,
   NUMERO               VARCHAR(11) NOT NULL,
   TIPO                 CHAR(1) NOT NULL DEFAULT 'A',
   SALDO                DECIMAL(10,2) NOT NULL,
   PRIMARY KEY (CODIGO)
);

/*==============================================================*/
/* Table: MOVIMIENTOS                                           */
/*==============================================================*/
CREATE TABLE MOVIMIENTOS
(
   CODIGO               INT NOT NULL AUTO_INCREMENT,
   CUENTAEMISOR         INT NOT NULL,
   CUENTARECEPTOR       INT NOT NULL,
   FECHA                DATETIME NOT NULL,
   TIPO                 CHAR(1) NOT NULL,
   VALOR                DECIMAL (10,2),
   SALDOFINAL           DECIMAL (10,2),
   PRIMARY KEY (CODIGO)
);

ALTER TABLE CUENTA ADD CONSTRAINT FK_CLIENTECUENTA FOREIGN KEY (CODIGOCLIENTE)
      REFERENCES CLIENTE (CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE MOVIMIENTOS ADD CONSTRAINT FK_CUENTAEMISOR FOREIGN KEY (CUENTARECEPTOR)
      REFERENCES CUENTA (CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE MOVIMIENTOS ADD CONSTRAINT FK_CUENTARECEPTOR FOREIGN KEY (CUENTAEMISOR)
      REFERENCES CUENTA (CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

