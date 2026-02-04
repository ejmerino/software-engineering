/*==============================================================*/
/* DBMS name:      Microsoft SQL Server 2008                                    */
/* Created on:     02/06/2022 0:07:41                           */
/*==============================================================*/

Create database Eurekabank;
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('CUENTAS') and o.name = 'FK_CUENTAS_TENER_CLIENTES')
alter table CUENTAS
   drop constraint FK_CUENTAS_TENER_CLIENTES
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('MOVIMIENTOS') and o.name = 'FK_MOVIMIEN_GENERAR_CUENTAS')
alter table MOVIMIENTOS
   drop constraint FK_MOVIMIEN_GENERAR_CUENTAS
go

if exists (select 1
            from  sysobjects
           where  id = object_id('CLIENTES')
            and   type = 'U')
   drop table CLIENTES
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('CUENTAS')
            and   name  = 'TENER_FK'
            and   indid > 0
            and   indid < 255)
   drop index CUENTAS.TENER_FK
go

if exists (select 1
            from  sysobjects
           where  id = object_id('CUENTAS')
            and   type = 'U')
   drop table CUENTAS
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('MOVIMIENTOS')
            and   name  = 'GENERAR_FK'
            and   indid > 0
            and   indid < 255)
   drop index MOVIMIENTOS.GENERAR_FK
go

if exists (select 1
            from  sysobjects
           where  id = object_id('MOVIMIENTOS')
            and   type = 'U')
   drop table MOVIMIENTOS
go

/*==============================================================*/
/* Table: CLIENTES                                              */
/*==============================================================*/
create table CLIENTES (
   CLI_ID               int IDENTITY(1,1)    not null,
   CLI_CEDULA           varchar(10)          null,
   CLI_NOMBRE           varchar(32)          null,
   CLI_APELLIDO         varchar(32)          null,
   CLI_CORREO           varchar(50)          null,
   CLI_TELEFONO         varchar(10)          null,
   constraint PK_CLIENTES primary key (CLI_ID)
)
go

/*==============================================================*/
/* Table: CUENTAS                                               */
/*==============================================================*/
create table CUENTAS (
   CU_ID                int IDENTITY(1,1)    not null,
   CLI_ID               int                  not null,
   CU_NUMERO            varchar(10)          null,
   CU_TIPO              varchar(10)          null,
   CU_SALDO             float(10)            null,
   constraint PK_CUENTAS primary key (CU_ID)
)
go

/*==============================================================*/
/* Index: TENER_FK                                              */
/*==============================================================*/




create nonclustered index TENER_FK on CUENTAS (CLI_ID ASC)
go

/*==============================================================*/
/* Table: MOVIMIENTOS                                           */
/*==============================================================*/
create table MOVIMIENTOS (
   MOV_ID               int IDENTITY(1,1)    not null,
   CU_ID                int                  not null,
   MOV_FECHA            datetime             null,
   MOV_TIPO             varchar(16)          null,
   MOV_VALOR            float(10)            null,
   MOV_SALDO_FINAL      float(5)             null,
   constraint PK_MOVIMIENTOS primary key (MOV_ID)
)
go

/*==============================================================*/
/* Index: GENERAR_FK                                            */
/*==============================================================*/




create nonclustered index GENERAR_FK on MOVIMIENTOS (CU_ID ASC)
go

alter table CUENTAS
   add constraint FK_CUENTAS_TENER_CLIENTES foreign key (CLI_ID)
      references CLIENTES (CLI_ID)
go

alter table MOVIMIENTOS
   add constraint FK_MOVIMIEN_GENERAR_CUENTAS foreign key (CU_ID)
      references CUENTAS (CU_ID)
go

-- Crear la tabla USUARIOS
CREATE TABLE USUARIOS (
    USU_ID INT IDENTITY(1,1) PRIMARY KEY,           -- ID incremental
    USU_NOMBRE NVARCHAR(100) NOT NULL,              -- Nombre del usuario
    USU_APELLIDO NVARCHAR(100) NOT NULL,            -- Apellido del usuario
    USU_CORREO NVARCHAR(255) NOT NULL UNIQUE,       -- Correo electrónico (debe ser único)
    USU_TELEFONO NVARCHAR(20) NOT NULL,             -- Teléfono del usuario
    USU_CEDULA NVARCHAR(20) NOT NULL UNIQUE,        -- Número de cédula (debe ser único)
    USU_ROL NVARCHAR(20) CHECK (USU_ROL IN ('Usuario', 'Administrador')) NOT NULL, -- Rol (Usuario o Administrador)
    USU_CONTRASENA NVARCHAR(50) NOT NULL            -- Contraseña del usuario
);
GO

-- Agregar columna USU_ID en la tabla CLIENTES
ALTER TABLE CLIENTES
ADD USU_ID INT NULL;
GO

-- Definir la clave foránea que relaciona CLIENTES con USUARIOS (no obligatoria)
ALTER TABLE CLIENTES
ADD CONSTRAINT FK_CLIENTES_USUARIOS
FOREIGN KEY (USU_ID) REFERENCES USUARIOS(USU_ID);
GO

-- Actualizar USU_ID en CLIENTES basado en la cédula
UPDATE CLIENTES
SET USU_ID = U.USU_ID
FROM CLIENTES C
INNER JOIN USUARIOS U
ON C.CLI_CEDULA = U.USU_CEDULA;
GO

-- Hacer que USU_ID en CLIENTES permita valores nulos (si no es obligatorio)
ALTER TABLE CLIENTES
ALTER COLUMN USU_ID INT NULL;
GO
