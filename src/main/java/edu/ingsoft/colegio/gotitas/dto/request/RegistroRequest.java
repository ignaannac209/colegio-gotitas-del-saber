package main.java.edu.ingsoft.colegio.gotitas.dto.request;

/**
 * DTO de entrada para la operación de registro de un nuevo usuario
 * (Vista B - RegistroView).
 */
public class RegistroRequest {

    private String nombre;
    private String usuario;
    private String password;
    private String email;

    public RegistroRequest(String nombre, String usuario, String password, String email) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.password = password;
        this.email = email;
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
}
