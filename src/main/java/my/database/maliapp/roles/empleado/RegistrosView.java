package my.database.maliapp.roles.empleado;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import my.database.maliapp.tablas.ListaBoletosView;
import my.database.maliapp.tablas.ListaIdentificacionesView;
import my.database.maliapp.tablas.ListaVisitantesView;

import java.sql.Connection;

public class RegistrosView {
    private final Connection conn;

    public RegistrosView(Connection conn) {
        this.conn = conn;
    }

    public void mostrar(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Label title = new Label("Registros");

        Button btnVisitantes = new Button("Visitantes");
        Button btnIdentificaciones = new Button("Identificaciones");
        Button btnBoletos = new Button("Boletos");

        ToolBar navBar = new ToolBar(btnVisitantes, btnIdentificaciones, btnBoletos);
        root.setTop(navBar);

        StackPane contentPane = new StackPane();
        root.setCenter(contentPane);

        btnVisitantes.setOnAction(e -> {
            contentPane.getChildren().setAll(new ListaVisitantesView(conn).getVista());
        });

        btnIdentificaciones.setOnAction(e -> {
            contentPane.getChildren().setAll(new ListaIdentificacionesView(conn).getVista());
        });

        btnBoletos.setOnAction(e -> {
            contentPane.getChildren().setAll(new ListaBoletosView(conn).getVista());
        });

        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Acceso a registros");
        stage.getIcons().add(new Image(getClass().getResource("/img/mali.jpg").toExternalForm()));
        stage.show();
    }
}
