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
        TextField tfTitulo = new TextField(); tfTitulo.setPromptText("Título");
        TextField tfFechaMin = new TextField(); tfFechaMin.setPromptText("Fecha Min");
        TextField tfFechaMax = new TextField(); tfFechaMax.setPromptText("Fecha Max");
        ComboBox<String> cbTipo = new ComboBox<>(); cbTipo.setPromptText("Tipo");
        ComboBox<String> cbEstado = new ComboBox<>(); cbEstado.setPromptText("Estado");
        cbTipo.setItems(FXCollections.observableArrayList("Pintura", "Escultura", "Grabado"));
        cbEstado.setItems(FXCollections.observableArrayList("EXHIBIENDOSE", "ALMACENADA", "EN_RESTAURACION"));
        filtrosObra.getChildren().addAll(tfTitulo, tfFechaMin, tfFechaMax, cbTipo, cbEstado);

        VBox filtrosArtista = new VBox(5);
        TextField tfNombre = new TextField(); tfNombre.setPromptText("Nombre");
        TextField tfApellido = new TextField(); tfApellido.setPromptText("Apellido");
        TextField tfPais = new TextField(); tfPais.setPromptText("País");
        filtrosArtista.getChildren().addAll(tfNombre, tfApellido, tfPais);

        VBox filtrosColeccion = new VBox(5);
        TextField tfNombreColeccion = new TextField(); tfNombreColeccion.setPromptText("Nombre colección");
        filtrosColeccion.getChildren().add(tfNombreColeccion);

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

        btnBuscar.setOnAction(e -> buscar(rbObra, rbArtista, rbColeccion, tfTitulo, tfFechaMin, tfFechaMax, cbTipo, cbEstado, tfNombre, tfApellido, tfPais, tfNombreColeccion));

        btnLimpiar.setOnAction(e -> {
            grupoBusqueda.selectToggle(null);
            contenedorFiltros.getChildren().clear();
            tfTitulo.clear(); tfFechaMin.clear(); tfFechaMax.clear(); cbTipo.setValue(null); cbEstado.setValue(null);
            tfNombre.clear(); tfApellido.clear(); tfPais.clear();
            tfNombreColeccion.clear();
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

        HBox botonesBusqueda = new HBox(10, btnBuscar, btnLimpiar);
        VBox centroDerecha = new VBox(10, rbObra, rbArtista, rbColeccion, contenedorFiltros, botonesBusqueda);
        centroDerecha.setPadding(new Insets(10));

        BorderPane panelDerecho = new BorderPane();
        panelDerecho.setCenter(centroDerecha);
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
                        TextField tfNombre, TextField tfApellido, TextField tfPais,
                        TextField tfNombreColeccion) {

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
            if (cbTipo.getValue() != null) {
                sql.append(" AND o.tipo = ?");
                parametros.add(cbTipo.getValue());
            }
            if (cbEstado.getValue() != null) {
                sql.append(" AND o.estado = ?");
                parametros.add(cbEstado.getValue());
            }
        } else if (rbArtista.isSelected()) {
            if (!tfNombre.getText().isEmpty()) {
                sql.append(" AND a.nombre ILIKE ?");
                parametros.add("%" + tfNombre.getText() + "%");
            }
            if (!tfApellido.getText().isEmpty()) {
                sql.append(" AND a.apellido ILIKE ?");
                parametros.add("%" + tfApellido.getText() + "%");
            }
            if (!tfPais.getText().isEmpty()) {
                sql.append(" AND a.pais ILIKE ?");
                parametros.add("%" + tfPais.getText() + "%");
            }
        } else if (rbColeccion.isSelected()) {
            if (!tfNombreColeccion.getText().isEmpty()) {
                sql.append(" AND c.nombre_coleccion ILIKE ?");
                parametros.add("%" + tfNombreColeccion.getText() + "%");
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
}
