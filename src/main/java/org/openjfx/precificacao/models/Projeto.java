package org.openjfx.precificacao.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Projeto {

    private Integer id;
    private String codProjeto;
    private String nomeProjeto;
    private Integer idCliente;
    private String tipo;    // Novo ou Reforma
    private String status;
    private double precificacao;

    @Override
    public String toString() {
        return (nomeProjeto + " - Projeto Nº " + codProjeto + " . Status: " + status).toUpperCase();
    }
}
