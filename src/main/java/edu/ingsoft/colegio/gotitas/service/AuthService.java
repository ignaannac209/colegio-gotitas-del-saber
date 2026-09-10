package main.java.edu.ingsoft.colegio.gotitas.service;

import java.sql.SQLException;
import main.java.edu.ingsoft.colegio.gotitas.dto.request.LoginRequest;
import main.java.edu.ingsoft.colegio.gotitas.dto.request.RegistroRequest;
import main.java.edu.ingsoft.colegio.gotitas.dto.response.LoginResponse;
import main.java.edu.ingsoft.colegio.gotitas.repository.AuthRepository;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Contiene la lógica de negocio para autenticar usuarios contra la
 * base de datos, comparando la contraseña ingresada con el hash
 * almacenado usando BCrypt.
 */
public class AuthService {

    private final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public LoginResponse login(LoginRequest loginRequest) throws SQLException {
        if (loginRequest == null) {
            throw new IllegalArgumentException("Credenciales vacías");
        }
        if (loginRequest.getEmail() == null || loginRequest.getEmail().isBlank()
                || loginRequest.getPassword() == null || loginRequest.getPassword().isBlank()) {
            throw new IllegalArgumentException("El correo o la contraseña no pueden estar vacíos");
        }

        LoginResponse response = authRepository.findUserByEmail(loginRequest);

        if (response == null) {
            throw new IllegalStateException("Usuario no encontrado");
        }

        String contrasenaHashed = response.getContrasenaHash();

        if (contrasenaHashed == null || !BCrypt.checkpw(loginRequest.getPassword(), contrasenaHashed)) {
            throw new IllegalStateException("Contraseña incorrecta");
        }

        return response;
    }

    /**
     * Registra un nuevo docente/usuario en la base de datos. La contraseña
     * se hashea con BCrypt antes de guardarse; nunca se persiste en texto
     * plano.
     */
    public void registrar(RegistroRequest registroRequest) throws SQLException {
        if (registroRequest == null) {
            throw new IllegalArgumentException("Datos de registro vacíos");
        }
        if (registroRequest.getNombre() == null || registroRequest.getNombre().isBlank()
                || registroRequest.getEmail() == null || registroRequest.getEmail().isBlank()
                || registroRequest.getPassword() == null || registroRequest.getPassword().isBlank()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }

        String contrasenaHash = BCrypt.hashpw(registroRequest.getPassword(), BCrypt.gensalt());

        try {
            authRepository.registrarUsuario(registroRequest, contrasenaHash);
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("duplicate")) {
                throw new IllegalStateException("Ya existe una cuenta registrada con ese correo.");
            }
            throw e;
        }
    }
}
