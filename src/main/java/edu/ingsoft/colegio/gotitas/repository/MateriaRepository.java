package main.java.edu.ingsoft.colegio.gotitas.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.java.edu.ingsoft.colegio.gotitas.config.DataBaseConnection;
import main.java.edu.ingsoft.colegio.gotitas.model.cursos.Materia;

/** Acceso a datos de la tabla materias. */
public class MateriaRepository {

    public List<Materia> findAll() throws SQLException {
        String sql = "SELECT id_materia, nombre FROM materias ORDER BY nombre";
        List<Materia> resultado = new ArrayList<>();

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                resultado.add(new Materia(rs.getInt("id_materia"), rs.getString("nombre")));
            }
        }
        return resultado;
    }

    public void insertar(String nombre) throws SQLException {
        String sql = "INSERT INTO materias (nombre) VALUES (?)";
        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setString(1, nombre);
            pstm.executeUpdate();
        }
    }

    public void eliminar(int idMateria) throws SQLException {
        String sql = "DELETE FROM materias WHERE id_materia = ?";
        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setInt(1, idMateria);
            pstm.executeUpdate();
        }
    }
}
