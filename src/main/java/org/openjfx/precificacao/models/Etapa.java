package org.openjfx.precificacao.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Etapa {

    private int id;
    private String etapa;

    @Override
    public String toString() {
        return (etapa).toUpperCase();
    }
}
