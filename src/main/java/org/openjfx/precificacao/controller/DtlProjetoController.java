package org.openjfx.precificacao.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.openjfx.precificacao.App;
import org.openjfx.precificacao.models.Atividade;
import org.openjfx.precificacao.models.Etapa;
import org.openjfx.precificacao.models.Profissionais;
import org.openjfx.precificacao.service.ProjetoService;


import java.util.List;
import java.util.stream.Collectors;

public class DtlProjetoController {

	private ProjetoService projetoService;
	private List<Etapa> EtapasDoBanco;
	private int controlador = 0;
	private List<Integer> idsEtapas;


	@FXML
	protected void btnClientes(ActionEvent e) {
		App.mudarTela("Clientes");
	};

	@FXML
	protected void btnCustos(ActionEvent e) {
		App.mudarTela("Custos");
	}

	@FXML
	protected void btnProfissionais(ActionEvent e) {
		App.mudarTela("Profissionais");
	};

	@FXML
	protected void btnProjeto(ActionEvent e) {
		App.mudarTela("Projeto");
	}

	@FXML
	private ComboBox<String> listaAtividades;

	@FXML
	void initialize(){
		this.projetoService = new ProjetoService();
		listaDeEtapas();
		buscarIdsDasEtapas();
	}

	@FXML
	private VBox dynamicStageContainer;

	@FXML
	protected void btnhandleAddEtapa(ActionEvent e) {

		int totalEtapas = this.idsEtapas.size();

		if(controlador<totalEtapas) {

			// Cria um novo contêiner para a etapa
			VBox stageBox = new VBox(5);
			stageBox.getStyleClass().add("etapa-container");

			// Adiciona componentes para selecionar a etapa e definir ações e preços
			Label stageLabel = new Label(this.EtapasDoBanco.get(controlador).getEtapa());
			// Aqui você pode adicionar ComboBoxes, TextFields, etc., conforme necessário

			Label totalEtapa = new Label("Total da Etapa -> ");
			TextField subtotalEtapa = new TextField("R$ 0,00");
			subtotalEtapa.getStyleClass().add("text-field-transparent");
			subtotalEtapa.setEditable(false);

			HBox totalAtividade = new HBox(totalEtapa, subtotalEtapa);
			totalAtividade.setAlignment(Pos.CENTER_RIGHT);

			stageBox.getChildren().addAll(totalAtividade);


			// Cria um VBox para armazenar as atividades
			VBox activitiesContainer = new VBox(5);
			stageBox.getChildren().add(activitiesContainer);

			// Adiciona a primeira atividade
			adicionarNovaAtividade(activitiesContainer, this.EtapasDoBanco.get(controlador).getId());

			HBox buttonContainer = new HBox(10);
			buttonContainer.setAlignment(Pos.CENTER);

			// Botão para adicionar mais atividades
			Button btnAddActivity = new Button("Adicionar Atividade");
			btnAddActivity.getStyleClass().add("button-style");
			btnAddActivity.setOnAction(event -> adicionarNovaAtividade(activitiesContainer, this.EtapasDoBanco.get(controlador).getId()));


			// Adiciona os botões ao HBox
			buttonContainer.getChildren().addAll(btnAddActivity);

			stageBox.getChildren().add(buttonContainer);


			// Adiciona o contêiner da nova etapa ao contêiner dinâmico
			dynamicStageContainer.getChildren().add(stageBox);

			controlador++;


		}else{

			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Atenção");
			alert.setHeaderText("Todas as etapas existentes já foram inseridas no formulário");
			alert.showAndWait();
		}
	}

