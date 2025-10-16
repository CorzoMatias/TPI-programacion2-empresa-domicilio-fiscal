package tpi.service;

import java.util.List;

public interface GenericService<T> {
    T insertar(T t) throws Exception;
    T actualizar(T t) throws Exception;
    boolean eliminar(Long id) throws Exception;
    T getById(Long id) throws Exception;
    List<T> getAll() throws Exception;
}
