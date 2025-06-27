package my.database.maliapp.modelos;

public class Identificacion {
    private int idIdentificacion;
    private String forma;
    private String numero;

    public Identificacion(int idIdentificacion, String forma, String numero) {
        this.idIdentificacion = idIdentificacion;
        this.forma = forma;
        this.numero = numero;
    }

    public Identificacion(String forma, String numero) {
        this(-1, forma, numero);
    }

    public int getIdIdentificacion() {
        return idIdentificacion;
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
    public void setForma(String forma) {
        this.forma = forma;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
}
