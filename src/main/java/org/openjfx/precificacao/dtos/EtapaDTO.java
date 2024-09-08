package org.openjfx.precificacao.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EtapaDTO {

    private int id;
    private String etapa;
    private Float TotalPorEtapaProjeto;
}
