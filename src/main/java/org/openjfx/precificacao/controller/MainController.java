package org.openjfx.precificacao.controller;

import org.openjfx.precificacao.App;

import javafx.event.ActionEvent;
import javafx.fxml.*;

public class MainController {
	
	@FXML
	protected void btnClientes(ActionEvent e) {
		App.mudarTela("Clientes");
	};

}
