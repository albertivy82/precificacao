package org.openjfx.precificacao.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.openjfx.precificacao.App;
import org.openjfx.precificacao.models.Projeto;
import org.openjfx.precificacao.shared.ProjetoSingleton;

import java.sql.SQLException;

public class PrecificacaoController {

	Projeto projeto = ProjetoSingleton.getInstance().getProjeto();

	@FXML
	private void btnDetalhamentoProjeto(ActionEvent e) {
		System.out.println("acionou?");
		App.mudarTela("DetalhamentoProjeto");
	}



	@FXML
	void initialize() throws SQLException {

	}



}





