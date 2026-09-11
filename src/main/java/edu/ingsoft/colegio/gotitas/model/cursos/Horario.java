package main.java.edu.ingsoft.colegio.gotitas.model.cursos;

/**
 * POJO que representa una asignación de horario: en una sección, un día y
 * un periodo determinados, un docente imparte una materia.
 */
public class Horario {

    private int idHorario;
    private int idSeccion;
    private String nombreSeccion; // solo lectura
    private int idPeriodo;
    private String rangoPeriodo;  // solo lectura, ej. "07:05 AM - 07:55 AM"
    private int ordenPeriodo;     // solo lectura, para ordenar
    private int idMateria;
    private String nombreMateria; // solo lectura
    private int idDocente;
    private String nombreDocente; // solo lectura
    private String diaSemana;     // LUNES, MARTES, MIERCOLES, JUEVES, VIERNES

    public Horario() {
    }

    public int getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(int idHorario) {
        this.idHorario = idHorario;
    }

    public int getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(int idSeccion) {
        this.idSeccion = idSeccion;
    }

    public String getNombreSeccion() {
        return nombreSeccion;
    }

    public void setNombreSeccion(String nombreSeccion) {
        this.nombreSeccion = nombreSeccion;
    }

    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public String getRangoPeriodo() {
        return rangoPeriodo;
    }

    public void setRangoPeriodo(String rangoPeriodo) {
        this.rangoPeriodo = rangoPeriodo;
    }

    public int getOrdenPeriodo() {
        return ordenPeriodo;
    }

    public void setOrdenPeriodo(int ordenPeriodo) {
        this.ordenPeriodo = ordenPeriodo;
    }

    public int getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(int idMateria) {
        this.idMateria = idMateria;
    }

    public String getNombreMateria() {
        return nombreMateria;
    }

    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }

    public int getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(int idDocente) {
        this.idDocente = idDocente;
    }

    public String getNombreDocente() {
        return nombreDocente;
    }

    public void setNombreDocente(String nombreDocente) {
        this.nombreDocente = nombreDocente;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }
}
