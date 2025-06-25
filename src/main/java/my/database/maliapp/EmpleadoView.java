package my.database.maliapp;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmpleadoView {
    private final Connection conn;

    public EmpleadoView(Connection connection) {
        this.conn = connection;
    }

    public void mostrar(Stage stage) {
        Label title = new Label("Registrar nuevo visitante");

        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre");

        TextField apellidoField = new TextField();
        apellidoField.setPromptText("Apellido");

        ComboBox<String> generoBox = new ComboBox<>();
        generoBox.getItems().addAll("M", "F", "O");
        generoBox.setPromptText("Genero");

        TextField paisField = new TextField();
        paisField.setPromptText("Pais");

        TextField telefonoField = new TextField();
        telefonoField.setPromptText("Telefono");

        Button registrar = new Button("Registrar");
        Label status = new Label();

        registrar.setOnAction(e -> {
            String nombre = nombreField.getText();
            String apellido = apellidoField.getText();
            String genero = generoBox.getValue();
            String pais_origen = paisField.getText();
            String telefono = telefonoField.getText();

            if (nombre.isEmpty() || apellido.isEmpty() || genero == null || pais_origen.isEmpty() || telefono.isEmpty()) {
                status.setText("Por favor, complete todos los campos.");
                return;
            }
            if (!telefono.matches("\\d{9}")) {
                status.setText("El teléfono debe tener 9 dígitos.");
                return;
            }

            String sql = "INSERT INTO visitante (nombre, apellido, genero, pais_origen, telefono) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nombre);
                stmt.setString(2, apellido);
                stmt.setString(3, genero);
                stmt.setString(4, pais_origen);
                stmt.setString(5, telefono);
                stmt.executeUpdate();
                status.setText("Visitante registrado exitosamente.");
                nombreField.clear();
                apellidoField.clear();
                generoBox.setValue("Genero");
                paisField.clear();
                telefonoField.clear();
            } catch (SQLException ex) {
                status.setText("Error: " + ex.getMessage());
            }
        });

        Button verVisitantesBtn = new Button("Ver visitantes registrados");
        verVisitantesBtn.setOnAction(e -> {
            new ListaVisitantesView(conn).mostrar(new Stage());
        });

        VBox root = new VBox(10, title, nombreField, apellidoField, generoBox, paisField, telefonoField, registrar, verVisitantesBtn, status);
        root.setPadding(new Insets(20));

        stage.setScene(new Scene(root, 350, 400));
        stage.setTitle("Empleado – Registro de visitante");
        stage.show();
    }
}
