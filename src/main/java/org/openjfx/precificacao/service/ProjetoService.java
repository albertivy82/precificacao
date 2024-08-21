package org.openjfx.precificacao.service;

import org.openjfx.precificacao.database.AtividadeSQLite;
import org.openjfx.precificacao.database.EtapaSQLite;
import org.openjfx.precificacao.database.ProfissionaisSQLite;
import org.openjfx.precificacao.models.Atividade;
import org.openjfx.precificacao.models.Etapa;
import org.openjfx.precificacao.models.Profissionais;

import java.util.List;

public class ProjetoService {

    private EtapaSQLite etapas;
    private AtividadeSQLite atividades;
    private ProfissionaisSQLite profissionais;

    public ProjetoService() {
        this.etapas = new EtapaSQLite();
        this.atividades = new AtividadeSQLite();
        this.profissionais = new ProfissionaisSQLite();
    }

    public List<Etapa> listaEtapas(){
        return this.etapas.all();
    }

    public List<Atividade> listaAtividades(int idEtapas){
        return this.atividades.atividadePorEtapa(idEtapas);
    }

    public List<Profissionais> listaProfissionais(){
        return this.profissionais.all();
    }
}
