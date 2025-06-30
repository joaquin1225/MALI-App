package my.database.maliapp.roles.admin_arte;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import my.database.maliapp.modelos.*;
import my.database.maliapp.Filtros;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegistrarObraView {
    private final Connection conn;
    private final Runnable onObraAgregada;

    public RegistrarObraView(Connection conn, Runnable onObraAgregada) {
        this.conn = conn;
        this.onObraAgregada = onObraAgregada;
    }

    public void mostrar(Stage owner) {
        mostrar(owner, null); // modo creación por defecto
    }

    public void mostrar(Stage owner, ObraExtendida obra) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(owner);
        stage.setTitle(obra == null ? "Registrar nueva obra" : "Editar obra");

        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        // Campos obra
        TextField tfTitulo = new TextField();
        Filtros.bloquearTildes(tfTitulo);
        tfTitulo.setPromptText("Titulo");

        TextField tfFechaMin = new TextField();
        Filtros.soloNumeros(tfFechaMin, 4);
        tfFechaMin.setPromptText("Fecha mínima (ej. 1980)");

        TextField tfFechaMax = new TextField();
        Filtros.soloNumeros(tfFechaMax, 4);
        tfFechaMax.setPromptText("Fecha máxima (ej. 1990)");

        ComboBox<String> cbTipo = new ComboBox<>();
        cbTipo.setPromptText("Tipo");
        cbTipo.setEditable(true);
        cargarValoresUnicos("tipo", cbTipo);

        ComboBox<String> cbEstado = new ComboBox<>();
        cbEstado.setPromptText("Estado");
        cbEstado.setEditable(true);
        cargarValoresUnicos("estado", cbEstado);

        // Artista
        Label lblArtista = new Label("Artista:");
        ComboBox<Artista> cbArtistas = new ComboBox<>();
        cbArtistas.setPromptText("Seleccionar artista");
        cargarArtistas(cbArtistas);

        Button btnNuevoArtista = new Button("Añadir artista");
        btnNuevoArtista.setOnAction(event -> mostrarVentanaNuevoArtista(cbArtistas));

        // Colección
        ComboBox<Coleccion> cbColeccion = new ComboBox<>();
        Button btnNuevaColeccion = new Button("Añadir colección");
        btnNuevaColeccion.setOnAction(e -> mostrarVentanaNuevaColeccion(cbColeccion));
        cbColeccion.setPromptText("Colección (opcional)");
        cargarColecciones(cbColeccion);

        // Si se está editando, precargar los campos
        Integer idObra = null;
        if (obra != null) {
            idObra = obra.getObra().getIdObra();
            tfTitulo.setText(obra.getTitulo());
            tfFechaMin.setText(String.valueOf(obra.getFechaMin()));
            tfFechaMax.setText(String.valueOf(obra.getFechaMax()));
            cbTipo.setValue(obra.getTipo());
            cbEstado.setValue(obra.getEstado());
            cbArtistas.getSelectionModel().select(obra.getArtista());
            if (obra.getColeccion() != null) {
                cbColeccion.getSelectionModel().select(obra.getColeccion());
            }
        }

        // Botones
        Button btnGuardar = new Button(obra == null ? "Guardar" : "Actualizar");
        Button btnCancelar = new Button("Cancelar");

        Integer finalIdObra = idObra;
        btnGuardar.setOnAction(e -> {
            try {
                String titulo = tfTitulo.getText();
                int fechaMin = Integer.parseInt(tfFechaMin.getText());
                int fechaMax = Integer.parseInt(tfFechaMax.getText());
                String tipo = cbTipo.getValue();
                String estado = cbEstado.getValue();

                if (tipo == null || tipo.isEmpty()) {
                    mostrarAlerta("Error", "Debes ingresar un tipo.");
                    return;
                }
                if (estado == null || estado.isEmpty()) {
                    mostrarAlerta("Error", "Debes ingresar un estado.");
                    return;
                }

                Artista artistaSeleccionado = cbArtistas.getValue();
                if (artistaSeleccionado == null) {
                    mostrarAlerta("Error", "Debes seleccionar un artista.");
                    return;
                }
                int idArtista = artistaSeleccionado.getIdArtista();

                // Insertar o actualizar obra
                int idObraEditada;
                if (finalIdObra == null) {
                    try (PreparedStatement ps = conn.prepareStatement("""
                    INSERT INTO obra_de_arte(id_artista, titulo, fecha_min, fecha_max, tipo, estado)
                    VALUES (?, ?, ?, ?, ?, ?) RETURNING id_obra
                """)) {
                        ps.setInt(1, idArtista);
                        ps.setString(2, titulo);
                        ps.setInt(3, fechaMin);
                        ps.setInt(4, fechaMax);
                        ps.setString(5, tipo);
                        ps.setString(6, estado);
                        ResultSet rs = ps.executeQuery();
                        rs.next();
                        idObraEditada = rs.getInt(1);
                    }
                } else {
                    try (PreparedStatement ps = conn.prepareStatement("""
                    UPDATE obra_de_arte
                    SET id_artista = ?, titulo = ?, fecha_min = ?, fecha_max = ?, tipo = ?, estado = ?
                    WHERE id_obra = ?
                """)) {
                        ps.setInt(1, idArtista);
                        ps.setString(2, titulo);
                        ps.setInt(3, fechaMin);
                        ps.setInt(4, fechaMax);
                        ps.setString(5, tipo);
                        ps.setString(6, estado);
                        ps.setInt(7, finalIdObra);
                        ps.executeUpdate();
                    }
                    idObraEditada = finalIdObra;
                }

                // Insertar/Actualizar colección
                Coleccion seleccionada = cbColeccion.getValue();
                try (PreparedStatement ps = conn.prepareStatement("""
                DELETE FROM pertenece_a WHERE id_obra = ?
            """)) {
                    ps.setInt(1, idObraEditada);
                    ps.executeUpdate();
                }
                if (seleccionada != null) {
                    try (PreparedStatement ps = conn.prepareStatement("""
                    INSERT INTO pertenece_a(id_obra, id_coleccion) VALUES (?, ?)
                """)) {
                        ps.setInt(1, idObraEditada);
                        ps.setInt(2, seleccionada.getIdColeccion());
                        ps.executeUpdate();
                    }
                }

                stage.close();
                onObraAgregada.run();

            } catch (Exception ex) {
                ex.printStackTrace();
                mostrarAlerta("Error", "Verifica que todos los campos estén correctos.");
            }
        });

        btnCancelar.setOnAction(e -> stage.close());

        root.getChildren().addAll(
                new Label("Datos de la obra:"), tfTitulo, tfFechaMin, tfFechaMax, cbTipo, cbEstado,
                lblArtista, new HBox(10, cbArtistas, btnNuevoArtista),
                new Label("Colección (opcional):"), new HBox(10, cbColeccion, btnNuevaColeccion),
                new HBox(10, btnGuardar, btnCancelar)
        );

        stage.setScene(new Scene(root));
        stage.show();
    }

    private void cargarColecciones(ComboBox<Coleccion> comboBox) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM coleccion ORDER BY nombre_coleccion");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Coleccion c = new Coleccion(
                        rs.getInt("id_coleccion"),
                        rs.getString("nombre_coleccion")
                );
                comboBox.getItems().add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void cargarArtistas(ComboBox<Artista> comboBox) {
        comboBox.getItems().clear();
        try (PreparedStatement ps = conn.prepareStatement("""
            SELECT id_artista, nombre, apellido, pais, fecha_nac, fecha_fallec
            FROM artista ORDER BY nombre, apellido
        """);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Artista artista = new Artista(
                        rs.getInt("id_artista"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("pais"),
                        (Integer) rs.getObject("fecha_nac"),
                        (Integer) rs.getObject("fecha_fallec")
                );
                comboBox.getItems().add(artista);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarVentanaNuevoArtista(ComboBox<Artista> comboBox) {
        Stage ventana = new Stage();
        ventana.initModality(Modality.APPLICATION_MODAL);
        ventana.setTitle("Nuevo artista");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));

        TextField tfNombre = new TextField(); tfNombre.setPromptText("Nombre"); Filtros.bloquearTildes(tfNombre);
        TextField tfApellido = new TextField(); tfApellido.setPromptText("Apellido"); Filtros.bloquearTildes(tfApellido);
        TextField tfPais = new TextField(); tfPais.setPromptText("País"); Filtros.bloquearTildes(tfPais);
        TextField tfNacimiento = new TextField(); tfNacimiento.setPromptText("Año nacimiento"); Filtros.soloNumeros(tfNacimiento, 4);
        TextField tfFallecimiento = new TextField(); tfFallecimiento.setPromptText("Año fallecimiento (opcional)"); Filtros.soloNumeros(tfFallecimiento, 4);

        Button btnGuardar = new Button("Guardar");
        Button btnCancelar = new Button("Cancelar");

        btnGuardar.setOnAction(e -> {
            try {
                String nombre = tfNombre.getText();
                String apellido = tfApellido.getText();
                String pais = tfPais.getText();
                Integer nac = Integer.parseInt(tfNacimiento.getText());
                Integer fallec = tfFallecimiento.getText().isEmpty() ? null : Integer.parseInt(tfFallecimiento.getText());

                Artista nuevoArtista;
                try (PreparedStatement ps = conn.prepareStatement("""
                INSERT INTO artista(nombre, apellido, pais, fecha_nac, fecha_fallec)
                VALUES (?, ?, ?, ?, ?) RETURNING id_artista
            """)) {
                    ps.setString(1, nombre);
                    ps.setString(2, apellido);
                    ps.setString(3, pais);
                    ps.setObject(4, nac);
                    ps.setObject(5, fallec);
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    nuevoArtista = new Artista(rs.getInt(1), nombre, apellido, pais, nac, fallec);
                }

                ventana.close();
                cargarArtistas(comboBox);
                comboBox.getSelectionModel().select(nuevoArtista);

            } catch (Exception ex) {
                ex.printStackTrace();
                mostrarAlerta("Error", "Verifica los campos del nuevo artista.");
            }
        });

        btnCancelar.setOnAction(e -> ventana.close());

        vbox.getChildren().addAll(tfNombre, tfApellido, tfPais, tfNacimiento, tfFallecimiento, new HBox(10, btnGuardar, btnCancelar));
        ventana.setScene(new Scene(vbox));
        ventana.show();
    }

    private void mostrarVentanaNuevaColeccion(ComboBox<Coleccion> comboBox) {
        Stage ventana = new Stage();
        ventana.initModality(Modality.APPLICATION_MODAL);
        ventana.setTitle("Nueva colección");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));

        TextField tfNombre = new TextField(); Filtros.bloquearTildes(tfNombre); tfNombre.setPromptText("Nombre de la colección");

        Button btnGuardar = new Button("Guardar");
        Button btnCancelar = new Button("Cancelar");

        btnGuardar.setOnAction(e -> {
            try {
                String nombre = tfNombre.getText();

                if (nombre.isEmpty()) {
                    mostrarAlerta("Error", "El nombre de la colección no puede estar vacío.");
                    return;
                }

                Coleccion nueva;
                try (PreparedStatement ps = conn.prepareStatement("""
                INSERT INTO coleccion(nombre_coleccion)
                VALUES (?) RETURNING id_coleccion
            """)) {
                    ps.setString(1, nombre);
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    nueva = new Coleccion(rs.getInt(1), nombre);
                }

                ventana.close();
                cargarColecciones(comboBox);
                comboBox.getSelectionModel().select(nueva);

            } catch (Exception ex) {
                ex.printStackTrace();
                mostrarAlerta("Error", "No se pudo crear la colección.");
            }
        });

        btnCancelar.setOnAction(e -> ventana.close());

        vbox.getChildren().addAll(tfNombre, new HBox(10, btnGuardar, btnCancelar));
        ventana.setScene(new Scene(vbox));
        ventana.show();
    }

    private void cargarValoresUnicos(String columna, ComboBox<String> comboBox) {
        comboBox.getItems().clear();
        String sql = "SELECT DISTINCT " + columna + " FROM obra_de_arte WHERE " + columna + " IS NOT NULL ORDER BY " + columna;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                comboBox.getItems().add(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
