package main.java.edu.ingsoft.colegio.gotitas.service;

import java.sql.SQLException;
import java.util.List;
import main.java.edu.ingsoft.colegio.gotitas.model.Alumno;
import main.java.edu.ingsoft.colegio.gotitas.repository.AlumnoRepository;

/** Lógica de negocio para la administración de alumnos (crear, editar, eliminar). */
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;

    public AlumnoService(AlumnoRepository alumnoRepository) {
        this.alumnoRepository = alumnoRepository;
    }

    public List<Alumno> listar() throws SQLException {
        return alumnoRepository.findAll();
    }

    public List<Alumno> listarPorSecciones(List<Integer> idsSeccion) throws SQLException {
        return alumnoRepository.findBySecciones(idsSeccion);
    }

    public void crear(Alumno alumno) throws SQLException {
        validar(alumno);
        alumnoRepository.insertar(alumno);
    }

    public void actualizar(Alumno alumno) throws SQLException {
        validar(alumno);
        if (alumno.getIdAlumno() <= 0) {
            throw new IllegalArgumentException("Selecciona un alumno de la tabla para poder editarlo.");
        }
        alumnoRepository.actualizar(alumno);
    }

    public void eliminar(int idAlumno) throws SQLException {
        alumnoRepository.eliminar(idAlumno);
    }

    private void validar(Alumno alumno) {
        if (alumno == null || alumno.getNombre() == null || alumno.getNombre().isBlank()
                || alumno.getApellido() == null || alumno.getApellido().isBlank()) {
            throw new IllegalArgumentException("El nombre y el apellido del alumno son obligatorios.");
        }
    }
}
