# TFI Programación 2 – Empresa → Domicilio Fiscal (1→1, JDBC, DAO + Service)

Proyecto de **consola** (Java 21 + Maven) que implementa un dominio con relación **1→1 unidireccional**:  
**Empresa (A)** → **Domicilio Fiscal (B)**, usando **JDBC (sin ORM)**, patrón **DAO + Service**, **transacciones** (commit/rollback), **CRUD completo**, **baja lógica**, menú de consola, **UML**, y **scripts SQL**.  
Cumple las consignas del documento “Trabajo Final Integrador – Programación 2” y la rúbrica de “Pautas de Corrección de TPI”.

## Requisitos

- **JDK 21**
- **Maven 3.9+**
- **MySQL Server 8.x** junto con **Workbench**

## Base de Datos

1. Ejecutar los scripts (en este orden) en MySQL Workbench o CLI:

   - `sql/01_create_schema.sql`: Crea la BD `tfi_empresa`, tablas `empresa` y `domicilio_fiscal`, FK **única** `empresa_id` (asegura 1→1) y `ON DELETE CASCADE`.

   - `sql/02_seed_data.sql`: Carga datos de ejemplo (2 empresas y sus domicilios).

   - `sql/03_create_user.sql`: Crea el usuario `tfi_user` con contraseña **CAMBIAR_ESTA_CLAVE**, y le otorga permisos sobre `tfi_empresa`


## Compilación y ejecución

Para ejecutar el código se debe hacer uso de un IDE compatible con Maven o bien ejecutar los siguientes comandos en consola:

```bash
# Compilación y ejecución del proyecto
mvn exec:java
```

```bash
# Construcción del JAR en caso de querer empaquetar el proyecto
mvn clean package
```

## Video
<TBD>