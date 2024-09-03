package org.openjfx.precificacao.service;

import org.openjfx.precificacao.database.*;
import org.openjfx.precificacao.dtos.DetalhementoDTO;
import org.openjfx.precificacao.models.Atividade;
import org.openjfx.precificacao.models.Detalhamento;
import org.openjfx.precificacao.models.Etapa;
import org.openjfx.precificacao.models.Profissionais;

import java.sql.SQLException;
import java.util.*;

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

    public Map<String, Map<String, List<DetalhementoDTO>>> etapasSalvas(int idProjeto){

        List<Detalhamento> detahesDoProjetoAtual = this.detalhamentos.detalhementoPorProjeto(idProjeto);
        List<DetalhementoDTO> detalhesCompletos = new ArrayList<>();
        for(Detalhamento item: detahesDoProjetoAtual){
            DetalhementoDTO detalhamentoatual = new DetalhementoDTO();
            detalhamentoatual.setNomeProjeto(buscarNomeProjetoPorId(item.getIdProjeto()));
            detalhamentoatual.setNomeEtapa(buscarNomeEtapaPorId(item.getIdEtapa()));
            detalhamentoatual.setNomeAtividade(buscarNomeAtividadePorId(item.getIdAtividade()));
            detalhamentoatual.setNomeProfissional(buscarNomeProfissionalPorId(item.getIdProfissional()));
            detalhamentoatual.setValorHoras(item.getValorHora());
            detalhamentoatual.setHoras(item.getHoras());
            detalhesCompletos.add(detalhamentoatual);
        }

        Map<String, Map<String, List<DetalhementoDTO>>> agrupados = new HashMap<>();

        for (DetalhementoDTO detalhe : detalhesCompletos) {
            agrupados
                    .computeIfAbsent(detalhe.getNomeEtapa(), k -> new HashMap<>())
                    .computeIfAbsent(detalhe.getNomeAtividade(), k -> new ArrayList<>())
                    .add(detalhe);
        }

        return agrupados;
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
