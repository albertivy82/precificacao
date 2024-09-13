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
import org.openjfx.precificacao.database.CustosVariaveisSQLite;
import org.openjfx.precificacao.models.CustosVariaveis;
import org.openjfx.precificacao.service.LimparMoeda;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CustosVariaveisController {

    private CustosVariaveisSQLite custosVariaveisDB;
    private LimparMoeda limpaMoeda;
    private Float valorCusto;

    private int id = -1;

    @FXML
    private TextField itemCustoInput;

    @FXML
    private TextField valorCustoInput;

    @FXML
    private ListView<CustosVariaveis> LvCustosVariaveis;

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
    protected void btnMain(ActionEvent e) {
        App.mudarTela("DashBoard");
    }

    @FXML
    protected void btnClientes(ActionEvent e) {
        App.mudarTela("Clientes");
    }


    @FXML
    protected void btnProfissionais(ActionEvent e) {
        App.mudarTela("Profissionais");
    }

    @FXML
    protected void btnProjeto(ActionEvent e) {
        App.mudarTela("Projeto");
    }

    @FXML
    protected void btnAtividade(ActionEvent e) {
        App.mudarTela("Atividade");
    }

    @FXML
    protected void btnEtapas(ActionEvent e) {
        App.mudarTela("Etapas");
    }

    @FXML
    protected void btnCustos(ActionEvent e) {
        App.mudarTela("Custos");
    }

    @FXML
    protected void btnCadastrarCusto(ActionEvent e) {
        if (camposEstaoValidos()) {
            CustosVariaveis novoCusto = new CustosVariaveis();
            novoCusto.setItem(itemCustoInput.getText());
            novoCusto.setValor(this.valorCusto);

            try {
                this.custosVariaveisDB = new CustosVariaveisSQLite();
                if (this.id == -1) {
                    custosVariaveisDB.NovoCustosVariaveis(novoCusto);
                } else {
                    novoCusto.setId(this.id);
                    custosVariaveisDB.editarCustosVariaveis(novoCusto);
                }
                clearFields();
                updateList();
            } catch (SQLException ex) {
                showAlert("Erro ao Cadastrar Custo Fixo", "Não foi possível cadastrar o custo fixo: " + ex.getMessage());
            }
        } else {
            showAlert("Erro de Validação", "Por favor, corrija os campos destacados antes de enviar.");
        }
    }

    private boolean camposEstaoValidos() {
        boolean valid = true;
        if (itemCustoInput.getText().trim().isEmpty()) {
            showAlert("Item Vazio", "O campo item não pode estar vazio.");
            itemCustoInput.requestFocus();
            valid = false;
        }
        if (valorCustoInput.getText().trim().isEmpty()) {
            showAlert("Valor do custo vazio", "O campo valor não pode estar vazio.");
            valorCustoInput.requestFocus();
            valid = false;
        } else {
            try {
                this.limpaMoeda = new LimparMoeda();
                String valorHoraTexto = valorCustoInput.getText();
                this.valorCusto = this.limpaMoeda.LimpaMoeda(valorHoraTexto);
            } catch (NumberFormatException e) {
                showAlert("Valor Inválido", "O campo valor deve ser um número válido.");
                valorCustoInput.requestFocus();
                valid = false;
            }
        }

        return valid;
    }

    private void clearFields() {
        itemCustoInput.clear();
        valorCustoInput.clear();
        this.id = -1;
    }

    @FXML
    void btnDeletarCusto(ActionEvent event) {
        this.custosVariaveisDB = new CustosVariaveisSQLite();
        ObservableList<CustosVariaveis> escolhido = LvCustosVariaveis.getSelectionModel().getSelectedItems();

        if (!escolhido.isEmpty()) {
            CustosVariaveis custoEscolhido = escolhido.get(0);

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("ATENÇÃO");
            alert.setHeaderText("Deseja realmente excluir o custo fixo selecionado?");
            alert.setContentText(custoEscolhido.toString());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                custosVariaveisDB.deletarCustosVariaveis(custoEscolhido);
                updateList();
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("ATENÇÃO");
            alert.setHeaderText("Nenhum custo fixo foi selecionado");
            alert.showAndWait();
        }
    }

    @FXML
    void btnEditarCusto(ActionEvent event) {


        ObservableList<CustosVariaveis> editar = LvCustosVariaveis.getSelectionModel().getSelectedItems();

        if(!editar.isEmpty()) {

            CustosVariaveis custoEscolhido = editar.get(0);

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Atenção");
            alert.setHeaderText("Deseja realmente editar o custo selecionado?");
            alert.setContentText(custoEscolhido.toString());

            Optional<ButtonType> result = alert.showAndWait();


            if(result.isPresent() && result.get()==ButtonType.OK) {
                itemCustoInput.setText(custoEscolhido.getItem());
                valorCustoInput.setText(custoEscolhido.getItem());
                this.id = custoEscolhido.getId();
                //System.out.println(this.id);
            }

        }else{

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Atenção");
            alert.setHeaderText("Nenhum ccusto foi selecionado");
            alert.showAndWait();

        }
    }

    private void updateList() {
        this.custosVariaveisDB = new CustosVariaveisSQLite();
        LvCustosVariaveis.getItems().clear();
        List<CustosVariaveis> listaCustos = this.custosVariaveisDB.all();
        listaCustos.stream().forEach(custo->LvCustosVariaveis.getItems().add(custo));

    }

}