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
import org.openjfx.precificacao.database.ProfissionaisSQLite;
import org.openjfx.precificacao.models.Profissionais;
import org.openjfx.precificacao.service.LimparMoeda;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProfissionaisController {

    private ProfissionaisSQLite profissionaisDB;
    private LimparMoeda limpaMoeda;
    private Float valorHoraTrabalho;

    private int id = -1;

    @FXML
    private TextField nomeProfissionalInput;

    @FXML
    private TextField ProfissionalInput;

    @FXML
    private TextField valorHoraInput;

    @FXML
    private ListView<Profissionais> LvProfissionais;

    @FXML
    void initialize() {
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
    protected void btnEtapas(ActionEvent e) {
        App.mudarTela("Etapas");
    }

    @FXML
    protected void btnCadastrarProfissional(ActionEvent e) {
        if (camposEstaoValidos()) {
            Profissionais novoProfissional = new Profissionais();
            novoProfissional.setNome(nomeProfissionalInput.getText());
            novoProfissional.setProfissional(ProfissionalInput.getText());
            novoProfissional.setValorHora(valorHoraTrabalho);

            try {
                this.profissionaisDB = new ProfissionaisSQLite();
                if (this.id == -1) {
                    profissionaisDB.novoProfissional(novoProfissional);
                } else {
                    novoProfissional.setId(this.id);
                    System.out.println("O id do profissional"+this.id);
                    profissionaisDB.editarProfissionais(novoProfissional);
                }
                clearFields();
                updateList();
            } catch (SQLException ex) {
                showAlert("Erro ao Cadastrar Profissional", "Não foi possível cadastrar o profissional: " + ex.getMessage());
            }
        } else {
            showAlert("Erro de Validação", "Por favor, corrija os campos destacados antes de enviar.");
        }
    }

    private boolean camposEstaoValidos() {
        boolean valid = true;
        if (nomeProfissionalInput.getText().trim().isEmpty()) {
            showAlert("Nome Vazio", "O campo nome não pode estar vazio.");
            nomeProfissionalInput.requestFocus();
            valid = false;
        }
        if (ProfissionalInput.getText().trim().isEmpty()) {
            showAlert("Profissão Vazia", "O campo profissão não pode estar vazio.");
            ProfissionalInput.requestFocus();
            valid = false;
        }
        if (valorHoraInput.getText().trim().isEmpty()) {
            showAlert("Valor Hora Vazio", "O campo valor hora não pode estar vazio.");
            valorHoraInput.requestFocus();
            valid = false;
        } else {
            try {
                this.limpaMoeda = new LimparMoeda();
                String valorHoraTexto = valorHoraInput.getText();
               // System.out.println("Valor hora input: " + valorHoraTexto);
                this.valorHoraTrabalho = this.limpaMoeda.LimpaMoeda(valorHoraTexto);
                //System.out.println("Valor hora trabalho: " + this.valorHoraTrabalho);
            } catch (NumberFormatException e) {
                showAlert("Valor Hora Inválido", "O campo valor hora deve ser um número válido.");
                valorHoraInput.requestFocus();
                valid = false;
            }
        }
        return valid;
    }

    private void clearFields() {
        nomeProfissionalInput.clear();
        ProfissionalInput.clear();
        valorHoraInput.clear();
        this.id = -1;
    }

    @FXML
    void btnDeletarProfissional(ActionEvent event) {
        this.profissionaisDB = new ProfissionaisSQLite();
        ObservableList<Profissionais> escolhido = LvProfissionais.getSelectionModel().getSelectedItems();

        if (!escolhido.isEmpty()) {
            Profissionais profissionalEscolhido = escolhido.get(0);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("ATENÇÃO");
            alert.setHeaderText("Deseja realmente excluir o profissional selecionado?");
            alert.setContentText(profissionalEscolhido.toString());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                profissionaisDB.deletarProfissional(profissionalEscolhido.getId());
                updateList();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ATENÇÃO");
            alert.setHeaderText("Nenhum profissional foi selecionado");
            alert.showAndWait();
        }
    }

    @FXML
    void btnEditarProfissional(ActionEvent event) {
        ObservableList<Profissionais> editar = LvProfissionais.getSelectionModel().getSelectedItems();

        if (!editar.isEmpty()) {
            Profissionais profissionalEscolhido = editar.get(0);

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Atenção");
            alert.setHeaderText("Deseja realmente editar o profissional selecionado?");
            alert.setContentText(profissionalEscolhido.toString());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                nomeProfissionalInput.setText(profissionalEscolhido.getNome());
                ProfissionalInput.setText(profissionalEscolhido.getProfissional());
                valorHoraInput.setText(String.valueOf(profissionalEscolhido.getValorHora()));
                this.id = profissionalEscolhido.getId();
            }
        } else {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Atenção");
            alert.setHeaderText("Nenhum profissional foi selecionado");
            alert.showAndWait();
        }
    }

    private void updateList() {
        this.profissionaisDB = new ProfissionaisSQLite();
        LvProfissionais.getItems().clear();
        List<Profissionais> listaProfissionais = this.profissionaisDB.all();
        listaProfissionais.forEach(p -> LvProfissionais.getItems().add(p));
    }
}
