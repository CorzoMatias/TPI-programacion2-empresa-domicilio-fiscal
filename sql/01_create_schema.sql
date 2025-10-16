CREATE DATABASE IF NOT EXISTS tfi_empresa
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE tfi_empresa;

DROP TABLE IF EXISTS domicilio_fiscal;
DROP TABLE IF EXISTS empresa;

CREATE TABLE empresa (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  eliminado BOOLEAN NOT NULL DEFAULT FALSE,
  razon_social VARCHAR(120) NOT NULL,
  cuit VARCHAR(13) NOT NULL UNIQUE,
  actividad_principal VARCHAR(80),
  email VARCHAR(120)
);

CREATE TABLE domicilio_fiscal (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  eliminado BOOLEAN NOT NULL DEFAULT FALSE,
  calle VARCHAR(100) NOT NULL,
  numero INT,
  ciudad VARCHAR(80) NOT NULL,
  provincia VARCHAR(80) NOT NULL,
  codigo_postal VARCHAR(10),
  pais VARCHAR(80) NOT NULL,
  empresa_id BIGINT UNIQUE,
  CONSTRAINT fk_domicilio_empresa FOREIGN KEY (empresa_id)
    REFERENCES empresa(id) ON DELETE CASCADE
);
