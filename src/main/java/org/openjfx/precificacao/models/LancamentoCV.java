package org.openjfx.precificacao.models;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
public class LancamentoCV {

    private int id;
    private int idProjeto;
    private int idCustoVariavel;
    private String nomeCusto;
    private Float valorUnitario;
    private Float quantidade;
    private String obs;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LancamentoCV that = (LancamentoCV) o;
        return idProjeto == that.idProjeto &&
                idCustoVariavel == that.idCustoVariavel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProjeto, idCustoVariavel);
    }


    @Override
    public String toString() {
        return (nomeCusto+": R$" +  String.format("%.2f", valorUnitario * quantidade)).toUpperCase();
    }
}
