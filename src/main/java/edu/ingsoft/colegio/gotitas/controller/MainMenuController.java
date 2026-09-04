package main.java.edu.ingsoft.colegio.gotitas.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import main.java.edu.ingsoft.colegio.gotitas.model.auth.Auth;
import main.java.edu.ingsoft.colegio.gotitas.util.SceneManager;

/**
 * Controlador de la Vista C (Menú Principal / Dashboard).
 * Muestra el nombre del usuario autenticado, la navegación lateral y
 * gestiona el cierre de sesión.
 */
public class MainMenuController implements Initializable {

    private final SceneManager sceneManager;
    private final Auth usuarioAutenticado;

    @FXML
    private Label lblUsuarioActivo;

    @FXML
    private Label lblTituloSeccion;

    @FXML
    private StackPane paneContenido;

    public MainMenuController(SceneManager sceneManager, Auth usuarioAutenticado) {
        this.sceneManager = sceneManager;
        this.usuarioAutenticado = usuarioAutenticado;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String nombre = usuarioAutenticado != null ? usuarioAutenticado.getNombreCompleto() : "Usuario";
        lblUsuarioActivo.setText(nombre);
        mostrarSeccion("Inicio", "Bienvenido(a) de nuevo, " + nombre + ".");
    }

    @FXML
    private void handleInicio(ActionEvent event) {
        mostrarSeccion("Inicio", "Bienvenido(a) de nuevo, " + lblUsuarioActivo.getText() + ".");
    }

    @FXML
    private void handlePerfil(ActionEvent event) {
        mostrarSeccion("Perfil", "Aquí se mostrará la información del perfil del usuario.");
    }

    @FXML
    private void handleConfiguracion(ActionEvent event) {
        mostrarSeccion("Configuración", "Aquí se mostrarán las opciones de configuración de la cuenta.");
    }

    @FXML
    private void handleCerrarSesion(ActionEvent event) throws Exception {
        paneContenido.getChildren().clear();
        sceneManager.showLoginView();
    }

    /** Actualiza el área central con un contenido de ejemplo por sección. */
    private void mostrarSeccion(String titulo, String contenido) {
        lblTituloSeccion.setText(titulo);
        paneContenido.getChildren().clear();
        Label label = new Label(contenido);
        label.getStyleClass().add("contenido-placeholder");
        paneContenido.getChildren().add(label);
    }
}
