package org.openjfx.precificacao.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Atividade {

    private int id;
    private String atividade;
    private int etapa;

    @Override
    public String toString() {
        return (atividade + " da etapa: "  + etapa--).toUpperCase();
    }
}
