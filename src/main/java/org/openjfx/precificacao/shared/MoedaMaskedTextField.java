package org.openjfx.precificacao.shared;

import javafx.scene.control.TextField;

public class MoedaMaskedTextField extends TextField {

    public MoedaMaskedTextField() {
        this.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*([\\.,]\\d{0,2})?")) {
                setText(oldValue);
            } else {
                newValue = newValue.replaceAll("[^\\d\\.,]", "");
                setText(formatCurrency(newValue));
                positionCaret(getText().length());
            }
        });
    }

    private String formatCurrency(String value) {
        // Remove non-numeric characters
        value = value.replaceAll("[^\\d]", "");
        if (value.isEmpty()) return "";

        // Ensure correct formatting for the decimal part
        if (value.length() <= 2) {
            return "0." + ("00" + value).substring(value.length());
        }

        // Separate the integer part
        int length = value.length();
        String decimalPart = value.substring(length - 2);
        String integerPart = value.substring(0, length - 2);

        // Add thousands separator
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < integerPart.length(); i++) {
            if (i > 0 && (integerPart.length() - i) % 3 == 0) {
                formatted.append(".");
            }
            formatted.append(integerPart.charAt(i));
        }
        formatted.append(",").append(decimalPart);

        return formatted.toString();
    }
}
