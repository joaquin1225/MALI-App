package my.database.maliapp.modelos;

public class TrabajoObra {
    private int idTrabajo;
    private int idObra;

    public TrabajoObra(int idTrabajo, int idObra) {
        this.idTrabajo = idTrabajo;
        this.idObra = idObra;
    }

    public int getIdTrabajo() {
        return idTrabajo;
    }
    public int getIdObra() {
        return idObra;
    }
}
