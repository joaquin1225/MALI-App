package my.database.maliapp.roles.empleado;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import my.database.maliapp.modelos.Visitante;
import my.database.maliapp.HelloApplication;

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
        formaBox.getItems().addAll("DNI", "Pasaporte", "Carnet");
        formaBox.setPromptText("Tipo de identificación");

        TextField numeroField = new TextField();
        numeroField.setPromptText("Número de identificación");

        Button buscarBtn = new Button("Buscar visitante");
        Label estado = new Label();

        buscarBtn.setOnAction(e -> {
            String forma = formaBox.getValue();
            String numero = numeroField.getText();

            if (forma == null || numero.isEmpty()) {
                estado.setText("Completa ambos campos.");
                return;
            }

            try {
                String sql = "SELECT v.id_visitante, v.nombre, v.apellido, v.genero, v.pais_origen " +
                        "FROM visitante v " +
                        "JOIN identificacion i ON v.id_visitante = i.id_visitante " +
                        "WHERE i.forma = ? AND i.numero = ?";

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, forma);
                    stmt.setString(2, numero);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        Visitante visitante = new Visitante(
                                rs.getInt("id_visitante"),
                                rs.getString("nombre"),
                                rs.getString("apellido"),
                                rs.getString("genero"),
                                rs.getString("pais_origen"),
                                null
                        );
                        new RegistrarBoletoView(conn, visitante).mostrar(new Stage());
                        stage.close();
                    } else {
                        new RegistrarVisitanteView(conn, forma, numero).mostrar(new Stage());
                        stage.close();
                    }
                }
            } catch (SQLException ex) {
                estado.setText("Error al buscar: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        Button verVisitantesBtn = new Button("Ver visitantes");
        verVisitantesBtn.setOnAction(e -> new my.database.maliapp.tablas.ListaVisitantesView(conn).mostrar(new Stage()));

        Button verBoletosBtn = new Button("Ver boletos");
        verBoletosBtn.setOnAction(e -> new my.database.maliapp.tablas.ListaBoletosView(conn).mostrar(new Stage()));

        Button verIdentificacionesBtn = new Button("Ver identificaciones");
        verIdentificacionesBtn.setOnAction(e -> new my.database.maliapp.tablas.ListaIdentificacionesView(conn).mostrar(new Stage()));

        Button cerrarSesionBtn = new Button("Cerrar sesión");
        cerrarSesionBtn.setOnAction(e -> {
            stage.close();
            try {
                new HelloApplication().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox root = new VBox(10, title, formaBox, numeroField, buscarBtn, verVisitantesBtn, verBoletosBtn, verIdentificacionesBtn, cerrarSesionBtn, estado);
        root.setPadding(new Insets(20));
        stage.setScene(new Scene(root, 350, 350));
        stage.setTitle("Empleado – Buscar visitante");
        stage.show();
    }
}
