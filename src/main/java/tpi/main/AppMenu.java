package tpi.main;

import tpi.entities.DomicilioFiscal;
import tpi.entities.Empresa;
import tpi.service.DomicilioFiscalService;
import tpi.service.EmpresaService;

import java.util.List;
import java.util.Scanner;

public class AppMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final InputUtils in = new InputUtils(scanner);
    private final EmpresaService empresaService = new EmpresaService();
    private final DomicilioFiscalService domicilioService = new DomicilioFiscalService();

    public void start() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== MENÚ  ===");
            System.out.println("1) Crear Empresa + Domicilio (transaccional)");
            System.out.println("2) Listar Empresas");
            System.out.println("3) Buscar Empresa por ID");
            System.out.println("4) Buscar Empresa por CUIT");
            System.out.println("5) Actualizar Empresa (+ Domicilio opcional)");
            System.out.println("6) Eliminar Empresa");
            System.out.println("7) Listar Domicilios Fiscales");
            System.out.println("8) Simular rollback (falla intencional)");
            System.out.println("0) Salir");
            System.out.print("Seleccione opción: ");
            String opt = scanner.nextLine().trim();

            try {
                switch (opt) {
                    case "1" -> crearEmpresaTransaccional();
                    case "2" -> listarEmpresas();
                    case "3" -> buscarEmpresaPorId();
                    case "4" -> buscarEmpresaPorCuit();
                    case "5" -> actualizarEmpresa();
                    case "6" -> eliminarEmpresa();
                    case "7" -> listarDomicilios();
                    case "8" -> simularRollback();
                    case "0" -> { System.out.println("Saliendo"); exit = true; }
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception ex) {
                System.out.println("❌ Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    // --------- Opciones ---------

    // private void crearEmpresaTransaccional() throws Exception {
    //     System.out.println("\n-- Alta Empresa + Domicilio --");
    //     Empresa e = new Empresa();
    //     e.setRazonSocial(in.readNonEmpty("Razón Social").toUpperCase());
    //     e.setCuit(in.readNonEmpty("CUIT"));
    //     e.setActividadPrincipal(in.readNullable("Actividad principal"));
    //     e.setEmail(in.readNullable("Email"));

    //     DomicilioFiscal d = new DomicilioFiscal();
    //     d.setCalle(in.readNonEmpty("Calle"));
    //     d.setNumero(in.readPositiveIntNullable("Número"));
    //     d.setCiudad(in.readNonEmpty("Ciudad").toUpperCase());
    //     d.setProvincia(in.readNonEmpty("Provincia").toUpperCase());
    //     d.setCodigoPostal(in.readNullable("Código Postal"));
    //     d.setPais(in.readNonEmpty("País").toUpperCase());
    //     e.setDomicilioFiscal(d);

    //     Empresa creada = empresaService.insertar(e); // transacción commit/rollback
    //     System.out.println("Empresa creada con éxito. La misma fue creada con ID: " + creada.getId());
    //     System.out.println(creada);
    // }
    private void crearEmpresaTransaccional() {
    System.out.println("\n-- Alta Empresa + Domicilio --");
    try {
        Empresa e = new Empresa();
        e.setRazonSocial(in.readNonEmpty("Razón Social").toUpperCase());
        e.setCuit(in.readNonEmpty("CUIT"));
        e.setActividadPrincipal(in.readNullable("Actividad principal"));
        e.setEmail(in.readNullable("Email"));

        DomicilioFiscal d = new DomicilioFiscal();
        d.setCalle(in.readNonEmpty("Calle"));
        d.setNumero(in.readPositiveIntNullable("Número"));
        d.setCiudad(in.readNonEmpty("Ciudad").toUpperCase());
        d.setProvincia(in.readNonEmpty("Provincia").toUpperCase());
        d.setCodigoPostal(in.readNullable("Código Postal"));
        d.setPais(in.readNonEmpty("País").toUpperCase());
        e.setDomicilioFiscal(d);

        empresaService.insertar(e); // transacción commit/rollback
        System.out.println("Empresa creada con éxito. ID: " + e.getId());
        System.out.println(e);

    } catch (java.sql.SQLIntegrityConstraintViolationException dup) {
        System.out.println("Error: CUIT duplicado. No se insertó la empresa (rollback).");
    } catch (Exception ex) {
        // Mensaje claro para validaciones/otros errores
        System.out.println("Error: " + ex.getMessage());
        // aca puede ir un logging interno, ej: log.debug("Fallo en alta", ex);
    }
}

    private void listarEmpresas() throws Exception {
        System.out.println("\n-- Listado de Empresas --");
        List<Empresa> list = empresaService.getAll();
        if (list.isEmpty()) { System.out.println("(sin resultados)"); return; }
        list.forEach(System.out::println);
    }

    private void buscarEmpresaPorId() throws Exception {
        System.out.println("\n-- Buscar por ID --");
        Long id = in.readId("ID de Empresa");
        Empresa e = empresaService.getById(id);
        if (e == null) System.out.println("No existe empresa con ID " + id);
        else System.out.println(e);
    }

    private void buscarEmpresaPorCuit() throws Exception {
        System.out.println("\n-- Buscar por CUIT --");
        String cuit = in.readNonEmpty("CUIT");
        Empresa e = empresaService.getByCuit(cuit);
        if (e == null) System.out.println("No existe empresa con CUIT " + cuit);
        else System.out.println(e);
    }

    private void actualizarEmpresa() throws Exception {
        System.out.println("\n-- Actualizar Empresa --");
        Long id = in.readId("ID de Empresa a actualizar");
        Empresa existente = empresaService.getById(id);
        if (existente == null) { System.out.println("No existe la empresa con ID " + id); return; }

        String rs = in.readNullable("Nueva razón social (ENTER mantiene)");
        if (rs != null && !rs.isBlank()) existente.setRazonSocial(rs.toUpperCase());
        String cuit = in.readNullable("Nuevo CUIT (ENTER mantiene)");
        if (cuit != null && !cuit.isBlank()) existente.setCuit(cuit);
        String act = in.readNullable("Nueva actividad principal (ENTER mantiene)");
        if (act != null) existente.setActividadPrincipal(act);
        String email = in.readNullable("Nuevo email (ENTER mantiene)");
        if (email != null) existente.setEmail(email);

        if (existente.getDomicilioFiscal() != null) {
            System.out.println("¿Actualizar domicilio fiscal también?");
            if (in.confirm("Confirmar")) {
                DomicilioFiscal d = existente.getDomicilioFiscal();
                String calle = in.readNullable("Calle (ENTER mantiene)");
                if (calle != null && !calle.isBlank()) d.setCalle(calle);
                Integer numero = in.readPositiveIntNullable("Número (ENTER mantiene)");
                if (numero != null) d.setNumero(numero);
                String ciudad = in.readNullable("Ciudad (ENTER mantiene)");
                if (ciudad != null && !ciudad.isBlank()) d.setCiudad(ciudad.toUpperCase());
                String prov = in.readNullable("Provincia (ENTER mantiene)");
                if (prov != null && !prov.isBlank()) d.setProvincia(prov.toUpperCase());
                String cp = in.readNullable("Código Postal (ENTER mantiene)");
                if (cp != null) d.setCodigoPostal(cp);
                String pais = in.readNullable("País (ENTER mantiene)");
                if (pais != null && !pais.isBlank()) d.setPais(pais.toUpperCase());
            }
        }

        Empresa actualizada = empresaService.actualizar(existente);
        System.out.println("Empresa actualizada con éxito.");
        System.out.println(actualizada);
    }

    private void eliminarEmpresa() throws Exception {
        System.out.println("\n-- Empresa eliminada --");
        Long id = in.readId("ID de Empresa a eliminar");
        if (!in.confirm("Confirma baja lógica?")) { System.out.println("Operación cancelada."); return; }
        boolean ok = empresaService.eliminar(id);
        System.out.println(ok ? "Empresa eliminada correctamente." : "No se encontró la empresa o ya estaba dada de baja.");
    }

    private void listarDomicilios() throws Exception {
        System.out.println("\n-- Listado de Domicilios Fiscales --");
        var list = domicilioService.getAll();
        if (list.isEmpty()) { System.out.println("(sin resultados)"); return; }
        list.forEach(System.out::println);
    }

    private void simularRollback() {
    System.out.println("\n-- Simulación de rollback (CUIT duplicado) --");
    try {
        // Pedimos un CUIT EXISTENTE para forzar el error de UNIQUE en DB
        String cuitExistente = in.readNonEmpty("Ingresá un CUIT que YA exista (para forzar el error)");

        // Empresa VÁLIDA pero con CUIT duplicado (pasa validaciones, falla en DB)
        Empresa e = new Empresa();
        e.setRazonSocial("PRUEBA ROLLBACK SA");
        e.setCuit(cuitExistente);               // <-- dispara violacion UNIQUE
        e.setActividadPrincipal("TEST");
        e.setEmail("rollback@test.com");

        DomicilioFiscal d = new DomicilioFiscal();
        d.setCalle("CALLE VALIDA");
        d.setNumero(123);
        d.setCiudad("CABA");
        d.setProvincia("CABA");
        d.setCodigoPostal("1000");
        d.setPais("ARGENTINA");
        e.setDomicilioFiscal(d);

        empresaService.insertar(e);  // aca se abre la transaccion y falla en el INSERT de empresa

        System.out.println("Esto no debería verse (faltó disparar la UNIQUE de CUIT).");
    } catch (Exception ex) {
        System.out.println("Excepción capturada (esperada): " + ex.getMessage());
        System.out.println("Rollback ejecutado. La BD quedó igual (sin nuevas filas).");
    }
}
}
