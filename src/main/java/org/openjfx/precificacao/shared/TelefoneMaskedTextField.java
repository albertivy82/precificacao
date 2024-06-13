package org.openjfx.precificacao.shared;

import javafx.scene.control.TextField;

public class TelefoneMaskedTextField extends TextField {

    public TelefoneMaskedTextField() {
        this.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(oldValue)) {
                return;
            }

            newValue = newValue.replaceAll("[^\\d]", ""); // Remove caracteres não numéricos
            if (newValue.length() > 11) {
                newValue = oldValue; // Evita entrada excessiva
            }
            if (newValue.length() > 2) {
                newValue = "(" + newValue.substring(0, 2) + ") " + newValue.substring(2);
            }
            if (newValue.length() > 10) {
                newValue = newValue.substring(0, 10) + "-" + newValue.substring(10);
            }

            setText(newValue);
            positionCaret(newValue.length());
        });
    }
}
