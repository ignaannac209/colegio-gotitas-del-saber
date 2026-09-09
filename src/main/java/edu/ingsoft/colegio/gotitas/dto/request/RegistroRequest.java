package main.java.edu.ingsoft.colegio.gotitas.dto.request;

/**
 * DTO de entrada para la operación de registro de un nuevo usuario
 * (Vista B - RegistroView).
 */
public class RegistroRequest {

    public static final String ROL_DOCENTE = "DOCENTE";
    public static final String ROL_ESTUDIANTE = "ESTUDIANTE";

    private String nombre;
    private String usuario;
    private String password;
    private String email;
    private String rol;   // ROL_DOCENTE o ROL_ESTUDIANTE
    private String carne; // solo aplica cuando rol = ROL_ESTUDIANTE

    public RegistroRequest(String nombre, String usuario, String password, String email, String rol, String carne) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.password = password;
        this.email = email;
        this.rol = rol;
        this.carne = carne;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getCarne() {
        return carne;
    }

    public void setCarne(String carne) {
        this.carne = carne;
    }

    public boolean esDocente() {
        return ROL_DOCENTE.equals(rol);
    }

    public boolean esEstudiante() {
        return ROL_ESTUDIANTE.equals(rol);
    }
}
