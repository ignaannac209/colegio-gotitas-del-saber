
package main.java.edu.ingsoft.colegio.gotitas.model.estudiante;

public class Estudiante {
    
 private String idEstudiante;
 private String nombre;
 private String apellido;
 private String carne;

    public Estudiante(String idEstudiante, String nombre, String apellido, String carne) {
        this.idEstudiante = idEstudiante;
        this.nombre = nombre;
        this.apellido = apellido;
        this.carne = carne;
     
    }

    public String getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(String idEstudiante) {
        this.idEstudiante = idEstudiante;
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

    public String getCarne() {
        return carne;
    }

    public void setCarne(String carne) {
        this.carne = carne;
    }

    

   
    

    
}
