package my.database.maliapp.roles.jefe_empleado;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import my.database.maliapp.modelos.Empleado;
import my.database.maliapp.Filtros;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AsignarEmpleadosView {
    private final Connection conn;
    private final int idTrabajo;
    private final ObservableList<Empleado> empleadosAsignados = FXCollections.observableArrayList();
    private final ObservableList<Empleado> empleadosDisponibles = FXCollections.observableArrayList();

    private TableView<Empleado> tablaAsignados;
    private TableView<Empleado> tablaDisponibles;

    public AsignarEmpleadosView(Connection conn, int idTrabajo) {
        this.conn = conn;
        this.idTrabajo = idTrabajo;
    }

    private TableView<Empleado> construirTablaEmpleados() {
        TableView<Empleado> tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Empleado, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Empleado, String> colApellido = new TableColumn<>("Apellido");
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));

        TableColumn<Empleado, String> colPuesto = new TableColumn<>("Puesto");
        colPuesto.setCellValueFactory(new PropertyValueFactory<>("puesto"));

        tabla.getColumns().addAll(colNombre, colApellido, colPuesto);
        return tabla;
    }

    private TableView<Empleado> construirTablaEmpleadosConSeleccion() {
        TableView<Empleado> tabla = construirTablaEmpleados();

        TableColumn<Empleado, Boolean> colSeleccion = new TableColumn<>("Seleccionar");
        colSeleccion.setCellFactory(tc -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();

            {
                setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(checkBox);
                }
            }

            @Override
            public void updateIndex(int i) {
                super.updateIndex(i);
                if (i >= 0 && i < tabla.getItems().size()) {
                    Empleado emp = tabla.getItems().get(i);
                    checkBox.setSelected(empSeleccionados.contains(emp));
                    checkBox.setOnAction(e -> {
                        if (checkBox.isSelected()) {
                            empSeleccionados.add(emp);
                        } else {
                            empSeleccionados.remove(emp);
                        }
                    });
                }
            }
        });

        tabla.getColumns().add(colSeleccion);
        return tabla;
    }

    private final List<Empleado> empSeleccionados = new ArrayList<>();

    private List<Empleado> obtenerEmpleadosAsignados() {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT e.* FROM empleado e JOIN trabaja_en t ON e.id_empleado = t.id_empleado WHERE t.id_trabajo = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTrabajo);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Empleado(
                        rs.getInt("id_empleado"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getDate("fecha_inicio").toLocalDate(),
                        rs.getString("puesto"),
                        rs.getInt("id_dep")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private List<Empleado> obtenerEmpleadosDisponibles(String depNombre, String puesto) {
        List<Empleado> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT e.* FROM empleado e JOIN departamento d ON e.id_dep = d.id_dep WHERE e.id_empleado NOT IN (SELECT id_empleado FROM trabaja_en WHERE id_trabajo = ?)");

        if (depNombre != null && !depNombre.isEmpty()) {
            sql.append(" AND d.nombre_dep ILIKE ?");
        }
        if (puesto != null && !puesto.isEmpty()) {
            sql.append(" AND e.puesto ILIKE ?");
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int i = 1;
            stmt.setInt(i++, idTrabajo);
            if (depNombre != null && !depNombre.isEmpty()) stmt.setString(i++, depNombre);
            if (puesto != null && !puesto.isEmpty()) stmt.setString(i++, puesto);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Empleado(
                        rs.getInt("id_empleado"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getDate("fecha_inicio").toLocalDate(),
                        rs.getString("puesto"),
                        rs.getInt("id_dep")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private void asignarSeleccionados() {
        for (Empleado emp : empSeleccionados) {
            String sql = "INSERT INTO trabaja_en (id_trabajo, id_empleado) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idTrabajo);
                stmt.setInt(2, emp.getIdEmpleado());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        empleadosAsignados.setAll(obtenerEmpleadosAsignados());
        empleadosDisponibles.setAll(obtenerEmpleadosDisponibles(null, null));
        empSeleccionados.clear();
    }

    public VBox getVista() {
        // Tabla superior: empleados asignados
        tablaAsignados = construirTablaEmpleados();
        empleadosAsignados.setAll(obtenerEmpleadosAsignados());
        tablaAsignados.setItems(empleadosAsignados);

        // Tabla inferior: empleados disponibles
        tablaDisponibles = construirTablaEmpleadosConSeleccion();
        empleadosDisponibles.setAll(obtenerEmpleadosDisponibles(null, null));
        tablaDisponibles.setItems(empleadosDisponibles);

        // Buscador a la derecha
        VBox filtroBox = new VBox(5);
        filtroBox.setPadding(new Insets(10));
        filtroBox.setAlignment(Pos.TOP_CENTER);
        TextField tfDepartamento = new TextField(); Filtros.bloquearTildes(tfDepartamento);
        tfDepartamento.setPromptText("Departamento");
        TextField tfPuesto = new TextField(); Filtros.bloquearTildes(tfPuesto);
        tfPuesto.setPromptText("Puesto");
        Button btnBuscar = new Button("OK");

        btnBuscar.setOnAction(e -> {
            String dep = tfDepartamento.getText().trim();
            String puesto = tfPuesto.getText().trim();
            empleadosDisponibles.setAll(obtenerEmpleadosDisponibles(dep, puesto));
        });

        filtroBox.getChildren().addAll(new Label("Filtrar por:"), tfDepartamento, tfPuesto, btnBuscar);

        Button btnAsignar = new Button("Asignar seleccionados");
        btnAsignar.setOnAction(e -> asignarSeleccionados());

        HBox filtroYTabla = new HBox(10, tablaDisponibles, filtroBox);

        VBox layout = new VBox(15,
                new Label("Empleados asignados al trabajo:"),
                tablaAsignados,
                new Label("Selecciona empleados para asignar:"),
                filtroYTabla,
                btnAsignar);

        layout.setPadding(new Insets(15));

        return layout;
    }
}
