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
import java.time.LocalDate;

public class RegistrarBoletoView {
    private final Connection conn;
    private final Visitante visitante;

    public RegistrarBoletoView(Connection conn, Visitante visitante) {
        this.conn = conn;
        this.visitante = visitante;
    }

    public void mostrar(Stage stage) {
        Label title = new Label("Registrar boleto para:");
        Label datos = new Label(
                visitante.getNombre() + " " + visitante.getApellido() + "\n" +
                        "Género: " + visitante.getGenero() + "\n" +
                        "País: " + visitante.getPais()
        );

        ComboBox<String> tipoBoletoBox = new ComboBox<>();
        tipoBoletoBox.getItems().addAll("GEN", "EST_MYR_8", "EXTRJ");
        tipoBoletoBox.setPromptText("Tipo de boleto");

        DatePicker fechaVisitaPicker = new DatePicker();
        fechaVisitaPicker.setPromptText("Fecha de visita");

        Button registrarBtn = new Button("Registrar boleto");
        Button finalizarBtn = new Button("Finalizar");
        Label estado = new Label();

        registrarBtn.setOnAction(e -> {
            String tipoBoleto = tipoBoletoBox.getValue();
            LocalDate fechaVisita = fechaVisitaPicker.getValue();

            if (tipoBoleto == null || fechaVisita == null) {
                estado.setText("❌ Completa ambos campos.");
                return;
            }

            try {
                String sql = """
                    INSERT INTO boleto (id_visitante, tipo_boleto, fecha_visita)
                    VALUES (?, ?, ?)
                    """;

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, visitante.getId());
                    stmt.setString(2, tipoBoleto);
                    stmt.setDate(3, java.sql.Date.valueOf(fechaVisita));
                    stmt.executeUpdate();
                }

                estado.setText("✅ Boleto registrado correctamente.");
                tipoBoletoBox.setValue(null);
                fechaVisitaPicker.setValue(null);

            } catch (SQLException ex) {
                estado.setText("❌ Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        finalizarBtn.setOnAction(e -> {
            stage.close();
            new EmpleadoView(conn).mostrar(new Stage());
        });

        VBox root = new VBox(10, title, datos, tipoBoletoBox, fechaVisitaPicker,
                registrarBtn, finalizarBtn, estado);
        root.setPadding(new Insets(20));
        stage.setScene(new Scene(root, 350, 370));
        stage.setTitle("Registrar boleto");
        stage.show();
    }
}
