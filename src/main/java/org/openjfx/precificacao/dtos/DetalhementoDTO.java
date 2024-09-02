package org.openjfx.precificacao.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetalhementoDTO {

    private String nomeProjeto;
    private String nomeEtapa;
    private String nomeAtividade;
    private String nomeProfissional;

    private float valorHoras;
    private float horas;

    public DetalhementoDTO(String nomeProjeto, String nomeEtapa, String nomeAtividade, String nomeProfissional, float valorHoras, float horas) {
    }
}
