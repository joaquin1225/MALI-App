package my.database.maliapp.roles.jefe_empleado;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import my.database.maliapp.modelos.*;
import my.database.maliapp.tablas.ListaTrabajosView;

import java.sql.*;
import java.time.LocalDate;
import java.util.Set;

public class ModificarTrabajosView extends ListaTrabajosView {

    public ModificarTrabajosView(Connection conn) {
        super(conn);
    }

    @Override
    public VBox getVista() {
        TableView<Trabajo> tabla = construirTabla();

        TableColumn<Trabajo, String> colAsignacion = new TableColumn<>("Asignación");
        colAsignacion.setCellValueFactory(cellData -> {
            Trabajo trabajo = cellData.getValue();
            String descripcion = obtenerDescripcionAsignacion(trabajo.getIdTrabajo());
            return new javafx.beans.property.SimpleStringProperty(descripcion);
        });
        tabla.getColumns().add(colAsignacion);

        tabla.setItems(obtenerDatos());

        Button btnAgregar = new Button("Agregar trabajo");
        Button btnEditar = new Button("Editar trabajo seleccionado");
        Button btnEliminar = new Button("Eliminar trabajo seleccionado");
        Button btnAsignarEmpleados = new Button("Asignar empleados");
        Label estado = new Label();

        btnAgregar.setOnAction(e -> mostrarFormularioAgregar(tabla, estado));

        btnEditar.setOnAction(e -> {
            Trabajo seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                mostrarFormularioEditar(seleccionado, tabla, estado);
            } else {
                estado.setText("❗ Selecciona un trabajo para editar.");
            }
        });

