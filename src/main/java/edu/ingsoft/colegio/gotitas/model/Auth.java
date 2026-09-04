package main.java.edu.ingsoft.colegio.gotitas.model;

/**
 * POJO que representa la sesión del usuario autenticado.
 * Se construye al iniciar sesión correctamente y se comparte con las
 * vistas siguientes (ej. Menú Principal) a través del SceneManager.
 */
public class Auth {

    private String nombre;
    private String apellido;
    private String email;

    public Auth() {
    }

    public Auth(String nombre, String apellido, String email) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
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
