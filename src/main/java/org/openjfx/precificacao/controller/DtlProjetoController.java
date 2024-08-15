package org.openjfx.precificacao.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import org.openjfx.precificacao.App;

public class DtlProjetoController {
	
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
	protected void btnProjeto(ActionEvent e) {
		App.mudarTela("Projeto");
	}

	@FXML
	private VBox dynamicStageContainer;

	@FXML
	protected void btnhandleAddStage(ActionEvent e) {

		// Cria um novo contêiner para a etapa
		VBox stageBox = new VBox(5);
		stageBox.getStyleClass().add("stage-box");

		// Adiciona componentes para selecionar a etapa e definir ações e preços
		Label stageLabel = new Label("Etapa: [Selecione a etapa]");
		// Aqui você pode adicionar ComboBoxes, TextFields, etc., conforme necessário
		stageBox.getChildren().addAll(stageLabel);

		// Adiciona o contêiner da nova etapa ao contêiner dinâmico
		dynamicStageContainer.getChildren().add(stageBox);
	}

}





