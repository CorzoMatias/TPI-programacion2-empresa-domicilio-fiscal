package tpi.service;

import tpi.dao.DomicilioFiscalDao;
import tpi.entities.DomicilioFiscal;

import java.sql.SQLException;
import java.util.List;

public class DomicilioFiscalService implements GenericService<DomicilioFiscal> {

    private final DomicilioFiscalDao dao = new DomicilioFiscalDao();

    private void validar(DomicilioFiscal d) throws ServiceException {
        if (d == null) throw new ServiceException("Domicilio nulo");
        if (d.getCalle() == null || d.getCalle().isBlank()) throw new ServiceException("Calle obligatoria");
        if (d.getCiudad() == null || d.getCiudad().isBlank()) throw new ServiceException("Ciudad obligatoria");
        if (d.getProvincia() == null || d.getProvincia().isBlank()) throw new ServiceException("Provincia obligatoria");
        if (d.getPais() == null || d.getPais().isBlank()) throw new ServiceException("País obligatorio");
        if (d.getNumero() != null && d.getNumero() < 0) throw new ServiceException("Número debe ser positivo");
    }

    @Override public DomicilioFiscal insertar(DomicilioFiscal d) throws ServiceException {
        validar(d);
        try {
            return dao.crear(d);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }
    }
    @Override public DomicilioFiscal actualizar(DomicilioFiscal d) throws ServiceException {
        validar(d);
        try {
            return dao.actualizar(d);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }
    }
    @Override public boolean eliminar(Long id) throws ServiceException {
        if (id==null) throw new ServiceException("ID obligatorio");
        try {
            return dao.eliminarLogico(id);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }
    }
    @Override public DomicilioFiscal getById(Long id) throws ServiceException {
        if (id==null) throw new ServiceException("ID obligatorio");
        try {
            return dao.leerPorId(id);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }
    }
    @Override public List<DomicilioFiscal> getAll() throws ServiceException {
        try {
            return dao.leerTodos();
        } catch (SQLException e) {
            throw new ServiceException(e);
        }
    }
}
