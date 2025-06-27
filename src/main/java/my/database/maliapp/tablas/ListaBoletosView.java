package my.database.maliapp.tablas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import my.database.maliapp.modelos.Boleto;
import my.database.maliapp.TablaGenerica;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ListaBoletosView extends TablaGenerica<Boleto> {

    public ListaBoletosView(Connection conn) {
        super(conn);
    }

    @Override
    public void mostrar(Stage stage) {
        TableView<Boleto> tabla = construirTabla();
        tabla.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tabla.setItems(obtenerDatos());

        Label estado = new Label();

        Button eliminarBtn = new Button("Eliminar boletos seleccionados");
        eliminarBtn.setOnAction(e -> {
            ObservableList<Boleto> seleccionados = tabla.getSelectionModel().getSelectedItems();
            if (seleccionados == null || seleccionados.isEmpty()) {
                estado.setText("❗ No hay boletos seleccionados para eliminar.");
                return;
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmar Eliminación");
            confirm.setHeaderText("¿Estás seguro de eliminar los boletos seleccionados?");
            confirm.setContentText("Esta acción no se puede deshacer.");

            confirm.showAndWait().ifPresent(respuesta -> {
                if (respuesta == ButtonType.OK) {
                    for (Boleto b : seleccionados) {
                        eliminarBoleto(b.getIdBoleto());
                    }
                    tabla.setItems(obtenerDatos());
                    estado.setText("✅ Boletos eliminados correctamente.");
                }
            });
        });

        VBox root = new VBox(10, tabla, eliminarBtn, estado);
        root.setPadding(new Insets(15));
        stage.setScene(new Scene(root, 600, 400));
        stage.setTitle("Lista de boletos");
        stage.show();
    }

    private void eliminarBoleto(int id) {
        String sql = "DELETE FROM boleto WHERE id_boleto = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
