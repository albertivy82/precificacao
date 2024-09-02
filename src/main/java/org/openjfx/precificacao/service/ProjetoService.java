package org.openjfx.precificacao.service;

import org.openjfx.precificacao.database.*;
import org.openjfx.precificacao.dtos.DetalhementoDTO;
import org.openjfx.precificacao.models.Atividade;
import org.openjfx.precificacao.models.Detalhamento;
import org.openjfx.precificacao.models.Etapa;
import org.openjfx.precificacao.models.Profissionais;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProjetoService {

    private EtapaSQLite etapas;
    private AtividadeSQLite atividades;
    private ProfissionaisSQLite profissionais;
    private DetalhamentoSQLite detalhamentos;
    private ProjetoSQLite projetosBnaco;

    public ProjetoService() {
        this.etapas = new EtapaSQLite();
        this.detalhamentos = new DetalhamentoSQLite();
        this.atividades = new AtividadeSQLite();
        this.profissionais = new ProfissionaisSQLite();
        this.projetosBnaco = new ProjetoSQLite();
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

    public List<DetalhementoDTO> etpasSalvas(int id_projeto){

        List<Detalhamento> detahesDoProjetoAtual = this.detalhamentos.detalhementoPorProjeto(id_projeto);
        List<DetalhementoDTO> detalhesCompletos = new ArrayList<>();
        for(Detalhamento item: detahesDoProjetoAtual){
            String nomeProjeto = buscarNomeProjetoPorId(item.getIdProjeto());
            String nomeEtapa = buscarNomeEtapaPorId(item.getIdEtapa());
            String nomeAtividade = buscarNomeAtividadePorId(item.getIdAtividade());
            String nomeProfissional = buscarNomeProfissionalPorId(item.getIdProfissional());
            float valorHoras = item.getValorHora();
            float horas = item.getHoras();

            DetalhementoDTO detalhamentoatual = new DetalhementoDTO(nomeProjeto, nomeEtapa, nomeAtividade, nomeProfissional, valorHoras, horas);
            detalhesCompletos.add(detalhamentoatual);
        }
       return null;
    }


    private String buscarNomeProjetoPorId(int idProjeto) {
        return this.projetosBnaco.projetoPorNome(idProjeto);
    }
    private String buscarNomeEtapaPorId(int idEtapa) {
        return this.etapas.etapaPorNome(idEtapa);
    }
    private String buscarNomeAtividadePorId(int idAtividade) {
        return this.atividades.atividadePorNome(idAtividade);
    }
    private String buscarNomeProfissionalPorId(int idProfissional) {
        return this.profissionais.profissionalPorNome(idProfissional);
    }




}
