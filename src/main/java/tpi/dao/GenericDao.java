package tpi.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface GenericDao<T> {
    // Autonomas (manejan su propia Connection)
    T crear(T t) throws SQLException;
    T leerPorId(Long id) throws SQLException;
    java.util.List<T> leerTodos() throws SQLException;
    T actualizar(T t) throws SQLException;
    boolean eliminarLogico(Long id) throws SQLException;

    // Transaccionales (usan Connection externa compartida)
    T crear(T t, Connection externalConn) throws SQLException;
    T actualizar(T t, Connection externalConn) throws SQLException;
    boolean eliminarLogico(Long id, Connection externalConn) throws SQLException;
}
