package main.java.edu.ingsoft.colegio.gotitas.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.java.edu.ingsoft.colegio.gotitas.config.DataBaseConnection;
import main.java.edu.ingsoft.colegio.gotitas.model.cursos.Horario;

/**
 * Acceso a datos de la tabla horarios: asigna, por sección/día/periodo,
 * qué docente imparte qué materia.
 */
public class HorarioRepository {

    private static final String SELECT_BASE =
            "SELECT h.id_horario, h.dia_semana, "
            + "h.id_seccion, CONCAT(c.nombre, ' \"', s.nombre, '\"') AS nombre_seccion, "
            + "h.id_periodo, p.orden AS orden_periodo, p.hora_inicio, p.hora_fin, "
            + "h.id_materia, m.nombre AS nombre_materia, "
            + "h.id_docente, CONCAT(d.nombre, ' ', d.apellido) AS nombre_docente "
            + "FROM horarios AS h "
            + "JOIN secciones AS s ON s.id_seccion = h.id_seccion "
            + "JOIN cursos AS c ON c.id_curso = s.id_curso "
            + "JOIN periodos AS p ON p.id_periodo = h.id_periodo "
            + "JOIN materias AS m ON m.id_materia = h.id_materia "
            + "JOIN docentes AS d ON d.id_docente = h.id_docente ";

    /** Todos los horarios, ordenados por sección, día y periodo (vista de tabla general). */
    public List<Horario> findAll() throws SQLException {
        String sql = SELECT_BASE + "ORDER BY nombre_seccion, FIELD(h.dia_semana,'LUNES','MARTES','MIERCOLES','JUEVES','VIERNES'), p.orden";
        return ejecutarConsulta(sql, null);
    }

    /** Horario semanal completo de un docente (vista "horario diario de profes"). */
    public List<Horario> findByDocente(int idDocente) throws SQLException {
        String sql = SELECT_BASE + "WHERE h.id_docente = ? "
                + "ORDER BY FIELD(h.dia_semana,'LUNES','MARTES','MIERCOLES','JUEVES','VIERNES'), p.orden";
        return ejecutarConsulta(sql, idDocente);
    }

    /** Ids de las secciones distintas en las que da clase un docente (para hallar sus alumnos). */
    public List<Integer> findSeccionesDistintasPorDocente(int idDocente) throws SQLException {
        String sql = "SELECT DISTINCT id_seccion FROM horarios WHERE id_docente = ?";
        List<Integer> resultado = new ArrayList<>();
        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setInt(1, idDocente);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    resultado.add(rs.getInt("id_seccion"));
                }
            }
        }
        return resultado;
    }

    /**
     * Crea una nueva asignación de horario (sección + día + periodo -> materia + docente).
     * Lanza SQLException con "Duplicate entry" si ya existe choque de horario
     * (gracias a las restricciones UNIQUE de la tabla).
     */
    public void asignar(Horario horario) throws SQLException {
        String sql = "INSERT INTO horarios (id_seccion, id_periodo, id_materia, id_docente, dia_semana) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setInt(1, horario.getIdSeccion());
            pstm.setInt(2, horario.getIdPeriodo());
            pstm.setInt(3, horario.getIdMateria());
            pstm.setInt(4, horario.getIdDocente());
            pstm.setString(5, horario.getDiaSemana());
            pstm.executeUpdate();
        }
    }

    public void eliminar(int idHorario) throws SQLException {
        String sql = "DELETE FROM horarios WHERE id_horario = ?";
        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setInt(1, idHorario);
            pstm.executeUpdate();
        }
    }

    private List<Horario> ejecutarConsulta(String sql, Integer idDocente) throws SQLException {
        List<Horario> resultado = new ArrayList<>();
        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            if (idDocente != null) {
                pstm.setInt(1, idDocente);
            }
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapear(rs));
                }
            }
        }
        return resultado;
    }

    private Horario mapear(ResultSet rs) throws SQLException {
        Horario horario = new Horario();
        horario.setIdHorario(rs.getInt("id_horario"));
        horario.setDiaSemana(rs.getString("dia_semana"));
        horario.setIdSeccion(rs.getInt("id_seccion"));
        horario.setNombreSeccion(rs.getString("nombre_seccion"));
        horario.setIdPeriodo(rs.getInt("id_periodo"));
        horario.setOrdenPeriodo(rs.getInt("orden_periodo"));
        String horaInicio = rs.getTime("hora_inicio").toLocalTime()
                .format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a"));
        String horaFin = rs.getTime("hora_fin").toLocalTime()
                .format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a"));
        horario.setRangoPeriodo(horaInicio + " - " + horaFin);
        horario.setIdMateria(rs.getInt("id_materia"));
        horario.setNombreMateria(rs.getString("nombre_materia"));
        horario.setIdDocente(rs.getInt("id_docente"));
        horario.setNombreDocente(rs.getString("nombre_docente"));
        return horario;
    }
}
