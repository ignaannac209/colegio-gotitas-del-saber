package main.java.edu.ingsoft.colegio.gotitas.model;

public class Carne {

    private long id;
    private String carne;
    private String nombre;
    private String apellido;

    public Carne() {
    }

    public Carne(String carne, String nombre, String apellido) {
        this.carne = carne;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public Carne(long id, String carne, String nombre, String apellido) {
        this.id = id;
        this.carne = carne;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getCarne() { return carne; }
    public void setCarne(String carne) { this.carne = carne; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getNombreCompleto() {
        if (apellido == null || apellido.isBlank()) {
            return nombre;
        }
        return nombre + " " + apellido;
    }
}

