package main.java.edu.ingsoft.colegio.gotitas.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import main.java.edu.ingsoft.colegio.gotitas.config.DataBaseConnection;
import main.java.edu.ingsoft.colegio.gotitas.dto.request.LoginRequest;
import main.java.edu.ingsoft.colegio.gotitas.dto.request.RegistroRequest;
import main.java.edu.ingsoft.colegio.gotitas.dto.response.LoginResponse;

/**
 * Acceso a datos relacionado con la autenticación de usuarios.
 * Divide y vencerás: cada método tiene una única responsabilidad.
 */
public class AuthRepository {

    public AuthRepository() {
    }

    /**
     * Busca un docente/usuario por su correo electrónico.
     *
     * @param loginRequest credenciales capturadas en el formulario de login.
     * @return los datos del usuario encontrado o {@code null} si no existe.
     */
    public LoginResponse findUserByEmail(LoginRequest loginRequest) throws SQLException {
        String sql = "SELECT d.nombre, d.apellido, u.contrasena_hash "
                + "FROM usuarios AS u "
                + "RIGHT JOIN docentes AS d ON d.id_docente = u.id_docente "
                + "WHERE u.email = ?";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setString(1, loginRequest.getEmail());

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return new LoginResponse(
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("contrasena_hash")
                    );
                }
            }
        }

        return null;
    }

    /**
     * Inserta un nuevo docente y su usuario de acceso asociado en una sola
     * transacción: si alguna de las dos inserciones falla, no se guarda
     * ninguna (evita registros huérfanos).
     *
     * @param registroRequest  datos capturados en el formulario de Registro.
     * @param contrasenaHash   contraseña ya hasheada con BCrypt (nunca se
     *                          guarda en texto plano).
     */
    public void registrarUsuario(RegistroRequest registroRequest, String contrasenaHash) throws SQLException {
        Connection connection = DataBaseConnection.getConnectionDataBase();
        boolean autoCommitOriginal = connection.getAutoCommit();

        try {
            connection.setAutoCommit(false);

            long idDocente = insertarDocente(connection, registroRequest.getNombre());
            insertarUsuario(connection, idDocente, registroRequest.getEmail(), contrasenaHash);

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(autoCommitOriginal);
        }
    }

    private long insertarDocente(Connection connection, String nombreCompleto) throws SQLException {
        String sql = "INSERT INTO docentes (nombre, apellido) VALUES (?, ?)";
        String[] partesNombre = dividirNombreCompleto(nombreCompleto);

        try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, partesNombre[0]);
            pstm.setString(2, partesNombre[1]);
            pstm.executeUpdate();

            try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
                if (!generatedKeys.next()) {
                    throw new SQLException("No se pudo obtener el id del docente recién creado");
                }
                return generatedKeys.getLong(1);
            }
        }
    }

    private void insertarUsuario(Connection connection, long idDocente, String email, String contrasenaHash) throws SQLException {
        String sql = "INSERT INTO usuarios (id_docente, email, contrasena_hash) VALUES (?, ?, ?)";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setLong(1, idDocente);
            pstm.setString(2, email);
            pstm.setString(3, contrasenaHash);
            pstm.executeUpdate();
        }
    }

    /** Divide "Nombre Apellido" en sus dos partes para la tabla docentes. */
    private String[] dividirNombreCompleto(String nombreCompleto) {
        String limpio = nombreCompleto.trim();
        int indiceEspacio = limpio.indexOf(' ');

        if (indiceEspacio == -1) {
            return new String[]{limpio, ""};
        }
        return new String[]{limpio.substring(0, indiceEspacio), limpio.substring(indiceEspacio + 1).trim()};
    }
}
