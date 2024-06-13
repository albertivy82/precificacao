package org.openjfx.precificacao.shared;

import javafx.scene.control.TextField;

public class CPFMaskedTextField extends TextField{
	
	public CPFMaskedTextField() {
        this.textProperty().addListener((observable, oldValue, newValue) -> {
            // Evita reação a mudanças recursivas
            if (newValue.equals(oldValue)) {
                return;
            }
            
            if (!newValue.matches("\\d*")) {
                newValue = newValue.replaceAll("[^\\d]", "");
            }
            if (newValue.length() > 11) {
                newValue = oldValue;
            }
            if (newValue.length() > 3 && !newValue.contains(".")) {
                newValue = newValue.replaceAll("(\\d{3})(\\d+)", "$1.$2");
            }
            if (newValue.length() > 7 && newValue.charAt(7) != '.') {
                newValue = newValue.replaceAll("(\\d{3})\\.(\\d{3})(\\d+)", "$1.$2.$3");
            }
            if (newValue.length() > 11 && newValue.charAt(11) != '-') {
                newValue = newValue.replaceAll("(\\d{3})\\.(\\d{3})\\.(\\d{3})(\\d+)", "$1.$2.$3-$4");
            }

            // Aplica a mudança de forma controlada
            setText(newValue);
            positionCaret(newValue.length()); // Move o cursor para o final
        });
    }

}
