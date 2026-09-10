package main.java.edu.ingsoft.colegio.gotitas.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.java.edu.ingsoft.colegio.gotitas.config.DataBaseConnection;
import main.java.edu.ingsoft.colegio.gotitas.model.docente.Docente;

/** Acceso a datos de docentes, incluyendo su cuenta de acceso (tabla usuarios). */
public class DocenteRepository {

    private static final String SELECT_BASE =
            "SELECT d.id_docente, d.nombre, d.apellido, u.id_usuario, u.email "
            + "FROM docentes AS d LEFT JOIN usuarios AS u ON u.id_docente = d.id_docente ";

    public List<Docente> findAll() throws SQLException {
        String sql = SELECT_BASE + "ORDER BY d.apellido, d.nombre";
        List<Docente> resultado = new ArrayList<>();

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                resultado.add(mapear(rs));
            }
        }
        return resultado;
    }

    /** Crea un docente sin cuenta de acceso (solo para asignarle materias/horarios). */
    public int insertarSoloDocente(String nombre, String apellido) throws SQLException {
        String sql = "INSERT INTO docentes (nombre, apellido) VALUES (?, ?)";
        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase()
                .prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, nombre);
            pstm.setString(2, apellido);
            pstm.executeUpdate();
            try (ResultSet keys = pstm.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        }
    }

    public void actualizarDatosPersonales(int idDocente, String nombre, String apellido) throws SQLException {
        String sql = "UPDATE docentes SET nombre = ?, apellido = ? WHERE id_docente = ?";
        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setString(1, nombre);
            pstm.setString(2, apellido);
            pstm.setInt(3, idDocente);
            pstm.executeUpdate();
        }
    }

    public void actualizarEmail(int idUsuario, String email) throws SQLException {
        String sql = "UPDATE usuarios SET email = ? WHERE id_usuario = ?";
        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setString(1, email);
            pstm.setInt(2, idUsuario);
            pstm.executeUpdate();
        }
    }

    /** Elimina al docente (CASCADE elimina también su cuenta y sus horarios asignados). */
    public void eliminar(int idDocente) throws SQLException {
        String sql = "DELETE FROM docentes WHERE id_docente = ?";
        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setInt(1, idDocente);
            pstm.executeUpdate();
        }
    }

    private Docente mapear(ResultSet rs) throws SQLException {
        Docente docente = new Docente();
        docente.setIdDocente(rs.getInt("id_docente"));
        docente.setNombre(rs.getString("nombre"));
        docente.setApellido(rs.getString("apellido"));
        docente.setEmail(rs.getString("email"));
        int idUsuario = rs.getInt("id_usuario");
        docente.setIdUsuario(rs.wasNull() ? null : idUsuario);
        return docente;
    }
}
