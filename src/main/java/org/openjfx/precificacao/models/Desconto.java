package org.openjfx.precificacao.models;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Desconto {

    private int id;
    private int idProjeto;
    private double margemDesconto;
    private double desconto;



    @Override
    public String toString() {
        return ("Total de descontos do Projeto: R$" +  String.format("%.2f", desconto)).toUpperCase();
    }
}
