package my.database.maliapp.modelos;

import java.time.LocalDate;

public class Empleado {
    private int idEmpleado;
    private String nombre;
    private String apellido;
    private String DNI;
    private LocalDate fechaInicio;
    private String puesto;
    private int idDepartamento;

    public Empleado(int idEmpleado, String nombre, String apellido, String DNI, LocalDate fechaInicio, String puesto, int idDepartamento) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.apellido = apellido;
        this.DNI = DNI;
        this.fechaInicio = fechaInicio;
        this.puesto = puesto;
        this.idDepartamento = idDepartamento;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }
    public String getNombre() {
        return nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public String getDNI() {
        return DNI;
    }
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }
    public int getIdDepartamento() {
        return idDepartamento;
    }
    public String getPuesto() {
        return puesto;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public void setDNI(String DNI) {
        this.DNI = DNI;
    }
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }
    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }
}
