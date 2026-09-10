package main.java.edu.ingsoft.colegio.gotitas.service;

import java.sql.SQLException;
import java.util.List;
import main.java.edu.ingsoft.colegio.gotitas.model.estudiante.Alumno;
import main.java.edu.ingsoft.colegio.gotitas.model.Horario;
import main.java.edu.ingsoft.colegio.gotitas.repository.AlumnoRepository;
import main.java.edu.ingsoft.colegio.gotitas.repository.HorarioRepository;

/**
 * Lógica de negocio para asignar cursos, secciones, materias y docentes a
 * un horario, y para consultar el horario resultante desde distintas vistas.
 */
public class HorarioService {

    private final HorarioRepository horarioRepository;
    private final AlumnoRepository alumnoRepository;

    public HorarioService(HorarioRepository horarioRepository, AlumnoRepository alumnoRepository) {
        this.horarioRepository = horarioRepository;
        this.alumnoRepository = alumnoRepository;
    }

    /** Vista: tabla general con sección + horario + materia (docente). */
    public List<Horario> listarTodos() throws SQLException {
        return horarioRepository.findAll();
    }

    /** Vista: horario diario/semanal de un docente específico. */
    public List<Horario> listarPorDocente(int idDocente) throws SQLException {
        return horarioRepository.findByDocente(idDocente);
    }

    /** Vista: lista de alumnos que le corresponden a un docente (según las secciones donde da clase). */
    public List<Alumno> listarAlumnosDeDocente(int idDocente) throws SQLException {
        List<Integer> secciones = horarioRepository.findSeccionesDistintasPorDocente(idDocente);
        return alumnoRepository.findBySecciones(secciones);
    }

    public void asignar(Horario horario) throws SQLException {
        validar(horario);
        try {
            horarioRepository.asignar(horario);
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("duplicate")) {
                throw new IllegalStateException(
                        "Ya existe una clase asignada en ese día y periodo, ya sea para la sección o para el docente. "
                        + "Verifica que no haya un choque de horario.");
            }
            throw e;
        }
    }

    public void eliminar(int idHorario) throws SQLException {
        horarioRepository.eliminar(idHorario);
    }

    private void validar(Horario horario) {
        if (horario == null || horario.getIdSeccion() <= 0 || horario.getIdPeriodo() <= 0
                || horario.getIdMateria() <= 0 || horario.getIdDocente() <= 0
                || horario.getDiaSemana() == null || horario.getDiaSemana().isBlank()) {
            throw new IllegalArgumentException("Debes seleccionar sección, día, periodo, materia y docente.");
        }
    }
}
