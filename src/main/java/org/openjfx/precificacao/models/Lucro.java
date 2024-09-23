package org.openjfx.precificacao.models;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Lucro {

    private int id;
    private int idProjeto;
    private double lucro;



    @Override
    public String toString() {
        return ("Total de quota sobre as despesas R$" +  String.format("%.2f", lucro)).toUpperCase();
    }
}
