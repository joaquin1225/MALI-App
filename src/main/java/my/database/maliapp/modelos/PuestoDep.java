package my.database.maliapp.modelos;

import java.math.BigDecimal;

public class PuestoDep {
    private int idDep;
    private String puesto;
    private BigDecimal sueldo;

    public PuestoDep(int idDep, String puesto, BigDecimal sueldo) {
        this.idDep = idDep;
        this.puesto = puesto;
        this.sueldo = sueldo;
    }

    public int getIdDep() {
        return idDep;
    }

    public void setIdDep(int idDep) {
        this.idDep = idDep;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public BigDecimal getSueldo() {
        return sueldo;
    }

    public void setSueldo(BigDecimal sueldo) {
        this.sueldo = sueldo;
    }

    @Override
    public String toString() {
        return puesto + " (S/ " + sueldo + ")";
    }
}
