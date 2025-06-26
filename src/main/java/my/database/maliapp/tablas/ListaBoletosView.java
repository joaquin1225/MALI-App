package my.database.maliapp.tablas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import my.database.maliapp.modelos.Boleto;
import my.database.maliapp.TablaGenerica;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ListaBoletosView extends TablaGenerica<Boleto> {

    public ListaBoletosView(Connection conn) {
        super(conn);
    }

    @Override
    public ObservableList<Boleto> obtenerDatos() {
        ObservableList<Boleto> data = FXCollections.observableArrayList();

        try (var stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM boleto")) {
            while (rs.next()) {
                Boleto b = new Boleto(
                        rs.getInt("id_boleto"),
                        rs.getInt("id_visitante"),
                        rs.getString("tipo_boleto"),
                        rs.getDate("fecha_visita").toLocalDate()
                );
                data.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    @Override
    public TableView<Boleto> construirTabla() {
        TableView<Boleto> table = new TableView<>();

        TableColumn<Boleto, Integer> idCol = new TableColumn<>("ID Boleto");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idBoleto"));

        TableColumn<Boleto, Integer> visitanteCol = new TableColumn<>("ID Visitante");
        visitanteCol.setCellValueFactory(new PropertyValueFactory<>("idVisitante"));

        TableColumn<Boleto, String> tipoCol = new TableColumn<>("Tipo de Boleto");
        tipoCol.setCellValueFactory(new PropertyValueFactory<>("tipoBoleto"));

        TableColumn<Boleto, LocalDate> fechaCol = new TableColumn<>("Fecha de Visita");
        fechaCol.setCellValueFactory(new PropertyValueFactory<>("fechaVisita"));

        table.getColumns().addAll(idCol, visitanteCol, tipoCol, fechaCol);
        return table;
    }

    public VBox getVista() {
        TableView<Boleto> tabla = construirTabla();
        tabla.setItems(obtenerDatos());

        VBox layout = new VBox(tabla);
        layout.setPadding(new Insets(10));
        return layout;
    }
}
