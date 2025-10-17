package tpi.service;

import tpi.dao.DomicilioFiscalDao;
import tpi.entities.DomicilioFiscal;

import java.util.List;

public class DomicilioFiscalService implements GenericService<DomicilioFiscal> {

    private final DomicilioFiscalDao dao = new DomicilioFiscalDao();

    private void validar(DomicilioFiscal d) throws Exception {
        if (d == null) throw new Exception("Domicilio nulo");
        if (d.getCalle() == null || d.getCalle().isBlank()) throw new Exception("Calle obligatoria");
        if (d.getCiudad() == null || d.getCiudad().isBlank()) throw new Exception("Ciudad obligatoria");
        if (d.getProvincia() == null || d.getProvincia().isBlank()) throw new Exception("Provincia obligatoria");
        if (d.getPais() == null || d.getPais().isBlank()) throw new Exception("País obligatorio");
        if (d.getNumero() != null && d.getNumero() < 0) throw new Exception("Número debe ser positivo");
    }

    @Override public DomicilioFiscal insertar(DomicilioFiscal d) throws Exception { validar(d); return dao.crear(d); }
    @Override public DomicilioFiscal actualizar(DomicilioFiscal d) throws Exception { validar(d); return dao.actualizar(d); }
    @Override public boolean eliminar(Long id) throws Exception { if (id==null) throw new Exception("ID obligatorio"); return dao.eliminarLogico(id); }
    @Override public DomicilioFiscal getById(Long id) throws Exception { if (id==null) throw new Exception("ID obligatorio"); return dao.leerPorId(id); }
    @Override public List<DomicilioFiscal> getAll() throws Exception { return dao.leerTodos(); }
}
