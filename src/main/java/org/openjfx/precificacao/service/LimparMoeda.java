package org.openjfx.precificacao.service;

public class LimparMoeda {

    public Float LimpaMoeda(String valor) throws NumberFormatException {
        if (valor == null || valor.isEmpty()) {
            throw new NumberFormatException("Valor vazio ou nulo");
        }

        try {
            // Remover o símbolo de moeda e espaços (incluindo espaços não separáveis)
            String valorSemMoeda = valor.replace("R$", "").replace("\u00A0", "").trim();
            System.out.println("Valor sem moeda: " + valorSemMoeda);

            // Substituir pontos por vazio
            String valorSemPontos = valorSemMoeda.replace(".", "");
            System.out.println("Valor sem pontos: " + valorSemPontos);

            // Substituir vírgula por ponto
            String valorComPontoDecimal = valorSemPontos.replace(",", ".");
            System.out.println("Valor com ponto decimal: " + valorComPontoDecimal);

            // Converter para float
            float valorFloat = Float.parseFloat(valorComPontoDecimal);
            System.out.println("Valor float: " + valorFloat);

            return valorFloat;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Erro ao converter valor: " + valor);
        }
    }
}
