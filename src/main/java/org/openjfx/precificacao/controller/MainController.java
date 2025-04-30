package org.openjfx.precificacao.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import org.openjfx.precificacao.App;
import org.openjfx.precificacao.models.ConsolidaProjeto;
import org.openjfx.precificacao.service.ProjetoService;

import java.sql.SQLException;
import java.util.*;

public class MainController {

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
	protected void btnProjetos(ActionEvent e) {
		App.mudarTela("Projeto");
	};

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
	protected void btnImpostos(ActionEvent e) {
		App.mudarTela("Impostos");
	}


	@FXML
	protected void btnProjeto(ActionEvent e) {
		App.mudarTela("Projeto");
	}

	@FXML
	private PieChart projetosPorStatusChart;

	@FXML
	private BarChart<String, Double> projetosComValoresChart;

	@FXML
	private BarChart<String, Double> profissionaisPorHorasChart;

	@FXML
	private BarChart<String, Double> profissionaisPorValorRecebidoChart;

	@FXML
	private BarChart<String, Double> clienteValoresChart;

	@FXML
	private BarChart<String, Number> consolidacaoProjetos;



	private ProjetoService projetoService = new ProjetoService();


	@FXML
	public void initialize() throws SQLException {
		carregarProjetosPorStatus();
		carregarGraficoProjetosComValores();
		carregarGraficoDeHoras();
		carregarGraficoDeValor();
		carregarGraficoDeValorPorCliente();
		carregaConsolidacaoProjetos();
	}

	private void carregarProjetosPorStatus() {
		// Obtenha os dados do serviço de projetos
		Map<String, Integer> projetosPorStatus = projetoService.buscarStatusDeProjeto();

		// Limpe os dados anteriores do gráfico
		projetosPorStatusChart.getData().clear();

		// Adicione os dados ao gráfico de anel
		for (Map.Entry<String, Integer> entry : projetosPorStatus.entrySet()) {
			projetosPorStatusChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
		}
	}


	private void carregarGraficoProjetosComValores() {


		// Obtenha os dados do serviço de projetos
		Map<String, Double> projetosComValores = projetoService.listarProjetosComValores();

		// Limpe os dados anteriores do gráfico
		projetosComValoresChart.getData().clear();

		// Adicione os dados ao gráfico de barras
		for (Map.Entry<String, Double> entry : projetosComValores.entrySet()) {
			XYChart.Series<String, Double> series = new XYChart.Series<>();
			series.setName(entry.getKey());  // Nome do projeto
			series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));  // Valor total
			projetosComValoresChart.getData().add(series);


		}



	}

	private void carregarGraficoDeHoras() {

		Map<String, Double> horasPorProfissional = projetoService.listarhorasPorProfissional();


		profissionaisPorHorasChart.getData().clear();


		for (Map.Entry<String, Double> entry : horasPorProfissional.entrySet()) {
			XYChart.Series<String, Double> series = new XYChart.Series<>();
			series.setName(entry.getKey());
			series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
			profissionaisPorHorasChart.getData().add(series);


		}



	}

	private void carregarGraficoDeValor() {

		Map<String, Double> vlaorHorasPorProfissional = projetoService.listarValorPorProfissional();


		profissionaisPorValorRecebidoChart.getData().clear();


		for (Map.Entry<String, Double> entry : vlaorHorasPorProfissional.entrySet()) {
			XYChart.Series<String, Double> series = new XYChart.Series<>();
			series.setName(entry.getKey());
			series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
			profissionaisPorValorRecebidoChart.getData().add(series);


		}

	}

	private void carregarGraficoDeValorPorCliente() {

		Map<String, Double> vlaorPorCliente = projetoService.listarTotalPorCliente();


		clienteValoresChart.getData().clear();


		for (Map.Entry<String, Double> entry : vlaorPorCliente.entrySet()) {
			XYChart.Series<String, Double> series = new XYChart.Series<>();
			series.setName(entry.getKey());
			series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
			clienteValoresChart.getData().add(series);


		}





	}



	private void carregaConsolidacaoProjetos() throws SQLException {
		List<ConsolidaProjeto> projetos = projetoService.listaConsolidacao();
		System.out.println(projetos.toString());
		consolidacaoProjetos.getData().clear();

		// Obter todos os anos distintos
		Set<Integer> anos = new HashSet<>();
		for (ConsolidaProjeto projeto : projetos) {
			if (projeto.getAnoInicio() > 0) {
			 anos.add(projeto.getAnoInicio());
			}
			if (projeto.getAnoFim() > 0) {
				anos.add(projeto.getAnoFim());
			}
		}

		List<Integer> anosOrdenados = new ArrayList<>(anos);
		Collections.sort(anosOrdenados);
         System.out.println(anosOrdenados);
		for (Integer ano : anosOrdenados) {
			Map<String, Double> valoresIniciais = new HashMap<>();
			Map<String, Double> valoresFinais = new HashMap<>();

			for (ConsolidaProjeto projeto : projetos) {
				if (projeto.getAnoInicio() == ano) {
					valoresIniciais.merge(projeto.getMesInicio(), projeto.getValorInicial(), Double::sum);
					System.out.println(valoresIniciais);
				}
				if (projeto.getAnoFim() == ano) {
					valoresFinais.merge(projeto.getMesFinal(), projeto.getValorFinal(), Double::sum);
					System.out.println(valoresFinais);
				}
			}

			XYChart.Series<String, Number> serieInicial = new XYChart.Series<>();
			serieInicial.setName("Valor Inicial " + ano);
			System.out.println(serieInicial);
			XYChart.Series<String, Number> serieFinal = new XYChart.Series<>();
			serieFinal.setName("Valor Final " + ano);
			System.out.println(serieFinal);
					List<String> mesesDoAno = List.of(
							"janeiro", "fevereiro", "março", "abril", "maio", "junho",
							"julho", "agosto", "setembro", "outubro", "novembro", "dezembro"
					);

					for (String mes : mesesDoAno) {
						double valorInicial = valoresIniciais.getOrDefault(mes, 0.0);
						double valorFinal = valoresFinais.getOrDefault(mes, 0.0);

						serieInicial.getData().add(new XYChart.Data<>(mes, valorInicial));
						serieFinal.getData().add(new XYChart.Data<>(mes, valorFinal));
					}


			System.out.println(serieInicial);
			System.out.println(serieFinal);

			consolidacaoProjetos.getData().add(serieInicial);
			consolidacaoProjetos.getData().add(serieFinal);

		}
	}






}




