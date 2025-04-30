package org.openjfx.precificacao.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ConsolidaProjeto {

    private int id;
    private int idProjeto;
    private int idCliente;
    private int anoInicio;
    private String mesInicio;
    private double valorInicial;
    private String mesFinal;
    private double valorFinal;
    private int anoFim;

}


