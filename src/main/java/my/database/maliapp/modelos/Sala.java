package my.database.maliapp.modelos;

public class Sala {
    private int idSala;
    private String nombre;

    public Sala(int idSala, String nombre) {
        this.idSala = idSala;
        this.nombre = nombre;
    }

    public int getIdSala() {
        return idSala;
    }
    public String getNombre() {
        return nombre;
    }
}
