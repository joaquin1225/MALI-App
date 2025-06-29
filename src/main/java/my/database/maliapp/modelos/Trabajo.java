package my.database.maliapp.modelos;

import java.time.LocalDate;

public class Trabajo {
    private int idTrabajo;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public Trabajo(int idTrabajo, String descripcion, LocalDate fechaInicio, LocalDate fechaFin) {
        this.idTrabajo = idTrabajo;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Trabajo(String descripcion, LocalDate fechaInicio, LocalDate fechaFin) {
        this(-1, descripcion, fechaInicio, fechaFin);
    }

    public int getIdTrabajo() {
        return idTrabajo;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }
    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setIdTrabajo(int idTrabajo) {
        this.idTrabajo = idTrabajo;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
}
