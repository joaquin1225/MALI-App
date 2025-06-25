package my.database.maliapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ListaVisitantesView {

    private final Connection conn;

    public ListaVisitantesView(Connection connection) {
        this.conn = connection;
    }

    public void mostrar(Stage stage) {
        TableView<Visitante> table = new TableView<>();
        ObservableList<Visitante> data = FXCollections.observableArrayList();

        // Columnas
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

        // Llenar tabla con datos de la BD
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

        table.setItems(data);
        VBox root = new VBox(table);
        stage.setScene(new Scene(root, 700, 400));
        stage.setTitle("Lista de visitantes");
        stage.show();
    }
}
