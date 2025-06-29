package my.database.maliapp.tablas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import my.database.maliapp.TablaGenerica;
import my.database.maliapp.modelos.ObraDeArte;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ListaObrasView extends TablaGenerica<ObraDeArte> {

    public ListaObrasView(Connection conn) {
        super(conn);
    }

    @Override
    public ObservableList<ObraDeArte> obtenerDatos() {
        ObservableList<ObraDeArte> lista = FXCollections.observableArrayList();

        String sql = "SELECT * FROM obra_de_arte ORDER BY id_obra";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ObraDeArte obra = new ObraDeArte(
                        rs.getInt("id_obra"),
                        rs.getInt("id_artista"),
                        rs.getString("titulo"),
                        (Integer) rs.getObject("fecha_min"),
                        (Integer) rs.getObject("fecha_max"),
                        rs.getString("tipo"),
                        rs.getString("estado")
                );
                lista.add(obra);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public TableView<ObraDeArte> construirTabla() {
        TableView<ObraDeArte> tabla = new TableView<>();

        TableColumn<ObraDeArte, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idObra"));

        TableColumn<ObraDeArte, Integer> colArtista = new TableColumn<>("ID Artista");
        colArtista.setCellValueFactory(new PropertyValueFactory<>("idArtista"));

        TableColumn<ObraDeArte, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));

        TableColumn<ObraDeArte, Integer> colMin = new TableColumn<>("Fecha Mín.");
        colMin.setCellValueFactory(new PropertyValueFactory<>("fechaMin"));

        TableColumn<ObraDeArte, Integer> colMax = new TableColumn<>("Fecha Máx.");
        colMax.setCellValueFactory(new PropertyValueFactory<>("fechaMax"));

        TableColumn<ObraDeArte, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        TableColumn<ObraDeArte, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tabla.getColumns().addAll(colId, colArtista, colTitulo, colMin, colMax, colTipo, colEstado);
        return tabla;
    }

    public VBox getVista() {
        TableView<ObraDeArte> tabla = construirTabla();
        tabla.setItems(obtenerDatos());

        VBox layout = new VBox(tabla);
        layout.setPadding(new Insets(10));
        return layout;
    }
}
