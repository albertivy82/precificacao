package org.openjfx.precificacao.shared;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class MoedaMaskedTextField extends TextField {
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public MoedaMaskedTextField() {
        setText(currencyFormat.format(0));

        this.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == null || newValue.isEmpty()) {
                    return;
                }

                // Remove non-digit characters
                String digits = newValue.replaceAll("[^0-9]", "");

                // Convert to double
                double value;
                try {
                    value = Double.parseDouble(digits) / 100;
                } catch (NumberFormatException e) {
                    setText(oldValue);
                    return;
                }

                // Format as currency
                setText(currencyFormat.format(value));

                // Position the caret at the end
                positionCaret(getText().length());
            }
        });

        this.setOnMouseClicked(event -> {
            if (getText().equals(currencyFormat.format(0))) {
                clear();
            }
        });

        this.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (getText().isEmpty()) {
                    setText(currencyFormat.format(0));
                }
            }
        });
    }

    public double getAmount() {
        try {
            return currencyFormat.parse(getText()).doubleValue();
        } catch (ParseException e) {
            return 0;
        }
    }
}
