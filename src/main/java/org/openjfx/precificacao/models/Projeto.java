package org.openjfx.precificacao.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Projeto {

    private Integer id;
    private String nomeProjeto;
    private Integer idCliente;
    private String status;
    private float precificacao;


    @Override
    public String toString() {
        return (nomeProjeto + " - " + id + " . Satus: " + status).toUpperCase();
    }
}
