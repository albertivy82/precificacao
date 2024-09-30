package org.openjfx.precificacao.models;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cidade {

    private int id;
    private String cidade;
    private int idEstado;

    @Override
    public String toString() {
        return (cidade).toUpperCase();
    }
}
