package my.database.maliapp.tablas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import my.database.maliapp.TablaGenerica;
import my.database.maliapp.modelos.Identificacion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ListaIdentificacionesView extends TablaGenerica<Identificacion> {

    public ListaIdentificacionesView(Connection conn) {
        super(conn);
    }

    @Override
    public ObservableList<Identificacion> obtenerDatos() {
        ObservableList<Identificacion> data = FXCollections.observableArrayList();
        try (var stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM identificacion")) {
            while (rs.next()) {
                data.add(new Identificacion(
                        rs.getInt("id_identificacion"),
                        rs.getInt("id_visitante"),
                        rs.getString("forma"),
                        rs.getString("numero")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public TableView<Identificacion> construirTabla() {
        TableView<Identificacion> table = new TableView<>();

        TableColumn<Identificacion, Integer> idIdentCol = new TableColumn<>("ID Identificación");
        idIdentCol.setCellValueFactory(new PropertyValueFactory<>("idIdentificacion"));

        TableColumn<Identificacion, Integer> idVisitanteCol = new TableColumn<>("ID Visitante");
        idVisitanteCol.setCellValueFactory(new PropertyValueFactory<>("idVisitante"));

        TableColumn<Identificacion, String> formaCol = new TableColumn<>("Forma");
        formaCol.setCellValueFactory(new PropertyValueFactory<>("forma"));

        TableColumn<Identificacion, String> numeroCol = new TableColumn<>("Número");
        numeroCol.setCellValueFactory(new PropertyValueFactory<>("numero"));

        table.getColumns().addAll(idIdentCol, idVisitanteCol, formaCol, numeroCol);
        return table;
    }
}
