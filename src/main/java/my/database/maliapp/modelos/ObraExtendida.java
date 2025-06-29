package my.database.maliapp.modelos;

public class ObraExtendida {
    private ObraDeArte obra;
    private Artista artista;
    private Coleccion coleccion;

    public ObraExtendida(ObraDeArte obra, Artista artista, Coleccion coleccion) {
        this.obra = obra;
        this.artista = artista;
        this.coleccion = coleccion;
    }

    public ObraDeArte getObra() {
        return obra;
    }

    public Artista getArtista() {
        return artista;
    }

    public Coleccion getColeccion() {
        return coleccion;
    }

    public String getTitulo() {
        return obra.getTitulo();
    }

    public String getTipo() {
        return obra.getTipo();
    }

    public String getEstado() {
        return obra.getEstado();
    }

    public Integer getFechaMin() {
        return obra.getFechaMin();
    }

    public Integer getFechaMax() {
        return obra.getFechaMax();
    }

    public String getNombreArtista() {
        return artista.getNombre() + (artista.getApellido() != null ? " " + artista.getApellido() : "");
    }

    public String getNombreColeccion() {
        return coleccion != null ? coleccion.getNombreColeccion() : "(Sin colección)";
    }
}
