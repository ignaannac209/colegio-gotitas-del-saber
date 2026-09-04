package main.java.edu.ingsoft.colegio.gotitas.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.java.edu.ingsoft.colegio.gotitas.dto.request.LoginRequest;
import main.java.edu.ingsoft.colegio.gotitas.dto.response.LoginResponse;
import main.java.edu.ingsoft.colegio.gotitas.model.auth.Auth;
import main.java.edu.ingsoft.colegio.gotitas.service.AuthService;
import main.java.edu.ingsoft.colegio.gotitas.util.SceneManager;

/**
 * Controlador de la Vista  (Login). Valida las credenciales ingresadas y
 * navega hacia el Menú Principal o hacia la vista de Registro.
 */
public class LoginController implements Initializable {

    /** Credenciales de prueba solicitadas por la guía (ej: admin/123). */
    private static final String USUARIO_PRUEBA = "admin";
    private static final String PASSWORD_PRUEBA = "123";

    private final AuthService authService;
    private final SceneManager sceneManager;

    @FXML
    private TextField txtFieldEmail;

    @FXML
    private PasswordField txtFieldPassword;

    @FXML
    private Label lblMensajeError;

    @FXML
    private Hyperlink linkCrearCuenta;

    public LoginController(AuthService authService, SceneManager sceneManager) {
        this.authService = authService;
        this.sceneManager = sceneManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblMensajeError.setText("");
    }

    @FXML
    private void handleIniciarSesion(ActionEvent event) {
        String email = txtFieldEmail.getText() == null ? "" : txtFieldEmail.getText().trim();
        String password = txtFieldPassword.getText() == null ? "" : txtFieldPassword.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            mostrarError("Debes ingresar tu usuario/correo y tu contraseña.");
            return;
        }

        try {
            Auth usuarioAutenticado = autenticar(email, password);
            lblMensajeError.setText("");
            sceneManager.showMainMenuView(usuarioAutenticado);
        } catch (Exception e) {
            mostrarError(e.getMessage() != null ? e.getMessage() : "No fue posible iniciar sesión.");
        }
    }

    @FXML
    private void handleIrARegistro(ActionEvent event) throws Exception {
        sceneManager.showRegistroView();
    }

    /**
     * Autentica al usuario.
     */
    private Auth autenticar(String email, String password) throws Exception {
        if (USUARIO_PRUEBA.equalsIgnoreCase(email) && PASSWORD_PRUEBA.equals(password)) {
            return new Auth("Administrador", "General", email);
        }

        LoginRequest loginRequest = new LoginRequest(email, password);
        LoginResponse response = authService.login(loginRequest);
        return new Auth(response.getNombre(), response.getApellido(), email);
    }

    private void mostrarError(String mensaje) {
        lblMensajeError.setText(mensaje);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error al iniciar sesión");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
