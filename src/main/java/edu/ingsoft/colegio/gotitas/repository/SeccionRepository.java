package main.java.edu.ingsoft.colegio.gotitas.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.java.edu.ingsoft.colegio.gotitas.config.DataBaseConnection;
import main.java.edu.ingsoft.colegio.gotitas.model.Seccion;

/** Acceso a datos de la tabla secciones. */
public class SeccionRepository {

    public List<Seccion> findAll() throws SQLException {
        String sql = "SELECT s.id_seccion, s.nombre, s.id_curso, c.nombre AS nombre_curso "
                + "FROM secciones AS s JOIN cursos AS c ON c.id_curso = s.id_curso "
                + "ORDER BY c.nombre, s.nombre";
        List<Seccion> resultado = new ArrayList<>();

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                resultado.add(new Seccion(
                        rs.getInt("id_seccion"),
                        rs.getString("nombre"),
                        rs.getInt("id_curso"),
                        rs.getString("nombre_curso")
                ));
            }
        }
        return resultado;
    }

    public void insertar(String nombre, int idCurso) throws SQLException {
        String sql = "INSERT INTO secciones (nombre, id_curso) VALUES (?, ?)";
        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setString(1, nombre);
            pstm.setInt(2, idCurso);
            pstm.executeUpdate();
        }
    }

    public void eliminar(int idSeccion) throws SQLException {
        String sql = "DELETE FROM secciones WHERE id_seccion = ?";
        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setInt(1, idSeccion);
            pstm.executeUpdate();
        }
    }
}
