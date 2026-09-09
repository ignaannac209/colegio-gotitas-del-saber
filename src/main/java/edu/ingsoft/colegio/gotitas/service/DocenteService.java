package main.java.edu.ingsoft.colegio.gotitas.service;

import java.sql.SQLException;
import java.util.List;
import main.java.edu.ingsoft.colegio.gotitas.model.Docente;
import main.java.edu.ingsoft.colegio.gotitas.repository.DocenteRepository;

/** Lógica de negocio para la administración de docentes (crear, editar, eliminar). */
public class DocenteService {

    private final DocenteRepository docenteRepository;

    public DocenteService(DocenteRepository docenteRepository) {
        this.docenteRepository = docenteRepository;
    }

    public List<Docente> listar() throws SQLException {
        return docenteRepository.findAll();
    }

    /** Crea un docente nuevo. La cuenta de acceso (email/contraseña) se gestiona en la vista de Registro. */
    public void crear(String nombre, String apellido) throws SQLException {
        validarNombre(nombre, apellido);
        docenteRepository.insertarSoloDocente(nombre.trim(), apellido.trim());
    }

    public void actualizar(Docente docente) throws SQLException {
        validarNombre(docente.getNombre(), docente.getApellido());
        if (docente.getIdDocente() <= 0) {
            throw new IllegalArgumentException("Selecciona un docente de la tabla para poder editarlo.");
        }
        docenteRepository.actualizarDatosPersonales(docente.getIdDocente(), docente.getNombre().trim(), docente.getApellido().trim());

        if (docente.getIdUsuario() != null && docente.getEmail() != null && !docente.getEmail().isBlank()) {
            docenteRepository.actualizarEmail(docente.getIdUsuario(), docente.getEmail().trim());
        }
    }

    public void eliminar(int idDocente) throws SQLException {
        docenteRepository.eliminar(idDocente);
    }

    private void validarNombre(String nombre, String apellido) {
        if (nombre == null || nombre.isBlank() || apellido == null || apellido.isBlank()) {
            throw new IllegalArgumentException("El nombre y el apellido del docente son obligatorios.");
        }
    }
}
