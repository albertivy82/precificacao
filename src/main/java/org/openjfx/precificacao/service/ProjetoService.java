package org.openjfx.precificacao.service;

import org.openjfx.precificacao.database.*;
import org.openjfx.precificacao.dtos.DetalhamentoDTO;
import org.openjfx.precificacao.enums.StatusProjeto;
import org.openjfx.precificacao.models.*;
import org.openjfx.precificacao.validator.StatusRules;

import java.time.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ProjetoService {

    private EtapaSQLite etapas;
    private AtividadeSQLite atividades;
    private ProfissionaisSQLite profissionais;
    private DetalhamentoSQLite detalhamentos;
    private ProjetoSQLite projetosBnaco;
    private CustosService lancamentoCVService;
    private CustosFixosService lancamentoCFService;
    private LucroService lucroService;
    private ConsolidaProjetoSQLite consolidacoes;
    private final StatusRules statusRules;


    public ProjetoService() {
        this.etapas = new EtapaSQLite();
        this.detalhamentos = new DetalhamentoSQLite();
        this.atividades = new AtividadeSQLite();
        this.profissionais = new ProfissionaisSQLite();
        this.projetosBnaco = new ProjetoSQLite();
        this.consolidacoes = new ConsolidaProjetoSQLite();
        this.statusRules = new StatusRules();
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
        return this.atividades.buscaNomeAtividadePorId(idAtividade);
    }
    private String buscarNomeProfissionalPorId(int idProfissional) {
        return this.profissionais.profissionalPorNome(idProfissional);
    }


    public int buscarIdProjetoPorNome(String nome) {
        return this.projetosBnaco.buscaIdProjetoPorNome(nome);
    }

    public void precificarProjeto(Projeto projeto) throws SQLException {
        System.out.println("Executando automáticamente?");
        this.projetosBnaco.editarProjeto(projeto);
    }
    public int buscarIdEtapaPorNome(String nome) {
        return this.etapas.buscaIdEtapaPorNome(nome);
    }
    public int buscarIdAtividadePorNome(String nome) {
        return this.atividades.buscaIdAtividadePorNome(nome);
    }
    public int buscarIdProfissionalPorNome(String nome) {
        return this.profissionais.buscaIdProfissionalPorNome(nome);
    }

    public void deletarRegistroDetalhamento(int idProjeto, int idEtapa, int idAtividade, int idProfissional, float valorHoras, float horas){
        this.detalhamentos.deletarRegistro(idProjeto, idEtapa, idAtividade, idProfissional, valorHoras, horas);
    }

    public boolean deletarEtapa( int idProjeto, int idEtapa){
        return this.detalhamentos.deletarEtapa(idProjeto, idEtapa);
    }

    public boolean deletarAtividade( int idProjeto, int idAtividade){
        return this.detalhamentos.deletarAtividade(idProjeto, idAtividade);
    }
    public float totalDeServicosDoProjeto(int idProjeto) {
        return this.detalhamentos.totalDeServicosPorProjeto(idProjeto);
    }

   public float totalEtapaPeloId(int idProjeto, int idEtapa) {
       return this.etapas.totalEtapaPorId(idProjeto, idEtapa);
    }

    public void statusProjeto(Projeto projeto) throws SQLException {
       this.projetosBnaco.editarProjeto(projeto);

    }

    public void gerarCodProjeto(String nomeProjeto){

        int id =  this.projetosBnaco.projetoPorNome(nomeProjeto);
        LocalDate hoje = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMyyyy");
        String dataFormatada = hoje.format(formatter);
        String cod = String.format("%d%s", id, dataFormatada);
        System.out.println(cod);
        this.projetosBnaco.gerarCodProjeto(id, cod);

    }

    public boolean projetoNomeRepetido(String nomeProjeto){
        int id =this.projetosBnaco.projetoPorNome(nomeProjeto);

        if(id==0){
            return false;
        }else{
            return true;
        }

    }

    public void deletaProjetoEDependencias(int projetoId) throws SQLException {
       this.projetosBnaco.deletarProjeto(projetoId);
       this.lancamentoCVService = new CustosService();
       this.lancamentoCFService = new CustosFixosService();
       this.lucroService = new LucroService();
       this.detalhamentos.deletarDetalhamentoPorProjeto(projetoId);
       this.lancamentoCVService.deletarLacamentoCVPorProjeto(projetoId);
       this.lancamentoCFService.deletarPorProjeto(projetoId);
       this.lucroService.deletarLucroDoProjeto(projetoId);

    }

    public List<Integer> idsProejoPorCliente(int idCliente){
        return this.projetosBnaco.buscarProjetosPorCliente(idCliente);
    }

    public Map<String, Integer> buscarStatusDeProjeto() {
        return this.projetosBnaco.contarProjetosPorStatus();
    }

    public Map<String, Double> listarProjetosComValores() {
        return this.detalhamentos.listarProjetosComValores();
    }


    public Map<String, Double> listarhorasPorProfissional() {
        return this.detalhamentos.buscarHorasPorTodosProfissional();
    }

    public Map<String, Double> listarValorPorProfissional() {
        return this.detalhamentos.buscarValorEHorasPorProfissional();
    }

    public Map<String, Double> listarTotalPorCliente() {
        return this.detalhamentos.buscarTotalPorCliente();
    }


    public Map<String, Double> listarhorasPorProfissionalPorProjeto(int ProjetoId) {
        return this.detalhamentos.buscarHorasPorProfissionalProjeto(ProjetoId);
    }

    public Map<String, Double> listarValorPorProfissionalPorProjeto(int ProjetoId) {
        return this.detalhamentos.buscarValorEHorasPorProfissionalProjeto(ProjetoId);
    }

    public void inicioProjeto(Projeto projeto) throws SQLException {
        ConsolidaProjeto novaConsolidacao = new ConsolidaProjeto();

        novaConsolidacao.setIdProjeto(projeto.getId());
        novaConsolidacao.setIdCliente(projeto.getIdCliente());
        novaConsolidacao.setAnoInicio(Year.now().getValue());

        String mesAtual = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("MMMM", new Locale("pt", "BR")));
        novaConsolidacao.setMesInicio(mesAtual.toLowerCase());

        novaConsolidacao.setValorInicial(projeto.getPrecificacao() * 0.5);

        novaConsolidacao.setAnoFim(-1); // Indefinido por enquanto
        novaConsolidacao.setMesFinal("sem previsão");
        novaConsolidacao.setValorFinal(0); // Ou outro valor padrão

        this.consolidacoes.novaConsolida(novaConsolidacao);
    }

    public Boolean verificaProjetoExitente(int idProjeto) throws SQLException {
             return this.consolidacoes.buscarRegistroExistente(idProjeto).isPresent();
   }


    public Boolean verificaExecucaoProjeto(int idProjeto) throws SQLException {
         ConsolidaProjeto projeto = this.consolidacoes.buscarRegistroExistente(idProjeto).
                 orElseThrow(() -> new NoSuchElementException("Nenhum projeto consolidado encontrado para o ID: " + idProjeto));
         if(projeto.getAnoFim()== -1 && projeto.getMesFinal().equals("sem previsão") && projeto.getValorFinal()==0){
             return true;
         }else{
             return false;
         }
    }

    public void finalizaProjeto(Projeto projeto) throws SQLException {

        ConsolidaProjeto consolidado = this.consolidacoes
                .buscarRegistroExistente(projeto.getId())
                .orElseThrow(() -> new NoSuchElementException("Consolidação não encontrada"));

        ConsolidaProjeto finalizaConsolidacao = new ConsolidaProjeto();
        finalizaConsolidacao.setAnoFim(Year.now().getValue());

        String mesAtual = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("MMMM", new Locale("pt", "BR")));
        finalizaConsolidacao.setMesFinal(mesAtual.toLowerCase());
        finalizaConsolidacao.setValorFinal(projeto.getPrecificacao() * 0.5);

        this.consolidacoes.editarConsolida(finalizaConsolidacao, consolidado.getId());
    }

    public List<ConsolidaProjeto> listaConsolidacao() throws SQLException {

        return this.consolidacoes.all();

    }

    public void alterarStatusProjeto(int idProjeto, StatusProjeto novoStatus) throws SQLException {
        this.statusRules.alterarStatus(idProjeto, novoStatus);
    }






}
