package my.database.maliapp.modelos;

public class TrabajaEn {
    private int idEmpleado;
    private int idTrabajo;

    public TrabajaEn(int idEmpleado, int idTrabajo) {
        this.idEmpleado = idEmpleado;
        this.idTrabajo = idTrabajo;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }
    public int getIdTrabajo() {
        return idTrabajo;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }
    public void setIdTrabajo(int idTrabajo) {
        this.idTrabajo = idTrabajo;
    }
}
