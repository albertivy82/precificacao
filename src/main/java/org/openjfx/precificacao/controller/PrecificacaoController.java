package org.openjfx.precificacao.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import org.openjfx.precificacao.App;
import org.openjfx.precificacao.dtos.DetalhamentoDTO;
import org.openjfx.precificacao.models.Impostos;
import org.openjfx.precificacao.models.LancamentoCF;
import org.openjfx.precificacao.models.Lucro;
import org.openjfx.precificacao.models.Projeto;
import org.openjfx.precificacao.service.*;
import org.openjfx.precificacao.shared.FormatadorMoeda;
import org.openjfx.precificacao.shared.ProjetoSingleton;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class PrecificacaoController {

	Projeto projeto = ProjetoSingleton.getInstance().getProjeto();

	private ClienteService clienteService;
	private CustosFixosService custosFixosRaiz;
	private ProjetoService projetoService;
	private CustosService custosVariaveisService;
	private LancamentoCF lancamentoDeDesconto;
	private LucroService lucroService;
	private ImpostoService impostoService;
	private double lucroEsperado = 0;
	private double valorTotal = 0;


	@FXML
	private Label nomeProjetoLabel;

	@FXML
	private Label clienteLabel;

	@FXML
	private Label StatusLabel;

	@FXML
	private Label custosFixos;

	@FXML
	private Label valorFinanceiroLabel;

	@FXML
	private Label valorPercentualLabel;


	@FXML
	private Label valorLucro;

	@FXML
	private Label lucroLabel;

	@FXML
	private Label Porcentagem;

	@FXML
	private Slider sliderCustosFixos;

	@FXML
	private Label totalImpostosLabel;

	@FXML
	private CheckBox iss;

	@FXML
	private CheckBox simplesNacional;

	@FXML
	private Slider lucro;

	@FXML
	private Label totalProjetoLabel;

	@FXML
	private Label totalCustosVariaveisLabel;

	@FXML
	private Label totalDeLancamentosDeCF;

	@FXML
	private Label situacaoDeCustos;

	@FXML
	private Label lancamentoCF;

	@FXML
	private Label precoTotalProjetoLabel;




	@FXML
	private void btnDetalhamentoProjeto(ActionEvent e) {
		App.mudarTela("DetalhamentoProjeto");
	}

	@FXML
	protected VBox savedEtapasContainer;



	@FXML
	void initialize() throws SQLException {
		this.lancamentoDeDesconto = new LancamentoCF();
		this.lancamentoDeDesconto.setIdProjeto(projeto.getId());
		this.projetoService = new ProjetoService();
		this.custosVariaveisService = new CustosService();
		this.lucroService = new LucroService();
		this.impostoService = new ImpostoService();
		labelvalorDoProjeto();
		lalabelTotalCustosVariaveis();
		this.custosFixosRaiz = new CustosFixosService();
		identificacaoProjeto();
		slider();
		lucro();
		saldoAtualDeCusto();
		siuacaoDeCustos();
		atualizarLabelLancaentoCV();
		atualizarLabelImpostos();
		atualizarLabelLucro();
		somaTotalProjeto();
		somaTotalProjeto();
		btnPrecificarProjeto();
		precoTotalProjetoLabel();
		listaResultados();
	}

	private void identificacaoProjeto(){

		nomeProjetoLabel.setText("Nome do Projeto: " + projeto.getNomeProjeto());
		this.clienteService = new ClienteService();
		clienteLabel.setText("Cliente: " + this.clienteService.nomeCliente(projeto.getIdCliente()));
		StatusLabel.setText("Status: " + projeto.getStatus());
		custosFixos.setText("Custos Fixos Atual: " + String.format("R$ %.2f", custosFixosRaiz.totalCustosFixos()));
	}

	private void slider(){
		sliderCustosFixos.valueProperty().addListener((observable, oldValue, newValue) -> {
			double totalCustosFixos = custosFixosRaiz.totalCustosFixos();
			double percentual = newValue.doubleValue() / 100; // Converte o valor do slider para percentual.
			double valorLancado =totalCustosFixos * percentual;
			this.lancamentoDeDesconto.setDesconto(valorLancado);

			valorFinanceiroLabel.setText(String.format("Valor: R$ %.2f", valorLancado));
			// Atualiza o valor do Label ou outro componente que exibe o valor percentual.
			valorPercentualLabel.setText(String.format("Proporção: %.1f%%", percentual*100));

		});
	}

	private void labelvalorDoProjeto(){
		totalProjetoLabel.setText(String.format(" R$ %.2f", this.projetoService.totalDoProjeto(projeto.getId())));
	}
	//Retirar este exception daqui e ajeitar a aclasse de erviço
	private void lalabelTotalCustosVariaveis() throws SQLException {
		totalCustosVariaveisLabel.setText(String.format("R$ %.2f", this.custosVariaveisService.totalProjeto(projeto.getId())));
	}

	private void saldoAtualDeCusto(){
		double totalDeCustosLancados = this.custosFixosRaiz.TotalDeLancamentos();
		totalDeLancamentosDeCF.setText(String.format("Total custos distribuídos: R$ %.2f", totalDeCustosLancados));
	}

	private void siuacaoDeCustos(){
		double situacao = this.custosFixosRaiz.totalCustosFixos() - this.custosFixosRaiz.TotalDeLancamentos();
		if(situacao>0) {
			situacaoDeCustos.setText(String.format("Déficit de R$ %.2f", situacao));
		}else{
			situacaoDeCustos.setText("Custo compensado");
		}
	}

	@FXML
	private void btnLancarDesconto() throws SQLException {
		this.custosFixosRaiz.lancarCusto(this.lancamentoDeDesconto);
		saldoAtualDeCusto();
		siuacaoDeCustos();
		atualizarLabelLancaentoCV();

	};

	@FXML
	private void btnLancarImpostos() throws SQLException {

		Impostos novosImposto = new Impostos();
		double issBox = 0;
		double sipless = 0;

		if (iss.isSelected()) {
			issBox = 0.25 * this.projetoService.totalDoProjeto(projeto.getId());
		}

		if (simplesNacional.isSelected()) {
			 sipless = 0.25 * this.projetoService.totalDoProjeto(projeto.getId());

		}
		novosImposto.setIdProjeto(projeto.getId());
		novosImposto.setIss(issBox);
		novosImposto.setSimplesNac(sipless);
		this.impostoService.lancarImpostos(novosImposto);
		atualizarLabelImpostos();

	};

	private void lucro(){
		lucro.valueProperty().addListener((observable, oldValue, newValue) -> {
			double totalCustosFixos = custosFixosRaiz.totalCustosFixos();
			double margemDesejada = newValue.doubleValue() / 100; // Converte o valor do slider para percentual.
			double LucroDesejado = totalCustosFixos * margemDesejada;
			valorLucro.setText(String.format("Valor: R$ %.2f", LucroDesejado));
			Porcentagem.setText(String.format("Proporção: %.1f%%", margemDesejada*100));

			this.lucroEsperado = LucroDesejado;

		});
	}

	@FXML
	private void btnLancarLucro() throws SQLException {
		Lucro novoLucro = new Lucro();
		novoLucro.setIdProjeto(projeto.getId());
		novoLucro.setLucro(this.lucroEsperado);
		this.lucroService.lancarLucro(novoLucro);

		atualizarLabelLucro();
	};

	private void atualizarLabelLucro(){
		lucroLabel.setText(String.format("R$ %.2f", this.lucroService.buscarLucro(projeto.getId())));

	}
	private void atualizarLabelImpostos(){
		totalImpostosLabel.setText(String.format("R$ %.2f", this.impostoService.buscarImpostos(projeto.getId())));
	}

	private void atualizarLabelLancaentoCV() {
		lancamentoCF.setText(String.format("R$ %.2f", this.custosFixosRaiz.lancamentoProjetos(projeto.getId())));
	}

	private void somaTotalProjeto() throws SQLException {
		        this.valorTotal = this.custosVariaveisService.totalProjeto(projeto.getId())+
				this.custosVariaveisService.totalProjeto(projeto.getId())+
				this.custosFixosRaiz.lancamentoProjetos(projeto.getId()) +
				this.impostoService.buscarImpostos(projeto.getId()) +
				this.lucroService.buscarLucro(projeto.getId());

	}

	private void precoTotalProjetoLabel(){
		precoTotalProjetoLabel.setText(String.format("R$ %.2f", this.valorTotal));
	}

	@FXML
	private void btnPrecificarProjeto() throws SQLException {
		projeto.setStatus("Precificado");
		projeto.setPrecificacao(this.valorTotal);
		this.projetoService.precificarProjeto(projeto);
	}

	public void listaResultados() throws SQLException {
		Map<String, Map<String, List<DetalhamentoDTO>>> etapasAgrupadas = projetoService.etapasSalvas(projeto.getId());
		exibirDetalhamentos(etapasAgrupadas);
	}

	public void exibirDetalhamentos(Map<String, Map<String, List<DetalhamentoDTO>>> agrupados) throws SQLException {
		VBox container = new VBox(10);  // Cria o container principal

		// Para cada etapa no mapa de agrupados
		agrupados.forEach((etapa, atividades) -> {
			// Cria um label para a etapa
			Label labelEtapa = new Label("Etapa: " + etapa);
			labelEtapa.getStyleClass().add("label-etapa");

			// Adiciona a label da etapa ao container principal
			container.getChildren().add(labelEtapa);

			// Para cada atividade dentro dessa etapa
			atividades.forEach((atividade, profissionais) -> {
				// Inicializa o subtotal da atividade
				float subtotalAtividade = 0;

				// Acumula o valor total dos profissionais para a atividade
				for (DetalhamentoDTO detalhe : profissionais) {
					subtotalAtividade += detalhe.getHoras() * detalhe.getValorHoras();
				}

				// Cria um label para a atividade, exibindo o subtotal
				Label labelAtividade = new Label("Atividade: " + atividade +
						" | Valor Total: " + FormatadorMoeda.formatarValorComoMoeda(subtotalAtividade));
				labelAtividade.getStyleClass().add("label-atividade");

				// Adiciona a label da atividade ao container principal
				container.getChildren().add(labelAtividade);
			});
		});

		// Exibe o total do projeto
		float ttProjeto = projetoService.totalDoProjeto(projeto.getId());
		Label totalDoProjeto = new Label("Subtotal - Serviços: " + FormatadorMoeda.formatarValorComoMoeda(ttProjeto));
		totalDoProjeto.getStyleClass().add("label-subtotal-tt");
		container.getChildren().add(totalDoProjeto);

		// Adiciona o container ao layout da interface
		savedEtapasContainer.getChildren().add(container);  // Certifique-se de que o `savedEtapasContainer` está acessível
	}




}





