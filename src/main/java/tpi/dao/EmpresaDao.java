package tpi.dao;

import tpi.config.DatabaseConnection;
import tpi.entities.DomicilioFiscal;
import tpi.entities.Empresa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpresaDao implements GenericDao<Empresa> {

    private final DomicilioFiscalDao domicilioDao = new DomicilioFiscalDao();

    // --------- Helpers ---------
    private Empresa mapBasico(ResultSet rs) throws SQLException {
        Empresa e = new Empresa();
        e.setId(rs.getLong("id"));
        e.setEliminado(rs.getBoolean("eliminado"));
        e.setRazonSocial(rs.getString("razon_social"));
        e.setCuit(rs.getString("cuit"));
        e.setActividadPrincipal(rs.getString("actividad_principal"));
        e.setEmail(rs.getString("email"));
        return e;
    }

    private Empresa mapConDomicilio(Connection con, ResultSet rs) throws SQLException {
        Empresa e = mapBasico(rs);
        DomicilioFiscal d = domicilioDao.leerPorEmpresaId(e.getId(), con);
        e.setDomicilioFiscal(d);
        return e;
    }

    // --------- Metodos extras ---------
    public Empresa leerPorCuit(String cuit) throws SQLException {
        String sql = "SELECT * FROM empresa WHERE cuit=? AND eliminado=FALSE";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cuit);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return mapConDomicilio(con, rs);
            }
        }
    }

    // --------- Implementacion GenericDao ---------
    @Override
    public Empresa crear(Empresa e) throws SQLException {
        try (Connection con = DatabaseConnection.getConnection()) {
            return crear(e, con);
        }
    }

    @Override
    public Empresa crear(Empresa e, Connection con) throws SQLException {
        String sql = """
            INSERT INTO empresa (eliminado, razon_social, cuit, actividad_principal, email)
            VALUES (FALSE, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getRazonSocial());
            ps.setString(2, e.getCuit());
            ps.setString(3, e.getActividadPrincipal());
            ps.setString(4, e.getEmail());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) e.setId(rs.getLong(1));
            }
            return e;
        }
    }

    @Override
    public Empresa leerPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM empresa WHERE id=? AND eliminado=FALSE";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return mapConDomicilio(con, rs);
            }
        }
    }

    @Override
    public List<Empresa>  leerTodos() throws SQLException {
        String sql = "SELECT * FROM empresa WHERE eliminado=FALSE";
        List<Empresa> list = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapConDomicilio(con, rs));
        }
        return list;
    }

    @Override
    public Empresa actualizar(Empresa e) throws SQLException {
        try (Connection con = DatabaseConnection.getConnection()) {
            return actualizar(e, con);
        }
    }

    @Override
    public Empresa actualizar(Empresa e, Connection con) throws SQLException {
        String sql = """
            UPDATE empresa
               SET razon_social=?, cuit=?, actividad_principal=?, email=?
             WHERE id=? AND eliminado=FALSE
            """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getRazonSocial());
            ps.setString(2, e.getCuit());
            ps.setString(3, e.getActividadPrincipal());
            ps.setString(4, e.getEmail());
            ps.setLong(5, e.getId());
            ps.executeUpdate();
            return e;
        }
    }

    @Override
    public boolean eliminarLogico(Long id) throws SQLException {
        try (Connection con = DatabaseConnection.getConnection()) {
            return eliminarLogico(id, con);
        }
    }

    @Override
    public boolean eliminarLogico(Long id, Connection con) throws SQLException {
        String sql = "UPDATE empresa SET eliminado=TRUE WHERE id=? AND eliminado=FALSE";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
