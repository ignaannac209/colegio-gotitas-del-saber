package main.java.edu.ingsoft.colegio.gotitas.model;

/** POJO que representa una sección de un curso (ej. "A" de "Primero Primaria"). */
public class Seccion {

    private int idSeccion;
    private String nombre;
    private int idCurso;
    private String nombreCurso; // dato de solo lectura

    public Seccion() {
    }

    public Seccion(int idSeccion, String nombre, int idCurso, String nombreCurso) {
        this.idSeccion = idSeccion;
        this.nombre = nombre;
        this.idCurso = idCurso;
        this.nombreCurso = nombreCurso;
    }

    public int getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(int idSeccion) {
        this.idSeccion = idSeccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public String getEtiqueta() {
        return (nombreCurso != null ? nombreCurso : "") + " \"" + nombre + "\"";
    }

    @Override
    public String toString() {
        return getEtiqueta();
    }
}
