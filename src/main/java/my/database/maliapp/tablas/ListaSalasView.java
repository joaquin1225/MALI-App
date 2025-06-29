package my.database.maliapp.tablas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import my.database.maliapp.modelos.Sala;
import my.database.maliapp.TablaGenerica;

import java.sql.*;
import java.time.LocalDate;

public class ListaSalasView extends TablaGenerica<Sala> {

    public ListaSalasView(Connection conn) {
        super(conn);
    }

    @Override
    public ObservableList<Sala> obtenerDatos() {
        ObservableList<Sala> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM sala ORDER BY id_sala";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Sala(
                        rs.getInt("id_sala"),
                        rs.getString("nombre_sala")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public TableView<Sala> construirTabla() {
        TableView<Sala> tabla = new TableView<>();

        TableColumn<Sala, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idSala"));

        TableColumn<Sala, String> colNomb = new TableColumn<>("Nombre");
        colNomb.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        tabla.getColumns().addAll(colId, colNomb);
        return tabla;
    }

    public VBox getVista() {
        TableView<Sala> tabla = construirTabla();
        tabla.setItems(obtenerDatos());

        VBox layout = new VBox(10, tabla);
        layout.setPadding(new Insets(10));
        return layout;
    }
}
