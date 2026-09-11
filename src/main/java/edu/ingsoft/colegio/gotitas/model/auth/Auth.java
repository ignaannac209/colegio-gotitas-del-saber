package main.java.edu.ingsoft.colegio.gotitas.model.auth;

/**
 * POJO que representa la sesión del usuario autenticado.
 * Se construye al iniciar sesión correctamente y se comparte con las
 * vistas siguientes (ej. Menú Principal) a través del SceneManager.
 */
public class Auth {

    public static final String ROL_ADMINISTRADOR = "ADMINISTRADOR";
    public static final String ROL_DOCENTE = "DOCENTE";
    public static final String ROL_ESTUDIANTE = "ESTUDIANTE";

    private String nombre;
    private String apellido;
    private String email;
    private String rol;

    public Auth() {
    }

    /** Constructor de compatibilidad: mantiene el comportamiento previo (sin rol explícito). */
    public Auth(String nombre, String apellido, String email) {
        this(nombre, apellido, email, ROL_ADMINISTRADOR);
    }

    public Auth(String nombre, String apellido, String email, String rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.rol = rol;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean esAdministrador() {
        return ROL_ADMINISTRADOR.equals(rol);
    }

    public boolean esDocente() {
        return ROL_DOCENTE.equals(rol);
    }

    public boolean esEstudiante() {
        return ROL_ESTUDIANTE.equals(rol);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombreCompleto() {
        if (apellido == null || apellido.isBlank()) {
            return nombre;
        }
        return nombre + " " + apellido;
    }
}
