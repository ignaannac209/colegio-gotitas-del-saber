package main.java.edu.ingsoft.colegio.gotitas.dto.response;

/**
 * DTO de salida con los datos que devuelve la base de datos al buscar
 * un usuario por su correo electrónico.
 */
public class LoginResponse {

    private String nombre;
    private String apellido;
    private String contrasenaHash;

    public LoginResponse(String nombre, String apellido, String contrasenaHash) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.contrasenaHash = contrasenaHash;
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

    public String getContrasenaHash() {
        return contrasenaHash;
    }

    public void setContrasenaHash(String contrasenaHash) {
        this.contrasenaHash = contrasenaHash;
    }
}
