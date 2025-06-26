package my.database.maliapp.modelos;

public class Identificacion {
    private int idIdentificacion;
    private int idVisitante;
    private String forma;
    private String numero;

    public Identificacion(int idIdentificacion, int idVisitante, String forma, String numero) {
        this.idIdentificacion = idIdentificacion;
        this.idVisitante = idVisitante;
        this.forma = forma;
        this.numero = numero;
    }

    public Identificacion(int idVisitante, String forma, String numero) {
        this(-1, idVisitante, forma, numero);
    }

    public int getIdIdentificacion() {
        return idIdentificacion;
    }
    public int getIdVisitante() {
        return idVisitante;
    }
    public String getForma() {
        return forma;
    }
    public String getNumero() {
        return numero;
    }


    public void setIdIdentificacion(int idIdentificacion) {
        this.idIdentificacion = idIdentificacion;
    }
    public void setIdVisitante(int idVisitante) {
        this.idVisitante = idVisitante;
    }
    public void setForma(String forma) {
        this.forma = forma;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
}
