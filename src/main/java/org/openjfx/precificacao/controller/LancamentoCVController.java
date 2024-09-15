package org.openjfx.precificacao.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.openjfx.precificacao.App;
import org.openjfx.precificacao.dtos.DetalhamentoDTO;
import org.openjfx.precificacao.models.*;
import org.openjfx.precificacao.service.ClienteService;
import org.openjfx.precificacao.service.CustosService;
import org.openjfx.precificacao.service.ProjetoService;
import org.openjfx.precificacao.shared.FormatadorMoeda;
import org.openjfx.precificacao.shared.ProjetoSingleton;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LancamentoCVController {

	Projeto projeto = ProjetoSingleton.getInstance().getProjeto();

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
	private ComboBox<Etapa> etapaComboBox;

	@FXML
	protected VBox savedEtapasContainer;

	@FXML
	private VBox dynamicAtvivityContainer;

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

		List<CustosVariaveis> listaDeCustos = this.custosService.listaCustos();


		// VBox principal para conter o custo
		VBox custoVBox = new VBox(10);

		// HBox para organizar o item, quantidade, total e botão de exclusão
		HBox custoBox = new HBox(10);

		// Rótulo para o nome do custo variável
		Label itemCusto = new Label(item.toString());
		itemCusto.setPrefWidth(350); // Ajusta a largura do label para organizar melhor

		// Campo de entrada para a quantidade
		TextField quantidade = new TextField();
		quantidade.setPromptText("Quantidade");
		quantidade.setPrefWidth(100); // Ajusta o tamanho da caixa de texto

		// Campo para armazenar o total (quantidade x valor unitário)
		TextField total = new TextField();
		total.setPromptText("Total");
		total.setEditable(false); // Define como não editável, pois será calculado
		total.setPrefWidth(150); // Define uma largura fixa para o campo total

		// Listener para calcular o total automaticamente ao inserir a quantidade
		quantidade.textProperty().addListener((observable, oldValue, newValue) -> {
			try {
				float qtd = Float.parseFloat(newValue);
				total.setText("R$ " + String.format("%.2f", qtd * item.getValor()));
			} catch (NumberFormatException e) {
				total.setText("R$ 0,00"); // Se o valor não for numérico, define o total como 0
			}
		});

		// Botão de exclusão para remover o item da lista
		Button btnExcluir = new Button("x");
		btnExcluir.setOnAction(event -> {
			dynamicCustosContainer.getChildren().remove(custoVBox);
		});

		// Adiciona todos os elementos ao HBox
		custoBox.getChildren().addAll(itemCusto, quantidade, total, btnExcluir);

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
	protected void btnCadatrarEtapas(ActionEvent e) {
		//Lógica para salvar
	}





}





