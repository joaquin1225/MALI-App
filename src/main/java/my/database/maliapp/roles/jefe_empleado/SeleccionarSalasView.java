package my.database.maliapp.roles.jefe_empleado;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import my.database.maliapp.modelos.Sala;
import my.database.maliapp.tablas.ListaSalasView;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;

public class SeleccionarSalasView extends ListaSalasView {

    private final Set<Sala> salasSeleccionadas = new HashSet<>();
    private Runnable alVolver;
    private java.util.function.Consumer<Set<Sala>> alConfirmar;

    public SeleccionarSalasView(Connection conn) {
        super(conn);
    }

    public void setOnVolver(Runnable alVolver) {
        this.alVolver = alVolver;
    }

    public void setOnConfirmar(java.util.function.Consumer<Set<Sala>> alConfirmar) {
        this.alConfirmar = alConfirmar;
    }

    @Override
    public VBox getVista() {
        TableView<Sala> tabla = construirTabla();
        ObservableList<Sala> datos = obtenerDatos();
        tabla.setItems(datos);

        TableColumn<Sala, Void> seleccionarCol = new TableColumn<>("Seleccionar");
        seleccionarCol.setCellFactory(tc -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.setOnAction(e -> {
                    Sala sala = getTableView().getItems().get(getIndex());
                    if (checkBox.isSelected()) {
                        salasSeleccionadas.add(sala);
                    } else {
                        salasSeleccionadas.remove(sala);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Sala sala = getTableView().getItems().get(getIndex());
                    checkBox.setSelected(salasSeleccionadas.contains(sala));
                    setGraphic(checkBox);
                }
            }
        });

        tabla.getColumns().add(0, seleccionarCol);

        Button btnVolver = new Button("Volver");
        btnVolver.setOnAction(e -> {
            if (alVolver != null) alVolver.run();
        });

        Button btnConfirmar = new Button("Confirmar selección");
        btnConfirmar.setOnAction(e -> {
            if (alConfirmar != null) alConfirmar.accept(salasSeleccionadas);
        });

        HBox botones = new HBox(10, btnVolver, btnConfirmar);
        VBox layout = new VBox(10, tabla, botones);
        layout.setPadding(new Insets(10));
        return layout;
    }
}
