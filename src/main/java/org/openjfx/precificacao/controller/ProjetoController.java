package org.openjfx.precificacao.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import org.openjfx.precificacao.App;
import org.openjfx.precificacao.database.CustosFixosSQLite;
import org.openjfx.precificacao.database.ProjetoSQLite;
import org.openjfx.precificacao.service.ClienteService;
import org.openjfx.precificacao.models.CustosFixos;
import org.openjfx.precificacao.models.Projeto;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProjetoController {

    private ProjetoSQLite projetosDB;

    private ClienteService cliente;

    private int id = -1;

    @FXML
    private TextField nomeProjetolInput;

    @FXML
    private ComboBox<String> listaClientes;

    @FXML
    private ListView<Projeto> LvProjetos;

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
    protected void btnCadastrarProjeto(ActionEvent e) {
        if (camposEstaoValidos()) {
            Projeto novoProjeto = new Projeto();
            novoProjeto.setNomeProjeto(nomeProjetolInput.getText());
            novoProjeto.setIdCliente(Integer.parseInt(idClienteInput.getText()));
            novoProjeto.setStatus("Cadastrado");


            try {
                this.projetosDB = new ProjetoSQLite();
                if (this.id == -1) {
                    projetosDB.NovoProjeto(novoProjeto);
                } else {
                    novoProjeto.setId(this.id);
                    projetosDB.editarProjeto(novoProjeto);
                }
                clearFields();
                updateList();
            } catch (SQLException ex) {
                showAlert("Erro ao Cadastrar Projeto", "Não foi possível cadastrar o projeto: " + ex.getMessage());
            }
        } else {
            showAlert("Erro de Validação", "Por favor, corrija os campos destacados antes de enviar.");
        }
    }

    private boolean camposEstaoValidos() {
        boolean valid = true;
        if (nomeProjetoInput.getText().trim().isEmpty()) {
            showAlert("Nome do Projeto Vazio", "O campo nome do projeto não pode estar vazio.");
            nomeProjetoInput.requestFocus();
            valid = false;
        }
        if (idClienteInput.getText().trim().isEmpty()) {
            showAlert("ID do Cliente Vazio", "O campo ID do cliente não pode estar vazio.");
            idClienteInput.requestFocus();
            valid = false;
        } else {
            try {
                Integer.parseInt(idClienteInput.getText());
            } catch (NumberFormatException e) {
                showAlert("ID do Cliente Inválido", "O campo ID do cliente deve ser um número válido.");
                idClienteInput.requestFocus();
                valid = false;
            }
        }

        return valid;
    }

    private void clearFields() {
        nomeProjetoInput.clear();
        idClienteInput.clear();
        this.id = -1;
    }

    @FXML
    void btnDeletarProjeto(ActionEvent event) {
        this.projetosDB = new ProjetoSQLite();
        ObservableList<Projeto> escolhido = LvProjetos.getSelectionModel().getSelectedItems();

        if (!escolhido.isEmpty()) {
            Projeto projetoEscolhido = escolhido.get(0);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("ATENÇÃO");
            alert.setHeaderText("Deseja realmente excluir o projeto selecionado?");
            alert.setContentText(projetoEscolhido.toString());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                projetosDB.deletarProjeto(projetoEscolhido);
                updateList();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ATENÇÃO");
            alert.setHeaderText("Nenhum projeto foi selecionado");
            alert.showAndWait();
        }
    }

    @FXML
    void btnEditarProjeto(ActionEvent event) {

        //setar "projeto alterado" em status
        ObservableList<Projeto> editar = LvProjetos.getSelectionModel().getSelectedItems();

        if(!editar.isEmpty()) {

            Projeto projetoEscolhido = editar.get(0);

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Atenção");
            alert.setHeaderText("Deseja realmente editar o projeto selecionado?");
            alert.setContentText(projetoEscolhido.toString());

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


    private void populaLista(){
        this.cliente  = new ClienteService();
        List<String> nomes = cliente.clientesPorNome();

        ObservableList<String> observableList = FXCollections.observableArrayList(nomes);
        listaClientes.setItems(observableList);

    }

    private void updateList() {
        this.custosFixosDB = new CustosFixosSQLite();
        LvCustosFixos.getItems().clear();
        List<CustosFixos> listaCustos = this.custosFixosDB.all();
        listaCustos.stream().forEach(custo->LvCustosFixos.getItems().add(custo));

    }

}
