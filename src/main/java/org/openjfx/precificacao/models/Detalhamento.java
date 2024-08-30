package org.openjfx.precificacao.models;


import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Detalhamento {

    private int id;
    private int idProjeto;
    private int idEtapa;
    private int idAtividade;
    private int idProfissional;
    private Float valorHora;
    private Float valorHoras;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Detalhamento that = (Detalhamento) o;
        return idProjeto == that.idProjeto && idEtapa == that.idEtapa && idAtividade == that.idAtividade && idProfissional == that.idProfissional;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProjeto, idEtapa, idAtividade, idProfissional);
    }


    @Override
    public String toString() {
        return ("Projeto: " + idProjeto + "|Etapa: "+ idEtapa+" |Ativiadade: " + idAtividade + " |Profissional: " + idProfissional +" |Valor unitário R$: "+ valorHora +" |Valor do Serviço R$ "+valorHoras);
    }
}
