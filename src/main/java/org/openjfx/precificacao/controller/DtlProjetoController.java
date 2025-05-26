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
import org.openjfx.precificacao.service.CustosFixosService;
import org.openjfx.precificacao.service.ImpostoService;
import org.openjfx.precificacao.service.ProjetoService;
import org.openjfx.precificacao.shared.FormatadorMoeda;
import org.openjfx.precificacao.shared.ProjetoSingleton;

import java.sql.SQLException;
import java.util.*;

public class DtlProjetoController {

	Projeto projeto = ProjetoSingleton.getInstance().getProjeto();

	private ProjetoService projetoService;
	private ClienteService clienteService;
	private List<Etapa> EtapasDoBanco;
	private Etapa etapaSelecionada;
	private Set<Detalhamento> listaDeItens = new HashSet<>();
	private int idAtividadeSelecionada;
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
	protected void btnMain(ActionEvent e){
		App.mudarTela("DashBoard");
	};

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
	protected void btnAtividade(ActionEvent e) {
		App.mudarTela("Atividade");
	}

	@FXML
	protected void btnCustosVariaveis(ActionEvent e) {
		App.mudarTela("CustosVariaveis");
	}

	@FXML
	protected void btnEtapas(ActionEvent e) {
		App.mudarTela("Etapas");
	}

	@FXML
	protected void btnDetalharVariaveis(ActionEvent e) {
		App.mudarTela("qnfrCustosVariaveis");
	}

	@FXML
	protected void btnImpostos(ActionEvent e) {
		App.mudarTela("Impostos");
	}

	@FXML
	protected VBox savedEtapasContainer;


	@FXML
	void initialize() throws SQLException {
		this.projetoService = new ProjetoService();
		if (projeto.getStatus().equals("INICIADO") || projeto.getStatus().equals("EXECUTADO")) {
			desabilitarEdicao();
		}
		populaLista();
		identificacaoProjeto();
		listaResultados();
		atualizarStatusBtnPrecificar();

	}

	@FXML
	private VBox dynamicAtvivityContainer;
	@FXML
	private void btnPrecificar(ActionEvent e) {
		try {
			App.mudarTela("Precificacao");
		} catch (Exception ex) {
			ex.printStackTrace(); // mantém a depuração no console
			mostrarErro("Erro ao carregar tela", "Não foi possível abrir a tela de precificação.\nDetalhes: " + ex.getMessage());
		}
	}



	private void desabilitarEdicao() {
		etapaComboBox.setDisable(true);
		dynamicAtvivityContainer.setDisable(true);

	}

	@FXML
	private Button btnPrecificar;


