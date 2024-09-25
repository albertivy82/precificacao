package org.openjfx.precificacao.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.openjfx.precificacao.App;
import org.openjfx.precificacao.dtos.DetalhamentoDTO;
import org.openjfx.precificacao.models.Impostos;
import org.openjfx.precificacao.models.LancamentoCF;
import org.openjfx.precificacao.models.Lucro;
import org.openjfx.precificacao.models.Projeto;
import org.openjfx.precificacao.service.*;
import org.openjfx.precificacao.shared.FormatadorMoeda;
import org.openjfx.precificacao.shared.PdfGenerator;
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
	private double lucroSobreServicos = 0;
	private double custoFixoDoProjeto = 0;
	private double impostosProjeto = 0;
	private double totalServicos = 0;
	private double custoVariavelDoProjeto = 0;

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
		labelvalorDeServicosDoProjeto();
		lalabelTotalCustosVariaveis();
		this.custosFixosRaiz = new CustosFixosService();
		identificacaoProjeto();
		slider();
		lucro();
		saldoAtualDeCusto();
		siuacaoDeCustos();
		atualizarLabelLancamentoCFProjeto();
		atualizarLabelImpostos();
		atualizarLabelLucro();
		somaTotalProjeto();
		precoTotalProjetoLabel();
		btnPrecificarProjeto();
		precoTotalProjetoLabel();
		listaResultados();
	}

	private void atualizarLabelLucro() {
		float lucro = this.lucroService.buscarLucro(projeto.getId());
		this.lucroSobreServicos = lucro;
		lucroLabel.setText(String.format("R$ %.2f", this.lucroSobreServicos));
	}

	private void atualizarLabelLancamentoCFProjeto(){
		float cf = this.custosFixosRaiz.lancamentoCFProjetos(projeto.getId());
		this.custoFixoDoProjeto = cf;
		lancamentoCF.setText(String.format("R$ %.2f", this.custoFixoDoProjeto));
	}

	private void atualizarLabelImpostos(){
		float impostos = this.impostoService.buscarImpostos(projeto.getId());
		this.impostosProjeto = impostos;
		totalImpostosLabel.setText(String.format("R$ %.2f", this.impostosProjeto));
	}

	private void labelvalorDeServicosDoProjeto(){
		float servicos = this.projetoService.totalDeServicosDoProjeto(projeto.getId());
		this.totalServicos = servicos;
		totalProjetoLabel.setText(String.format(" R$ %.2f", this.totalServicos));
	}

	private void lalabelTotalCustosVariaveis() throws SQLException {
		float cv = this.custosVariaveisService.totalCVProjeto(projeto.getId());
		this.custoVariavelDoProjeto = cv;
		totalCustosVariaveisLabel.setText(String.format("R$ %.2f", this.custoVariavelDoProjeto));
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
		atualizarLabelLancamentoCFProjeto();
		somaTotalProjeto();
		precoTotalProjetoLabel();

	};

	@FXML
	private void btnLancarImpostos() throws SQLException {

		Impostos novosImposto = new Impostos();
		double issBox = 0;
		double sipless = 0;

		if (iss.isSelected()) {
			issBox = 0.25 * this.totalServicos;
		}

		if (simplesNacional.isSelected()) {
			 sipless = 0.25 * this.totalServicos;

		}
		novosImposto.setIdProjeto(projeto.getId());
		novosImposto.setIss(issBox);
		novosImposto.setSimplesNac(sipless);
		this.impostoService.lancarImpostos(novosImposto);
		atualizarLabelImpostos();
		somaTotalProjeto();
		precoTotalProjetoLabel();

	};

	private void lucro(){
		lucro.valueProperty().addListener((observable, oldValue, newValue) -> {
			double totalCustosFixos = this.totalServicos;
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
		somaTotalProjeto();
		precoTotalProjetoLabel();
	};

	private void somaTotalProjeto() throws SQLException {
		        this.valorTotal = this.totalServicos + custoVariavelDoProjeto + this.custoFixoDoProjeto + this.impostosProjeto + this.lucroSobreServicos;

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
		// Limpa o container antes de adicionar novos detalhes
		savedEtapasContainer.getChildren().clear();

		agrupados.forEach((etapa, atividades) -> {
			// Cria o GridPane para a etapa
			GridPane etapaGrid = new GridPane();
			etapaGrid.setHgap(10);
			etapaGrid.setVgap(10);
			etapaGrid.setMaxWidth(Double.MAX_VALUE); // Permite que o GridPane ocupe toda a largura disponível

			// Definir as restrições das colunas
			ColumnConstraints col1 = new ColumnConstraints();
			col1.setHgrow(Priority.ALWAYS); // Coluna esquerda se expande
			col1.setHalignment(HPos.LEFT);

			ColumnConstraints col2 = new ColumnConstraints();
			col2.setHgrow(Priority.ALWAYS); // Coluna direita também se expande
			col2.setHalignment(HPos.RIGHT); // Alinhamento à direita para os valores

			etapaGrid.getColumnConstraints().addAll(col1, col2);

			// Cria o label da etapa e aplica o estilo de "etapa"
			Label labelEtapa = new Label("Etapa: " + etapa);
			labelEtapa.getStyleClass().add("etapa");

			// Adiciona a etapa na primeira linha (colunas 0 e 1)
			etapaGrid.add(labelEtapa, 0, 0, 2, 1); // Span across both columns

			int rowIndex = 1;
			for (String atividade : atividades.keySet()) {
				// Cria o label para a atividade e aplica o estilo de "item"
				Label labelAtividade = new Label("Atividade: " + atividade);
				labelAtividade.getStyleClass().add("item");
				labelAtividade.setMaxWidth(Double.MAX_VALUE); // Permite expandir a largura

				// Calcula o subtotal da atividade
				float subtotalAtividade = atividades.get(atividade).stream()
						.map(DetalhamentoDTO::getHoras)
						.reduce(0f, Float::sum);

				// Cria o label para o subtotal da atividade e aplica o alinhamento à direita
				Label labelSubtotalAtividade = new Label(FormatadorMoeda.formatarValorComoMoeda(subtotalAtividade));
				labelSubtotalAtividade.getStyleClass().add("item");
				labelSubtotalAtividade.setMaxWidth(Double.MAX_VALUE); // Permite expandir a largura
				labelSubtotalAtividade.setAlignment(Pos.CENTER_RIGHT); // Garante que o texto seja alinhado à direita
				GridPane.setHalignment(labelSubtotalAtividade, HPos.RIGHT); // Alinhamento no GridPane

				// Adiciona a atividade e o valor no GridPane
				etapaGrid.add(labelAtividade, 0, rowIndex); // Coluna 0 para a atividade (alinhado à esquerda)
				etapaGrid.add(labelSubtotalAtividade, 1, rowIndex); // Coluna 1 para o valor (alinhado à direita)

				rowIndex++;
			}

			// Adiciona o GridPane da etapa no container principal
			savedEtapasContainer.getChildren().add(etapaGrid);
		});



	}

	@FXML
	private void btnGerarPdf() {

			// Definir o caminho de destino para o PDF
			String caminhoDoArquivoPdf = "/pdf_template.pdf";  // Atualize com o caminho desejado

			// Recuperar os dados necessários para o PDF
			Map<String, Map<String, List<DetalhamentoDTO>>> etapasAgrupadas = projetoService.etapasSalvas(projeto.getId());

			String totalServicos = totalProjetoLabel.getText();
			String totalCustosVariaveis = totalCustosVariaveisLabel.getText();
			String custosFixosLancados = lancamentoCF.getText();
			String totalImpostos = totalImpostosLabel.getText();
			String quotaServicos = lucroLabel.getText();
			String precoTotalProjeto = precoTotalProjetoLabel.getText();

			// Gerar o PDF usando a classe PdfGenerator
			PdfGenerator pdfGenerator = new PdfGenerator();
			pdfGenerator.gerarPDF(caminhoDoArquivoPdf, etapasAgrupadas, totalServicos, totalCustosVariaveis,
					custosFixosLancados, totalImpostos, quotaServicos, precoTotalProjeto);

			// Exibir mensagem de confirmação (opcional)
			System.out.println("PDF gerado com sucesso em: " + caminhoDoArquivoPdf);

	}











}





