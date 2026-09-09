package main.java.edu.ingsoft.colegio.gotitas.controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import main.java.edu.ingsoft.colegio.gotitas.dto.request.RegistroRequest;
import main.java.edu.ingsoft.colegio.gotitas.service.AuthService;
import main.java.edu.ingsoft.colegio.gotitas.util.SceneManager;

/**
 * Controlador de la Vista B (Registro). Valida el formulario de creación
 * de cuenta, guarda el nuevo docente/estudiante y su usuario en la base
 * de datos (con la contraseña hasheada con BCrypt) y, si todo sale bien,
 * redirige al Login.
 *
 * Puede registrar dos tipos de cuenta, según el rol elegido:
 *   - Docente:    requiere correo @hotmail.com (regla de negocio de la BD)
 *   - Estudiante: requiere correo @gmail.com y un número de carné
 */
public class RegistroController implements Initializable {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    // La base de datos exige estos dominios mediante el CHECK
    // "chk_email_dominio_por_rol" de la tabla usuarios.
    private static final String DOMINIO_DOCENTE = "@hotmail.com";
    private static final String DOMINIO_ESTUDIANTE = "@gmail.com";

    private final AuthService authService;
    private final SceneManager sceneManager;

    @FXML
    private ToggleGroup grupoRol;

    @FXML
    private RadioButton radioDocente;

    @FXML
    private RadioButton radioEstudiante;

    @FXML
    private TextField txtFieldNombre;

    @FXML
    private VBox campoCarne;

    @FXML
    private TextField txtFieldCarne;

    @FXML
    private TextField txtFieldUsuario;

    @FXML
    private PasswordField txtFieldPassword;

    @FXML
    private Label lblEtiquetaEmail;

    @FXML
    private TextField txtFieldEmail;

    @FXML
    private Label lblMensajeError;

    public RegistroController(AuthService authService, SceneManager sceneManager) {
        this.authService = authService;
        this.sceneManager = sceneManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblMensajeError.setText("");
        actualizarFormularioSegunRol();
    }

    @FXML
    private void handleCambioRol(ActionEvent event) {
        actualizarFormularioSegunRol();
    }

    @FXML
    private void handleGuardar(ActionEvent event) throws Exception {
        RegistroRequest registroRequest = leerFormulario();

        String errorValidacion = validar(registroRequest);
        if (errorValidacion != null) {
            lblMensajeError.setText(errorValidacion);
            return;
        }

        try {
            authService.registrar(registroRequest);
        } catch (SQLException | IllegalStateException | IllegalArgumentException e) {
            String mensaje = e.getMessage() != null ? e.getMessage() : "No fue posible completar el registro.";
            lblMensajeError.setText(mensaje);
            mostrarAlerta(Alert.AlertType.ERROR, "Error al registrar", mensaje);
            return;
        }

        lblMensajeError.setText("");
        mostrarAlerta(Alert.AlertType.INFORMATION, "Registro exitoso",
                "¡Cuenta creada correctamente! Ahora puedes iniciar sesión, " + registroRequest.getNombre() + ".");

        sceneManager.showLoginView();
    }

    @FXML
    private void handleCancelar(ActionEvent event) throws Exception {
        sceneManager.showLoginView();
    }

    /** Muestra/oculta el campo de carné y ajusta las pistas visuales según el rol elegido. */
    private void actualizarFormularioSegunRol() {
        boolean esEstudiante = radioEstudiante.isSelected();

        campoCarne.setVisible(esEstudiante);
        campoCarne.setManaged(esEstudiante);

        if (esEstudiante) {
            txtFieldEmail.setPromptText("tunombre" + DOMINIO_ESTUDIANTE);
            lblEtiquetaEmail.setText("Correo electrónico (" + DOMINIO_ESTUDIANTE + ")");
        } else {
            txtFieldEmail.setPromptText("tunombre" + DOMINIO_DOCENTE);
            lblEtiquetaEmail.setText("Correo electrónico (" + DOMINIO_DOCENTE + ")");
        }
        lblMensajeError.setText("");
    }

    private RegistroRequest leerFormulario() {
        String nombre = txtFieldNombre.getText() == null ? "" : txtFieldNombre.getText().trim();
        String usuario = txtFieldUsuario.getText() == null ? "" : txtFieldUsuario.getText().trim();
        String password = txtFieldPassword.getText() == null ? "" : txtFieldPassword.getText().trim();
        String email = txtFieldEmail.getText() == null ? "" : txtFieldEmail.getText().trim();
        String carne = txtFieldCarne.getText() == null ? "" : txtFieldCarne.getText().trim();
        String rol = radioEstudiante.isSelected() ? RegistroRequest.ROL_ESTUDIANTE : RegistroRequest.ROL_DOCENTE;
        return new RegistroRequest(nombre, usuario, password, email, rol, carne);
    }

    private String validar(RegistroRequest registroRequest) {
        if (registroRequest.getNombre().isEmpty() || registroRequest.getUsuario().isEmpty()
                || registroRequest.getPassword().isEmpty() || registroRequest.getEmail().isEmpty()) {
            return "Todos los campos son obligatorios.";
        }
        if (registroRequest.esEstudiante() && registroRequest.getCarne().isEmpty()) {
            return "El carné de estudiante es obligatorio.";
        }
        if (registroRequest.getPassword().length() < 4) {
            return "La contraseña debe tener al menos 4 caracteres.";
        }
        if (!EMAIL_PATTERN.matcher(registroRequest.getEmail()).matches()) {
            return "Ingresa un correo electrónico válido.";
        }

        String dominioRequerido = registroRequest.esEstudiante() ? DOMINIO_ESTUDIANTE : DOMINIO_DOCENTE;
        String tipoCuenta = registroRequest.esEstudiante() ? "Los estudiantes" : "Los docentes";
        if (!registroRequest.getEmail().toLowerCase().endsWith(dominioRequerido)) {
            return tipoCuenta + " deben registrarse con un correo " + dominioRequerido
                    + " (ej. nombre" + dominioRequerido + ").";
        }
        return null;
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
