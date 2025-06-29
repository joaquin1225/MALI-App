package my.database.maliapp.roles.jefe_empleado;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import my.database.maliapp.modelos.ObraDeArte;
import my.database.maliapp.tablas.ListaObrasView;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;

public class SeleccionarObrasView extends ListaObrasView {
    private final Set<ObraDeArte> obrasSeleccionadas = new HashSet<>();
    private final Runnable alVolver;
    private final java.util.function.Consumer<Set<ObraDeArte>> alConfirmar;

    public SeleccionarObrasView(Connection conn, Runnable alVolver, java.util.function.Consumer<Set<ObraDeArte>> alConfirmar) {
        super(conn);
        this.alVolver = alVolver;
        this.alConfirmar = alConfirmar;
    }

    public VBox getVista() {
        TableView<ObraDeArte> tabla = construirTabla();
        tabla.setItems(obtenerDatos());

        TableColumn<ObraDeArte, Boolean> colSeleccion = new TableColumn<>("Seleccionar");
        colSeleccion.setCellFactory(tc -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.setOnAction(e -> {
                    ObraDeArte obra = getTableView().getItems().get(getIndex());
                    if (checkBox.isSelected()) {
                        obrasSeleccionadas.add(obra);
                    } else {
                        obrasSeleccionadas.remove(obra);
                    }
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(checkBox);
                    ObraDeArte obra = getTableView().getItems().get(getIndex());
                    checkBox.setSelected(obrasSeleccionadas.contains(obra));
                }
            }
        });

        tabla.getColumns().add(0, colSeleccion);

        Button btnVolver = new Button("Volver");
        Button btnConfirmar = new Button("Confirmar selección");

        btnVolver.setOnAction(e -> alVolver.run());
        btnConfirmar.setOnAction(e -> alConfirmar.accept(obrasSeleccionadas));

        VBox layout = new VBox(10, tabla, new Separator(), btnConfirmar, btnVolver);
        layout.setPadding(new Insets(10));
        return layout;
    }
}
