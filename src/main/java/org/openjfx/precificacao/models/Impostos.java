package org.openjfx.precificacao.models;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Impostos {

    private int id;
    private int idProjeto;
    private double iss;
    private double simplesNac;



    @Override
    public String toString() {
        return ("Valor dos impostos: ISS: R$" +  String.format("%.2f", iss) + " Simples nacional  R$ " + simplesNac).toUpperCase();
    }
}
