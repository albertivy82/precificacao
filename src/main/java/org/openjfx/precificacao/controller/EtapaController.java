package org.openjfx.precificacao.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.openjfx.precificacao.App;
import org.openjfx.precificacao.database.EtapaSQLite;
import org.openjfx.precificacao.models.Etapa;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class EtapaController {

    private EtapaSQLite etapaSQLite;
    private int id = -1;

    @FXML
    private TextField nomeEtapaInput;

    @FXML
    private ListView<Etapa> LvEtapas;

    @FXML
    void initialize() {
        this.etapaSQLite = new EtapaSQLite();
        updateList();

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    protected void btnMain(ActionEvent e){
        App.mudarTela("DashBoard");
    }

    @FXML
    protected void btnClientes(ActionEvent e) {
        App.mudarTela("Clientes");
    }

    @FXML
    protected void btnProfissionais(ActionEvent e) {
        App.mudarTela("Profissionais");
    };

    @FXML
    protected void btnProjeto(ActionEvent e) {
        App.mudarTela("Projeto");
    }

    @FXML
    protected void btnCustos(ActionEvent e) {
        App.mudarTela("Custos");
    }

    @FXML
    protected void btnProjetos(ActionEvent e) {
        App.mudarTela("Projeto");
    }

    @FXML
    protected void btnAtividade(ActionEvent e) {
        App.mudarTela("Atividade");
    }

    @FXML
    protected void btnCustosVariaveis(ActionEvent e) {
        App.mudarTela("CustosVariaveis");
    }

    @FXML
    protected void btnImpostos(ActionEvent e) {
        App.mudarTela("Impostos");
    }

    @FXML
    protected void btnCadastrarEtapa(ActionEvent e) {
        if (camposEstaoValidos()) {
            Etapa novaEtapa = new Etapa();
            novaEtapa.setEtapa(nomeEtapaInput.getText());

            try {

                if (this.id == -1) {
                    this.etapaSQLite.novaEtapa(novaEtapa);
                } else {
                    novaEtapa.setId(this.id);
                    this.etapaSQLite.editarEtapa(novaEtapa);
                }
                clearFields();
                updateList();
            } catch (SQLException ex) {
                showAlert("Erro ao Cadastrar Etapa", "Não foi possível cadastrar o Etapa: " + ex.getMessage());
            }
        } else {
            showAlert("Erro de Validação", "Por favor, corrija os campos destacados antes de enviar.");
        }
    }

    private boolean camposEstaoValidos() {
        boolean valid = true;
        if (nomeEtapaInput.getText().trim().isEmpty()) {
            showAlert("Nome Vazio", "O campo nome não pode estar vazio.");
            nomeEtapaInput.requestFocus();
            valid = false;
        }
        return valid;
    }

    private void clearFields() {
        nomeEtapaInput.clear();
        this.id = -1;
    }

    @FXML
    void btnDeletarEtapa(ActionEvent event) {

        ObservableList<Etapa> escolhida = LvEtapas.getSelectionModel().getSelectedItems();

        if (!escolhida.isEmpty()) {
            Etapa EtapaEscolhido = escolhida.get(0);

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("ATENÇÃO");
            alert.setHeaderText("Deseja realmente excluir o Etapa selecionado?");
            alert.setContentText(EtapaEscolhido.toString());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                this.etapaSQLite.deletarEtapa(EtapaEscolhido.getId());
                updateList();
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("ATENÇÃO");
            alert.setHeaderText("Nenhum Etapa foi selecionado");
            alert.showAndWait();
        }
    }

    @FXML
    void btnEditarEtapa(ActionEvent event) {
        ObservableList<Etapa> editar = LvEtapas.getSelectionModel().getSelectedItems();

        if (!editar.isEmpty()) {
            Etapa EtapaEscolhido = editar.get(0);

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Atenção");
            alert.setHeaderText("Deseja realmente editar o Etapa selecionado?");
            alert.setContentText(EtapaEscolhido.toString());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                nomeEtapaInput.setText(EtapaEscolhido.getEtapa());
                this.id = EtapaEscolhido.getId();
            }
        } else {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Atenção");
            alert.setHeaderText("Nenhum Etapa foi selecionado");
            alert.showAndWait();
        }
    }

    private void updateList() {
        LvEtapas.getItems().clear();
        List<Etapa> listaEtapa = this.etapaSQLite.all();
        listaEtapa.forEach(p -> LvEtapas.getItems().add(p));
    }
}
