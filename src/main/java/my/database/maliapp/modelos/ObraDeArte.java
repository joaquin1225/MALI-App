package my.database.maliapp.modelos;

public class ObraDeArte {
    private int idObra;
    private int idArtista;
    private String titulo;
    private Integer fechaMin;
    private Integer fechaMax;
    private String tipo;
    private String estado;

    public ObraDeArte(int idObra, int idArtista, String titulo, Integer fechaMin, Integer fechaMax, String tipo, String estado) {
        this.idObra = idObra;
        this.idArtista = idArtista;
        this.titulo = titulo;
        this.fechaMin = fechaMin;
        this.fechaMax = fechaMax;
        this.tipo = tipo;
        this.estado = estado;
    }

    public int getIdObra() {
        return idObra;
    }
    public int getIdArtista() {
        return idArtista;
    }
    public String getTitulo() {
        return titulo;
    }
    public Integer getFechaMin() {
        return fechaMin;
    }
    public Integer getFechaMax() {
        return fechaMax;
    }
    public String getTipo() {
        return tipo;
    }
    public String getEstado() {
        return estado;
    }

    public void setIdObra(int idObra) {
        this.idObra = idObra;
    }
    public void setIdArtista(int idArtista) {
        this.idArtista = idArtista;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public void setFechaMin(Integer fechaMin) {
        this.fechaMin = fechaMin;
    }
    public void setFechaMax(Integer fechaMax) {
        this.fechaMax = fechaMax;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
}
