package main.java.edu.ingsoft.colegio.gotitas.model.cursos;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/** POJO que representa un bloque fijo de horario (ej. 07:05 - 07:55). */
public class Periodo {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("hh:mm a");

    private int idPeriodo;
    private int orden;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    public Periodo() {
    }

    public Periodo(int idPeriodo, int orden, LocalTime horaInicio, LocalTime horaFin) {
        this.idPeriodo = idPeriodo;
        this.orden = orden;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public String getRangoMostrable() {
        return horaInicio.format(FORMATO) + " - " + horaFin.format(FORMATO);
    }

    @Override
    public String toString() {
        return "Periodo " + orden + " (" + getRangoMostrable() + ")";
    }
}
