package org.openjfx.precificacao.shared;

public class CPFValidator {

	
	public static boolean isValidCPF(String cpf) {
	 
		
		String strCPF = cpf.replaceAll("\\D", ""); // Remove caracteres não numéricos
        int sum = 0;
        int rest;

        if (strCPF.length() != 11 || strCPF.equals("00000000000")) {
            return false;
        }

        // Cálculo do primeiro dígito verificador
        for (int i = 1; i <= 9; i++) {
            sum += Integer.parseInt(strCPF.substring(i - 1, i)) * (11 - i);
        }
        rest = (sum * 10) % 11;
        if (rest == 10 || rest == 11) {
            rest = 0;
        }
        if (rest != Integer.parseInt(strCPF.substring(9, 10))) {
            return false;
        }

        // Cálculo do segundo dígito verificador
        sum = 0;
        for (int i = 1; i <= 10; i++) {
            sum += Integer.parseInt(strCPF.substring(i - 1, i)) * (12 - i);
        }
        rest = (sum * 10) % 11;
        if (rest == 10 || rest == 11) {
            rest = 0;
        }

        return rest == Integer.parseInt(strCPF.substring(10, 11));
    }

		
		
		
		
	}		


