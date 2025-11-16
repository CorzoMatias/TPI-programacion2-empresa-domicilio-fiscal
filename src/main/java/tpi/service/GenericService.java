package tpi.service;

import java.util.List;

public interface GenericService<T> {
    T insertar(T t) throws ServiceException;
    T actualizar(T t) throws ServiceException;
    boolean eliminar(Long id) throws ServiceException;
    T getById(Long id) throws ServiceException;
    List<T> getAll() throws ServiceException;
}
