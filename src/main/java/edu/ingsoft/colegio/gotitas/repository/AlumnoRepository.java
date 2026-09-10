package main.java.edu.ingsoft.colegio.gotitas.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.java.edu.ingsoft.colegio.gotitas.config.DataBaseConnection;
import main.java.edu.ingsoft.colegio.gotitas.model.estudiante.Alumno;

/** Acceso a datos de la tabla alumnos. */
public class AlumnoRepository {

    private static final String SELECT_BASE =
            "SELECT a.id_alumno, a.nombre, a.apellido, a.id_seccion, "
            + "s.nombre AS nombre_seccion, c.nombre AS nombre_curso "
            + "FROM alumnos AS a "
            + "LEFT JOIN secciones AS s ON s.id_seccion = a.id_seccion "
            + "LEFT JOIN cursos AS c ON c.id_curso = s.id_curso ";

    public List<Alumno> findAll() throws SQLException {
        String sql = SELECT_BASE + "ORDER BY a.apellido, a.nombre";
        List<Alumno> resultado = new ArrayList<>();

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                resultado.add(mapear(rs));
            }
        }
        return resultado;
    }

    /** Alumnos que pertenecen a cualquiera de las secciones indicadas (usado para "docente y sus alumnos"). */
    public List<Alumno> findBySecciones(List<Integer> idsSeccion) throws SQLException {
        List<Alumno> resultado = new ArrayList<>();
        if (idsSeccion == null || idsSeccion.isEmpty()) {
            return resultado;
        }
        String placeholders = String.join(",", idsSeccion.stream().map(id -> "?").toArray(String[]::new));
        String sql = SELECT_BASE + "WHERE a.id_seccion IN (" + placeholders + ") ORDER BY a.apellido, a.nombre";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            int i = 1;
            for (Integer id : idsSeccion) {
                pstm.setInt(i++, id);
            }
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapear(rs));
                }
            }
        }
        return resultado;
    }

    public void insertar(Alumno alumno) throws SQLException {
        String sql = "INSERT INTO alumnos (nombre, apellido, id_seccion) VALUES (?, ?, ?)";
        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setString(1, alumno.getNombre());
            pstm.setString(2, alumno.getApellido());
            setNullableInt(pstm, 3, alumno.getIdSeccion());
            pstm.executeUpdate();
        }
    }

    public void actualizar(Alumno alumno) throws SQLException {
        String sql = "UPDATE alumnos SET nombre = ?, apellido = ?, id_seccion = ? WHERE id_alumno = ?";
        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setString(1, alumno.getNombre());
            pstm.setString(2, alumno.getApellido());
            setNullableInt(pstm, 3, alumno.getIdSeccion());
            pstm.setInt(4, alumno.getIdAlumno());
            pstm.executeUpdate();
        }
    }

    public void eliminar(int idAlumno) throws SQLException {
        String sql = "DELETE FROM alumnos WHERE id_alumno = ?";
        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setInt(1, idAlumno);
            pstm.executeUpdate();
        }
    }

    private Alumno mapear(ResultSet rs) throws SQLException {
        Alumno alumno = new Alumno();
        alumno.setIdAlumno(rs.getInt("id_alumno"));
        alumno.setNombre(rs.getString("nombre"));
        alumno.setApellido(rs.getString("apellido"));
        int idSeccion = rs.getInt("id_seccion");
        alumno.setIdSeccion(rs.wasNull() ? null : idSeccion);
        alumno.setNombreSeccion(rs.getString("nombre_seccion"));
        alumno.setNombreCurso(rs.getString("nombre_curso"));
        return alumno;
    }

    private void setNullableInt(PreparedStatement pstm, int index, Integer valor) throws SQLException {
        if (valor == null) {
            pstm.setNull(index, java.sql.Types.INTEGER);
        } else {
            pstm.setInt(index, valor);
        }
    }
}
