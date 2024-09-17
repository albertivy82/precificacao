package org.openjfx.precificacao.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.openjfx.precificacao.App;
import org.openjfx.precificacao.models.*;
import org.openjfx.precificacao.service.ClienteService;
import org.openjfx.precificacao.service.CustosService;
import org.openjfx.precificacao.service.ProjetoService;
import org.openjfx.precificacao.shared.ProjetoSingleton;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LancamentoCVController {

	Projeto projeto = ProjetoSingleton.getInstance().getProjeto();

	private Set<LancamentoCV> lacamentos;
	private ProjetoService projetoService;
	private CustosService custosService;
	private ClienteService clienteService;
	//private Set<CustosProjeto> listaDeItens = new HashSet<>();
	private float totalAtividade;



	@FXML
	private Label nomeProjetoLabel;

	@FXML
	private Label clienteLabel;

	@FXML
	private Label StatusLabel;



	@FXML
	private void btnVoltar(ActionEvent e) {
		App.mudarTela("DetalhamentoProjeto");
	}

	@FXML
	private void btnPrecificar(ActionEvent e) {
		App.mudarTela("Precificacao");
	}

	@FXML
	private Button btnPrecificar;

	@FXML
	private VBox dynamicCustosContainer;

	@FXML
	void initialize() throws SQLException {
		this.lacamentos = new HashSet<>();
		this.projetoService = new ProjetoService();
		this.custosService = new CustosService();
		//manter a identificação do projeto
		identificacaoProjeto();
		// Exemplo de chamada do método adicionarCustos no initialize
		List<CustosVariaveis> listaDeCustos = this.custosService.listaCustos();

		// Adiciona todos os custos ao container
		adicionarCustos(listaDeCustos);

	}

	private void adicionarNovoCusto(CustosVariaveis item) {

		LancamentoCV lancamento = new LancamentoCV();

		List<CustosVariaveis> listaDeCustos = this.custosService.listaCustos();

		// VBox principal para conter o custo
		VBox custoVBox = new VBox(10);
		custoVBox.getStyleClass().add("vbox-custos");

		// HBox para organizar o item, quantidade, total e botão de exclusão
		HBox custoBox = new HBox(10);
		custoBox.getStyleClass().add("hbox-custos");

		// Rótulo para o nome do custo variável
		Label itemCusto = new Label(item.toString());
		itemCusto.setPrefWidth(350); // Ajusta a largura do label
		itemCusto.getStyleClass().add("label-item-custo");

		// Campo de entrada para a quantidade
		TextField quantidade = new TextField();
		quantidade.setPromptText("Qtd");
		quantidade.setPrefWidth(80);
		quantidade.getStyleClass().add("text-field-custo");

		// Campo para armazenar o total (quantidade x valor unitário)
		TextField total = new TextField();
		total.setPromptText("Total");
		total.setEditable(false);
		total.setPrefWidth(80);
		total.getStyleClass().add("text-field-custo");

		// Listener para calcular o total automaticamente ao inserir a quantidade
		quantidade.textProperty().addListener((observable, oldValue, newValue) -> {
			try {
				if (newValue == null || newValue.trim().isEmpty()) {
					// Remover o lançamento correspondente se a quantidade for apagada
					lacamentos.remove(lancamento);
					System.out.println("Lançamento removido:");
					lacamentos.stream()
							.map(LancamentoCV::toString)
							.forEach(System.out::println);
				} else {
					float qtd = Float.parseFloat(newValue);
					total.setText("R$ " + String.format("%.2f", qtd * item.getValor()));
					if (qtd > 0) {
						lancamento.setIdProjeto(projeto.getId());
						lancamento.setIdCustoVariavel(item.getId());
						lancamento.setValorUnitario(item.getValor());
						lancamento.setQuantidade(qtd);
						lacamentos.add(lancamento);
						System.out.println("Lançamento adicionado:");
						lacamentos.stream()
								.map(LancamentoCV::toString)
								.forEach(System.out::println);
					}
				}
			} catch (NumberFormatException e) {
				total.setText("R$ 0,00");
			}
		});

		// Campo de entrada para a observação
		TextField obs = new TextField();
		obs.setPromptText("Observação (opcional)");
		obs.setPrefWidth(200);
		obs.getStyleClass().add("text-field-custo");

		// Botão de exclusão para remover o item da lista
		Button btnExcluir = new Button("x");
		btnExcluir.getStyleClass().add("button-excluir");
		btnExcluir.setOnAction(event -> {
			dynamicCustosContainer.getChildren().remove(custoVBox);
		});

		// Adiciona todos os elementos ao HBox
		custoBox.getChildren().addAll(itemCusto, quantidade, total, obs, btnExcluir);

		// Adiciona o HBox à VBox principal
		custoVBox.getChildren().add(custoBox);

		// Adiciona o VBox ao container principal (dynamicCustosContainer)
		dynamicCustosContainer.getChildren().add(custoVBox);
	}


	// Método para adicionar vários custos, um por vez
	private void adicionarCustos(List<CustosVariaveis> listaDeCustos) {
		for (CustosVariaveis item : listaDeCustos) {
			adicionarNovoCusto(item);
		}
	}


	private void identificacaoProjeto(){

		nomeProjetoLabel.setText("Nome do Projeto: " + projeto.getNomeProjeto());
		this.clienteService = new ClienteService();
		clienteLabel.setText("Cliente: " + this.clienteService.nomeCliente(projeto.getIdCliente()));
		StatusLabel.setText("Status: " + projeto.getStatus());

	}

	@FXML
	protected void btnCadatrarLancamento(ActionEvent e) {
		//Lógica para salvar
	}





}





