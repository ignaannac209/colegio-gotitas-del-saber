package main.java.edu.ingsoft.colegio.gotitas.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.java.edu.ingsoft.colegio.gotitas.model.Alumno;
import main.java.edu.ingsoft.colegio.gotitas.model.Docente;
import main.java.edu.ingsoft.colegio.gotitas.service.DocenteService;
import main.java.edu.ingsoft.colegio.gotitas.service.HorarioService;

/**
 * Controlador de la vista "Profesores y Alumnos": al seleccionar un
 * profesor en la tabla izquierda, muestra a la derecha los alumnos de
 * todas las secciones en las que ese profesor da clase.
 */
public class AdminDocentesAlumnosController implements Initializable {

    private final DocenteService docenteService;
    private final HorarioService horarioService;

    @FXML private TableView<Docente> tablaDocentes;
    @FXML private TableColumn<Docente, String> colDocenteNombre;
    @FXML private TableColumn<Docente, String> colDocenteApellido;

    @FXML private Label lblTituloAlumnos;
    @FXML private TableView<Alumno> tablaAlumnos;
    @FXML private TableColumn<Alumno, String> colAlumnoNombre;
    @FXML private TableColumn<Alumno, String> colAlumnoApellido;
    @FXML private TableColumn<Alumno, String> colAlumnoSeccion;

    public AdminDocentesAlumnosController(DocenteService docenteService, HorarioService horarioService) {
        this.docenteService = docenteService;
        this.horarioService = horarioService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colDocenteNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombre()));
        colDocenteApellido.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getApellido()));

        colAlumnoNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombre()));
        colAlumnoApellido.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getApellido()));
        colAlumnoSeccion.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getSeccionMostrable()));

        tablaDocentes.getSelectionModel().selectedItemProperty().addListener((obs, viejo, nuevo) -> mostrarAlumnosDe(nuevo));

        cargarDocentes();
    }

    private void cargarDocentes() {
        try {
            List<Docente> docentes = docenteService.listar();
            tablaDocentes.setItems(FXCollections.observableArrayList(docentes));
        } catch (Exception e) {
            lblTituloAlumnos.setText(mensajeDe(e));
        }
    }

    private void mostrarAlumnosDe(Docente docente) {
        if (docente == null) {
            tablaAlumnos.setItems(FXCollections.observableArrayList());
            lblTituloAlumnos.setText("Alumnos");
            return;
        }
        try {
            List<Alumno> alumnos = horarioService.listarAlumnosDeDocente(docente.getIdDocente());
            tablaAlumnos.setItems(FXCollections.observableArrayList(alumnos));
            lblTituloAlumnos.setText("Alumnos de " + docente.getNombreCompleto() + " (" + alumnos.size() + ")");
        } catch (Exception e) {
            lblTituloAlumnos.setText(mensajeDe(e));
        }
    }

    private String mensajeDe(Exception e) {
        return e.getMessage() != null ? e.getMessage() : "Ocurrió un error inesperado.";
    }
}
