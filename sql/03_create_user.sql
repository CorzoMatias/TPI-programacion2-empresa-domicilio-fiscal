-- 1) Crear (o actualizar) el usuario con la contraseña que ya tengo en application.properties:

CREATE USER IF NOT EXISTS 'tfi_user'@'localhost' IDENTIFIED BY 'CAMBIAR_ESTA_CLAVE';
ALTER USER 'tfi_user'@'localhost' IDENTIFIED BY 'CAMBIAR_ESTA_CLAVE';

-- (opcional) para evitar plugin de autenticación:
-- ALTER USER 'tfi_user'@'localhost' IDENTIFIED WITH mysql_native_password BY 'CAMBIAR_ESTA_CLAVE';

-- 2) Dar privilegios sobre el esquema ya creado:
GRANT ALL PRIVILEGES ON tfi_empresa.* TO 'tfi_user'@'localhost';

-- 3) Aplicar cambios
FLUSH PRIVILEGES;

-- 4) Verificar que quedó con permisos:
SHOW GRANTS FOR 'tfi_user'@'localhost';