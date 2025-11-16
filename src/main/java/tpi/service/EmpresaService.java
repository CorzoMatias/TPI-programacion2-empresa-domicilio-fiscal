package tpi.service;

import tpi.config.DatabaseConnection;
import tpi.dao.DomicilioFiscalDao;
import tpi.dao.EmpresaDao;
import tpi.entities.DomicilioFiscal;
import tpi.entities.Empresa;

import java.sql.Connection;
import java.sql.SQLException;

public class EmpresaService implements GenericService<Empresa> {

    private final EmpresaDao empresaDao = new EmpresaDao();
    private final DomicilioFiscalDao domicilioDao = new DomicilioFiscalDao();

    // ---------- Validaciones ----------
    private void validar(Empresa e, boolean validarDomicilio) throws ServiceException {
        if (e == null) throw new ServiceException("Empresa nula");
        if (e.getRazonSocial() == null || e.getRazonSocial().isBlank())
            throw new ServiceException("La razón social es obligatoria");
        if (e.getCuit() == null || e.getCuit().isBlank())
            throw new ServiceException("El CUIT es obligatorio");
        if (e.getCuit().length() > 13)
            throw new ServiceException("El CUIT no debe exceder 13 caracteres");
        if (validarDomicilio) {
            if (e.getDomicilioFiscal() == null) throw new ServiceException("El domicilio fiscal es obligatorio");
            validarDomicilio(e.getDomicilioFiscal());
        }
    }

    private void validarDomicilio(DomicilioFiscal d) throws ServiceException {
        if (d.getCalle() == null || d.getCalle().isBlank()) throw new ServiceException("Calle obligatoria");
        if (d.getCiudad() == null || d.getCiudad().isBlank()) throw new ServiceException("Ciudad obligatoria");
        if (d.getProvincia() == null || d.getProvincia().isBlank()) throw new ServiceException("Provincia obligatoria");
        if (d.getPais() == null || d.getPais().isBlank()) throw new ServiceException("País obligatorio");
        if (d.getNumero() != null && d.getNumero() < 0) throw new ServiceException("Número debe ser positivo");
    }

    // ---------- Transaccion insertar Empresa + Domicilio (1→1) ----------
    @Override
    public Empresa insertar(Empresa e) throws ServiceException {
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
                System.out.println("Se realizará rollback por detección de un error");
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(original);
            }
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    @Override
    public Empresa actualizar(Empresa e) throws ServiceException {
        validar(e, false);
        if (e.getId() == null) throw new ServiceException("ID de empresa obligatorio para actualizar");

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
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    @Override
public boolean eliminar(Long id) throws ServiceException {
    if (id == null) throw new ServiceException("ID obligatorio");
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
    } catch (Exception e) {
        throw new ServiceException(e);
    }
}


    @Override
    public Empresa getById(Long id) throws ServiceException {
        if (id == null) throw new ServiceException("ID obligatorio");
        try {
            return empresaDao.leerPorId(id);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }
    }

    public Empresa getByCuit(String cuit) throws ServiceException {
        if (cuit == null || cuit.isBlank()) throw new ServiceException("CUIT obligatorio");
        try {
            return empresaDao.leerPorCuit(cuit);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public java.util.List<Empresa> getAll() throws ServiceException {
        try {
            return empresaDao.leerTodos();
        } catch (SQLException e) {
            throw new ServiceException(e);
        }
    }
}
