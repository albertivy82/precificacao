package org.openjfx.precificacao.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.openjfx.precificacao.App;
import org.openjfx.precificacao.models.Atividade;
import org.openjfx.precificacao.models.Etapa;
import org.openjfx.precificacao.service.ProjetoService;


import java.util.List;
import java.util.stream.Collectors;

public class DtlProjetoController {

	private ProjetoService etapasProjeto;
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
		listaDeEtapas();
		buscarIdsDasEtapas();
	}

	@FXML
	private VBox dynamicStageContainer;

	@FXML
	protected void btnhandleAddStage(ActionEvent e) {

		int totalEtapas = this.idsEtapas.size();

		if(controlador<totalEtapas) {

			// Cria um novo contêiner para a etapa
			VBox stageBox = new VBox(5);
			stageBox.getStyleClass().add("stage-box");

			// Adiciona componentes para selecionar a etapa e definir ações e preços
			Label stageLabel = new Label(this.EtapasDoBanco.get(controlador).getEtapa());
			// Aqui você pode adicionar ComboBoxes, TextFields, etc., conforme necessário
			stageBox.getChildren().addAll(stageLabel);


			// Cria um VBox para armazenar as atividades
			VBox activitiesContainer = new VBox(5);
			stageBox.getChildren().add(activitiesContainer);

			// Adiciona a primeira atividade
			adicionarNovaAtividade(activitiesContainer, this.EtapasDoBanco.get(controlador).getId());

			// Botão para adicionar mais atividades
			Button btnAddActivity = new Button("Adicionar Atividade");
			btnAddActivity.setOnAction(event -> adicionarNovaAtividade(activitiesContainer, this.EtapasDoBanco.get(controlador).getId()));
			stageBox.getChildren().add(btnAddActivity);





			// Adiciona o contêiner da nova etapa ao contêiner dinâmico
			dynamicStageContainer.getChildren().add(stageBox);
			System.out.println(idsEtapas.get(controlador)+ "/" +totalEtapas+ " /"+ controlador);

			controlador++;


		}else{

			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Atenção");
			alert.setHeaderText("Todas as etapas existentes já foram inseridas no formulário");
			alert.showAndWait();
		}
	}

	private void adicionarNovaAtividade(VBox activitiesContainer, int idEtapa) {

		List<Atividade> listaAtividadesDaEtapa = this.etapasProjeto.listaAtividades(idEtapa);
		HBox activityBox = new HBox(10);

		// ComboBox para selecionar a atividade
		ComboBox<String> comboBoxAtividade = new ComboBox<>();
		ObservableList<String> observableList = FXCollections.observableArrayList(
				listaAtividadesDaEtapa.stream().map(Atividade::getAtividade).collect(Collectors.toList()));
		comboBoxAtividade.setItems(observableList);
		comboBoxAtividade.setPromptText("Selecione a atividade");




		// VBox para armazenar os responsáveis
		VBox responsaveisContainer = new VBox(5);



		// Botão para adicionar novos responsáveis
		Button btnAddResponsavel = new Button("Adicionar Responsável");
		btnAddResponsavel.setOnAction(event -> adicionarNovoResponsavel(responsaveisContainer));

		// TextField para inserir o preço da atividade
		TextField quantidade = new TextField();
		quantidade.setPromptText("quantidade");

		// Adiciona os componentes ao HBox da atividade
		activityBox.getChildren().addAll(comboBoxAtividade, quantidade, responsaveisContainer, btnAddResponsavel);

		// Adiciona o HBox ao container de atividades
		activitiesContainer.getChildren().add(activityBox);

		// Adiciona o primeiro responsável (opcional)
		adicionarNovoResponsavel(responsaveisContainer);
	}

	private void adicionarNovoResponsavel(VBox responsaveisContainer) {
		HBox responsavelBox = new HBox(10);

		// TextField para inserir o nome do responsável
		TextField txtNomeResponsavel = new TextField();
		txtNomeResponsavel.setPromptText("Nome do Responsável");

		// ComboBox para selecionar o tipo de responsabilidade
		ComboBox<String> comboBoxTipo = new ComboBox<>();
		comboBoxTipo.getItems().addAll("Tipo 1", "Tipo 2", "Tipo 3"); // Exemplos de tipos de responsabilidade
		comboBoxTipo.setPromptText("Tipo de Responsabilidade");

		// Adiciona os componentes ao HBox do responsável
		responsavelBox.getChildren().addAll(txtNomeResponsavel, comboBoxTipo);

		// Adiciona o HBox ao container de responsáveis
		responsaveisContainer.getChildren().add(responsavelBox);
	}






	public List<Etapa> listaDeEtapas(){
		this.etapasProjeto = new ProjetoService();
		this.EtapasDoBanco = this.etapasProjeto.listaEtapas();
		return EtapasDoBanco;
	}

	public List<Integer> buscarIdsDasEtapas() {
		this.idsEtapas =  this.EtapasDoBanco.stream()
				.map(Etapa::getId)
				.collect(Collectors.toList());
		return this.idsEtapas;
	};


}





