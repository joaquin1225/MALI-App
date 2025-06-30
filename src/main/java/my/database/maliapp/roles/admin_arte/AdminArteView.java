package my.database.maliapp.roles.admin_arte;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import my.database.maliapp.HelloApplication;
import my.database.maliapp.modelos.*;
import my.database.maliapp.Filtros;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminArteView {
    private final Connection conn;
    private TableView<ObraExtendida> tablaObras;
    private ObservableList<ObraExtendida> obras = FXCollections.observableArrayList();

    public AdminArteView(Connection connection) {
        this.conn = connection;
    }

    public void mostrar(Stage stage) {
        BorderPane root = new BorderPane();

        tablaObras = new TableView<>();
        inicializarTabla();
        root.setCenter(tablaObras);

        VBox panelBusqueda = new VBox(10);
        panelBusqueda.setPadding(new Insets(10));
        ToggleGroup grupoBusqueda = new ToggleGroup();

        RadioButton rbObra = new RadioButton("Buscar por obra");
        RadioButton rbArtista = new RadioButton("Buscar por artista");
        RadioButton rbColeccion = new RadioButton("Buscar por colección");
        rbObra.setToggleGroup(grupoBusqueda);
        rbArtista.setToggleGroup(grupoBusqueda);
        rbColeccion.setToggleGroup(grupoBusqueda);

        VBox contenedorFiltros = new VBox(10);

        panelBusqueda.getChildren().addAll(rbObra, rbArtista, rbColeccion, contenedorFiltros);

        VBox filtrosObra = new VBox(5);
        TextField tfTitulo = new TextField(); tfTitulo.setPromptText("Título"); Filtros.bloquearTildes(tfTitulo);
        TextField tfFechaMin = new TextField(); tfFechaMin.setPromptText("Fecha Min"); Filtros.soloNumeros(tfFechaMin, 4);
        TextField tfFechaMax = new TextField(); tfFechaMax.setPromptText("Fecha Max"); Filtros.soloNumeros(tfFechaMax, 4);

        ComboBox<String> cbTipo = new ComboBox<>();
        cbTipo.setPromptText("Tipo");
        ObservableList<String> tipos = obtenerOpcionesUnicas("tipo");
        tipos.add(0, "Seleccionar tipo");
        cbTipo.setItems(tipos);
        cbTipo.getSelectionModel().selectFirst();
        cbTipo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Tipo" : item);
            }
        });

        ComboBox<String> cbEstado = new ComboBox<>();
        cbEstado.setPromptText("Estado");
        ObservableList<String> estados = obtenerOpcionesUnicas("estado");
        estados.add(0, "Seleccionar estado");
        cbEstado.setItems(estados);
        cbEstado.getSelectionModel().selectFirst();
        cbEstado.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Estado" : item);
            }
        });

        filtrosObra.getChildren().addAll(tfTitulo, tfFechaMin, tfFechaMax, cbTipo, cbEstado);

        VBox filtrosArtista = new VBox(5);
        ComboBox<String> cbNombreCompleto = new ComboBox<>();
        ComboBox<String> cbPais = new ComboBox<>();
        cbNombreCompleto.setPromptText("Nombre completo");
        cbPais.setPromptText("País");
        cargarNombresArtistas(cbNombreCompleto);
        cargarPaises(cbPais);

        filtrosArtista.getChildren().addAll(cbNombreCompleto, cbPais);

        VBox filtrosColeccion = new VBox(5);
        ComboBox<String> cbNombreColeccion = new ComboBox<>();
        cbNombreColeccion.setPromptText("Colección");
        cargarNombresColecciones(cbNombreColeccion);
        filtrosColeccion.getChildren().add(cbNombreColeccion);

        grupoBusqueda.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            contenedorFiltros.getChildren().clear();
            if (newToggle == rbObra) {
                contenedorFiltros.getChildren().add(filtrosObra);
            } else if (newToggle == rbArtista) {
                contenedorFiltros.getChildren().add(filtrosArtista);
            } else if (newToggle == rbColeccion) {
                contenedorFiltros.getChildren().add(filtrosColeccion);
            }
        });

        Button btnBuscar = new Button("Buscar");
        Button btnLimpiar = new Button("Limpiar filtros");
        Button btnCerrarSesion = new Button("Cerrar sesión");

        btnBuscar.setOnAction(e -> buscar(rbObra, rbArtista, rbColeccion, tfTitulo, tfFechaMin, tfFechaMax, cbTipo, cbEstado, cbNombreCompleto, cbPais, cbNombreColeccion));

        btnLimpiar.setOnAction(e -> {
            grupoBusqueda.selectToggle(null);
            contenedorFiltros.getChildren().clear();
            tfTitulo.clear(); tfFechaMin.clear(); tfFechaMax.clear();

            cbTipo.getItems().clear();
            cbTipo.getItems().add("Seleccionar tipo");
            cbTipo.getItems().addAll(obtenerOpcionesUnicas("tipo"));
            cbTipo.getSelectionModel().selectFirst();
            cbTipo.getSelectionModel().selectFirst();
            cbTipo.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "Tipo" : item);
                }
            });

            cbEstado.getItems().clear();
            cbEstado.getItems().add("Seleccionar estado");
            cbEstado.getItems().addAll(obtenerOpcionesUnicas("estado"));
            cbEstado.getSelectionModel().selectFirst();
            cbEstado.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "Estado" : item);
                }
            });

            cargarNombresArtistas(cbNombreCompleto);
            cbNombreCompleto.getSelectionModel().selectFirst();
            cbNombreCompleto.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "Nombre completo" : item);
                }
            });

            cargarPaises(cbPais);
            cbPais.getSelectionModel().selectFirst();
            cbPais.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "País" : item);
                }
            });

            cargarNombresColecciones(cbNombreColeccion);
            cbNombreColeccion.getSelectionModel().selectFirst();
            cbNombreColeccion.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "Colección" : item);
                }
            });

            cargarObras();
        });


        btnCerrarSesion.setOnAction(e -> {
            stage.close();
            try {
                new HelloApplication().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button btnAgregarObra = new Button("Añadir obra");
        Button btnEliminarObra = new Button("Eliminar obra");
        Button btnActualizarObra = new Button("Actualizar obra");

        btnAgregarObra.setOnAction(e -> {
            new RegistrarObraView(conn, this::cargarObras).mostrar(stage);
        });

        btnActualizarObra.setOnAction(e -> {
            ObraExtendida seleccionada = tablaObras.getSelectionModel().getSelectedItem();
            if (seleccionada != null) {
                new RegistrarObraView(conn, this::cargarObras).mostrar(stage, seleccionada);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "❗ Selecciona una obra para editar.");
                alert.showAndWait();
            }
        });

        btnEliminarObra.setOnAction(e -> {
            ObraExtendida seleccionada = tablaObras.getSelectionModel().getSelectedItem();
            if (seleccionada != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Deseas eliminar esta obra?", ButtonType.OK, ButtonType.CANCEL);
                confirm.showAndWait().ifPresent(r -> {
                    if (r == ButtonType.OK) {
                        eliminarObra(seleccionada.getObra().getIdObra());
                        cargarObras();
                        Alert info = new Alert(Alert.AlertType.INFORMATION, "✅ Obra eliminada correctamente.");
                        info.showAndWait();
                    }
                });
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "❗ Selecciona una obra para eliminar.");
                alert.showAndWait();
            }
        });

        HBox botonesAcciones = new HBox(10, btnAgregarObra, btnEliminarObra, btnActualizarObra);
        botonesAcciones.setPadding(new Insets(10));


        HBox botonesBusqueda = new HBox(10, btnBuscar, btnLimpiar);
        VBox centroDerecha = new VBox(10, rbObra, rbArtista, rbColeccion, contenedorFiltros, botonesBusqueda);
        centroDerecha.setPadding(new Insets(10));

        VBox contenedorCentro = new VBox(10, centroDerecha, botonesAcciones);
        contenedorCentro.setPadding(new Insets(10));

        BorderPane panelDerecho = new BorderPane();
        panelDerecho.setCenter(contenedorCentro);
        panelDerecho.setBottom(btnCerrarSesion);
        BorderPane.setMargin(btnCerrarSesion, new Insets(10));

        root.setRight(panelDerecho);

        tablaObras.setItems(obras);
        cargarObras();

        Scene scene = new Scene(root, 1000, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Admin de Arte – Obras y artistas");
        stage.getIcons().add(new Image(getClass().getResource("/img/mali.jpg").toExternalForm()));
        stage.show();
    }

    private void inicializarTabla() {
        TableColumn<ObraExtendida, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));

        TableColumn<ObraExtendida, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        TableColumn<ObraExtendida, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        TableColumn<ObraExtendida, Integer> colFechaMin = new TableColumn<>("Fecha Min");
        colFechaMin.setCellValueFactory(new PropertyValueFactory<>("fechaMin"));

        TableColumn<ObraExtendida, Integer> colFechaMax = new TableColumn<>("Fecha Max");
        colFechaMax.setCellValueFactory(new PropertyValueFactory<>("fechaMax"));

        TableColumn<ObraExtendida, String> colArtista = new TableColumn<>("Artista");
        colArtista.setCellValueFactory(new PropertyValueFactory<>("nombreArtista"));

        TableColumn<ObraExtendida, String> colColeccion = new TableColumn<>("Colección");
        colColeccion.setCellValueFactory(new PropertyValueFactory<>("nombreColeccion"));

        tablaObras.getColumns().addAll(colTitulo, colTipo, colEstado, colFechaMin, colFechaMax, colArtista, colColeccion);
    }

    private void cargarObras() {
        obras.clear();
        try (PreparedStatement ps = conn.prepareStatement("""
            SELECT o.id_obra, o.id_artista, o.titulo, o.fecha_min, o.fecha_max, o.tipo, o.estado,
                   a.id_artista, a.nombre, a.apellido, a.pais, a.fecha_nac, a.fecha_fallec,
                   c.id_coleccion, c.nombre_coleccion
            FROM obra_de_arte o
            JOIN artista a ON o.id_artista = a.id_artista
            LEFT JOIN pertenece_a p ON o.id_obra = p.id_obra
            LEFT JOIN coleccion c ON p.id_coleccion = c.id_coleccion
        """)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ObraDeArte obra = new ObraDeArte(
                        rs.getInt("id_obra"),
                        rs.getInt("id_artista"),
                        rs.getString("titulo"),
                        (Integer) rs.getObject("fecha_min"),
                        (Integer) rs.getObject("fecha_max"),
                        rs.getString("tipo"),
                        rs.getString("estado")
                );
                Artista artista = new Artista(
                        rs.getInt("id_artista"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("pais"),
                        (Integer) rs.getObject("fecha_nac"),
                        (Integer) rs.getObject("fecha_fallec")
                );
                Coleccion coleccion = null;
                if (rs.getObject("id_coleccion") != null) {
                    coleccion = new Coleccion(
                            rs.getInt("id_coleccion"),
                            rs.getString("nombre_coleccion")
                    );
                }
                obras.add(new ObraExtendida(obra, artista, coleccion));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void buscar(RadioButton rbObra, RadioButton rbArtista, RadioButton rbColeccion,
                        TextField tfTitulo, TextField tfFechaMin, TextField tfFechaMax,
                        ComboBox<String> cbTipo, ComboBox<String> cbEstado,
                        ComboBox<String> cbNombreCompleto, ComboBox<String> cbPais,
                        ComboBox<String> cbNombreColeccion) {

        obras.clear();
        StringBuilder sql = new StringBuilder("""
            SELECT o.id_obra, o.id_artista, o.titulo, o.fecha_min, o.fecha_max, o.tipo, o.estado,
                   a.id_artista, a.nombre, a.apellido, a.pais, a.fecha_nac, a.fecha_fallec,
                   c.id_coleccion, c.nombre_coleccion
            FROM obra_de_arte o
            JOIN artista a ON o.id_artista = a.id_artista
            LEFT JOIN pertenece_a p ON o.id_obra = p.id_obra
            LEFT JOIN coleccion c ON p.id_coleccion = c.id_coleccion
            WHERE 1=1
        """);

        List<Object> parametros = new ArrayList<>();

        if (rbObra.isSelected()) {
            if (!tfTitulo.getText().isEmpty()) {
                sql.append(" AND o.titulo ILIKE ?");
                parametros.add("%" + tfTitulo.getText() + "%");
            }
            if (!tfFechaMin.getText().isEmpty()) {
                sql.append(" AND o.fecha_min >= ?");
                parametros.add(Integer.parseInt(tfFechaMin.getText()));
            }
            if (!tfFechaMax.getText().isEmpty()) {
                sql.append(" AND o.fecha_max <= ?");
                parametros.add(Integer.parseInt(tfFechaMax.getText()));
            }
            if (cbTipo.getValue() != null && !cbTipo.getValue().equals("Seleccionar tipo")) {
                sql.append(" AND o.tipo = ?");
                parametros.add(cbTipo.getValue());
            }
            if (cbEstado.getValue() != null && !cbEstado.getValue().equals("Seleccionar estado")) {
                sql.append(" AND o.estado = ?");
                parametros.add(cbEstado.getValue());
            }

        } else if (rbArtista.isSelected()) {
            if (cbNombreCompleto.getValue() != null && !cbNombreCompleto.getValue().equals("Seleccionar nombre")) {
                String[] partes = cbNombreCompleto.getValue().split(" ", 2);
                if (partes.length > 0) {
                    sql.append(" AND a.nombre ILIKE ?");
                    parametros.add("%" + partes[0] + "%");
                }
                if (partes.length > 1) {
                    sql.append(" AND a.apellido ILIKE ?");
                    parametros.add("%" + partes[1] + "%");
                }
            }
            if (cbPais.getValue() != null && !cbPais.getValue().equals("Seleccionar pais")) {
                sql.append(" AND a.pais ILIKE ?");
                parametros.add("%" + cbPais.getValue() + "%");
            }
        } else if (rbColeccion.isSelected()) {
            if (cbNombreColeccion.getValue() != null && !cbNombreColeccion.getValue().equals("Seleccionar coleccion")) {
                sql.append(" AND c.nombre_coleccion ILIKE ?");
                parametros.add("%" + cbNombreColeccion.getValue() + "%");
            }
        }

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ObraDeArte obra = new ObraDeArte(
                        rs.getInt("id_obra"),
                        rs.getInt("id_artista"),
                        rs.getString("titulo"),
                        (Integer) rs.getObject("fecha_min"),
                        (Integer) rs.getObject("fecha_max"),
                        rs.getString("tipo"),
                        rs.getString("estado")
                );
                Artista artista = new Artista(
                        rs.getInt("id_artista"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("pais"),
                        (Integer) rs.getObject("fecha_nac"),
                        (Integer) rs.getObject("fecha_fallec")
                );
                Coleccion coleccion = null;
                if (rs.getObject("id_coleccion") != null) {
                    coleccion = new Coleccion(
                            rs.getInt("id_coleccion"),
                            rs.getString("nombre_coleccion")
                    );
                }
                obras.add(new ObraExtendida(obra, artista, coleccion));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<String> obtenerOpcionesUnicas(String columna) {
        ObservableList<String> opciones = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT " + columna + " FROM obra_de_arte ORDER BY " + columna;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                opciones.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return opciones;
    }

    private void cargarNombresArtistas(ComboBox<String> comboBox) {
        comboBox.getItems().clear();
        comboBox.getItems().add("Seleccionar nombre");
        try (PreparedStatement ps = conn.prepareStatement("""
        SELECT DISTINCT nombre, apellido FROM artista ORDER BY nombre, apellido
    """); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                comboBox.getItems().add(nombre + (apellido != null ? " " + apellido : ""));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        comboBox.getSelectionModel().selectFirst();
    }

    private void cargarPaises(ComboBox<String> comboBox) {
        comboBox.getItems().clear();
        comboBox.getItems().add("Seleccionar pais");
        try (PreparedStatement ps = conn.prepareStatement("""
        SELECT DISTINCT pais FROM artista WHERE pais IS NOT NULL ORDER BY pais
    """); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                comboBox.getItems().add(rs.getString("pais"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        comboBox.getSelectionModel().selectFirst();
    }

    private void cargarNombresColecciones(ComboBox<String> comboBox) {
        comboBox.getItems().clear();
        comboBox.getItems().add("Seleccionar coleccion");
        try (PreparedStatement ps = conn.prepareStatement("""
        SELECT DISTINCT nombre_coleccion FROM coleccion ORDER BY nombre_coleccion
    """); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                comboBox.getItems().add(rs.getString("nombre_coleccion"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        comboBox.getSelectionModel().selectFirst();
    }

    private void eliminarObra(int idObra) {
        try (PreparedStatement ps1 = conn.prepareStatement("DELETE FROM pertenece_a WHERE id_obra = ?");
             PreparedStatement ps2 = conn.prepareStatement("DELETE FROM obra_de_arte WHERE id_obra = ?")) {
            ps1.setInt(1, idObra);
            ps1.executeUpdate();

            ps2.setInt(1, idObra);
            ps2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo eliminar la obra.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
