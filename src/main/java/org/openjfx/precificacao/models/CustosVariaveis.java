package org.openjfx.precificacao.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustosVariaveis {

    private Integer id;

    private String item;

    private Float valor;


    @Override
    public String toString() {
        return (item + ": "  + valor).toUpperCase();
    }

}
