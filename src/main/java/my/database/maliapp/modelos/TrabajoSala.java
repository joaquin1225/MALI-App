package my.database.maliapp.modelos;

public class TrabajoSala {
    private int idTrabajo;
    private int idSala;

    public TrabajoSala(int idTrabajo, int idSala) {
        this.idTrabajo = idTrabajo;
        this.idSala = idSala;
    }

    public int getIdTrabajo() {
        return idTrabajo;
    }
    public int getIdSala() {
        return idSala;
    }
}
