package my.database.maliapp;

public class Visitante {
    private int id;
    private String nombre;
    private String apellido;
    private String genero;
    private String pais;
    private String telefono;

    public Visitante(int id, String nombre, String apellido, String genero, String pais, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.genero = genero;
        this.pais = pais;
        this.telefono = telefono;
    }

    // Getters necesarios para TableView
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getGenero() { return genero; }
    public String getPais() { return pais; }
    public String getTelefono() { return telefono; }
}
