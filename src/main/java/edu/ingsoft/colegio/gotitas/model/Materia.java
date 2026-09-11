package main.java.edu.ingsoft.colegio.gotitas.model;

/** POJO que representa una materia/asignatura (ej. "Matemática"). */
public class Materia {

    private int idMateria;
    private String nombre;

    public Materia() {
    }

    public Materia(int idMateria, String nombre) {
        this.idMateria = idMateria;
        this.nombre = nombre;
    }

    public int getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(int idMateria) {
        this.idMateria = idMateria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
