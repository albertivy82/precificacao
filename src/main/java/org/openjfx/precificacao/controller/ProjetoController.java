package org.openjfx.precificacao.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import org.openjfx.precificacao.App;
import org.openjfx.precificacao.database.ProjetoSQLite;
import org.openjfx.precificacao.models.Projeto;
import org.openjfx.precificacao.service.ClienteService;
import org.openjfx.precificacao.shared.ProjetoSingleton;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProjetoController {

    private ProjetoSQLite projetosDB;

    private ClienteService cliente;

    private int id = -1;

    private int idCliente = -1;

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
    protected void btnCustos(ActionEvent e) {
        App.mudarTela("Custos");
    }

    @FXML
    protected void btnProfissionais(ActionEvent e) {
        App.mudarTela("Profissionais");
    }

    @FXML
    protected void btnDtlProjeto(ActionEvent e) {

      ObservableList<Projeto> projetoPEditar = LvProjetos.getSelectionModel().getSelectedItems();

        if(!projetoPEditar.isEmpty()) {

            Projeto projetoEscolhido = projetoPEditar.get(0);

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Atenção");
            alert.setHeaderText("Deseja realmente editar o projeto selecionado?");
            alert.setContentText(projetoEscolhido.toString());

            Optional<ButtonType> result = alert.showAndWait();


            if(result.isPresent() && result.get()==ButtonType.OK) {
                ProjetoSingleton.getInstance().setProjeto(projetoEscolhido);
                App.mudarTela("DetalhamentoProjeto");

            }

        }else{

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Atenção");
            alert.setHeaderText("Nenhum projeto foi selecionado");
            alert.showAndWait();

        }





    }


    @FXML
    protected void btnCadastrarProjeto(ActionEvent e) {

            if (camposEstaoValidos()) {
            Projeto novoProjeto = new Projeto();
            novoProjeto.setNomeProjeto(nomeProjetolInput.getText());
            System.out.println(idCliente);
            novoProjeto.setIdCliente(idCliente);
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
        this.cliente = new ClienteService();
        if (nomeProjetolInput.getText().trim().isEmpty()) {
            showAlert("Nome do Projeto Vazio", "O campo nome do projeto não pode estar vazio.");
            nomeProjetolInput.requestFocus();
            valid = false;
        }
        String nome = listaClientes.getSelectionModel().getSelectedItem();
        this.idCliente = this.cliente.idCliente(nome);
        if(idCliente == -1) {
            showAlert("Cliente não encontrado", "Consulte o suporte: o cliente da lista não foi encontrado no banco de dados.");
            nomeProjetolInput.requestFocus();
            valid = false;
        }

        return valid;
    }

    private void clearFields() {
        nomeProjetolInput.clear();
        listaClientes.getSelectionModel().clearSelection();
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
                nomeProjetolInput.setText(projetoEscolhido.getNomeProjeto());
                listaClientes.getSelectionModel().clearSelection();
                this.id = projetoEscolhido.getId();

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
        this.projetosDB = new ProjetoSQLite();
        LvProjetos.getItems().clear();
        List<Projeto> listaProjetos = this.projetosDB.all();
        System.out.println(listaProjetos);
        listaProjetos.stream().forEach(projeto->LvProjetos.getItems().add(projeto));

    }

}
