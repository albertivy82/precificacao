package org.openjfx.precificacao.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.util.StringConverter;
import org.openjfx.precificacao.App;
import org.openjfx.precificacao.database.ProjetoSQLite;
import org.openjfx.precificacao.models.Cliente;
import org.openjfx.precificacao.models.Profissionais;
import org.openjfx.precificacao.models.Projeto;
import org.openjfx.precificacao.service.ClienteService;
import org.openjfx.precificacao.service.ProjetoService;
import org.openjfx.precificacao.shared.ProjetoSingleton;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProjetoController {

    private ProjetoSQLite projetosDB;

    private ClienteService cliente;

    private int id = -1;

    private int idCliente = -1;

    private ProjetoService projetoService;

    private String statusProjeto;

    private String tipoProjeto;

    private Projeto projetoEdicao;


    @FXML
    private TextField nomeProjetolInput;

    @FXML
    private ComboBox<Cliente> listaClientes;

    @FXML
    private ListView<Projeto> LvProjetos;

    @FXML
    private CheckBox cbNovo;
    @FXML
    private CheckBox cbReforma;
    @FXML
    private CheckBox cbExecutado;
    @FXML
    private CheckBox cbContratados;

    @FXML
    void initialize() {
        this.cliente = new ClienteService();
        this.projetoService = new ProjetoService();
        cbContratados.setDisable(true);
        cbExecutado.setDisable(true);
        updateList();
        populaLista();
        tipoCBs();
        statusCBs();
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
    protected void btnDtlProjeto(ActionEvent e) {

      ObservableList<Projeto> projetoPEditar = LvProjetos.getSelectionModel().getSelectedItems();

        if(!projetoPEditar.isEmpty()) {

            Projeto projetoEscolhido = projetoPEditar.get(0);

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Atenção");
            alert.setHeaderText("Deseja detalhar o projeto selecionado?");
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
            novoProjeto.setIdCliente(idCliente);
            novoProjeto.setTipo(this.tipoProjeto);
            try {
                this.projetosDB = new ProjetoSQLite();
                if (this.id == -1) {
                    novoProjeto.setStatus("CADASTRADO");
                    projetosDB.NovoProjeto(novoProjeto);
                    this.projetoService.gerarCodProjeto(nomeProjetolInput.getText());
                } else {
                    novoProjeto.setId(this.id);
                    novoProjeto.setCodProjeto(this.projetoEdicao.getCodProjeto());
                    novoProjeto.setStatus(this.projetoEdicao.getStatus());

                    if(this.statusProjeto != null){
                        System.out.println(this.statusProjeto);
                            novoProjeto.setStatus(this.statusProjeto);
                    }
                    novoProjeto.setPrecificacao(projetoEdicao.getPrecificacao());
                    System.out.println("OQUE HÁ AQUI?"+novoProjeto.getPrecificacao());
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

        // Verificar se o campo de nome do projeto está vazio
        if (nomeProjetolInput.getText().trim().isEmpty()) {
            showAlert("Nome do Projeto Vazio", "O campo nome do projeto não pode estar vazio.");
            nomeProjetolInput.requestFocus();
            valid = false;
        }

        // Verificar se algum cliente foi selecionado na lista
        Cliente clienteSelecionado = listaClientes.getSelectionModel().getSelectedItem();

        if (clienteSelecionado == null) {
            showAlert("Cliente não selecionado", "Você deve selecionar um cliente da lista.");
            listaClientes.requestFocus();  // Focar na lista de clientes
            valid = false;
        } else {
            // Se um cliente foi selecionado, obter o ID do cliente
            this.idCliente = clienteSelecionado.getId();
        }

        return valid;
    }


    private void clearFields() {
        nomeProjetolInput.clear();
        cbReforma.setSelected(false);
        cbNovo.setSelected(false);
        cbContratados.setSelected(false);
        cbExecutado.setSelected(false);
        this.id = -1;
        populaLista();
    }

    @FXML
    void btnDeletarProjeto(ActionEvent event) throws SQLException {
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
                this.projetoService.deletaProjetoEDependencias(projetoEscolhido.getId());
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
                this.projetoEdicao = new Projeto();
                this.projetoEdicao.setCodProjeto(projetoEscolhido.getCodProjeto());
                this.projetoEdicao.setStatus(projetoEscolhido.getStatus());
                this.projetoEdicao.setPrecificacao(projetoEscolhido.getPrecificacao());
                System.out.println("PQ ESTÁ ZERANDO?"+projetoEscolhido.getPrecificacao());
                this.id = projetoEscolhido.getId();
                nomeProjetolInput.setText(projetoEscolhido.getNomeProjeto());

                if(projetoEscolhido.getStatus().equals("Precificado")) {
                            cbContratados.setDisable(false);
                            cbExecutado.setDisable(false);
                        }
                       Cliente clienteDoProjeto = new Cliente();
                       clienteDoProjeto = this.cliente.clientePorId(projetoEscolhido.getIdCliente());
                       listaClientes.getSelectionModel().select(clienteDoProjeto);
            }

        }else{

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Atenção");
            alert.setHeaderText("Nenhum ccusto foi selecionado");
            alert.showAndWait();

        }
    }


    private void populaLista() {
        // Limpar a lista antes de adicionar novos itens
        listaClientes.getItems().clear();

        // Obter lista de clientes
        List<Cliente> clientes = this.cliente.listarClientes();

        // Filtrar a lista para remover objetos nulos, caso existam
        List<Cliente> clientesFiltrados = clientes.stream()
                .filter(cliente -> cliente != null)
                .collect(Collectors.toList());

        // Adicionar clientes filtrados ao ObservableList
        ObservableList<Cliente> clientesListados = FXCollections.observableArrayList(clientesFiltrados);
        listaClientes.setItems(clientesListados);

        // Definir o StringConverter para a ComboBox
        listaClientes.setConverter(new StringConverter<Cliente>() {
            @Override
            public String toString(Cliente object) {
                // Verificar se o objeto não é nulo
                return object != null ? object.toString() : "";
            }

            @Override
            public Cliente fromString(String string) {
                return null; // Não é necessário implementar para ComboBox
            }
        });
    }



    private void updateList() {
        this.projetosDB = new ProjetoSQLite();
        LvProjetos.getItems().clear();
        List<Projeto> listaProjetos = this.projetosDB.all();
        listaProjetos.stream().forEach(projeto->LvProjetos.getItems().add(projeto));

    }


    private void statusCBs() {

        cbContratados.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                cbExecutado.setSelected(false);
                this.statusProjeto = "CADASTRADO";

            }
        });

      cbExecutado.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // Se "Executado" for selecionado, desmarcar "Contratados"
                cbContratados.setSelected(false);
                this.statusProjeto = "EXECUTADO";

            }
        });
    }


    private void tipoCBs() {

        cbNovo.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                cbReforma.setSelected(false);
                this.tipoProjeto = "NOVO";
            }
        });

        cbReforma.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // Se "Executado" for selecionado, desmarcar "Contratados"
                cbNovo.setSelected(false);
                this.tipoProjeto = "REFORMA";

            }
        });
    }


}
