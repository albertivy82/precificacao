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
import org.openjfx.precificacao.database.CustosFixosSQLite;
import org.openjfx.precificacao.models.CustosFixos;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CustosFixosController {

    private CustosFixosSQLite custosFixosDB;

    private int id = -1;

    @FXML
    private TextField itemCustoInput;

    @FXML
    private TextField valorCustoInput;

    @FXML
    private ListView<CustosFixos> LvCustosFixos;

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
    protected void btnCadProjetos(ActionEvent e) {
        App.mudarTela("CadProjetos");
    }

    @FXML
    protected void btnCadastrarCusto(ActionEvent e) {
        if (camposEstaoValidos()) {
            CustosFixos novoCusto = new CustosFixos();
            novoCusto.setItem(itemCustoInput.getText());
            novoCusto.setValor(Float.parseFloat(valorCustoInput.getText()));

            try {
                this.custosFixosDB = new CustosFixosSQLite();
                if (this.id == -1) {
                    custosFixosDB.NovoCustosFixos(novoCusto);
                } else {
                    novoCusto.setId(this.id);
                    custosFixosDB.editarCustosFixos(novoCusto);
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
            showAlert("Valor Vazio", "O campo valor não pode estar vazio.");
            valorCustoInput.requestFocus();
            valid = false;
        } else {
            try {
                Float.parseFloat(valorCustoInput.getText());
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
        this.custosFixosDB = new CustosFixosSQLite();
        ObservableList<CustosFixos> escolhido = LvCustosFixos.getSelectionModel().getSelectedItems();

        if (!escolhido.isEmpty()) {
            CustosFixos custoEscolhido = escolhido.get(0);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("ATENÇÃO");
            alert.setHeaderText("Deseja realmente excluir o custo fixo selecionado?");
            alert.setContentText(custoEscolhido.toString());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                custosFixosDB.deletarCustosFixos(custoEscolhido);
                updateList();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ATENÇÃO");
            alert.setHeaderText("Nenhum custo fixo foi selecionado");
            alert.showAndWait();
        }
    }

    @FXML
    void btnEditarCusto(ActionEvent event) {


        ObservableList<CustosFixos> editar = LvCustosFixos.getSelectionModel().getSelectedItems();

        if(!editar.isEmpty()) {

            CustosFixos custoEscolhido = editar.get(0);

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Atenção");
            alert.setHeaderText("Deseja realmente editar o custo selecionado?");
            alert.setContentText(custoEscolhido.toString());

            Optional<ButtonType> result = alert.showAndWait();


            if(result.isPresent() && result.get()==ButtonType.OK) {
                itemCustoInput.setText(custoEscolhido.getItem());
                valorCustoInput.setText(custoEscolhido.getItem());
                this.id = custoEscolhido.getId();
                System.out.println(this.id);
            }

        }else{

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Atenção");
            alert.setHeaderText("Nenhum ccusto foi selecionado");
            alert.showAndWait();

        }
    }

    private void updateList() {
        this.custosFixosDB = new CustosFixosSQLite();
        LvCustosFixos.getItems().clear();
        List<CustosFixos> listaCustos = this.custosFixosDB.all();
        listaCustos.stream().forEach(custo->LvCustosFixos.getItems().add(custo));

    }

}