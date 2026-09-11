package main.java.edu.ingsoft.colegio.gotitas.model.cursos;

/** POJO que representa un curso/grado (ej. "Primero Primaria"). */
public class Curso {

    private int idCurso;
    private String nombre;

    public Curso() {
    }

    public Curso(int idCurso, String nombre) {
        this.idCurso = idCurso;
        this.nombre = nombre;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
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
