package my.database.maliapp.tablas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import my.database.maliapp.modelos.Boleto;
import my.database.maliapp.TablaGenerica;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ListaBoletosView extends TablaGenerica<Boleto> {

    public ListaBoletosView(Connection conn) {
        super(conn);
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

        Map<Boleto, CheckBox> checkboxMap = new HashMap<>();

        TableColumn<Boleto, Boolean> colSeleccion = new TableColumn<>("Seleccionar");
        colSeleccion.setCellFactory(tc -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Boleto b = getTableView().getItems().get(getIndex());
                    checkboxMap.put(b, checkBox);
                    checkBox.setSelected(false);
                    setGraphic(checkBox);
                }
            }
        });

        Label estado = new Label();
        Button btnModoEliminar = new Button("Eliminar boletos");
        Button btnEliminar = new Button("Eliminar");
        Button btnCancelar = new Button("Cancelar");
        HBox botones = new HBox(10, btnModoEliminar);

        btnModoEliminar.setOnAction(e -> {
            tabla.getColumns().add(0, colSeleccion);
            botones.getChildren().setAll(btnEliminar, btnCancelar);
            estado.setText("✅ Modo eliminación activado. Marca los boletos a eliminar.");
        });

        btnCancelar.setOnAction(e -> {
            tabla.getColumns().remove(colSeleccion);
            tabla.setItems(obtenerDatos());
            botones.getChildren().setAll(btnModoEliminar);
            checkboxMap.clear();
            estado.setText("❌ Eliminación cancelada.");
        });

        btnEliminar.setOnAction(e -> {
            ObservableList<Boleto> todos = tabla.getItems();
            ObservableList<Boleto> seleccionados = FXCollections.observableArrayList();

            for (Boleto b : todos) {
                CheckBox cb = checkboxMap.get(b);
                if (cb != null && cb.isSelected()) {
                    seleccionados.add(b);
                }
            }

            if (seleccionados.isEmpty()) {
                estado.setText("❗ No has seleccionado boletos para eliminar.");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmar eliminación");
            confirm.setHeaderText("¿Seguro que deseas eliminar los boletos seleccionados?");
            confirm.setContentText("Esta acción no se puede deshacer.");

            confirm.showAndWait().ifPresent(respuesta -> {
                if (respuesta == ButtonType.OK) {
                    for (Boleto b : seleccionados) {
                        eliminarBoleto(b.getIdBoleto());
                    }

                    tabla.getColumns().remove(colSeleccion);
                    tabla.setItems(obtenerDatos());
                    checkboxMap.clear();
                    botones.getChildren().setAll(btnModoEliminar);
                    estado.setText("✅ Se eliminaron " + seleccionados.size() + " boletos correctamente.");
                }
            });
        });

        VBox layout = new VBox(10, tabla, botones, estado);
        layout.setPadding(new Insets(10));
        return layout;
    }


}
