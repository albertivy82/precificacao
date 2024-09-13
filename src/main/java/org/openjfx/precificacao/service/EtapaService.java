package org.openjfx.precificacao.service;

import org.openjfx.precificacao.database.ClienteSQLite;
import org.openjfx.precificacao.database.EtapaSQLite;
import org.openjfx.precificacao.models.Etapa;

import java.util.List;

public class EtapaService {

    private EtapaSQLite etapas;


    public List<String> etapaPorNome(){
        this.etapas = new EtapaSQLite();

        List<String> etapas = this.etapas.nomesEtapas();

        return etapas;
    }

    public int idEtapa(String etapa){
        this.etapas = new EtapaSQLite();

        int idEtapa =  this.etapas.buscaIdEtapaPorNome(etapa);

        return idEtapa;
    }


    public String nomeEtapa(int id){
        this.etapas = new EtapaSQLite();

       String nomeEtapa =  this.etapas.etapaPorNome(id);

        return nomeEtapa;
    }

    public List<Etapa> todasetapas(){
        this.etapas = new EtapaSQLite();
        return this.etapas.all();
    }



}
