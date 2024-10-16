package org.openjfx.precificacao.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetalhamentoDTO {

    private String nomeProjeto;
    private String nomeEtapa;
    private String nomeAtividade;
    private String nomeProfissional;

    private float valorHoras;
    private float horas;

}
