package my.database.maliapp.tablas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import my.database.maliapp.modelos.Trabajo;
import my.database.maliapp.TablaGenerica;

import java.sql.*;
import java.time.LocalDate;

public class ListaTrabajosView extends TablaGenerica<Trabajo> {

    public ListaTrabajosView(Connection conn) {
        super(conn);
    }

    @Override
    public ObservableList<Trabajo> obtenerDatos() {
        ObservableList<Trabajo> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM trabajo ORDER BY id_trabajo";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Trabajo(
                        rs.getInt("id_trabajo"),
                        rs.getString("descripcion"),
                        rs.getDate("fecha_inicio").toLocalDate(),
                        rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toLocalDate() : null
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public TableView<Trabajo> construirTabla() {
        TableView<Trabajo> tabla = new TableView<>();

        TableColumn<Trabajo, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idTrabajo"));

        TableColumn<Trabajo, String> colDesc = new TableColumn<>("Descripción");
        colDesc.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        TableColumn<Trabajo, LocalDate> colInicio = new TableColumn<>("Inicio");
        colInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));

        TableColumn<Trabajo, LocalDate> colFin = new TableColumn<>("Fin");
        colFin.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));

        tabla.getColumns().addAll(colId, colDesc, colInicio, colFin);
        return tabla;
    }

    public VBox getVista() {
        TableView<Trabajo> tabla = construirTabla();
        tabla.setItems(obtenerDatos());

        VBox layout = new VBox(10, tabla);
        layout.setPadding(new Insets(10));
        return layout;
    }
}
