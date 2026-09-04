package main.java.edu.ingsoft.colegio.gotitas.model;

/**
 * POJO que representa los datos capturados en el formulario de Registro
 * (Vista B). Es un modelo simple e independiente de la capa de persistencia.
 */
public class Usuario {

    private String nombre;
    private String usuario;
    private String contrasena;
    private String email;

    public Usuario() {
    }

    public Usuario(String nombre, String usuario, String contrasena, String email) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.contrasena = contrasena;
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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
