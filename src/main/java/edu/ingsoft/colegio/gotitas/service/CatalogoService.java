package main.java.edu.ingsoft.colegio.gotitas.service;

import java.sql.SQLException;
import java.util.List;
import main.java.edu.ingsoft.colegio.gotitas.model.cursos.Curso;
import main.java.edu.ingsoft.colegio.gotitas.model.cursos.Materia;
import main.java.edu.ingsoft.colegio.gotitas.model.cursos.Periodo;
import main.java.edu.ingsoft.colegio.gotitas.model.cursos.Seccion;
import main.java.edu.ingsoft.colegio.gotitas.repository.CursoRepository;
import main.java.edu.ingsoft.colegio.gotitas.repository.MateriaRepository;
import main.java.edu.ingsoft.colegio.gotitas.repository.PeriodoRepository;
import main.java.edu.ingsoft.colegio.gotitas.repository.SeccionRepository;

/**
 * Lógica de negocio para los catálogos base del colegio:
 * cursos, secciones, materias y periodos de horario.
 */
public class CatalogoService {

    private final CursoRepository cursoRepository;
    private final SeccionRepository seccionRepository;
    private final MateriaRepository materiaRepository;
    private final PeriodoRepository periodoRepository;

    public CatalogoService(CursoRepository cursoRepository, SeccionRepository seccionRepository,
            MateriaRepository materiaRepository, PeriodoRepository periodoRepository) {
        this.cursoRepository = cursoRepository;
        this.seccionRepository = seccionRepository;
        this.materiaRepository = materiaRepository;
        this.periodoRepository = periodoRepository;
    }

    public List<Curso> listarCursos() throws SQLException {
        return cursoRepository.findAll();
    }

    public void crearCurso(String nombre) throws SQLException {
        validarTexto(nombre, "El nombre del curso");
        cursoRepository.insertar(nombre.trim());
    }

    public void eliminarCurso(int idCurso) throws SQLException {
        cursoRepository.eliminar(idCurso);
    }

    public List<Seccion> listarSecciones() throws SQLException {
        return seccionRepository.findAll();
    }

    public void crearSeccion(String nombre, int idCurso) throws SQLException {
        validarTexto(nombre, "El nombre de la sección");
        if (idCurso <= 0) {
            throw new IllegalArgumentException("Debes seleccionar un curso.");
        }
        seccionRepository.insertar(nombre.trim().toUpperCase(), idCurso);
    }

    public void eliminarSeccion(int idSeccion) throws SQLException {
        seccionRepository.eliminar(idSeccion);
    }

    public List<Materia> listarMaterias() throws SQLException {
        return materiaRepository.findAll();
    }

    public void crearMateria(String nombre) throws SQLException {
        validarTexto(nombre, "El nombre de la materia");
        materiaRepository.insertar(nombre.trim());
    }

    public void eliminarMateria(int idMateria) throws SQLException {
        materiaRepository.eliminar(idMateria);
    }

    public List<Periodo> listarPeriodos() throws SQLException {
        return periodoRepository.findAll();
    }

    private void validarTexto(String valor, String etiqueta) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(etiqueta + " no puede estar vacío.");
        }
    }
}
