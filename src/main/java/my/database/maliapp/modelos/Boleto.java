package my.database.maliapp.modelos;

import java.time.LocalDate;

public class Boleto {
    private int idBoleto;
    private int idVisitante;
    private String tipoBoleto;
    private LocalDate fechaVisita;

    public Boleto(int id, int idVisitante, String tipoBoleto, LocalDate fecha) {
        this.idBoleto = id;
        this.idVisitante = idVisitante;
        this.tipoBoleto = tipoBoleto;
        this.fechaVisita = fecha;
    }

    public int getIdBoleto() {
        return idBoleto;
    }
    public int getIdVisitante() {
        return idVisitante;
    }
    public String getTipoBoleto() {
        return tipoBoleto;
    }
    public LocalDate getFechaVisita() {
        return fechaVisita;
    }

    public void setIdBoleto(int idBoleto) {
        this.idBoleto = idBoleto;
    }
    public void setIdVisitante(int idVisitante) {
        this.idVisitante = idVisitante;
    }
    public void setTipoBoleto(String tipoBoleto) {
        this.tipoBoleto = tipoBoleto;
    }
    public void setFechaVisita(LocalDate fechaVisita) {
        this.fechaVisita = fechaVisita;
    }
}
