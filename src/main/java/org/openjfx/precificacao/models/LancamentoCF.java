package org.openjfx.precificacao.models;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
public class LancamentoCF {

    private int id;
    private int idProjeto;
    private Float desconto;
    private LocalDate data;


    @Override
    public String toString() {
        return ("Valor do desconto: R$" +  String.format("%.2f", desconto) + "registrado em " + data).toUpperCase();
    }
}