	private void adicionarNovaAtividade(VBox activitiesContainer, int idEtapa) {

		List<Atividade> listaAtividadesDaEtapa = this.projetoService.listaAtividades(idEtapa);
		VBox activityBox = new VBox(10);
		activityBox.getStyleClass().add("atividade-container");

		// ComboBox para selecionar a atividade
		ComboBox<String> comboBoxAtividade = new ComboBox<>();
		ObservableList<String> observableList = FXCollections.observableArrayList(
				listaAtividadesDaEtapa.stream().map(Atividade::getAtividade).collect(Collectors.toList()));
		comboBoxAtividade.setItems(observableList);
		comboBoxAtividade.setPromptText("Selecione a atividade");

		// VBox para armazenar os responsáveis
		VBox responsaveisContainer = new VBox(5);

		Button btnAddResponsavel = new Button("Adicionar Responsável");
		btnAddResponsavel.setOnAction(event -> adicionarNovoResponsavel(responsaveisContainer));

		HBox buttonBox = new HBox(btnAddResponsavel);
		buttonBox.setAlignment(Pos.CENTER_RIGHT);


		Label totalizandoAtividade = new Label("Subtotal-> ");
		TextField subTotalAtividade = new TextField("R$ 0,00");
		subTotalAtividade.getStyleClass().add("text-field-transparent");
		subTotalAtividade.setEditable(false);

		HBox totalAtividade = new HBox(totalizandoAtividade, subTotalAtividade);
		totalAtividade.setAlignment(Pos.CENTER_RIGHT);

		// Adiciona o ComboBox e o container de responsáveis ao HBox da atividade
		activityBox.getChildren().addAll(comboBoxAtividade, responsaveisContainer, totalAtividade, buttonBox);

		// Adiciona o HBox ao container de atividades
		activitiesContainer.getChildren().add(activityBox);

		// Adiciona o primeiro responsável (opcional)
		adicionarNovoResponsavel(responsaveisContainer);
	}


	private void adicionarNovoResponsavel(VBox responsaveisContainer) {

		List<Profissionais> listaDeProfissionais = this.projetoService.listaProfissionais();

		// VBox principal que vai conter as duas HBox
		VBox responsavelBox = new VBox(10);

		// HBox para o ComboBox e TextField
		HBox responsavelInfoBox = new HBox(10);

		// ComboBox para selecionar o tipo de responsabilidade
		ComboBox<String> comboBoxProfissional = new ComboBox<>();
		ObservableList<String> observableList = FXCollections.observableArrayList(
				listaDeProfissionais.stream().map(Profissionais::toString).collect(Collectors.toList()));
		comboBoxProfissional.setItems(observableList);
		comboBoxProfissional.setPromptText("Profissional");

		// Campo para armazenar o valor hora do profissional
		TextField valorHora = new TextField();
		valorHora.setPromptText("Valor Hora");
		valorHora.setEditable(false);

		// TextField para inserir a quantidade do serviço
		TextField quantidade = new TextField();
		quantidade.setPromptText("Quantidade");

		// Campo para armazenar o total (quantidade x valor raíz)
		TextField total = new TextField();
		total.setPromptText("Total");
		total.setEditable(false); // Define como não editável, pois será calculado

		// Botão de exclusão
		Button btnExcluir = new Button("x");
		btnExcluir.setOnAction(event -> responsaveisContainer.getChildren().remove(responsavelBox));


		// Adiciona o ComboBox e TextField à primeira HBox
		responsavelInfoBox.getChildren().addAll(comboBoxProfissional, valorHora, quantidade, total, btnExcluir);


		// Adiciona as duas HBox à VBox principal
		responsavelBox.getChildren().addAll(responsavelInfoBox);

		// Adiciona o VBox ao container de responsáveis
		responsaveisContainer.getChildren().add(responsavelBox);
	}




	public List<Etapa> listaDeEtapas(){
		this.EtapasDoBanco = this.projetoService.listaEtapas();
		return EtapasDoBanco;
	}

	public List<Integer> buscarIdsDasEtapas() {
		this.idsEtapas =  this.EtapasDoBanco.stream()
				.map(Etapa::getId)
				.collect(Collectors.toList());
		return this.idsEtapas;
	};


}





