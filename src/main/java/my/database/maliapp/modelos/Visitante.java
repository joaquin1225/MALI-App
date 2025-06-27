package my.database.maliapp.modelos;

public class Visitante {
    private int id;
    private String nombre;
    private String apellido;
    private int idIdentificacion;
    private String genero;
    private String pais;
    private String telefono;


    public Visitante(int id, String nombre, String apellido, int idIdentificacion, String genero, String pais, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.idIdentificacion = idIdentificacion;
        this.genero = genero;
        this.pais = pais;
        this.telefono = telefono;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public int getIdIdentificacion() { return idIdentificacion; }
    public String getGenero() { return genero; }
    public String getPais() { return pais; }
    public String getTelefono() { return telefono; }
}
