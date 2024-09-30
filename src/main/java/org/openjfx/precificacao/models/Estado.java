package org.openjfx.precificacao.models;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Estado {

    private int id;
    private String estado;

    @Override
    public String toString() {
        return (estado).toUpperCase();
    }
}
