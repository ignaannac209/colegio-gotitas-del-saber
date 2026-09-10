package main.java.edu.ingsoft.colegio.gotitas.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import main.java.edu.ingsoft.colegio.gotitas.model.estudiante.Alumno;
import main.java.edu.ingsoft.colegio.gotitas.model.docente.Docente;
import main.java.edu.ingsoft.colegio.gotitas.model.Seccion;
import main.java.edu.ingsoft.colegio.gotitas.service.AlumnoService;
import main.java.edu.ingsoft.colegio.gotitas.service.CatalogoService;
import main.java.edu.ingsoft.colegio.gotitas.service.DocenteService;

/**
 * Controlador de la vista de administración de Docentes y Alumnos:
 * un formulario para crear/editar y una tabla con los registros existentes.
 */
public class AdminPersonasController implements Initializable {

    private final DocenteService docenteService;
    private final AlumnoService alumnoService;
    private final CatalogoService catalogoService;

    // --- Docentes ---
    @FXML private TextField txtDocenteNombre;
    @FXML private TextField txtDocenteApellido;
    @FXML private TextField txtDocenteEmail;
    @FXML private Label lblMensajeDocente;
    @FXML private TableView<Docente> tablaDocentes;
    @FXML private TableColumn<Docente, String> colDocenteNombre;
    @FXML private TableColumn<Docente, String> colDocenteApellido;
    @FXML private TableColumn<Docente, String> colDocenteEmail;

    // --- Alumnos ---
    @FXML private TextField txtAlumnoNombre;
    @FXML private TextField txtAlumnoApellido;
    @FXML private ComboBox<Seccion> comboAlumnoSeccion;
    @FXML private Label lblMensajeAlumno;
    @FXML private TableView<Alumno> tablaAlumnos;
    @FXML private TableColumn<Alumno, String> colAlumnoNombre;
    @FXML private TableColumn<Alumno, String> colAlumnoApellido;
    @FXML private TableColumn<Alumno, String> colAlumnoSeccion;

    private Docente docenteSeleccionado;
    private Alumno alumnoSeleccionado;

    public AdminPersonasController(DocenteService docenteService, AlumnoService alumnoService, CatalogoService catalogoService) {
        this.docenteService = docenteService;
        this.alumnoService = alumnoService;
        this.catalogoService = catalogoService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarColumnasDocentes();
        configurarColumnasAlumnos();

        tablaDocentes.getSelectionModel().selectedItemProperty().addListener((obs, viejo, nuevo) -> cargarDocenteEnFormulario(nuevo));
        tablaAlumnos.getSelectionModel().selectedItemProperty().addListener((obs, viejo, nuevo) -> cargarAlumnoEnFormulario(nuevo));

        cargarSecciones();
        recargarDocentes();
        recargarAlumnos();
    }

    // ------------------------------------------------------------------
    // Docentes
    // ------------------------------------------------------------------

    @FXML
    private void handleGuardarDocente(ActionEvent event) {
        try {
            if (docenteSeleccionado != null) {
                Docente docente = new Docente();
                docente.setIdDocente(docenteSeleccionado.getIdDocente());
                docente.setNombre(txtDocenteNombre.getText());
                docente.setApellido(txtDocenteApellido.getText());
                docente.setIdUsuario(docenteSeleccionado.getIdUsuario());
                docente.setEmail(txtDocenteEmail.getText());
                docenteService.actualizar(docente);
                mostrarMensaje(lblMensajeDocente, "Docente actualizado correctamente.", false);
            } else {
                docenteService.crear(txtDocenteNombre.getText(), txtDocenteApellido.getText());
                mostrarMensaje(lblMensajeDocente, "Docente creado. Puede crear su cuenta de acceso desde 'Crear cuenta' en el login.", false);
            }
            handleNuevoDocente(null);
            recargarDocentes();
        } catch (Exception e) {
            mostrarMensaje(lblMensajeDocente, mensajeDe(e), true);
        }
    }

    @FXML
    private void handleNuevoDocente(ActionEvent event) {
        docenteSeleccionado = null;
        tablaDocentes.getSelectionModel().clearSelection();
        txtDocenteNombre.clear();
        txtDocenteApellido.clear();
        txtDocenteEmail.clear();
        txtDocenteEmail.setDisable(true);
        mostrarMensaje(lblMensajeDocente, "", false);
    }

    @FXML
    private void handleEliminarDocente(ActionEvent event) {
        if (docenteSeleccionado == null) {
            mostrarMensaje(lblMensajeDocente, "Selecciona un docente de la tabla primero.", true);
            return;
        }
        if (!confirmar("¿Eliminar a " + docenteSeleccionado.getNombreCompleto() + "? También se eliminará su cuenta y sus horarios asignados.")) {
            return;
        }
        try {
            docenteService.eliminar(docenteSeleccionado.getIdDocente());
            handleNuevoDocente(null);
            recargarDocentes();
        } catch (Exception e) {
            mostrarMensaje(lblMensajeDocente, mensajeDe(e), true);
        }
    }

