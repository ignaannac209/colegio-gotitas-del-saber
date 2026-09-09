package main.java.edu.ingsoft.colegio.gotitas.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import main.java.edu.ingsoft.colegio.gotitas.config.DataBaseConnection;
import main.java.edu.ingsoft.colegio.gotitas.model.Curso;

/** Acceso a datos de la tabla cursos. */
public class CursoRepository {

    public List<Curso> findAll() throws SQLException {
        String sql = "SELECT id_curso, nombre FROM cursos ORDER BY nombre";
        List<Curso> resultado = new ArrayList<>();

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                resultado.add(new Curso(rs.getInt("id_curso"), rs.getString("nombre")));
            }
        }
        return resultado;
    }

    public void insertar(String nombre) throws SQLException {
        String sql = "INSERT INTO cursos (nombre) VALUES (?)";
        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, nombre);
            pstm.executeUpdate();
        }
    }

    public void eliminar(int idCurso) throws SQLException {
        String sql = "DELETE FROM cursos WHERE id_curso = ?";
        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setInt(1, idCurso);
            pstm.executeUpdate();
        }
    }
}
