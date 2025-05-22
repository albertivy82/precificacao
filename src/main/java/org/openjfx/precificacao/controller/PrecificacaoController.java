package org.openjfx.precificacao.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.openjfx.precificacao.App;
import org.openjfx.precificacao.dtos.DetalhamentoDTO;
import org.openjfx.precificacao.models.*;
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
	private LancamentoCF lancamentoCustoFixo;
	private LucroService lucroService;
	private DescontoService descontoService;
	private ImpostoService impostoService;
	private BaseImpostosService baseImposto;
	private double lucroEsperado = 0,
			        descontoConcedido = 0,
					valorSubTotal = 0,
					lucroDoProjeto = 0,
					custoFixoDoProjeto = 0,
					impostosProjeto = 0,
					totalServicos = 0,
					custoVariavelDoProjeto = 0,
	                valorSubtotal1 = 0,
					margemDeLucro = 0,
					descontoMaximo = 0;


	@FXML private Label nomeProjetoLabel,
						clienteLabel,
						StatusLabel,
						custosFixos,
			            valorDistribuicaoCustoFixoLabel,
			            percentualCustoFixoDistribuidoLabel,
						valorLucro,
	                    valorDesconto,
						lucroLabel,
			            descontoLabel,
			            porcentagemLucro,
	                    porcentagemDesconto,
						totalImpostosLabel,
						totalProjetoLabel,
						graficosPojeto,
						totalCustosVariaveisLabel,
						totalDeLancamentosDeCF,
						situacaoDeCustos,
			            valorTotalCustoFixoDistribuido,
			            valorSubtotalProjetoLabel;
	@FXML private Slider sliderCustosFixos, lucro, desconto;
	@FXML private CheckBox iss, simplesNacional;
	@FXML protected VBox savedEtapasContainer;

	// --- Inicialização ---
	@FXML void initialize() throws SQLException {
		lancamentoCustoFixo = new LancamentoCF();
		lancamentoCustoFixo.setIdProjeto(projeto.getId());
		projetoService = new ProjetoService();
		custosVariaveisService = new CustosService();
		lucroService = new LucroService();
		descontoService = new DescontoService();
		impostoService = new ImpostoService();
		custosFixosRaiz = new CustosFixosService();
		baseImposto = new BaseImpostosService();
		sliderCustoFixo();
		sliderLucro();
		lucroProjeto();
		descontoProjeto();
		sliderDesconto();
		identificacaoProjeto();
		valorServicosProjeto();
		custosVariaveisProjeto();
		saldoAtualDeCusto();
		situacaoDeCustos();
		custoFixoProjeto();
		impostosProjeto();
		somaSubtotalTotalProjeto();
		precoSubTotalProjetoLabel();
		listaResultados();
		custosFixos();
		irParaProjetos();
	}

	// --- Configuração sliders + listener ---

	private void sliderCustoFixo() {
		sliderCustosFixos.setBlockIncrement(5);
		sliderCustosFixos.valueProperty().addListener((obs, oldVal, newVal) -> {
			double total = custosFixosRaiz.totalCustosFixos();
			double perc = newVal.doubleValue() / 100;
			double val = total * perc;
			lancamentoCustoFixo.setDesconto(val);
			valorDistribuicaoCustoFixoLabel.setText(String.format("Valor: R$ %.2f", val));
			percentualCustoFixoDistribuidoLabel.setText(String.format("Proporção: %.1f%%", perc*100));
		});
	}

	private void sliderLucro() {
		lucro.setBlockIncrement(5);
		lucro.valueProperty().addListener((obs, oldVal, newVal) -> {
			margemDeLucro = newVal.doubleValue() / 100;
			double baseLucro = calcularBaseLucroEDesconto();
			double val = baseLucro * margemDeLucro;
			valorLucro.setText(String.format("Valor: R$ %.2f", val));
			porcentagemLucro.setText(String.format("Proporção: %.1f%%", margemDeLucro*100));
			lucroEsperado = val;
		});
	}

	private void sliderDesconto() {
		// Sempre configura o listener, independente do estado
		desconto.setBlockIncrement(5);
		desconto.valueProperty().addListener((obs, oldVal, newVal) -> {
			double margem = newVal.doubleValue() / 100;
			double baseDesconto = calcularBaseLucroEDesconto() + this.lucroEsperado;
			double desc = baseDesconto * margem;
			valorDesconto.setText(String.format("Valor: R$ %.2f", desc));
			porcentagemDesconto.setText(String.format("Proporção: %.1f%%", margem * 100));
			descontoConcedido = desc;
		});

		System.out.println(lucroDoProjeto);
		if (lucroDoProjeto <= 0) {
			desconto.setDisable(true); // Desabilita slider
			desconto.setMax(0);
			porcentagemDesconto.setText("Lucro insuficiente");
			valorDesconto.setText("Desconto indisponível");
		} else {
			desconto.setDisable(false);
			margemDeLucro = lucroService.buscarMargemLucro(projeto.getId());
			double limite = calcularDescontoMaximo();
			desconto.setMax(limite);
		}
	}


	private void irParaProjetos() {
		graficosPojeto.setOnMouseClicked(e -> App.mudarTela("GraficosProjeto"));
	}

	// --- Identificação + dados projeto ---
	private void identificacaoProjeto() {
		clienteService = new ClienteService();
		nomeProjetoLabel.setText("Nome do Projeto: " + projeto.getNomeProjeto());
		clienteLabel.setText("Cliente: " + clienteService.nomeCliente(projeto.getIdCliente()));
		StatusLabel.setText("Status: " + projeto.getStatus());
	}

	private double calcularBaseLucroEDesconto() {
		return totalServicos + custoFixoDoProjeto + custoVariavelDoProjeto;
	}


	private void valorServicosProjeto() {
		totalServicos = projetoService.totalDeServicosDoProjeto(projeto.getId());
		totalProjetoLabel.setText(String.format(" R$ %.2f", totalServicos));
	}

	private void custosVariaveisProjeto() throws SQLException {
		custoVariavelDoProjeto = custosVariaveisService.totalCVProjeto(projeto.getId());
		totalCustosVariaveisLabel.setText(String.format("R$ %.2f", custoVariavelDoProjeto));
	}

	private void saldoAtualDeCusto() {
		double total = custosFixosRaiz.TotalDeLancamentos();
		totalDeLancamentosDeCF.setText(String.format("Total custos distribuídos: R$ %.2f", total));
	}

	private void situacaoDeCustos() {
		double situacao = custosFixosRaiz.totalCustosFixos() - custosFixosRaiz.TotalDeLancamentos();
		situacaoDeCustos.setText(situacao > 0 ? String.format("Déficit de R$ %.2f", situacao) : "Custo compensado");
	}

	private void custoFixoProjeto() {
		custoFixoDoProjeto = custosFixosRaiz.lancamentoCFProjetos(projeto.getId());
		valorTotalCustoFixoDistribuido.setText(String.format("R$ %.2f", custoFixoDoProjeto));
	}

	private void impostosProjeto() {
		impostosProjeto = impostoService.buscarImpostos(projeto.getId());
		totalImpostosLabel.setText(String.format("R$ %.2f", impostosProjeto));
	}

	private void lucroProjeto() {
		lucroDoProjeto = lucroService.buscarLucro(projeto.getId());
		margemDeLucro = lucroService.buscarMargemLucro(projeto.getId());
		lucroLabel.setText(String.format("(%.2f%%)   R$ %.2f", margemDeLucro * 100, lucroDoProjeto));
	}

	private void descontoProjeto() {
		descontoConcedido = descontoService.buscardesconto(projeto.getId());
		double margemDesconto = descontoService.buscarMargemDesconto(projeto.getId());
		System.out.println(descontoConcedido + "..." +  margemDesconto);
		descontoLabel.setText(String.format("(%.2f%%)   R$ %.2f", margemDesconto * 100, descontoConcedido));
	}



	private void custosFixos() {
		custosFixos.setText("Custos Fixos Atual: R$ " + String.format("%.2f", custosFixosRaiz.totalCustosFixos()));
	}

	private void somaSubtotalTotalProjeto() throws SQLException {
		valorSubTotal = calcularBaseLucroEDesconto() + lucroDoProjeto;
	}

	private void precoSubTotalProjetoLabel() {
		valorSubtotalProjetoLabel.setText(String.format("R$ %.2f", valorSubTotal));
	}

	// --- Ações usuário ---
	@FXML private void btnDetalhamentoProjeto(ActionEvent e) { App.mudarTela("DetalhamentoProjeto"); }

	@FXML private void btnLancarDistribuicaoCustoFixo() throws SQLException {
		custosFixosRaiz.lancarCusto(lancamentoCustoFixo);
		saldoAtualDeCusto(); situacaoDeCustos(); custoFixoProjeto(); somaSubtotalTotalProjeto(); precoSubTotalProjetoLabel();
	}

	@FXML private void btnLancarImpostos() throws SQLException {
		Impostos impostos = new Impostos();
		if (iss.isSelected()) impostos.setIss(baseImposto.buscarBaseImpostos().getIss() * totalServicos);
		if (simplesNacional.isSelected()) impostos.setSimplesNac(baseImposto.buscarBaseImpostos().getSimplesNac() * totalServicos);
		impostos.setIdProjeto(projeto.getId());
		impostoService.lancarImpostos(impostos);
		impostosProjeto(); somaSubtotalTotalProjeto(); precoSubTotalProjetoLabel();
	}

	@FXML private void btnLancarLucro() throws SQLException {
		Lucro novo = new Lucro();
		novo.setIdProjeto(projeto.getId());
		novo.setMargemLucro(margemDeLucro);
		novo.setLucro(lucroEsperado);
		lucroService.lancarLucro(novo);
				lucroProjeto(); somaSubtotalTotalProjeto(); precoSubTotalProjetoLabel();
				descontoMaximo = calcularDescontoMaximo();
				desconto.setMax(descontoMaximo);
				String limiteDeLucro = String.format(" %.2f%%", descontoMaximo);
				mostrarAviso("Limite de Lucro", "O desconto máximo permitido será de "+ limiteDeLucro);
	}

	@FXML private void btnLancarDesconto() throws SQLException {
		Desconto novo = new Desconto();
		novo.setIdProjeto(projeto.getId());
		novo.setMargemDesconto(margemDeLucro);
		novo.setDesconto(lucroEsperado);
		descontoService.lancardesconto(novo);
		lucroProjeto(); somaSubtotalTotalProjeto(); precoSubTotalProjetoLabel();
		//String descontoRaiz = String.format(" %.2f%%", preço raiz - preço com desconto);
		//mostrarAviso("O seu lucro toal é de"+ descontoRaiz);
	}

	private double calcularDescontoMaximo() {
		double p = margemDeLucro;
		return (1 - (1 / (1 + p))) * 100; // resultado em porcentagem
	}



	@FXML private void btnPrecificarProjeto() throws SQLException {
		projeto.setStatus("PRECIFICADO");
		projeto.setPrecificacao(valorSubtotal1);
		projetoService.precificarProjeto(projeto);
	}

	@FXML private void btnGerarPdf() {
		PdfGenerator pdf = new PdfGenerator();
		pdf.gerarPDF("/Orçamento_projeto_" + projeto.getId() + ".pdf",
				projetoService.etapasSalvas(projeto.getId()),
				totalProjetoLabel.getText(), totalCustosVariaveisLabel.getText(), valorTotalCustoFixoDistribuido.getText(),
				totalImpostosLabel.getText(), lucroLabel.getText(), valorSubtotalProjetoLabel.getText());
	}

	// --- Exibição Detalhamentos ---
	public void listaResultados() throws SQLException {
		exibirDetalhamentos(projetoService.etapasSalvas(projeto.getId()));
	}

	public void exibirDetalhamentos(Map<String, Map<String, List<DetalhamentoDTO>>> agrupados) throws SQLException {
		savedEtapasContainer.getChildren().clear();
		agrupados.forEach((etapa, atividades) -> {
			GridPane grid = new GridPane();
			grid.setHgap(10); grid.setVgap(10); grid.setMaxWidth(Double.MAX_VALUE);
			ColumnConstraints col1 = new ColumnConstraints(); col1.setHgrow(Priority.ALWAYS);
			ColumnConstraints col2 = new ColumnConstraints(); col2.setHgrow(Priority.ALWAYS);
			grid.getColumnConstraints().addAll(col1, col2);

			Label etapaLabel = new Label("Etapa: " + etapa);
			etapaLabel.getStyleClass().add("etapa");
			grid.add(etapaLabel, 0, 0, 2, 1);

			int row = 1;
			for (String atividade : atividades.keySet()) {
				Label atvLabel = new Label("Atividade: " + atividade);
				atvLabel.getStyleClass().add("item");
				Label subtotal = new Label(FormatadorMoeda.formatarValorComoMoeda(
						atividades.get(atividade).stream().map(d -> d.getHoras() * d.getValorHoras()).reduce(0f, Float::sum)));
				subtotal.getStyleClass().add("item"); subtotal.setAlignment(Pos.CENTER_RIGHT);
				GridPane.setHalignment(subtotal, HPos.RIGHT);
				grid.add(atvLabel, 0, row); grid.add(subtotal, 1, row++);
			}
			savedEtapasContainer.getChildren().add(grid);
		});
	}

	private void mostrarAviso(String titulo, String mensagem) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle(titulo);
		alert.setHeaderText(mensagem);
		alert.setContentText(null);
		alert.showAndWait();
	}
}
