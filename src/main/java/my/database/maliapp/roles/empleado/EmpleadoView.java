package my.database.maliapp.roles.empleado;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import my.database.maliapp.HelloApplication;
import my.database.maliapp.modelos.Visitante;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmpleadoView {
    private final Connection conn;

    public EmpleadoView(Connection connection) {
        this.conn = connection;
    }

    public void mostrar(Stage stage) {
        Label title = new Label("Ingresar identificación del visitante");

        ComboBox<String> formaBox = new ComboBox<>();
        formaBox.getItems().addAll("DNI", "PASAPORTE", "CE");
        formaBox.setValue("DNI");

        TextField numeroField = new TextField();
        numeroField.setPromptText("Número de identificación");

        Button buscarBtn = new Button("Buscar visitante");
        Label estado = new Label();

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));

        form.add(new Label("Tipo de identificación:"), 0, 0);
        form.add(formaBox, 1, 0);
        form.add(new Label("Número de identificación:"), 0, 1);
        form.add(numeroField, 1, 1);
        form.add(buscarBtn, 1, 2);
        form.add(estado, 1, 3);

        buscarBtn.setOnAction(e -> {
            String forma = formaBox.getValue();
            String numero = numeroField.getText();

            if (forma == null || numero.isEmpty()) {
                estado.setText("❌ Completa ambos campos.");
                return;
            }

            String sql = """
                SELECT * FROM visitante v 
                JOIN identificacion i ON v.id_ident = i.id_ident 
                WHERE i.forma = ? AND i.numero = ?
                """;

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, forma);
                stmt.setString(2, numero);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    Visitante visitante = new Visitante(
                            rs.getInt("id_visitante"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getInt("id_ident"),
                            rs.getString("genero"),
                            rs.getString("pais_origen"),
                            rs.getString("telefono")

                    );
                    new RegistrarBoletoView(conn, visitante).mostrar(new Stage());
                    stage.close();
                } else {
                    new RegistrarVisitanteView(conn, forma, numero).mostrar(new Stage());
                    stage.close();
                }
            } catch (SQLException ex) {
                estado.setText("❌ Error al buscar: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        Button accederRegistrosBtn = new Button("Acceder a registros");
        accederRegistrosBtn.setOnAction(e -> new RegistrosView(conn).mostrar(new Stage()));

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
        botonesInferiores.setLeft(accederRegistrosBtn);
        botonesInferiores.setRight(cerrarSesionBtn);

        VBox content = new VBox(10, title, form);
        content.setPadding(new Insets(20));

        BorderPane root = new BorderPane();
        root.setCenter(content);
        root.setBottom(botonesInferiores);

        Scene scene = new Scene(root, 450, 330);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Empleado – Buscar visitante");
        stage.getIcons().add(new Image(getClass().getResource("/img/mali.jpg").toExternalForm()));
        stage.show();
    }
}
