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
 *
 * La tabla "usuarios" liga cada cuenta a un docente O a un estudiante
 * (nunca ambos), según "id_rol":
 *   id_rol = 1 -> docente    (usuarios.id_docente relleno, id_estudiante NULL)
 *   id_rol = 2 -> estudiante (usuarios.id_estudiante relleno, id_docente NULL)
 */
public class AuthRepository {

    private static final int ID_ROL_DOCENTE = 1;
    private static final int ID_ROL_ESTUDIANTE = 2;

    public AuthRepository() {
    }

    /**
     * Busca una cuenta (docente o estudiante) por su correo electrónico.
     *
     * @param loginRequest credenciales capturadas en el formulario de login.
     * @return los datos del usuario encontrado o {@code null} si no existe.
     */
    public LoginResponse findUserByEmail(LoginRequest loginRequest) throws SQLException {
        String sql = "SELECT COALESCE(d.nombre, e.nombre) AS nombre, "
                + "COALESCE(d.apellido, e.apellido) AS apellido, "
                + "u.contrasena_hash, "
                + "u.id_rol "
                + "FROM usuarios AS u "
                + "LEFT JOIN docentes AS d ON d.id_docente = u.id_docente "
                + "LEFT JOIN estudiantes AS e ON e.id_estudiante = u.id_estudiante "
                + "WHERE u.email = ?";

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)) {
            pstm.setString(1, loginRequest.getEmail());

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return new LoginResponse(
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("contrasena_hash"),
                            rs.getInt("id_rol")
                    );
                }
            }
        }

        return null;
    }

    /** Expuesto para que otras capas (ej. LoginController) puedan mapear id_rol -> nombre de rol. */
    public static int idRolDocente() {
        return ID_ROL_DOCENTE;
    }

    public static int idRolEstudiante() {
        return ID_ROL_ESTUDIANTE;
    }

    /**
     * Inserta un nuevo docente/estudiante y su usuario de acceso asociado
     * en una sola transacción: si alguna de las dos inserciones falla, no
     * se guarda ninguna (evita registros huérfanos).
     *
     * @param registroRequest datos capturados en el formulario de Registro,
     *                          incluyendo el rol elegido (docente/estudiante).
     * @param contrasenaHash   contraseña ya hasheada con BCrypt (nunca se
     *                          guarda en texto plano).
     */
    public void registrarUsuario(RegistroRequest registroRequest, String contrasenaHash) throws SQLException {
        Connection connection = DataBaseConnection.getConnectionDataBase();
        boolean autoCommitOriginal = connection.getAutoCommit();

        try {
            connection.setAutoCommit(false);

            if (registroRequest.esEstudiante()) {
                long idEstudiante = insertarEstudiante(connection, registroRequest.getNombre(), registroRequest.getCarne());
                insertarUsuario(connection, ID_ROL_ESTUDIANTE, null, idEstudiante, registroRequest.getEmail(), contrasenaHash);
            } else {
                long idDocente = insertarDocente(connection, registroRequest.getNombre());
                insertarUsuario(connection, ID_ROL_DOCENTE, idDocente, null, registroRequest.getEmail(), contrasenaHash);
            }

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

    private long insertarEstudiante(Connection connection, String nombreCompleto, String carne) throws SQLException {
        String sql = "INSERT INTO estudiantes (carne, nombre, apellido) VALUES (?, ?, ?)";
        String[] partesNombre = dividirNombreCompleto(nombreCompleto);

        try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, carne);
            pstm.setString(2, partesNombre[0]);
            pstm.setString(3, partesNombre[1]);
            pstm.executeUpdate();

            try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
                if (!generatedKeys.next()) {
                    throw new SQLException("No se pudo obtener el id del estudiante recién creado");
                }
                return generatedKeys.getLong(1);
            }
        }
    }

    private void insertarUsuario(Connection connection, int idRol, Long idDocente, Long idEstudiante,
            String email, String contrasenaHash) throws SQLException {
        String sql = "INSERT INTO usuarios (id_rol, id_docente, id_estudiante, email, contrasena_hash) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, idRol);
            setNullableLong(pstm, 2, idDocente);
            setNullableLong(pstm, 3, idEstudiante);
            pstm.setString(4, email);
            pstm.setString(5, contrasenaHash);
            pstm.executeUpdate();
        }
    }

    private void setNullableLong(PreparedStatement pstm, int index, Long valor) throws SQLException {
        if (valor == null) {
            pstm.setNull(index, java.sql.Types.INTEGER);
        } else {
            pstm.setLong(index, valor);
        }
    }

    /** Divide "Nombre Apellido" en sus dos partes para las tablas docentes/estudiantes. */
    private String[] dividirNombreCompleto(String nombreCompleto) {
        String limpio = nombreCompleto.trim();
        int indiceEspacio = limpio.indexOf(' ');

        if (indiceEspacio == -1) {
            return new String[]{limpio, ""};
        }
        return new String[]{limpio.substring(0, indiceEspacio), limpio.substring(indiceEspacio + 1).trim()};
    }
}
