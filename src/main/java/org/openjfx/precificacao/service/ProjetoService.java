package org.openjfx.precificacao.service;

import org.openjfx.precificacao.database.AtividadeSQLite;
import org.openjfx.precificacao.database.EtapaSQLite;
import org.openjfx.precificacao.models.Atividade;
import org.openjfx.precificacao.models.Etapa;

import java.util.List;

public class ProjetoService {

    private EtapaSQLite etapas;
    private AtividadeSQLite atividades;

    public List<Etapa> listaEtapas(){


        this.etapas = new EtapaSQLite();
        List<Etapa> etapasBanco = this.etapas.all();

        return etapasBanco;

    }

    public List<Atividade> listaAtividades(int idEtapas){
        this.atividades = new AtividadeSQLite();
        List<Atividade> atividadesPorEtapa = this.atividades.atividadePorEtapa(idEtapas);
        return atividadesPorEtapa;
    }
}