        btnEliminar.setOnAction(e -> {
            Trabajo seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Deseas eliminar este trabajo?", ButtonType.OK, ButtonType.CANCEL);
                confirm.showAndWait().ifPresent(r -> {
                    if (r == ButtonType.OK) {
                        eliminarTrabajo(seleccionado.getIdTrabajo());
                        tabla.setItems(obtenerDatos());
                        estado.setText("✅ Trabajo eliminado correctamente.");
                    }
                });
            } else {
                estado.setText("❗ Selecciona un trabajo para eliminar.");
            }
        });

        btnAsignarEmpleados.setOnAction(e -> mostrarVistaAsignarEmpleados());

        VBox layout = new VBox(10, tabla, new HBox(10, btnAgregar, btnEditar, btnEliminar, btnAsignarEmpleados), estado);
        layout.setPadding(new Insets(10));
        return layout;
    }

    private void mostrarVistaAsignarEmpleados() {
        Stage stage = new Stage();
        stage.setTitle("Asignar empleados a trabajos");

        TableView<Trabajo> tabla = construirTabla();
        tabla.setItems(obtenerDatos());

        TableColumn<Trabajo, Void> colAccion = new TableColumn<>("Acciones");
        colAccion.setCellFactory(tc -> new TableCell<>() {
            private final Button btnAsignar = new Button("Asignar empleados");

            {
                btnAsignar.setOnAction(e -> {
                    Trabajo trabajo = getTableView().getItems().get(getIndex());
                    AsignarEmpleadosView vista = new AsignarEmpleadosView(conn, trabajo.getIdTrabajo());
                    Stage stageAsignar = new Stage();
                    stageAsignar.setTitle("Asignar empleados al trabajo");
                    stageAsignar.setScene(new Scene(vista.getVista()));
                    stageAsignar.show();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnAsignar);
            }
        });

        tabla.getColumns().add(colAccion);

        VBox layout = new VBox(10, new Label("Lista de trabajos"), tabla);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void mostrarFormularioAgregar(TableView<Trabajo> tabla, Label estado) {
        Stage stage = new Stage();
        stage.setTitle("Agregar trabajo");

        Label lblDesc = new Label("Descripción:");
        TextField tfDesc = new TextField();
        Label lblInicio = new Label("Fecha inicio:");
        DatePicker dpInicio = new DatePicker();
        Label lblFin = new Label("Fecha fin:");
        DatePicker dpFin = new DatePicker();

        Button btnSiguiente = new Button("Siguiente");
        btnSiguiente.setOnAction(ev -> {
            String desc = tfDesc.getText().trim();
            LocalDate inicio = dpInicio.getValue();
            LocalDate fin = dpFin.getValue();

            if (desc.isEmpty() || inicio == null) {
                new Alert(Alert.AlertType.ERROR, "Descripción y fecha de inicio son obligatorios").show();
                return;
            }

            int idTrabajo = insertarTrabajo(desc, inicio, fin);
            if (idTrabajo != -1) {
                stage.close();
                mostrarSeleccionDeAsignacion(idTrabajo, tabla, estado);
            } else {
                new Alert(Alert.AlertType.ERROR, "Error al crear el trabajo").show();
            }
        });

        VBox form = new VBox(10, lblDesc, tfDesc, lblInicio, dpInicio, lblFin, dpFin, btnSiguiente);
        form.setPadding(new Insets(10));
        stage.setScene(new Scene(form));
        stage.show();
    }

    private void mostrarSeleccionDeAsignacion(int idTrabajo, TableView<Trabajo> tabla, Label estado) {
        Stage stage = new Stage();
        stage.setTitle("Asignar trabajo");

        Button btnObra = new Button("Asignar a obras de arte");
        btnObra.setOnAction(e -> {
            stage.close();
            mostrarSeleccionObras(idTrabajo, tabla, estado);
        });

        Button btnSala = new Button("Asignar a salas");
        btnSala.setOnAction(e -> {
            stage.close();
            mostrarSeleccionSalas(idTrabajo, tabla, estado);
        });

        VBox opciones = new VBox(10, new Label("¿Dónde deseas asignar el trabajo?"), btnObra, btnSala);
        opciones.setPadding(new Insets(10));
        stage.setScene(new Scene(opciones));
        stage.show();
    }

    private void mostrarSeleccionObras(int idTrabajo, TableView<Trabajo> tabla, Label estado) {
        SeleccionarObrasView vista = new SeleccionarObrasView(conn,
                () -> mostrarSeleccionDeAsignacion(idTrabajo, tabla, estado),
                obras -> {
                    for (ObraDeArte obra : obras) {
                        insertarTrabajoObra(idTrabajo, obra.getIdObra());
                    }
                    tabla.setItems(obtenerDatos());
                    estado.setText("✅ Trabajo y obras asignadas correctamente.");
                });

        Stage stage = new Stage();
        stage.setTitle("Seleccionar obras");
        stage.setScene(new Scene(vista.getVista()));
        stage.show();
    }

    private void mostrarSeleccionSalas(int idTrabajo, TableView<Trabajo> tabla, Label estado) {
        SeleccionarSalasView vista = new SeleccionarSalasView(conn);
        vista.setOnVolver(() -> mostrarSeleccionDeAsignacion(idTrabajo, tabla, estado));
        vista.setOnConfirmar(salas -> {
            for (Sala sala : salas) {
                insertarTrabajoSala(idTrabajo, sala.getIdSala());
            }
            tabla.setItems(obtenerDatos());
            estado.setText("✅ Trabajo y salas asignadas correctamente.");
        });

        Stage stage = new Stage();
        stage.setTitle("Seleccionar salas");
        stage.setScene(new Scene(vista.getVista()));
        stage.show();
    }

    private int insertarTrabajo(String descripcion, LocalDate inicio, LocalDate fin) {
        String sql = "INSERT INTO trabajo (descripcion, fecha_inicio, fecha_fin) VALUES (?, ?, ?) RETURNING id_trabajo";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, descripcion);
            stmt.setDate(2, Date.valueOf(inicio));
            if (fin != null)
                stmt.setDate(3, Date.valueOf(fin));
            else
                stmt.setNull(3, Types.DATE);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_trabajo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void insertarTrabajoObra(int idTrabajo, int idObra) {
        if (yaExisteTrabajoObra(idTrabajo, idObra)) {
            return;
        }

        String sql = "INSERT INTO trabajoobra (id_trabajo, id_obra) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTrabajo);
            stmt.setInt(2, idObra);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertarTrabajoSala(int idTrabajo, int idSala) {
        if (yaExisteTrabajoSala(idTrabajo, idSala)) {
            return;
        }
        String sql = "INSERT INTO trabajosala (id_trabajo, id_sala) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTrabajo);
            stmt.setInt(2, idSala);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void mostrarFormularioEditar(Trabajo trabajo, TableView<Trabajo> tabla, Label estado) {
        Dialog<Trabajo> dialog = new Dialog<>();
        dialog.setTitle("Editar trabajo");

        Label lblDesc = new Label("Descripción:");
        TextField tfDesc = new TextField(trabajo.getDescripcion());

        Label lblInicio = new Label("Fecha inicio:");
        DatePicker dpInicio = new DatePicker(trabajo.getFechaInicio());

        Label lblFin = new Label("Fecha fin:");
        DatePicker dpFin = new DatePicker(trabajo.getFechaFin());

        VBox contenido = new VBox(10, lblDesc, tfDesc, lblInicio, dpInicio, lblFin, dpFin);
        dialog.getDialogPane().setContent(contenido);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(b -> {
            if (b == ButtonType.OK) {
                return new Trabajo(trabajo.getIdTrabajo(), tfDesc.getText(), dpInicio.getValue(), dpFin.getValue());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(resultado -> {
            actualizarTrabajo(resultado);
            tabla.setItems(obtenerDatos());
            estado.setText("✅ Trabajo editado correctamente.");
        });
    }

    private void actualizarTrabajo(Trabajo trabajo) {
        String sql = "UPDATE trabajo SET descripcion = ?, fecha_inicio = ?, fecha_fin = ? WHERE id_trabajo = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, trabajo.getDescripcion());
            stmt.setDate(2, Date.valueOf(trabajo.getFechaInicio()));
            if (trabajo.getFechaFin() != null) {
                stmt.setDate(3, Date.valueOf(trabajo.getFechaFin()));
            } else {
                stmt.setNull(3, java.sql.Types.DATE);
            }
            stmt.setInt(4, trabajo.getIdTrabajo());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void eliminarTrabajo(int id) {
        String sql = "DELETE FROM trabajo WHERE id_trabajo = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean yaExisteTrabajoObra(int idTrabajo, int idObra) {
        String sql = "SELECT 1 FROM trabajo_obra WHERE id_trabajo = ? AND id_obra = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTrabajo);
            stmt.setInt(2, idObra);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    private boolean yaExisteTrabajoSala(int idTrabajo, int idSala) {
        String sql = "SELECT 1 FROM trabajo_sala WHERE id_trabajo = ? AND id_sala = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTrabajo);
            stmt.setInt(2, idSala);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    private String obtenerDescripcionAsignacion(int idTrabajo) {
        StringBuilder descripcion = new StringBuilder();

        try {
            String sqlSala = "SELECT s.nombre_sala FROM sala s " +
                    "JOIN trabajosala ts ON s.id_sala = ts.id_sala " +
                    "WHERE ts.id_trabajo = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlSala)) {
                stmt.setInt(1, idTrabajo);
                ResultSet rs = stmt.executeQuery();
                boolean tieneSalas = false;
                while (rs.next()) {
                    if (!tieneSalas) {
                        descripcion.append("Trabajo asignado a la sala");
                        tieneSalas = true;
                    } else {
                        descripcion.append(",");
                    }
                    descripcion.append(" ").append(rs.getString("nombre_sala"));
                }
            }

            String sqlObra = "SELECT o.titulo FROM obra_de_arte o " +
                    "JOIN trabajoobra to2 ON o.id_obra = to2.id_obra " +
                    "WHERE to2.id_trabajo = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlObra)) {
                stmt.setInt(1, idTrabajo);
                ResultSet rs = stmt.executeQuery();
                boolean tieneObras = false;
                while (rs.next()) {
                    if (descripcion.length() > 0) {
                        descripcion.append(" / ");
                    }
                    if (!tieneObras) {
                        descripcion.append("Trabajo asignado a la obra");
                        tieneObras = true;
                    } else {
                        descripcion.append(",");
                    }
                    descripcion.append(" ").append(rs.getString("titulo"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (descripcion.length() == 0) {
            return "Trabajo no asignado";
        }
        return descripcion.toString();
    }
}
