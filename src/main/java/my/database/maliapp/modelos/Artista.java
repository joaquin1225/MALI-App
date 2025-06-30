package my.database.maliapp.modelos;

public class Artista {
    private int idArtista;
    private String nombre;
    private String apellido;
    private String pais;
    private Integer fechaNac;
    private Integer fechaFallec;

    public Artista(int idArtista, String nombre, String apellido, String pais, Integer fechaNac, Integer fechaFallec) {
        this.idArtista = idArtista;
        this.nombre = nombre;
        this.apellido = apellido;
        this.pais = pais;
        this.fechaNac = fechaNac;
        this.fechaFallec = fechaFallec;
    }

    public int getIdArtista() {
        return idArtista;
    }
    public String getNombre() {
        return nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public String getPais() {
        return pais;
    }
    public Integer getFechaNac() {
        return fechaNac;
    }
    public Integer getFechaFallec() {
        return fechaFallec;
    }

    public void setIdArtista(int idArtista) {
        this.idArtista = idArtista;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public void setPais(String pais) {
        this.pais = pais;
    }
    public void setFechaNac(Integer fechaNac) {
        this.fechaNac = fechaNac;
    }
    public void setFechaFallec(Integer fechaFallec) {
        this.fechaFallec = fechaFallec;
    }

    @Override
    public String toString() {
        return nombre + (apellido != null ? " " + apellido : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Artista)) return false;
        Artista a = (Artista) o;
        return idArtista == a.idArtista;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idArtista);
    }
}
