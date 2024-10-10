package org.openjfx.precificacao.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.openjfx.precificacao.App;
import org.openjfx.precificacao.dtos.DetalhamentoDTO;
import org.openjfx.precificacao.models.*;
import org.openjfx.precificacao.service.*;
import org.openjfx.precificacao.shared.FormatadorMoeda;
import org.openjfx.precificacao.shared.PdfGenerator;
import org.openjfx.precificacao.shared.ProjetoSingleton;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class BaseImpostosController {

	Projeto projeto = ProjetoSingleton.getInstance().getProjeto();

	private BaseImpostosService base;
	private BaseImpostos impostos;

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
	protected void btnProjeto(ActionEvent e) {
		App.mudarTela("Projeto");
	}

	@FXML
	private Slider sliderIss;

	@FXML
	private Slider sliderSimples;

	@FXML
	private Label valorIssLabel;

	@FXML
	private Label valorSimplesLabel;


	@FXML
	void initialize() throws SQLException {
		this.base = new BaseImpostosService();
		this.impostos = new BaseImpostos();
		iss();
		simples();
	}



	private void iss(){

		sliderIss.setMin(0);
		sliderIss.setMax(100);
		sliderIss.setValue(0); // Define o valor inicial como 0

		sliderIss.valueProperty().addListener((observable, oldValue, newValue) -> {

			double percentual = newValue.doubleValue();
			impostos.setIss(percentual);

			// Atualiza a label para exibir o percentual formatado
			valorIssLabel.setText(String.format("Proporção: %.1f%%", percentual));
		});
	}

	private void simples(){

		sliderSimples.setMin(0);
		sliderSimples.setMax(100);
		sliderSimples.setValue(0); // Define o valor inicial como 0

		sliderSimples.valueProperty().addListener((observable, oldValue, newValue) -> {

			double percentual = newValue.doubleValue();
			impostos.setSimplesNac(percentual);

			// Atualiza a label para exibir o percentual formatado
			valorSimplesLabel.setText(String.format("Proporção: %.1f%%", percentual));
		});
	}




	@FXML
	private void btnLancarImpostos() throws SQLException {
		this.base.cadImpostos(this.impostos);
	};

}





