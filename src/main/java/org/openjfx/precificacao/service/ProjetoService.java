package org.openjfx.precificacao.service;

import org.openjfx.precificacao.database.*;
import org.openjfx.precificacao.dtos.DetalhamentoDTO;
import org.openjfx.precificacao.models.Atividade;
import org.openjfx.precificacao.models.Detalhamento;
import org.openjfx.precificacao.models.Etapa;
import org.openjfx.precificacao.models.Profissionais;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

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

    public Map<String, Map<String, List<DetalhamentoDTO>>> etapasSalvas(int idProjeto) {
        List<Detalhamento> detalhesDoProjetoAtual = this.detalhamentos.detalhementoPorProjeto(idProjeto);

        List<DetalhamentoDTO> detalhesCompletos = detalhesDoProjetoAtual.stream().map(item -> {
            DetalhamentoDTO detalhamentoatual = new DetalhamentoDTO();
            detalhamentoatual.setNomeProjeto(buscarNomeProjetoPorId(item.getIdProjeto()));
            detalhamentoatual.setNomeEtapa(buscarNomeEtapaPorId(item.getIdEtapa()));
            detalhamentoatual.setNomeAtividade(buscarNomeAtividadePorId(item.getIdAtividade()));
            detalhamentoatual.setNomeProfissional(buscarNomeProfissionalPorId(item.getIdProfissional()));
            detalhamentoatual.setValorHoras(item.getValorHora());
            detalhamentoatual.setHoras(item.getHoras());
            return detalhamentoatual;
        }).collect(Collectors.toList());

        return detalhesCompletos.stream()
                .collect(Collectors.groupingBy(
                        DetalhamentoDTO::getNomeEtapa,
                        () -> new TreeMap<>(String::compareTo), // TreeMap garante a ordenação das etapas
                        Collectors.groupingBy(
                                DetalhamentoDTO::getNomeAtividade,
                                () -> new TreeMap<>(String::compareTo), // TreeMap garante a ordenação das atividades
                                Collectors.toList()
                        )
                ));
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
