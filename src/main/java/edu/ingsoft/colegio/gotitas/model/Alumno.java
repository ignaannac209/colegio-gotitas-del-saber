package main.java.edu.ingsoft.colegio.gotitas.model;

/**
 * POJO que representa a un alumno inscrito en una sección.
 */
public class Alumno {

    private int idAlumno;
    private String nombre;
    private String apellido;
    private Integer idSeccion;
    private String nombreSeccion; // dato de solo lectura, para mostrar en tablas
    private String nombreCurso;   // dato de solo lectura, para mostrar en tablas

    public Alumno() {
    }

    public Alumno(int idAlumno, String nombre, String apellido, Integer idSeccion) {
        this.idAlumno = idAlumno;
        this.nombre = nombre;
        this.apellido = apellido;
        this.idSeccion = idSeccion;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
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

    public Integer getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(Integer idSeccion) {
        this.idSeccion = idSeccion;
    }

    public String getNombreSeccion() {
        return nombreSeccion;
    }

    public void setNombreSeccion(String nombreSeccion) {
        this.nombreSeccion = nombreSeccion;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public String getSeccionMostrable() {
        if (nombreCurso == null && nombreSeccion == null) {
            return "Sin asignar";
        }
        return (nombreCurso != null ? nombreCurso : "") + " \"" + (nombreSeccion != null ? nombreSeccion : "") + "\"";
    }

    @Override
    public String toString() {
        return getNombreCompleto();
    }
}
