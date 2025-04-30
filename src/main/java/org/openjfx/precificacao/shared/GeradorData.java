package org.openjfx.precificacao.shared;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class GeradorData {

	
	public static String GeradorData() {

        YearMonth hoje = YearMonth.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM_yyyy", new Locale("pt", "BR"));
        String mesAnoFormatado = hoje.format(formatter);

        return mesAnoFormatado;
    }
}


