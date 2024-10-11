package org.openjfx.precificacao.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import org.openjfx.precificacao.App;
import org.openjfx.precificacao.service.ProjetoService;

import java.util.Map;

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


	private ProjetoService projetoService = new ProjetoService();


	@FXML
	public void initialize() {
		carregarProjetosPorStatus();
		carregarGraficoProjetosComValores();
		carregarGraficoDeHoras();
		carregarGraficoDeValor();
		carregarGraficoDeValorPorCliente();
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










}




