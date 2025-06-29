package my.database.maliapp.modelos;

public class Departamento {
    private int idDep;
    private String nombreDep;
    private Integer idJefe;

    public Departamento(int idDep, String nombreDep, Integer idJefe) {
        this.idDep = idDep;
        this.nombreDep = nombreDep;
        this.idJefe = idJefe;
    }

    public int getIdDep() {
        return idDep;
    }

    public void setIdDep(int idDep) {
        this.idDep = idDep;
    }

    public String getNombreDep() {
        return nombreDep;
    }

    public void setNombreDep(String nombreDep) {
        this.nombreDep = nombreDep;
    }

    public Integer getIdJefe() {
        return idJefe;
    }

    public void setIdJefe(Integer idJefe) {
        this.idJefe = idJefe;
    }

    @Override
    public String toString() {
        return nombreDep;
    }
}
