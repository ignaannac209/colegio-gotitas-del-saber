package main.java.edu.ingsoft.colegio.gotitas.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import main.java.edu.ingsoft.colegio.gotitas.model.cursos.Curso;
import main.java.edu.ingsoft.colegio.gotitas.model.cursos.Materia;
import main.java.edu.ingsoft.colegio.gotitas.model.cursos.Seccion;
import main.java.edu.ingsoft.colegio.gotitas.service.CatalogoService;

/**
 * Controlador de la vista de catálogos: cursos, secciones y materias.
 * Estos catálogos alimentan los combos de las demás vistas de administración.
 */
public class AdminCatalogosController implements Initializable {

    private final CatalogoService catalogoService;

    @FXML private TextField txtCursoNombre;
    @FXML private Label lblMensajeCurso;
    @FXML private TableView<Curso> tablaCursos;
    @FXML private TableColumn<Curso, String> colCursoNombre;

    @FXML private TextField txtSeccionNombre;
    @FXML private ComboBox<Curso> comboSeccionCurso;
    @FXML private Label lblMensajeSeccion;
    @FXML private TableView<Seccion> tablaSecciones;
    @FXML private TableColumn<Seccion, String> colSeccionEtiqueta;

    @FXML private TextField txtMateriaNombre;
    @FXML private Label lblMensajeMateria;
    @FXML private TableView<Materia> tablaMaterias;
    @FXML private TableColumn<Materia, String> colMateriaNombre;

    public AdminCatalogosController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCursoNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombre()));
        colSeccionEtiqueta.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEtiqueta()));
        colMateriaNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombre()));

        recargarTodo();
    }

    // ------------------------------------------------------------------
    // Cursos
    // ------------------------------------------------------------------

    @FXML
    private void handleAgregarCurso(ActionEvent event) {
        try {
            catalogoService.crearCurso(txtCursoNombre.getText());
            txtCursoNombre.clear();
            mostrarMensaje(lblMensajeCurso, "Curso agregado.", false);
            recargarTodo();
        } catch (Exception e) {
            mostrarMensaje(lblMensajeCurso, mensajeDe(e), true);
        }
    }

    @FXML
    private void handleEliminarCurso(ActionEvent event) {
        Curso seleccionado = tablaCursos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarMensaje(lblMensajeCurso, "Selecciona un curso primero.", true);
            return;
        }
        if (!confirmar("¿Eliminar el curso \"" + seleccionado.getNombre() + "\"? También se eliminarán sus secciones.")) {
            return;
        }
        try {
            catalogoService.eliminarCurso(seleccionado.getIdCurso());
            recargarTodo();
        } catch (Exception e) {
            mostrarMensaje(lblMensajeCurso, mensajeDe(e), true);
        }
    }

    // ------------------------------------------------------------------
    // Secciones
    // ------------------------------------------------------------------

    @FXML
    private void handleAgregarSeccion(ActionEvent event) {
        try {
            Curso curso = comboSeccionCurso.getValue();
            catalogoService.crearSeccion(txtSeccionNombre.getText(), curso != null ? curso.getIdCurso() : 0);
            txtSeccionNombre.clear();
            comboSeccionCurso.setValue(null);
            mostrarMensaje(lblMensajeSeccion, "Sección agregada.", false);
            recargarTodo();
        } catch (Exception e) {
            mostrarMensaje(lblMensajeSeccion, mensajeDe(e), true);
        }
    }

    @FXML
    private void handleEliminarSeccion(ActionEvent event) {
        Seccion seleccionada = tablaSecciones.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarMensaje(lblMensajeSeccion, "Selecciona una sección primero.", true);
            return;
        }
        if (!confirmar("¿Eliminar la sección \"" + seleccionada.getEtiqueta() + "\"? Los alumnos quedarán sin sección asignada.")) {
            return;
        }
        try {
            catalogoService.eliminarSeccion(seleccionada.getIdSeccion());
            recargarTodo();
        } catch (Exception e) {
            mostrarMensaje(lblMensajeSeccion, mensajeDe(e), true);
        }
    }

    // ------------------------------------------------------------------
    // Materias
    // ------------------------------------------------------------------

    @FXML
    private void handleAgregarMateria(ActionEvent event) {
        try {
            catalogoService.crearMateria(txtMateriaNombre.getText());
            txtMateriaNombre.clear();
            mostrarMensaje(lblMensajeMateria, "Materia agregada.", false);
            recargarTodo();
        } catch (Exception e) {
            mostrarMensaje(lblMensajeMateria, mensajeDe(e), true);
        }
    }

    @FXML
    private void handleEliminarMateria(ActionEvent event) {
        Materia seleccionada = tablaMaterias.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarMensaje(lblMensajeMateria, "Selecciona una materia primero.", true);
            return;
        }
        if (!confirmar("¿Eliminar la materia \"" + seleccionada.getNombre() + "\"?")) {
            return;
        }
        try {
            catalogoService.eliminarMateria(seleccionada.getIdMateria());
            recargarTodo();
        } catch (Exception e) {
            mostrarMensaje(lblMensajeMateria, mensajeDe(e), true);
        }
    }

    // ------------------------------------------------------------------
    // Utilidades
    // ------------------------------------------------------------------

    private void recargarTodo() {
        try {
            List<Curso> cursos = catalogoService.listarCursos();
            tablaCursos.setItems(FXCollections.observableArrayList(cursos));
            comboSeccionCurso.setItems(FXCollections.observableArrayList(cursos));

            List<Seccion> secciones = catalogoService.listarSecciones();
            tablaSecciones.setItems(FXCollections.observableArrayList(secciones));

            List<Materia> materias = catalogoService.listarMaterias();
            tablaMaterias.setItems(FXCollections.observableArrayList(materias));
        } catch (Exception e) {
            mostrarMensaje(lblMensajeCurso, mensajeDe(e), true);
        }
    }

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
