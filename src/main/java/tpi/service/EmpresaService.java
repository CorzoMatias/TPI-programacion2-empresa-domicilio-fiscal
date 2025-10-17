package tpi.service;

import tpi.config.DatabaseConnection;
import tpi.dao.DomicilioFiscalDao;
import tpi.dao.EmpresaDao;
import tpi.entities.DomicilioFiscal;
import tpi.entities.Empresa;

import java.sql.Connection;

public class EmpresaService implements GenericService<Empresa> {

    private final EmpresaDao empresaDao = new EmpresaDao();
    private final DomicilioFiscalDao domicilioDao = new DomicilioFiscalDao();

    // ---------- Validaciones ----------
    private void validar(Empresa e, boolean validarDomicilio) throws Exception {
        if (e == null) throw new Exception("Empresa nula");
        if (e.getRazonSocial() == null || e.getRazonSocial().isBlank())
            throw new Exception("La razón social es obligatoria");
        if (e.getCuit() == null || e.getCuit().isBlank())
            throw new Exception("El CUIT es obligatorio");
        if (e.getCuit().length() > 13)
            throw new Exception("El CUIT no debe exceder 13 caracteres");
        if (validarDomicilio) {
            if (e.getDomicilioFiscal() == null) throw new Exception("El domicilio fiscal es obligatorio");
            validarDomicilio(e.getDomicilioFiscal());
        }
    }

    private void validarDomicilio(DomicilioFiscal d) throws Exception {
        if (d.getCalle() == null || d.getCalle().isBlank()) throw new Exception("Calle obligatoria");
        if (d.getCiudad() == null || d.getCiudad().isBlank()) throw new Exception("Ciudad obligatoria");
        if (d.getProvincia() == null || d.getProvincia().isBlank()) throw new Exception("Provincia obligatoria");
        if (d.getPais() == null || d.getPais().isBlank()) throw new Exception("País obligatorio");
        if (d.getNumero() != null && d.getNumero() < 0) throw new Exception("Número debe ser positivo");
    }

    // ---------- Transaccion insertar Empresa + Domicilio (1→1) ----------
    @Override
    public Empresa insertar(Empresa e) throws Exception {
        validar(e, true);

        try (Connection con = DatabaseConnection.getConnection()) {
            boolean original = con.getAutoCommit();
            try {
                con.setAutoCommit(false);

                // 1) Crear Empresa (A) -> obtenemos id
                empresaDao.crear(e, con);

                // 2) Crear Domicilio (B) con empresa_id = e.id
                domicilioDao.crear(e.getDomicilioFiscal(), con, e.getId());

                con.commit();
                return e;
            } catch (Exception ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(original);
            }
        }
    }

    @Override
    public Empresa actualizar(Empresa e) throws Exception {
        validar(e, false);
        if (e.getId() == null) throw new Exception("ID de empresa obligatorio para actualizar");

        try (Connection con = DatabaseConnection.getConnection()) {
            boolean original = con.getAutoCommit();
            try {
                con.setAutoCommit(false);

                empresaDao.actualizar(e, con);
                if (e.getDomicilioFiscal() != null && e.getDomicilioFiscal().getId() != null) {
                    validarDomicilio(e.getDomicilioFiscal());
                    domicilioDao.actualizar(e.getDomicilioFiscal(), con);
                }

                con.commit();
                return e;
            } catch (Exception ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(original);
            }
        }
    }

    @Override
public boolean eliminar(Long id) throws Exception {
    if (id == null) throw new Exception("ID obligatorio");
    try (Connection con = DatabaseConnection.getConnection()) {
        boolean original = con.getAutoCommit();
        try {
            con.setAutoCommit(false);

            // 1) Baja lógica del domicilio (B) asociado a la Empresa (A)
            domicilioDao.eliminarLogicoPorEmpresaId(id, con);

            // 2) Baja lógica de la empresa (A)
            boolean ok = empresaDao.eliminarLogico(id, con);

            con.commit();
            return ok;
        } catch (Exception ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(original);
        }
    }
}


    @Override
    public Empresa getById(Long id) throws Exception {
        if (id == null) throw new Exception("ID obligatorio");
        return empresaDao.leerPorId(id);
    }

    public Empresa getByCuit(String cuit) throws Exception {
        if (cuit == null || cuit.isBlank()) throw new Exception("CUIT obligatorio");
        return empresaDao.leerPorCuit(cuit);
    }

    @Override
    public java.util.List<Empresa> getAll() throws Exception {
        return empresaDao.leerTodos();
    }
}
