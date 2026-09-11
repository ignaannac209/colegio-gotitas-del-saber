package main.java.edu.ingsoft.colegio.gotitas.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.java.edu.ingsoft.colegio.gotitas.model.auth.Auth;
import main.java.edu.ingsoft.colegio.gotitas.util.SceneManager;

/**
 * Controlador de la Vista de Dashboard para el rol Docente
 * (dashboard-view-docentes.fxml).
 *
 * @author JOSE BATEN
 */
public class DashboardController implements Initializable {

    private final SceneManager sceneManager;
    private final Auth usuarioAutenticado;

    @FXML
    private Button btnCursos;

    @FXML
    private Button btnAsistencia;

    @FXML
    private Button btnNotas;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private TableView<?> tableAlumnos;

    @FXML
    private TableColumn<?, ?> colCarnet;

    @FXML
    private TableColumn<?, ?> colNombre;

    @FXML
    private TableColumn<?, ?> colGrado;

    @FXML
    private TableColumn<?, ?> colAccion;

    public DashboardController(SceneManager sceneManager, Auth usuarioAutenticado) {
        this.sceneManager = sceneManager;
        this.usuarioAutenticado = usuarioAutenticado;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO: cargar aquí, con un DocenteService/AlumnoService, el listado
        // real de alumnos asignados al docente autenticado (usuarioAutenticado)
        // y asignarlo a tableAlumnos.setItems(...).
    }

    @FXML
    private void handleCursos(ActionEvent event) {
        // TODO: mostrar la sección "Mis Cursos" del docente.
    }

    @FXML
    private void handleAsistencia(ActionEvent event) {
        // TODO: mostrar la sección "Registro de Asistencia" del docente.
    }

    @FXML
    private void handleNotas(ActionEvent event) {
        // TODO: mostrar la sección "Ingreso de Notas" del docente.
    }

    @FXML
    private void handleCerrarSesion(ActionEvent event) {
        try {
            sceneManager.showLoginView();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al cerrar sesión");
            alert.setHeaderText(null);
            alert.setContentText("No fue posible volver a la pantalla de inicio de sesión.");
            alert.showAndWait();
        }
    }
}
