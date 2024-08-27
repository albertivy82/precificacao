package org.openjfx.precificacao.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
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


	@FXML
	private ComboBox<Etapa> etapaComboBox;

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
	void initialize(){
		this.projetoService = new ProjetoService();
		populaLista();
	}

	@FXML
	private VBox dynamicAtvivityContainer;



	private void populaLista(){
		this.EtapasDoBanco = this.projetoService.listaEtapas();
		ObservableList<Etapa> etapas = FXCollections.observableArrayList(this.EtapasDoBanco);
		etapaComboBox.setItems(etapas);
		etapaComboBox.setConverter(new StringConverter<Etapa>() {
			@Override
			public String toString(Etapa object) {
				return object.getEtapa();
			}

			@Override
			public Etapa fromString(String string) {
				return null;
			}
		});

		etapaComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				dynamicAtvivityContainer.getChildren().clear();
				carregarAtividadesParaEtapa(newValue);
			}
		});

	}

	@FXML
	protected void carregarAtividadesParaEtapa(Etapa etapa) {


			// Cria um VBox para armazenar as atividades
			VBox activitiesContainer = new VBox(5);


			// Adiciona a primeira atividade
			adicionarNovaAtividade(activitiesContainer, etapa.getId());

			HBox buttonContainer = new HBox(10);
			buttonContainer.setAlignment(Pos.CENTER);

			// Botão para adicionar mais atividades
			Button btnAddActivity = new Button("Adicionar Atividade");
			btnAddActivity.getStyleClass().add("button-style");
			btnAddActivity.setOnAction(event -> adicionarNovaAtividade(activitiesContainer, etapa.getId()));


			// Adiciona os botões ao HBox
			buttonContainer.getChildren().addAll(btnAddActivity);

		    activitiesContainer.getChildren().add(buttonContainer);


			// Adiciona o contêiner da nova atividade ao contêiner dinâmico
			dynamicAtvivityContainer.getChildren().add(activitiesContainer);

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


}





