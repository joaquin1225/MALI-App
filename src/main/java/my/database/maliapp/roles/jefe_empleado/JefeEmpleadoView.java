package my.database.maliapp.roles.jefe_empleado;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import my.database.maliapp.HelloApplication;

import java.sql.Connection;

public class JefeEmpleadoView {
    private final Connection conn;

    public JefeEmpleadoView(Connection conn) {
        this.conn = conn;
    }

    public void mostrar(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Label titulo = new Label("Panel del Jefe de Empleados");

        StackPane contentPane = new StackPane();
        root.setCenter(contentPane);

        contentPane.getChildren().setAll(new ModificarTrabajosView(conn).getVista());

        Button cerrarSesionBtn = new Button("Cerrar sesión");
        cerrarSesionBtn.setOnAction(e -> {
            stage.close();
            try {
                new HelloApplication().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        BorderPane botonesInferiores = new BorderPane();
        botonesInferiores.setPadding(new Insets(10));
        botonesInferiores.setRight(cerrarSesionBtn);

        root.setBottom(botonesInferiores);

        Scene scene = new Scene(root, 700, 450);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Jefe de Empleados");
        stage.getIcons().add(new Image(getClass().getResource("/img/mali.jpg").toExternalForm()));
        stage.show();
    }
}
