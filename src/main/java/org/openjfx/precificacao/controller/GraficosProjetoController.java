package org.openjfx.precificacao.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import org.openjfx.precificacao.App;
import org.openjfx.precificacao.dtos.DetalhamentoDTO;
import org.openjfx.precificacao.models.ConsolidaProjeto;
import org.openjfx.precificacao.models.Projeto;
import org.openjfx.precificacao.service.ProjetoService;
import org.openjfx.precificacao.shared.ProjetoSingleton;

import java.sql.SQLException;
import java.util.*;

public class GraficosProjetoController {

	Projeto projeto = ProjetoSingleton.getInstance().getProjeto();

	@FXML
	protected void btnVoltar(ActionEvent e) {
		App.mudarTela("Precificacao");
	}

	@FXML
	private BarChart<String, Double> profissionaisPorHorasNoProjetoChart;

	@FXML
	private BarChart<String, Double> profissionaisPorValorRecebidoNoProjetoChart;





	private ProjetoService projetoService = new ProjetoService();


	@FXML
	public void initialize() throws SQLException {
		carregarGraficoDeHorasDoProjeto();
		carregarGraficoDeValorDoProjeto();


	}



	private void carregarGraficoDeHorasDoProjeto() {

	Map<String, Double> horasPorProfissionalNoP = projetoService.listarhorasPorProfissionalPorProjeto(projeto.getId());


		profissionaisPorHorasNoProjetoChart.getData().clear();


		for (Map.Entry<String, Double> entry : horasPorProfissionalNoP.entrySet()) {
			XYChart.Series<String, Double> series = new XYChart.Series<>();
			series.setName(entry.getKey());
			series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
			profissionaisPorHorasNoProjetoChart.getData().add(series);


		}
	}






	private void carregarGraficoDeValorDoProjeto() {

		Map<String, Double> vlaorHorasPorProfissionalNoP = projetoService.listarValorPorProfissionalPorProjeto(projeto.getId());


		profissionaisPorValorRecebidoNoProjetoChart.getData().clear();


		for (Map.Entry<String, Double> entry : vlaorHorasPorProfissionalNoP.entrySet()) {
			XYChart.Series<String, Double> series = new XYChart.Series<>();
			series.setName(entry.getKey());
			series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
			profissionaisPorValorRecebidoNoProjetoChart.getData().add(series);


		}
	}




}





