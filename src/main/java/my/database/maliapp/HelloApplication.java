package my.database.maliapp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import my.database.maliapp.roles.empleado.EmpleadoView;
import my.database.maliapp.roles.jefe_empleado.JefeEmpleadoView;
import my.database.maliapp.roles.admin_arte.AdminArteView;

import java.sql.Connection;
import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        Image logo = new Image(getClass().getResource("/img/mali.jpg").toExternalForm());
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(120);
        logoView.setPreserveRatio(true);

        Label title = new Label("Iniciar sesión en MALI DB");

        Label userLabel = new Label("Usuario:");
        TextField userField = new TextField();
        Filtros.bloquearTildes(userField);

        Label passLabel = new Label("Contraseña:");
        PasswordField passField = new PasswordField();

        Button loginButton = new Button("Iniciar sesión");
        Label messageLabel = new Label();

        passField.setOnAction(e -> loginButton.fire());

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));
        form.setAlignment(Pos.CENTER);

        form.add(userLabel, 0, 0);
        form.add(userField, 1, 0);
        form.add(passLabel, 0, 1);
        form.add(passField, 1, 1);
        form.add(loginButton, 1, 2);
        form.add(messageLabel, 1, 3);

        loginButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();

            try {
                Connection conn = DBConnection.connect(username, password);

                var stmt = conn.createStatement();
                var rs = stmt.executeQuery("SELECT current_user");

                if (rs.next()) {
                    String currentUser = rs.getString(1);
                    messageLabel.setText("Conectado como: " + currentUser);

                    switch (currentUser) {
                        case "empleado" -> {
                            new EmpleadoView(conn).mostrar(new Stage());
                            stage.close();
                        }
                        case "jefe_empleado" -> {
                            new JefeEmpleadoView(conn).mostrar(new Stage());
                            stage.close();
                        }
                        case "admin_arte" -> {
                            new AdminArteView(conn).mostrar(new Stage());
                            stage.close();
                        }
                        default -> System.out.println("Rol no reconocido");
                    }
                }
            } catch (SQLException ex) {
                String mensaje = ex.getMessage().toLowerCase();
                if (mensaje.contains("authentication") || mensaje.contains("password") || ex.getSQLState().equals("28P01")) {
                    messageLabel.setText("❌ Usuario o contraseña inválidos.");
                    passField.clear();
                    userField.requestFocus();
                } else {
                    messageLabel.setText("❌ Error al conectar: " + ex.getMessage());
                }
            }

        });

        VBox layout = new VBox(15, logoView, title, form);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 400, 350);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Login – MALI DB");
        stage.getIcons().add(new Image(getClass().getResource("/img/mali.jpg").toExternalForm()));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
