package org.openjfx.precificacao.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Profissionais {

    private Integer id;
    private String nome;
    private String profissional;
    private Float valorHora;

    @Override
    public String toString() {
        return (nome + ": "  + profissional).toUpperCase();
    }

}
