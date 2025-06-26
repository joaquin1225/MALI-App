package my.database.maliapp.roles.empleado;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import my.database.maliapp.modelos.Visitante;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistrarVisitanteView {
    private final Connection conn;
    private final String forma;
    private final String numero;

    public RegistrarVisitanteView(Connection conn, String forma, String numero) {
        this.conn = conn;
        this.forma = forma;
        this.numero = numero;
    }

    public void mostrar(Stage stage) {
        Label title = new Label("Registrar nuevo visitante");

        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre");

        TextField apellidoField = new TextField();
        apellidoField.setPromptText("Apellido");

        ComboBox<String> generoBox = new ComboBox<>();
        generoBox.getItems().addAll("M", "F", "O");
        generoBox.setPromptText("Género");

        TextField paisField = new TextField();
        paisField.setPromptText("País de origen");

        TextField telefonoField = new TextField();
        telefonoField.setPromptText("Teléfono (9 dígitos)");

        Button registrarBtn = new Button("Registrar visitante");
        Label status = new Label();

        registrarBtn.setOnAction(e -> {
            String nombre = nombreField.getText();
            String apellido = apellidoField.getText();
            String genero = generoBox.getValue();
            String pais = paisField.getText();
            String telefono = telefonoField.getText();

            if (nombre.isEmpty() || apellido.isEmpty() || genero == null || pais.isEmpty() || telefono.isEmpty()) {
                status.setText("❌ Completa todos los campos.");
                return;
            }

            if (!telefono.matches("\\d{9}")) {
                status.setText("❌ Teléfono inválido.");
                return;
            }

            try {
                String sqlVisitante = """
                    INSERT INTO visitante (nombre, apellido, genero, pais_origen, telefono)
                    VALUES (?, ?, ?, ?, ?)
                    RETURNING id_visitante
                    """;

                int idVisitante;

                try (PreparedStatement stmt = conn.prepareStatement(sqlVisitante)) {
                    stmt.setString(1, nombre);
                    stmt.setString(2, apellido);
                    stmt.setString(3, genero);
                    stmt.setString(4, pais);
                    stmt.setString(5, telefono);

                    var rs = stmt.executeQuery();
                    if (rs.next()) {
                        idVisitante = rs.getInt("id_visitante");
                    } else {
                        status.setText("❌ No se pudo obtener ID del visitante.");
                        return;
                    }
                }

                String sqlIdent = """
                    INSERT INTO identificacion (id_visitante, forma, numero)
                    VALUES (?, ?, ?)
                    """;

                try (PreparedStatement stmt = conn.prepareStatement(sqlIdent)) {
                    stmt.setInt(1, idVisitante);
                    stmt.setString(2, forma);
                    stmt.setString(3, numero);
                    stmt.executeUpdate();
                }

                Visitante visitante = new Visitante(idVisitante, nombre, apellido, genero, pais, telefono);
                new RegistrarBoletoView(conn, visitante).mostrar(new Stage());
                stage.close();

            } catch (SQLException ex) {
                status.setText("❌ Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        VBox root = new VBox(10, title,
                nombreField, apellidoField, generoBox, paisField, telefonoField,
                registrarBtn, status);
        root.setPadding(new Insets(20));
        stage.setScene(new Scene(root, 350, 400));
        stage.setTitle("Registrar visitante");
        stage.show();
    }
}
