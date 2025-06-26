package my.database.maliapp.tablas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import my.database.maliapp.TablaGenerica;
import my.database.maliapp.modelos.Visitante;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ListaVisitantesView extends TablaGenerica<Visitante> {

    public ListaVisitantesView(Connection conn) {
        super(conn);
    }

    @Override
    public ObservableList<Visitante> obtenerDatos() {
        ObservableList<Visitante> data = FXCollections.observableArrayList();
        try (var stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM visitante")) {
            while (rs.next()) {
                data.add(new Visitante(
                        rs.getInt("id_visitante"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("genero"),
                        rs.getString("pais_origen"),
                        rs.getString("telefono")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public TableView<Visitante> construirTabla() {
        TableView<Visitante> table = new TableView<>();

        TableColumn<Visitante, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Visitante, String> nombreCol = new TableColumn<>("Nombre");
        nombreCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Visitante, String> apellidoCol = new TableColumn<>("Apellido");
        apellidoCol.setCellValueFactory(new PropertyValueFactory<>("apellido"));

        TableColumn<Visitante, String> generoCol = new TableColumn<>("Género");
        generoCol.setCellValueFactory(new PropertyValueFactory<>("genero"));

        TableColumn<Visitante, String> paisCol = new TableColumn<>("País");
        paisCol.setCellValueFactory(new PropertyValueFactory<>("pais"));

        TableColumn<Visitante, String> telefonoCol = new TableColumn<>("Teléfono");
        telefonoCol.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        table.getColumns().addAll(idCol, nombreCol, apellidoCol, generoCol, paisCol, telefonoCol);
        return table;
    }
}
