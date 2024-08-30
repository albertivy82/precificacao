package org.openjfx.precificacao.controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.openjfx.precificacao.App;
import org.openjfx.precificacao.models.*;
import org.openjfx.precificacao.service.ClienteService;
import org.openjfx.precificacao.service.ProjetoService;
import org.openjfx.precificacao.shared.ProjetoSingleton;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DtlProjetoController {

	Projeto projeto = ProjetoSingleton.getInstance().getProjeto();
	private ProjetoService projetoService;
	private ClienteService clienteService;
	private List<Etapa> EtapasDoBanco;
	private Etapa etapaSelecionada;
	private Set<Detalhamento> listaDeItens = new HashSet<>();
	private int idAtividadeSelecionada;



	@FXML
	private Label nomeProjetoLabel;

	@FXML
	private Label clienteLabel;

	@FXML
	private Label StatusLabel;

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
	protected VBox savedEtapasContainer;




	@FXML
	void initialize(){
		this.projetoService = new ProjetoService();
		populaLista();
		identificacaoProjeto();
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
				this.etapaSelecionada = newValue;
				dynamicAtvivityContainer.getChildren().clear();
				carregarAtividadesParaEtapa(newValue);
			}
		});

	}


	@FXML
	private void adicionarAtividade() {
		if (etapaSelecionada != null) {
			carregarAtividadesParaEtapa(etapaSelecionada);
		} else {
			// Tratar o caso em que nenhuma etapa foi selecionada
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Atenção");
			alert.setHeaderText("Selecione uma etapa antes de adicionar atividades.");
			alert.showAndWait();
		}
	}



	@FXML
	protected void carregarAtividadesParaEtapa(Etapa etapa) {

		// Cria um VBox para armazenar as atividades
		VBox activitiesContainer = new VBox(5);

		// Adiciona a primeira atividade
		adicionarNovaAtividade(activitiesContainer, etapa.getId());

		// Adiciona o contêiner da nova atividade ao contêiner dinâmico
		dynamicAtvivityContainer.getChildren().add(activitiesContainer);
	}

	private void adicionarNovaAtividade(VBox activitiesContainer, int idEtapa) {


		List<Atividade> listaAtividadesDaEtapa = this.projetoService.listaAtividades(idEtapa);
		VBox activityBox = new VBox(10);
		activityBox.getStyleClass().add("atividade-container");

		// ComboBox para selecionar a atividade
		ComboBox<Atividade> comboBoxAtividade = new ComboBox<>();
		ObservableList<Atividade> atividades = FXCollections.observableArrayList(listaAtividadesDaEtapa);
		comboBoxAtividade.setItems(atividades);
		comboBoxAtividade.setConverter(new StringConverter<Atividade>() {
			@Override
			public String toString(Atividade object) {
				return object.getAtividade();
			}

			@Override
			public Atividade fromString(String string) {
				return null;
			}
		});
		comboBoxAtividade.setPromptText("Selecione a atividade");


		comboBoxAtividade.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				int idAtividadeSelecionada = newValue.getId();

				boolean atividadeJaOrcada = listaDeItens.stream()
						.anyMatch(d -> d.getIdAtividade() == idAtividadeSelecionada);

				if (atividadeJaOrcada) {
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.setTitle("Atividade já orçada");
					alert.setHeaderText("Esta atividade já possui profissionais orçados.");
					alert.showAndWait();

					Platform.runLater(() -> comboBoxAtividade.getSelectionModel().clearSelection());


				} else {
					// Permitir a seleção da atividade
					this.idAtividadeSelecionada = idAtividadeSelecionada;
				}
			}
		});



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
		Detalhamento detalhamento = new Detalhamento();

		// VBox principal que vai conter as duas HBox
		VBox responsavelBox = new VBox(10);

		// HBox para o ComboBox e TextField
		HBox responsavelInfoBox = new HBox(10);

		// ComboBox para selecionar o tipo de responsabilidade
		ComboBox<Profissionais> comboBoxProfissional = new ComboBox<>();
		ObservableList<Profissionais> profissionais = FXCollections.observableArrayList(listaDeProfissionais);
		comboBoxProfissional.setItems(profissionais);
		comboBoxProfissional.setConverter(new StringConverter<Profissionais>() {
			@Override
			public String toString(Profissionais object) {
				return object.toString();
			}

			@Override
			public Profissionais fromString(String string) {
				return null;
			}
		});
		comboBoxProfissional.setPromptText("Profissional");

		// Campo para armazenar o valor hora do profissional
		TextField valorHora = new TextField();
		valorHora.setPromptText("Valor Hora");
		valorHora.setEditable(false);


		comboBoxProfissional.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				if (this.idAtividadeSelecionada != 0) {
					// Verifica se o profissional já foi adicionado para essa atividade e etapa
					boolean profissionalJaAdicionado = listaDeItens.stream()
							.anyMatch(d -> d.getIdAtividade() == this.idAtividadeSelecionada && d.getIdProfissional() == newValue.getId());

					if (profissionalJaAdicionado) {
						// Se já existir, exibe um alerta
						Alert alert = new Alert(Alert.AlertType.WARNING);
						alert.setTitle("Duplicação de Dados");
						alert.setHeaderText("Este profissional já foi adicionado para esta etapa e atividade.");
						alert.showAndWait();
						Platform.runLater(() -> comboBoxProfissional.getSelectionModel().clearSelection());
					} else {
						// Adiciona o novo detalhamento à lista usando o objeto principal
						detalhamento.setIdProjeto(this.projeto.getId());
						detalhamento.setIdEtapa(this.etapaSelecionada.getId());
						detalhamento.setIdAtividade(this.idAtividadeSelecionada);
						detalhamento.setIdProfissional(newValue.getId());
						detalhamento.setValorHora(newValue.getValorHora());

						valorHora.setText(newValue.getValorHora().toString());
						listaDeItens.add(detalhamento);

						// Atualiza a interface com o novo item
						VBox listaDetalhamento = new VBox(5);
						savedEtapasContainer.getChildren().add(listaDetalhamento);
					}
				} else {
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.setTitle("Atenção");
					alert.setHeaderText("Não lance valores sem uma atividade selecionada.");
					alert.showAndWait();
					Platform.runLater(() -> comboBoxProfissional.getSelectionModel().clearSelection());
				}
			}
		});



		// TextField para inserir a quantidade do serviço
		TextField quantidade = new TextField();
		quantidade.setPromptText("Quantidade");

		// Campo para armazenar o total (quantidade x valor raíz)
		TextField total = new TextField();
		total.setPromptText("Total");
		total.setEditable(false); // Define como não editável, pois será calculado

		quantidade.textProperty().addListener((observable, oldValue, newValue) -> {
			try {
				float qtd = Float.parseFloat(newValue);
				float valorHoraCovertido = Float.parseFloat(valorHora.getText());
				total.setText("R$ " + (qtd * valorHoraCovertido));
				// Atualiza o valorHoras no detalhamento correspondente
				detalhamento.setHoras(qtd * valorHoraCovertido);

				// Limpa o container
				savedEtapasContainer.getChildren().clear();

				// Cria um VBox para listar os itens
				VBox listaDetalhamento = new VBox(5);

				// Recria a lista de detalhamentos na interface
				for (Detalhamento item : listaDeItens) {
					Label lista = new Label(item.toString());
					listaDetalhamento.getChildren().add(lista);
				}

				// Adiciona a lista ao container
				savedEtapasContainer.getChildren().add(listaDetalhamento);
				comboBoxProfissional.setDisable(true);


			} catch (NumberFormatException e) {
				total.setText("R$ 0,00"); // Se o valor não for numérico, define o total como 0
			}
		});


		// Botão de exclusão
		Button btnExcluir = new Button("x");
		btnExcluir.setOnAction(event -> {
			responsaveisContainer.getChildren().remove(responsavelBox);
			// Remover o objeto `Detalhamento` correspondente da lista
			listaDeItens.remove(detalhamento);

			// Atualizar a exibição das etapas salvas
			savedEtapasContainer.getChildren().clear();
			VBox listaDetalhamento = new VBox(5);

			for (Detalhamento item : listaDeItens) {
				Label lista = new Label(item.toString());
				listaDetalhamento.getChildren().add(lista);
			}

			savedEtapasContainer.getChildren().add(listaDetalhamento);
		});


		// Adiciona o ComboBox e TextField à primeira HBox
		responsavelInfoBox.getChildren().addAll(comboBoxProfissional, valorHora, quantidade, total, btnExcluir);
		// Adiciona as duas HBox à VBox principal
		responsavelBox.getChildren().addAll(responsavelInfoBox);
		// Adiciona o VBox ao container de responsáveis
		responsaveisContainer.getChildren().add(responsavelBox);
	}

	private void identificacaoProjeto(){

		nomeProjetoLabel.setText("Nome do Projeto: " + projeto.getNomeProjeto());
		this.clienteService = new ClienteService();
		clienteLabel.setText("Cliente: " + this.clienteService.nomeCliente(projeto.getIdCliente()));
		StatusLabel.setText("Status: " + projeto.getStatus());

	}

	@FXML
	protected void btnCadatrarEtapas(ActionEvent e) throws SQLException {
		projetoService.cadastroDeEtapas(this.listaDeItens);

	}


}





