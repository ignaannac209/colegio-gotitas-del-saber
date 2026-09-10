package main.java.edu.ingsoft.colegio.gotitas.model.docente;

/**
 * POJO que representa a un docente y (opcionalmente) su cuenta de acceso.
 */
public class Docente {

    private int idDocente;
    private String nombre;
    private String apellido;
    private String email;      // proviene de la tabla usuarios (puede ser null si no tiene cuenta)
    private Integer idUsuario; // null si el docente no tiene cuenta de acceso todavía

    public Docente() {
    }

    public Docente(int idDocente, String nombre, String apellido, String email, Integer idUsuario) {
        this.idDocente = idDocente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.idUsuario = idUsuario;
    }

    public int getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(int idDocente) {
        this.idDocente = idDocente;
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

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    @Override
    public String toString() {
        return getNombreCompleto();
    }
}
