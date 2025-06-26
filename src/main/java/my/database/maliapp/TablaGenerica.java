package my.database.maliapp;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;

public abstract class TablaGenerica<T> {
    protected final Connection conn;

    public TablaGenerica(Connection conn) {
        this.conn = conn;
    }

    // metodo que devuelve los datos a mostrar en la tabla
    public abstract ObservableList<T> obtenerDatos();

    // metodo que devuelve la tabla configurada
    public abstract TableView<T> construirTabla();

    // metodo que muestra la tabla en una ventana
    public void mostrar(Stage stage) {
        TableView<T> tabla = construirTabla();
        tabla.setItems(obtenerDatos());
        VBox root = new VBox(tabla);
        stage.setScene(new Scene(root, 700, 400));
        stage.setTitle("Vista de " + this.getClass().getSimpleName());
        stage.show();
    }
}
