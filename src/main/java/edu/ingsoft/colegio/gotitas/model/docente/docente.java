
package main.java.edu.ingsoft.colegio.gotitas.model.docente;

public class docente {
    
    private String idDocente;
    private String nombre;
    private String apellido;

    public docente(String idDocente, String nombre, String apellido) {
        this.idDocente = idDocente;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(String idDocente) {
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
    
    
    
}
