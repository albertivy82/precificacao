package org.openjfx.precificacao.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustosFixos {

    private Integer id;

    private String item;

    private Float valor;


    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
