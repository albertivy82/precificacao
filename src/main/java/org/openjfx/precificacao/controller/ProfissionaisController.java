package org.openjfx.precificacao.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.openjfx.precificacao.App;

import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import org.openjfx.precificacao.database.ProfissionaisSQLite;
import org.openjfx.precificacao.models.Profissionais;
import org.openjfx.precificacao.shared.CPFMaskedTextField;
import org.openjfx.precificacao.shared.CPFValidator;
import org.openjfx.precificacao.shared.MoedaMaskedTextField;

public class ProfissionaisController {

    private ProfissionaisSQLite profissionais;

    private Profissionais profissional;

    @FXML
    private ListView<Profissionais> LvProfissionais;

    @FXML
    private TextField ProfissionalInput;

    @FXML
    private TextField nomeProfissionalInput;

    @FXML
    private MoedaMaskedTextField valorHoraInput;



    @FXML
    void initialize() {
        updateList();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    protected void btnMain(ActionEvent e) {
        App.mudarTela("DashBoard");
    }



    @FXML
    protected void btnClientes(ActionEvent e) {
        App.mudarTela("Clientes");
    }



    @FXML
    void btnCadastrarProfissional(ActionEvent event) {

        if(camposEstaoValidos()){
            this.profissional = new Profissionais();

        }

    }

    @FXML
    void btnEditarProfissional(ActionEvent event) {

    }

    @FXML
    void btnDeletarProfissional(ActionEvent event) {
    }


    private boolean camposEstaoValidos() {

        //REGEX-VALOR-HORA???
        boolean valid = true;

        // Verificação para nome (não pode estar vazio)
        if (nomeProfissionalInput.getText().trim().isEmpty()) {
            showAlert("Nome Vazio", "O campo nome não pode estar vazio.");
            nomeProfissionalInput.requestFocus();
            valid = false;
        }

        // Verificação para nome (não pode estar vazio)
        if (nomeProfissionalInput.getText().trim().isEmpty()) {
            showAlert("Nome Vazio", "O campo nome não pode estar vazio.");
            nomeProfissionalInput.requestFocus();
            valid = false;
        }

        // Verificação para nome (não pode estar vazio)
        if (valorHoraInput.getText().trim().isEmpty()) {
            showAlert("Nome Vazio", "O campo nome não pode estar vazio.");
            nomeProfissionalInput.requestFocus();
            valid = false;
        }



        return valid;
    }

    public void updateList(){

        this.profissionais = new ProfissionaisSQLite();
        LvProfissionais.getItems().clear();
        List<Profissionais> listaProfissioanis = this.profissionais.all();
        listaProfissioanis.stream().forEach(p->LvProfissionais.getItems().add(p));
    }

}