package org.openjfx.precificacao.shared;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatadorMoeda {

    // Método para formatar valores com separador de milhar e duas casas decimais
    public static String formatarValorComoMoeda(float valor) {
        // Configura o formato de moeda para o padrão brasileiro (pt-BR)
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        // Retorna o valor formatado como String com símbolo de moeda (R$)
        return formatter.format(valor);
    }

}

