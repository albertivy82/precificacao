package org.openjfx.precificacao.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import org.openjfx.precificacao.App;
import org.openjfx.precificacao.database.AtividadeSQLite;
import org.openjfx.precificacao.database.ProjetoSQLite;
import org.openjfx.precificacao.models.Atividade;
import org.openjfx.precificacao.models.Projeto;
import org.openjfx.precificacao.service.ClienteService;
import org.openjfx.precificacao.service.EtapaService;
import org.openjfx.precificacao.service.ProjetoService;
import org.openjfx.precificacao.shared.ProjetoSingleton;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AtividadeController {

    private AtividadeSQLite atividadeDB;

    private EtapaService etapa;

    private int id = -1;

    private int idEtapa = -1;



    @FXML
    private TextField nomeAtividadeInput;

    @FXML
    private ComboBox<String> listaEtapas;

    @FXML
    private ListView<Atividade> LvAtividades;

    @FXML
    void initialize() {
        updateList();
        populaLista();

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
    protected void btnProjeto(ActionEvent e) {
        App.mudarTela("Projeto");
    }

    @FXML
    protected void btnCustos(ActionEvent e) {
        App.mudarTela("Custos");
    }

    @FXML
    protected void btnProfissionais(ActionEvent e) {
        App.mudarTela("Profissionais");
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
    protected void btnEtapas(ActionEvent e) {
        App.mudarTela("Etapas");
    }

    @FXML
    protected void btnImpostos(ActionEvent e) {
        App.mudarTela("Impostos");
    }

    @FXML
    protected void btnCadastrarAtividade(ActionEvent e) {

            if (camposEstaoValidos()) {
            Atividade novaAtividade = new Atividade();
                novaAtividade.setAtividade(nomeAtividadeInput.getText());
                novaAtividade.setEtapa(idEtapa);

            try {
                this.atividadeDB = new AtividadeSQLite();
                if (this.id == -1) {
                    this.atividadeDB.novaAtividade(novaAtividade);
                } else {
                    novaAtividade.setId(this.id);
                    this.atividadeDB.editarAtividade(novaAtividade);
                }
                clearFields();
                updateList();
            } catch (SQLException ex) {
                showAlert("Erro ao Cadastrar Atividade", "Não foi possível cadastrar o atividade: " + ex.getMessage());
            }
        } else {
            showAlert("Erro de Validação", "Por favor, corrija os campos destacados antes de enviar.");
        }
    }

    private boolean camposEstaoValidos() {
        boolean valid = true;
        this.etapa = new EtapaService();
        if (nomeAtividadeInput.getText().trim().isEmpty()) {
            showAlert("Nome do Projeto Vazio", "O campo nome do projeto não pode estar vazio.");
            nomeAtividadeInput.requestFocus();
            valid = false;
        }
        String nomeEtapa = listaEtapas.getSelectionModel().getSelectedItem();
        this.idEtapa = this.etapa.idEtapa(nomeEtapa);
        if(idEtapa == -1) {
            showAlert("Eatapa não encontrada", "Consulte o suporte: a etapa da lista não foi encontrada no banco de dados.");
            nomeAtividadeInput.requestFocus();
            valid = false;
        }

        return valid;
    }

    private void clearFields() {
        nomeAtividadeInput.clear();
        listaEtapas.getSelectionModel().clearSelection();
        this.id = -1;
    }

    @FXML
    void btnDeletarAtividade(ActionEvent event) {
        this.atividadeDB = new AtividadeSQLite();
        ObservableList<Atividade> escolhido = LvAtividades.getSelectionModel().getSelectedItems();

        if (!escolhido.isEmpty()) {
            Atividade atividadeEscolhida = escolhido.get(0);

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("ATENÇÃO");
            alert.setHeaderText("Deseja realmente excluir a atividade selecionada?");
            alert.setContentText(atividadeEscolhida.toString());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                this.atividadeDB.deletarAtividde(atividadeEscolhida.getId());
                updateList();
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("ATENÇÃO");
            alert.setHeaderText("Nenhuma atividade foi selecionada");
            alert.showAndWait();
        }
    }


    @FXML
    void btnEditarAtividade(ActionEvent event) {
        ObservableList<Atividade> editar = LvAtividades.getSelectionModel().getSelectedItems();

        if (!editar.isEmpty()) {
            Atividade atividadeEscolhida = editar.get(0);

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Atenção");
            alert.setHeaderText("Deseja realmente editar a atividade selecionada?");
            alert.setContentText(atividadeEscolhida.toString());

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                nomeAtividadeInput.setText(atividadeEscolhida.getAtividade());
                listaEtapas.getSelectionModel().clearSelection();
                this.id = atividadeEscolhida.getId();
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Atenção");
            alert.setHeaderText("Nenhuma atividade foi selecionada");
            alert.showAndWait();
        }
    }



    private void populaLista() {
        this.etapa = new EtapaService();
        List<String> nomes = this.etapa.etapaPorNome();  // Alterado para pegar as etapas

        ObservableList<String> observableList = FXCollections.observableArrayList(nomes);
        listaEtapas.setItems(observableList);  // Alterado para 'listaEtapas'
    }


    private void updateList() {
        this.atividadeDB = new AtividadeSQLite();
        LvAtividades.getItems().clear();
        List<Atividade> listaAtividades = this.atividadeDB.all();
        listaAtividades.stream().forEach(atividade -> LvAtividades.getItems().add(atividade));
    }


}