    private void cargarDocenteEnFormulario(Docente docente) {
        docenteSeleccionado = docente;
        if (docente == null) {
            return;
        }
        txtDocenteNombre.setText(docente.getNombre());
        txtDocenteApellido.setText(docente.getApellido());
        txtDocenteEmail.setText(docente.getEmail());
        txtDocenteEmail.setDisable(docente.getIdUsuario() == null);
    }

    private void configurarColumnasDocentes() {
        colDocenteNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombre()));
        colDocenteApellido.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getApellido()));
        colDocenteEmail.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getEmail() != null ? d.getValue().getEmail() : "Sin cuenta de acceso"));
    }

    private void recargarDocentes() {
        try {
            List<Docente> docentes = docenteService.listar();
            tablaDocentes.setItems(FXCollections.observableArrayList(docentes));
        } catch (Exception e) {
            mostrarMensaje(lblMensajeDocente, mensajeDe(e), true);
        }
    }

    // ------------------------------------------------------------------
    // Alumnos
    // ------------------------------------------------------------------

    @FXML
    private void handleGuardarAlumno(ActionEvent event) {
        try {
            Alumno alumno = new Alumno();
            alumno.setNombre(txtAlumnoNombre.getText());
            alumno.setApellido(txtAlumnoApellido.getText());
            Seccion seccion = comboAlumnoSeccion.getValue();
            alumno.setIdSeccion(seccion != null ? seccion.getIdSeccion() : null);

            if (alumnoSeleccionado != null) {
                alumno.setIdAlumno(alumnoSeleccionado.getIdAlumno());
                alumnoService.actualizar(alumno);
                mostrarMensaje(lblMensajeAlumno, "Alumno actualizado correctamente.", false);
            } else {
                alumnoService.crear(alumno);
                mostrarMensaje(lblMensajeAlumno, "Alumno agregado correctamente.", false);
            }
            handleNuevoAlumno(null);
            recargarAlumnos();
        } catch (Exception e) {
            mostrarMensaje(lblMensajeAlumno, mensajeDe(e), true);
        }
    }

    @FXML
    private void handleNuevoAlumno(ActionEvent event) {
        alumnoSeleccionado = null;
        tablaAlumnos.getSelectionModel().clearSelection();
        txtAlumnoNombre.clear();
        txtAlumnoApellido.clear();
        comboAlumnoSeccion.setValue(null);
        mostrarMensaje(lblMensajeAlumno, "", false);
    }

    @FXML
    private void handleEliminarAlumno(ActionEvent event) {
        if (alumnoSeleccionado == null) {
            mostrarMensaje(lblMensajeAlumno, "Selecciona un alumno de la tabla primero.", true);
            return;
        }
        if (!confirmar("¿Eliminar a " + alumnoSeleccionado.getNombreCompleto() + "?")) {
            return;
        }
        try {
            alumnoService.eliminar(alumnoSeleccionado.getIdAlumno());
            handleNuevoAlumno(null);
            recargarAlumnos();
        } catch (Exception e) {
            mostrarMensaje(lblMensajeAlumno, mensajeDe(e), true);
        }
    }

    private void cargarAlumnoEnFormulario(Alumno alumno) {
        alumnoSeleccionado = alumno;
        if (alumno == null) {
            return;
        }
        txtAlumnoNombre.setText(alumno.getNombre());
        txtAlumnoApellido.setText(alumno.getApellido());
        for (Seccion seccion : comboAlumnoSeccion.getItems()) {
            if (alumno.getIdSeccion() != null && seccion.getIdSeccion() == alumno.getIdSeccion()) {
                comboAlumnoSeccion.setValue(seccion);
                return;
            }
        }
        comboAlumnoSeccion.setValue(null);
    }

    private void configurarColumnasAlumnos() {
        colAlumnoNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombre()));
        colAlumnoApellido.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getApellido()));
        colAlumnoSeccion.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getSeccionMostrable()));
    }

    private void recargarAlumnos() {
        try {
            List<Alumno> alumnos = alumnoService.listar();
            tablaAlumnos.setItems(FXCollections.observableArrayList(alumnos));
        } catch (Exception e) {
            mostrarMensaje(lblMensajeAlumno, mensajeDe(e), true);
        }
    }

    private void cargarSecciones() {
        try {
            List<Seccion> secciones = catalogoService.listarSecciones();
            ObservableList<Seccion> items = FXCollections.observableArrayList(secciones);
            comboAlumnoSeccion.setItems(items);
        } catch (Exception e) {
            mostrarMensaje(lblMensajeAlumno, mensajeDe(e), true);
        }
    }

    // ------------------------------------------------------------------
    // Utilidades
    // ------------------------------------------------------------------

    private boolean confirmar(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, mensaje);
        alert.setHeaderText(null);
        alert.setTitle("Confirmar");
        return alert.showAndWait().filter(b -> b.getButtonData().isDefaultButton()).isPresent();
    }

    private void mostrarMensaje(Label label, String mensaje, boolean esError) {
        label.setText(mensaje);
        label.getStyleClass().removeAll("mensaje-error", "mensaje-info");
        label.getStyleClass().add(esError ? "mensaje-error" : "mensaje-info");
    }

    private String mensajeDe(Exception e) {
        return e.getMessage() != null ? e.getMessage() : "Ocurrió un error inesperado.";
    }
}
