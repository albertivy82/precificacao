package org.openjfx.precificacao.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.openjfx.precificacao.App;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import org.openjfx.precificacao.shared.MoedaMaskedTextField;

public class ProfissionaisController {

    @FXML
    protected void btnMain(ActionEvent e) {
        App.mudarTela("DashBoard");
    }

    ;

    @FXML
    protected void btnClientes(ActionEvent e) {
        App.mudarTela("Clientes");
    }

    ;

    @FXML
    void btnCadastrarProfissional(ActionEvent event) {

    }

    @FXML
    void btnEditarProfissional(ActionEvent event) {

    }

    @FXML
    void btnDeletarProfissional(ActionEvent event) {
    }
}