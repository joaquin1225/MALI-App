package my.database.maliapp.modelos;

public class Coleccion {
    private int idColeccion;
    private String nombreColeccion;

    public Coleccion(int idColeccion, String nombreColeccion) {
        this.idColeccion = idColeccion;
        this.nombreColeccion = nombreColeccion;
    }

    public int getIdColeccion() {
        return idColeccion;
    }
    public String getNombreColeccion() {
        return nombreColeccion;
    }

    public void setIdColeccion(int idColeccion) {
        this.idColeccion = idColeccion;
    }
    public void setNombreColeccion(String nombreColeccion) {
        this.nombreColeccion = nombreColeccion;
    }

    @Override
    public String toString() {
        return nombreColeccion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coleccion)) return false;
        Coleccion c = (Coleccion) o;
        return idColeccion == c.idColeccion;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idColeccion);
    }
}
