package my.database.maliapp.modelos;

public class PerteneceA {
    private int idObra;
    private int idColeccion;

    public PerteneceA(int idObra, int idColeccion) {
        this.idObra = idObra;
        this.idColeccion = idColeccion;
    }

    public int getIdObra() {
        return idObra;
    }
    public int getIdColeccion() {
        return idColeccion;
    }

    public void setIdObra(int idObra) {
        this.idObra = idObra;
    }
    public void setIdColeccion(int idColeccion) {
        this.idColeccion = idColeccion;
    }
}