	private void atualizarStatusBtnPrecificar() {
		float ttProjeto = projetoService.totalDeServicosDoProjeto(projeto.getId());
		btnPrecificar.setDisable(ttProjeto <= 0);
	}




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
			mostrarAviso("Atenção", "Selecione uma etapa antes de adicionar atividades.");
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
					mostrarAviso("Atividade já orçada", "Esta atividade já possui profissionais orçados.");
					Platform.runLater(() -> comboBoxAtividade.getSelectionModel().clearSelection());
				}else {
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

		// Adiciona o ComboBox e o container de responsáveis ao HBox da atividade
		activityBox.getChildren().addAll(comboBoxAtividade, responsaveisContainer, buttonBox);

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
						mostrarAviso("Duplicação de Dados", "Este profissional já foi adicionado para esta etapa e atividade.");
						Platform.runLater(() -> comboBoxProfissional.getSelectionModel().clearSelection());
					}
					else {
						// Adiciona o novo detalhamento à lista usando o objeto principal
						detalhamento.setIdProjeto(this.projeto.getId());
						detalhamento.setIdEtapa(this.etapaSelecionada.getId());
						detalhamento.setIdAtividade(this.idAtividadeSelecionada);
						detalhamento.setIdProfissional(newValue.getId());
						detalhamento.setValorHora(newValue.getValorHora());
						valorHora.setText(newValue.getValorHora().toString());
						listaDeItens.add(detalhamento);

					}
				} else {
					mostrarAviso("Atenção", "Não lance valores sem uma atividade selecionada.");
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
				detalhamento.setHoras(qtd);
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
	protected void btnCadastrarEtapas(ActionEvent e) {
		try {
			salvarEtapas();
			listaResultados();
			identificacaoProjeto();
			atualizarStatusBtnPrecificar();
		} catch (SQLException ex) {
		mostrarErro("Erro ao salvar etapas", ex.getMessage());
	}

}



	private void salvarEtapas() throws SQLException {

		float totalProjetoAnterior = projetoService.totalDeServicosDoProjeto(projeto.getId());
		String statusAnterior = projeto.getStatus();

		projetoService.cadastroDeEtapas(this.listaDeItens);

		float totalProjetoAtual = projetoService.totalDeServicosDoProjeto(projeto.getId());

		if (totalProjetoAnterior == 0 && totalProjetoAtual > 0 && statusAnterior.equalsIgnoreCase("CADASTRADO")) {
			// Se o total anterior era 0 e o novo total é maior que 0 e o status era "CADASTRADO"
			projeto.setStatus("ORÇADO");
			projetoService.statusProjeto(projeto); // Atualizar o status no banco
		}

		listaDeItens.clear();

		dynamicAtvivityContainer.getChildren().clear();
	}

	public void listaResultados() throws SQLException {
		Map<String, Map<String, List<DetalhamentoDTO>>> etapasAgrupadas = projetoService.etapasSalvas(projeto.getId());
		exibirDetalhamentos(etapasAgrupadas);
	}


	public void exibirDetalhamentos(Map<String, Map<String, List<DetalhamentoDTO>>> agrupados) throws SQLException {
		savedEtapasContainer.getChildren().clear(); // Limpa o container de etapas previamente exibido

		agrupados.forEach((etapa, atividades) -> {
			exibirEtapa(etapa, atividades);
		});

		float ttProjeto = projetoService.totalDeServicosDoProjeto(projeto.getId());
		Label totalDoProjeto = new Label("TOTAL DO PROJETO: " + FormatadorMoeda.formatarValorComoMoeda(ttProjeto));
		/*if(ttProjeto>0){
			projeto.setStatus("ORÇADO");
			projetoService.statusProjeto(projeto);
		}else{
			projeto.setStatus("CADASTRADO");
			projetoService.statusProjeto(projeto);

		}*/
		totalDoProjeto.getStyleClass().add("label-subtotal-tt");
		savedEtapasContainer.getChildren().add(totalDoProjeto);
	}

	private void exibirEtapa(String etapa, Map<String, List<DetalhamentoDTO>> atividades) {
		Label labelEtapa = new Label("Etapa: " + etapa);
		labelEtapa.getStyleClass().add("label-etapa");

		HBox.setHgrow(labelEtapa, Priority.ALWAYS);
		labelEtapa.setMaxWidth(Double.MAX_VALUE);
		adicionarLogicaExclusaoEtapa(labelEtapa, etapa);
		savedEtapasContainer.getChildren().add(labelEtapa);

		atividades.forEach((atividade, profissionais) -> {
			exibirAtividade(atividade, profissionais);
		});


		int idEtapa = this.projetoService.buscarIdEtapaPorNome(etapa);

		Label totalDaEtapa = new Label("subtotal da Etapa: " + FormatadorMoeda.formatarValorComoMoeda(projetoService.totalEtapaPeloId(projeto.getId(), idEtapa)));
		totalDaEtapa.getStyleClass().add("label-subtotal-etp");
		savedEtapasContainer.getChildren().add(totalDaEtapa);

	}

	private void exibirAtividade(String atividade, List<DetalhamentoDTO> profissionais) {
		Label labelAtividade = new Label("Atividade: " + atividade);
		labelAtividade.getStyleClass().add("label-atividades");

		HBox.setHgrow(labelAtividade, Priority.ALWAYS);
		labelAtividade.setMaxWidth(Double.MAX_VALUE);
		savedEtapasContainer.getChildren().add(labelAtividade);

		// Inicializa o subtotal da atividade
		float sbtAtividade = 0;

		for (DetalhamentoDTO detalhe : profissionais) {
			sbtAtividade += detalhe.getHoras() * detalhe.getValorHoras();  // Multiplica as horas pelo valor da hora
		}

		// Exibe o subtotal da atividade
		Label labelSubtotalAtividade = new Label("Subtotal da Atividade: " + FormatadorMoeda.formatarValorComoMoeda(sbtAtividade));
		labelSubtotalAtividade.getStyleClass().add("label-subtotal-atv");
		adicionarLogicaExclusaoAtividades(labelAtividade, atividade);
		
		savedEtapasContainer.getChildren().add(labelSubtotalAtividade);
	}

	private float exibirProfissional(DetalhamentoDTO detalhe) {
		// Torna a variável detalhe efetivamente final
		final DetalhamentoDTO detalheFinal = detalhe;

		Label labelProfissional = new Label("Profissional: " + detalheFinal.getNomeProfissional() +
				" - Valor Hora: " + String.format("%.2f", detalheFinal.getValorHoras()) +
				" - Valor Orçado: " + String.format("%.2f", detalheFinal.getHoras() * detalheFinal.getValorHoras()));

		labelProfissional.getStyleClass().add("label-profissionais");
		HBox.setHgrow(labelProfissional, Priority.ALWAYS);
		labelProfissional.setMaxWidth(Double.MAX_VALUE);

		// Adiciona a lógica de clique para excluir o profissional
		adicionarLogicaExclusaoProfissionais(labelProfissional, detalheFinal);

		// Adiciona o label à interface
		savedEtapasContainer.getChildren().add(labelProfissional);

		// Retorna o valor orçado para acumular o subtotal
		return detalheFinal.getHoras() * detalheFinal.getValorHoras();  // Multiplica as horas pelo valor da hora

	}

	private void adicionarLogicaExclusaoEtapa(Label labelEtapa, String etapa) {
		if (projeto.getStatus().equals("INICIADO") || projeto.getStatus().equals("EXECUTADO")) return;
		labelEtapa.setOnMouseClicked(event -> {
			confirmarAcao(
					"Apagar etapa",
					"Deseja apagar a etapa selecionada do projeto?",
					"Etapa selecionada: " + etapa,
					() -> {
						try {
							removerEtapa(projeto.getId(), etapa);
							identificacaoProjeto();
							exibirConfirmacaoExclusao();
						} catch (SQLException e) {
							throw new RuntimeException(e);
						}
					}
			);
		});
	}


	private void adicionarLogicaExclusaoAtividades(Label labelSubtotalAtividade, String atividade) {
		if (projeto.getStatus().equals("INICIADO") || projeto.getStatus().equals("EXECUTADO")) return;
		labelSubtotalAtividade.setOnMouseClicked(event -> {
			confirmarAcao(
					"Apagar atividade",
					"Deseja apagar a atividade selecionada do projeto?",
					"Atividade selecionada: " + atividade,
					() -> {
						try {
							removerAtividade(projeto.getId(), atividade);
							exibirConfirmacaoExclusao();
						} catch (SQLException e) {
							throw new RuntimeException(e);
						}
					}
			);
		});
	}


	private void adicionarLogicaExclusaoProfissionais(Label labelProfissional, DetalhamentoDTO detalheFinal) {
		labelProfissional.setOnMouseClicked(event -> {
			confirmarAcao(
					"Informações do Profissional",
					"Detalhes do profissional",
					"Profissional: " + detalheFinal.getNomeProfissional() +
							"\nValor Hora: R$ " + String.format("%.2f", detalheFinal.getValorHoras()) +
							"\n\nDeseja excluir este profissional?",
					() -> {
						try {
							removerProfissional(detalheFinal);
							exibirConfirmacaoExclusao();
						} catch (SQLException e) {
							throw new RuntimeException(e);
						}
					}
			);
		});
	}


	private void removerProfissional(DetalhamentoDTO detalheFinal) throws SQLException {

		int idProjeto = this.projetoService.buscarIdProjetoPorNome(detalheFinal.getNomeProjeto());
		int idEtapa = this.projetoService.buscarIdEtapaPorNome(detalheFinal.getNomeEtapa());
		int idAtividade = this.projetoService.buscarIdAtividadePorNome(detalheFinal.getNomeAtividade());
		int idProfissional = this.projetoService.buscarIdProfissionalPorNome(detalheFinal.getNomeProfissional());
		float totalHoras = detalheFinal.getValorHoras();
		float horas = detalheFinal.getHoras();

		this.projetoService.deletarRegistroDetalhamento(idProjeto, idEtapa, idAtividade, idProfissional, totalHoras, horas);

		verificarStatusProjeto();
		listaResultados();

	}

	private void removerEtapa(int idProjeto, String nomeEtapa) throws SQLException {

		int idEtapa = this.projetoService.buscarIdEtapaPorNome(nomeEtapa);
		this.projetoService.deletarEtapa(idProjeto, idEtapa);
		verificarStatusProjeto();
		listaResultados();

	}

	private void removerAtividade(int idProjeto,String nomeAtividade) throws SQLException {

		int idAtividade = this.projetoService.buscarIdAtividadePorNome(nomeAtividade);
		this.projetoService.deletarAtividade(idProjeto, idAtividade);
		verificarStatusProjeto();
		listaResultados();

	}

	private void verificarStatusProjeto() throws SQLException {
		float totalProjetoAtual = projetoService.totalDeServicosDoProjeto(projeto.getId());

		if (totalProjetoAtual == 0) {
			// Se o total de serviços caiu para zero e o status é "ORÇADO", mudar para "CADASTRADO"
			projeto.setStatus("CADASTRADO");
			zerarProjeto();
			projetoService.statusProjeto(projeto);
			identificacaoProjeto();
			atualizarStatusBtnPrecificar();
		}
	}

private void zerarProjeto(){
	CustosFixosService custosRepassados = new CustosFixosService();
	ImpostoService impostosProjeto = new ImpostoService();
	projeto.setPrecificacao(0);
	custosRepassados.deletarPorProjeto(projeto.getId());
	impostosProjeto.limparImpostos(projeto.getId());

}


	private void exibirConfirmacaoExclusao() {
		Alert confirmacao = new Alert(Alert.AlertType.INFORMATION);
		confirmacao.setTitle("Confirmação");
		confirmacao.setHeaderText(null);
		confirmacao.setContentText("Item excluído com sucesso.");
		confirmacao.showAndWait();
	}

	private void confirmarAcao(String titulo, String cabecalho, String conteudo, Runnable acaoConfirmada) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(titulo);
		alert.setHeaderText(cabecalho);
		alert.setContentText(conteudo);

		ButtonType buttonExcluir = new ButtonType("Excluir", ButtonBar.ButtonData.OK_DONE);
		ButtonType buttonCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(buttonExcluir, buttonCancelar);

		alert.showAndWait().ifPresent(response -> {
			if (response == buttonExcluir) {
				acaoConfirmada.run();
			}
		});
	}

	private void mostrarErro(String titulo, String mensagem) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(titulo);
		alert.setHeaderText(null);
		alert.setContentText(mensagem);
		alert.showAndWait();
	}

	private void mostrarAviso(String titulo, String mensagem) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle(titulo);
		alert.setHeaderText(mensagem);
		alert.setContentText(null);
		alert.showAndWait();
	}



}





