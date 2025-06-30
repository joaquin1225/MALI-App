package my.database.maliapp;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import java.util.function.UnaryOperator;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyEvent;

public class Filtros {
    // Metodo para bloquear tildes en un campo de texto
    public static void bloquearTildes(TextInputControl campo) {
        campo.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.matches(".*[áéíóúÁÉÍÓÚ].*")) {
                campo.setText(newVal.replaceAll("[áéíóúÁÉÍÓÚ]", ""));
            }
        });
        campo.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String character = event.getCharacter();
            if (character.equals("´") || character.equals("`")) {
                event.consume();
            }
        });
    }

    // Metodo para permitir solo números en un campo de texto con un límite opcional de dígitos
    public static void soloNumeros(TextField textField, Integer maxDigitos) {
        UnaryOperator<TextFormatter.Change> filtro = change -> {
            String nuevoTexto = change.getControlNewText();
            if (maxDigitos == null) {
                return nuevoTexto.matches("\\d*") ? change : null;
            }
            return nuevoTexto.matches("\\d{0," + maxDigitos + "}") ? change : null;
        };

        textField.setTextFormatter(new TextFormatter<>(filtro));
    }
}