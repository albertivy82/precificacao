package org.openjfx.precificacao.service;

import org.openjfx.precificacao.database.AtividadeSQLite;
import org.openjfx.precificacao.database.DetalhamentoSQLite;
import org.openjfx.precificacao.database.EtapaSQLite;
import org.openjfx.precificacao.database.ProfissionaisSQLite;
import org.openjfx.precificacao.models.Atividade;
import org.openjfx.precificacao.models.Detalhamento;
import org.openjfx.precificacao.models.Etapa;
import org.openjfx.precificacao.models.Profissionais;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class ProjetoService {

    private EtapaSQLite etapas;
    private AtividadeSQLite atividades;
    private ProfissionaisSQLite profissionais;
    private DetalhamentoSQLite detalhamentos;

    public ProjetoService() {
        this.etapas = new EtapaSQLite();
        this.detalhamentos = new DetalhamentoSQLite();
        this.atividades = new AtividadeSQLite();
        this.profissionais = new ProfissionaisSQLite();
    }

    public List<Etapa> listaEtapas(){
        return this.etapas.all();
    }

    public List<Atividade> listaAtividades(int idEtapas){
        return this.atividades.atividadePorEtapa(idEtapas);
    }

    public void cadastroDeEtapas(Set<Detalhamento> detalhamento) throws SQLException {
        this.detalhamentos.CadastroDetalhamentoEtapas(detalhamento);
    }

    public List<Profissionais> listaProfissionais(){
        return this.profissionais.all();
    }

    public List<Detalhamento> listaEtapasCadastradas(){
        return this.detalhamentos.all();
    }

    public List<String> etpasSalvas(){
        //pegar etapa, prfissionais, atividades e valores.
       return null;
    }
}
