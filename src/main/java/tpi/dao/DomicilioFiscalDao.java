package tpi.dao;

import tpi.config.DatabaseConnection;
import tpi.entities.DomicilioFiscal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DomicilioFiscalDao implements GenericDao<DomicilioFiscal> {

    // --------- Helpers ---------
    private DomicilioFiscal map(ResultSet rs) throws SQLException {
        DomicilioFiscal d = new DomicilioFiscal();
        d.setId(rs.getLong("id"));
        d.setEliminado(rs.getBoolean("eliminado"));
        d.setCalle(rs.getString("calle"));
        int numero = rs.getInt("numero");
        d.setNumero(rs.wasNull() ? null : numero);
        d.setCiudad(rs.getString("ciudad"));
        d.setProvincia(rs.getString("provincia"));
        d.setCodigoPostal(rs.getString("codigo_postal"));
        d.setPais(rs.getString("pais"));
        return d;
    }

    // Para poblar por empresa_id (1→1)
    public DomicilioFiscal leerPorEmpresaId(Long empresaId) throws SQLException {
        String sql = "SELECT * FROM domicilio_fiscal WHERE empresa_id = ? AND eliminado = FALSE";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, empresaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
                return null;
            }
        }
    }

    public DomicilioFiscal leerPorEmpresaId(Long empresaId, Connection con) throws SQLException {
        String sql = "SELECT * FROM domicilio_fiscal WHERE empresa_id = ? AND eliminado = FALSE";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, empresaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
                return null;
            }
        }
    }

    // --------- Implementacion GenericDao ---------
    @Override
    public DomicilioFiscal crear(DomicilioFiscal d) throws SQLException {
        try (Connection con = DatabaseConnection.getConnection()) {
            return crear(d, con);
        }
    }

    @Override
    public DomicilioFiscal crear(DomicilioFiscal d, Connection con) throws SQLException {
        // Inserta SIN empresa_id (NULL).
        String sql = """
            INSERT INTO domicilio_fiscal
            (eliminado, calle, numero, ciudad, provincia, codigo_postal, pais, empresa_id)
            VALUES (FALSE, ?, ?, ?, ?, ?, ?, NULL)
            """;
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, d.getCalle());
            if (d.getNumero() == null) ps.setNull(2, Types.INTEGER); else ps.setInt(2, d.getNumero());
            ps.setString(3, d.getCiudad());
            ps.setString(4, d.getProvincia());
            ps.setString(5, d.getCodigoPostal());
            ps.setString(6, d.getPais());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) d.setId(rs.getLong(1));
            }
            return d;
        }
    }

    // Metodo para 1→1 (recibe empresaId)
    public DomicilioFiscal crear(DomicilioFiscal d, Connection con, Long empresaId) throws SQLException {
        String sql = """
            INSERT INTO domicilio_fiscal
            (eliminado, calle, numero, ciudad, provincia, codigo_postal, pais, empresa_id)
            VALUES (FALSE, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, d.getCalle());
            if (d.getNumero() == null) ps.setNull(2, Types.INTEGER); else ps.setInt(2, d.getNumero());
            ps.setString(3, d.getCiudad());
            ps.setString(4, d.getProvincia());
            ps.setString(5, d.getCodigoPostal());
            ps.setString(6, d.getPais());
            ps.setLong(7, empresaId);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) d.setId(rs.getLong(1));
            }
            return d;
        }
    }

    @Override
    public DomicilioFiscal leerPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM domicilio_fiscal WHERE id = ? AND eliminado = FALSE";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
                return null;
            }
        }
    }

    @Override
    public List<DomicilioFiscal> leerTodos() throws SQLException {
        String sql = "SELECT * FROM domicilio_fiscal WHERE eliminado = FALSE";
        List<DomicilioFiscal> list = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    @Override
    public DomicilioFiscal actualizar(DomicilioFiscal d) throws SQLException {
        try (Connection con = DatabaseConnection.getConnection()) {
            return actualizar(d, con);
        }
    }

    @Override
    public DomicilioFiscal actualizar(DomicilioFiscal d, Connection con) throws SQLException {
        String sql = """
            UPDATE domicilio_fiscal
               SET calle=?, numero=?, ciudad=?, provincia=?, codigo_postal=?, pais=?
             WHERE id=? AND eliminado=FALSE
            """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, d.getCalle());
            if (d.getNumero() == null) ps.setNull(2, Types.INTEGER); else ps.setInt(2, d.getNumero());
            ps.setString(3, d.getCiudad());
            ps.setString(4, d.getProvincia());
            ps.setString(5, d.getCodigoPostal());
            ps.setString(6, d.getPais());
            ps.setLong(7, d.getId());
            ps.executeUpdate();
            return d;
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
        String sql = "UPDATE domicilio_fiscal SET eliminado=TRUE WHERE id=? AND eliminado=FALSE";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // marco eliminado=TRUE para el domicilio de una empresa dada (1→1)
public boolean eliminarLogicoPorEmpresaId(Long empresaId, Connection con) throws SQLException {
    String sql = "UPDATE domicilio_fiscal SET eliminado=TRUE WHERE empresa_id=? AND eliminado=FALSE";
    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setLong(1, empresaId);
        return ps.executeUpdate() > 0;
    }
}

}
