package org.openjfx.precificacao.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.openjfx.precificacao.App;

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
	protected void btnProjeto(ActionEvent e) {
		App.mudarTela("Projeto");
	}

}





