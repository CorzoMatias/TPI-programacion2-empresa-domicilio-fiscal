USE tfi_empresa;

INSERT INTO empresa (razon_social, cuit, actividad_principal, email)
VALUES ('Acme SA','30-12345678-9','Manufactura','contacto@acme.com'),
       ('Beta SRL','30-87654321-0','Servicios','info@beta.com');

INSERT INTO domicilio_fiscal (calle,numero,ciudad,provincia,codigo_postal,pais,empresa_id)
VALUES ('Av. Siempre Viva',742,'Córdoba','Córdoba','5000','Argentina',1),
       ('Calle Falsa',123,'Rosario','Santa Fe','2000','Argentina',2);
