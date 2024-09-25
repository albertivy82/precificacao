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

	private Set<LancamentoCV> lancamentos;
	private ProjetoService projetoService;
	private CustosService custosService;
	private ClienteService clienteService;
	List<CustosVariaveis> listaDeCustos;
	//private Set<CustosProjeto> listaDeItens = new HashSet<>();
	private float totalAtividade;



	@FXML
	private Label nomeProjetoLabel;

	@FXML
	private Label clienteLabel;

	@FXML
	private Label StatusLabel;

	@FXML
	private Label totalLabel;

	@FXML
	private ListView<LancamentoCV> LvLancamentos;



	@FXML
	private void btnVoltar(ActionEvent e) {
		App.mudarTela("DetalhamentoProjeto");
	}

	@FXML
	private void btnPrecificar(ActionEvent e) {
		try {
			App.mudarTela("Precificacao");
		} catch (Exception ex) {
			exibirErro(ex);
		}
	}

	private void exibirErro(Exception ex) {
		// Criar uma caixa de alerta
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Erro");
		alert.setHeaderText("Erro ao carregar a tela: Precificacao");
		alert.setContentText("Ocorreu um erro ao tentar carregar a tela.\nDetalhes do erro: " + ex.getMessage());

		// Exibir o erro no console também para fins de depuração
		ex.printStackTrace();

		// Exibir a janela de alerta
		alert.showAndWait();
	}


	@FXML
	private Button btnPrecificar;

	@FXML
	private VBox dynamicCustosContainer;

	@FXML
	void initialize() throws SQLException {
		this.lancamentos = new HashSet<>();
		this.projetoService = new ProjetoService();
		this.custosService = new CustosService();
		//manter a identificação do projeto
		identificacaoProjeto();
		// Exemplo de chamada do método adicionarCustos no initialize
		this.listaDeCustos = this.custosService.listaCustos();

		// Adiciona todos os custos ao container
		adicionarCustos(listaDeCustos);
		updateList();
		atualizarTotal();

		atualizarStatusBtnLancamento();


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
					lancamentos.remove(lancamento);
					btnCadatrarLancamento.setDisable(true);

				} else {
					float qtd = Float.parseFloat(newValue);
					total.setText("R$ " + String.format("%.2f", qtd * item.getValor()));
					if (qtd > 0) {
						lancamento.setIdProjeto(projeto.getId());
						lancamento.setIdCustoVariavel(item.getId());
						lancamento.setNomeCusto(this.custosService.nomeDeCusto(item.getId()));
						lancamento.setValorUnitario(item.getValor());
						lancamento.setQuantidade(qtd);
						lancamentos.add(lancamento);
						btnCadatrarLancamento.setDisable(false);

					}
				}
			} catch (NumberFormatException e) {
				total.setText("R$ 0,00");
			} catch (SQLException e) {
                throw new RuntimeException(e);
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
	protected void btnCadatrarLancamento(ActionEvent e) throws SQLException {

			try {
				this.custosService.cadastrarLancamento(this.lancamentos);
				this.lancamentos.clear();
				dynamicCustosContainer.getChildren().clear();
				adicionarCustos(listaDeCustos);
				updateList();
				atualizarTotal();

			} catch (SQLException ex) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Erro");
				alert.setHeaderText("Ocorreu um erro ao salvar as etapas.");
				alert.setContentText(ex.getMessage());
				alert.showAndWait();
			}

	}

	@FXML
	private Button btnCadatrarLancamento;


	private void atualizarStatusBtnLancamento() {
		btnCadatrarLancamento.setDisable(true);
	}



	private void updateList() throws SQLException {

		LvLancamentos.getItems().clear();
		List<LancamentoCV> listaLancamentos = this.custosService.listagem();
		listaLancamentos.stream().forEach(c-> LvLancamentos.getItems().add(c));

	}

	@FXML
	private void btnDeletar(ActionEvent e) throws SQLException {
		LancamentoCV lancamentoEscolhido = new LancamentoCV();
		lancamentoEscolhido = LvLancamentos.getSelectionModel().getSelectedItem();
		this.custosService.apagarLacamentoCV(lancamentoEscolhido);
		updateList();
		atualizarTotal();

	}
	private void atualizarTotal() throws SQLException {
		totalLabel.setText("Total R$" +String.format("%.2f",this.custosService.totalCVProjeto(projeto.getId())));
		totalLabel.getStyleClass().add("label-subtotal-tt");
	}






}





