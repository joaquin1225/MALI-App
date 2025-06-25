package my.database.maliapp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        Label userLabel = new Label("Usuario:");
        TextField userField = new TextField();
        Label passLabel = new Label("Contraseña:");
        PasswordField passField = new PasswordField();

        Button loginButton = new Button("Iniciar sesión");
        Label messageLabel = new Label();

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
                            EmpleadoView ev = new EmpleadoView(conn);
                            ev.mostrar(new Stage());
                            stage.close();
                        }
                        case "jefe_empleado" -> System.out.println("Abrir ventana de privilegios de jefe de empleado");
                        case "admin_rrhh" -> System.out.println("Abrir ventana de privilegios de administrador de RRHH");
                        case "admin_arte" -> System.out.println("Abrir ventana de privilegios de administrador de obras de arte");
                        case "admin_espacios_museo" -> System.out.println("Abrir ventana de privilegios de administrador de espacios del museo");
                        case "admin_visitas_ingresos" -> System.out.println("Abrir ventana de privilegios de administrador de visitas e ingresos");
                        case "gestor_db" -> System.out.println("Abrir ventana de privilegios de gestor de base de datos");
                        default -> System.out.println("Rol no encontrado");
                    }
                }
            } catch (SQLException ex) {
                messageLabel.setText("Error: " + ex.getMessage());
            }
        });

        VBox root = new VBox(10, userLabel, userField, passLabel, passField, loginButton, messageLabel);
        root.setPadding(new Insets(20));

        stage.setScene(new Scene(root, 300, 250));
        stage.setTitle("Login - MALI DB");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}