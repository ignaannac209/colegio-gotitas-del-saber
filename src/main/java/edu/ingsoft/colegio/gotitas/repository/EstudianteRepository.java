package main.java.edu.ingsoft.colegio.gotitas.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import main.java.edu.ingsoft.colegio.gotitas.config.DataBaseConnection;
import main.java.edu.ingsoft.colegio.gotitas.model.Carne;

public class EstudianteRepository {

    public EstudianteRepository() {
    }

    public Carne guardar(Carne carne) throws SQLException {
        String sql = "INSERT INTO estudiantes (carne, nombre, apellido) VALUES (?, ?, ?)";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, carne.getCarne());
            pstm.setString(2, carne.getNombre());
            pstm.setString(3, carne.getApellido());
            pstm.executeUpdate();

            try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
                if (!generatedKeys.next()) {
                    throw new SQLException("No se pudo obtener el id del estudiante recién creado");
                }
                carne.setId(generatedKeys.getLong(1));
            }
        }

        return carne;
    }

    public Carne buscarPorId(long id) throws SQLException {
        String sql = "SELECT id_estudiante, carne, nombre, apellido FROM estudiantes WHERE id_estudiante = ?";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setLong(1, id);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return mapearCarne(rs);
                }
            }
        }

        return null;
    }

    public Carne buscarPorCarne(String numeroCarne) throws SQLException {
        String sql = "SELECT id_estudiante, carne, nombre, apellido FROM estudiantes WHERE carne = ?";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setString(1, numeroCarne);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return mapearCarne(rs);
                }
            }
        }

        return null;
    }

    public List<Carne> listarTodos() throws SQLException {
        String sql = "SELECT id_estudiante, carne, nombre, apellido FROM estudiantes ORDER BY apellido, nombre";

        List<Carne> estudiantes = new ArrayList<>();

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql);
                ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                estudiantes.add(mapearCarne(rs));
            }
        }

        return estudiantes;
    }

    public void actualizar(Carne carne) throws SQLException {
        String sql = "UPDATE estudiantes SET carne = ?, nombre = ?, apellido = ? WHERE id_estudiante = ?";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setString(1, carne.getCarne());
            pstm.setString(2, carne.getNombre());
            pstm.setString(3, carne.getApellido());
            pstm.setLong(4, carne.getId());
            pstm.executeUpdate();
        }
    }

    public void eliminar(long id) throws SQLException {
        String sql = "DELETE FROM estudiantes WHERE id_estudiante = ?";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setLong(1, id);
            pstm.executeUpdate();
        }
    }

    private Carne mapearCarne(ResultSet rs) throws SQLException {
        return new Carne(
                rs.getLong("id_estudiante"),
                rs.getString("carne"),
                rs.getString("nombre"),
                rs.getString("apellido")
        );
    }
}
