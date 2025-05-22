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
import org.openjfx.precificacao.enums.StatusProjeto;
import org.openjfx.precificacao.models.Cliente;
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
    void initialize() {
        this.cliente = new ClienteService();
        this.projetoService = new ProjetoService();
        updateList();
        populaLista();
        tipoCBs();

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
    protected void btnImpostos(ActionEvent e) {
        App.mudarTela("Impostos");
    }






    @FXML
    protected void btnDtlProjeto(ActionEvent e) {

        ObservableList<Projeto> projetoPEditar = LvProjetos.getSelectionModel().getSelectedItems();

        if (!projetoPEditar.isEmpty()) {

            Projeto projetoEscolhido = projetoPEditar.get(0);

            if ("INICIADO".equals(projetoEscolhido.getStatus()) || "EXECUTADO".equals(projetoEscolhido.getStatus())) {
                mostrarAviso("Atenção", "Você poderá visualizar os dados deste projeto, mas não poderá alterá-lo.\nSe precisar fazer ajustes, crie um projeto complementar.");
            }

            boolean result = confirmarAcao("Atenção", "Deseja detalhar o projeto selecionado?", projetoEscolhido.toString());

            if (result) {
                ProjetoSingleton.getInstance().setProjeto(projetoEscolhido);
                App.mudarTela("DetalhamentoProjeto");
            }

        } else {
            mostrarAviso("Atenção", "Nenhum projeto foi selecionado");
        }
    }



    @FXML
    protected void btnInicarProjeto(ActionEvent e) throws SQLException {
        ObservableList<Projeto> projetoIniciar = LvProjetos.getSelectionModel().getSelectedItems();

        if (projetoIniciar.isEmpty()) {
            mostrarAviso("Atenção", "Nenhum projeto foi selecionado");
            return;
        }

        Projeto projetoEscolhido = projetoIniciar.get(0);

        if (!"PRECIFICADO".equals(projetoEscolhido.getStatus()) || projetoEscolhido.getPrecificacao() <= 0) {
            mostrarAviso("Atenção", "Somente projeto com status 'PRECIFICADO' podem ser iniciados");
            return;
        }

        Alert confirmacao = new Alert(AlertType.CONFIRMATION);
        confirmacao.setTitle("Atenção");
        confirmacao.setHeaderText("Deseja Iniciar o projeto selecionado?\n" +
                "50% DO VALOR TOTAL DO PROJETO SERÁ REGISTRADO COMO RECEBIDO e o Porjeto não poderá mais ser editado ou ter seu orçamento alterado.");
        confirmacao.setContentText(projetoEscolhido.toString());

        Optional<ButtonType> result = confirmacao.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (!this.projetoService.verificaProjetoExitente(projetoEscolhido.getId())) {
                this.projetoService.inicioProjeto(projetoEscolhido);
                this.projetoService.alterarStatusProjeto(projetoEscolhido.getId(), StatusProjeto.INICIADO);

            } else {
                mostrarAviso("Atenção", "O projeto já foi iniciado");
            }
        }
    }

    @FXML
    protected void btnFinalizarProjeto(ActionEvent e) throws SQLException {

        ObservableList<Projeto> projetoFinalizar = LvProjetos.getSelectionModel().getSelectedItems();

        if (projetoFinalizar.isEmpty()) {
            mostrarAviso("Atenção", "Nenhum projeto foi selecionado");
            return;
        }

        Projeto projetoEscolhido = projetoFinalizar.get(0);

        if (!"INICIADO".equals(projetoEscolhido.getStatus()) || projetoEscolhido.getPrecificacao() <= 0) {
            mostrarAviso("Atenção", "Somente projetos com status 'INICIADO' podem ser encerrados");
            return;
        }

        boolean result = confirmarAcao("Atenção", "Deseja finalizar a execução do projeto selecionado?\n" +
                "50% FINAL DO VALOR TOTAL DO PROJETO SERÁ REGISTRADO COMO RECEBIDO.", projetoEscolhido.toString());

        if (result) {
            if (this.projetoService.verificaExecucaoProjeto(projetoEscolhido.getId())) {
               this.projetoService.finalizaProjeto(projetoEscolhido);
                this.projetoService.alterarStatusProjeto(projetoEscolhido.getId(), StatusProjeto.EXECUTADO);

            } else {
                mostrarAviso("Atenção", "O projeto já foi FINALIZADO");
            }
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
                    projetosDB.editarProjeto(novoProjeto);
                }

                clearFields();
                updateList();
            } catch (SQLException ex) {
                mostrarErro("Erro ao Cadastrar Projeto", "Não foi possível cadastrar o projeto: " + ex.getMessage());
            }
        } else {
                mostrarAviso("Erro de Validação", "Por favor, corrija os campos destacados antes de enviar.");
        }
    }

    private boolean camposEstaoValidos() {
        boolean valid = true;

        // Verificar se o campo de nome do projeto está vazio
        if (nomeProjetolInput.getText().trim().isEmpty()) {
            mostrarAviso("Nome do Projeto Vazio", "O campo nome do projeto não pode estar vazio.");
            nomeProjetolInput.requestFocus();
            valid = false;
        }

        // Verificar se algum cliente foi selecionado na lista
        Cliente clienteSelecionado = listaClientes.getSelectionModel().getSelectedItem();

        if (clienteSelecionado == null) {
            mostrarAviso("Cliente não selecionado", "Você deve selecionar um cliente da lista.");
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
        this.id = -1;
        populaLista();
    }

    @FXML
    void btnDeletarProjeto(ActionEvent event) throws SQLException {
        this.projetosDB = new ProjetoSQLite();
        ObservableList<Projeto> escolhido = LvProjetos.getSelectionModel().getSelectedItems();

        if (!escolhido.isEmpty()) {
            Projeto projetoEscolhido = escolhido.get(0);
            boolean result = confirmarAcao("ATENÇÃO", "Deseja realmente excluir o projeto selecionado?" , projetoEscolhido.toString() );
            if (result) {
                this.projetoService.deletaProjetoEDependencias(projetoEscolhido.getId());
                updateList();
            }
        } else {
           mostrarAviso("ATENÇÃO", "Nenhum projeto foi selecionado");
        }
    }

    @FXML
    void btnEditarProjeto(ActionEvent event) {


        ObservableList<Projeto> editar = LvProjetos.getSelectionModel().getSelectedItems();

        if(!editar.isEmpty()) {

            Projeto projetoEscolhido = editar.get(0);
            if(projetoEscolhido.getStatus().equals("INICIADO") || projetoEscolhido.getStatus().equals("EXECUTADO")){
                mostrarAviso("Operção Impossível", "O projeto já foi inicciado ou finalizado. Se precisar, crie um projeto complementar");
            }else{
                boolean result = confirmarAcao("Atenção", "Deseja realmente editar o projeto selecionado?", projetoEscolhido.toString());
                if(result) {
                    this.projetoEdicao = new Projeto();
                    this.projetoEdicao.setCodProjeto(projetoEscolhido.getCodProjeto());
                    this.projetoEdicao.setStatus(projetoEscolhido.getStatus());
                    this.projetoEdicao.setPrecificacao(projetoEdicao.getPrecificacao());
                    this.id = projetoEscolhido.getId();
                    nomeProjetolInput.setText(projetoEscolhido.getNomeProjeto());
                    Cliente clienteDoProjeto = new Cliente();
                    clienteDoProjeto = this.cliente.clientePorId(projetoEscolhido.getIdCliente());
                    listaClientes.getSelectionModel().select(clienteDoProjeto);
                }
            }
        }else{
            mostrarAviso("Atenção","Nenhum custo foi selecionado");
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


    private void mostrarErro(String titulo, String mensagem) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarAviso(String titulo, String mensagem) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(mensagem);
        alert.setContentText(null);
        alert.showAndWait();
    }

    private boolean confirmarAcao(String titulo, String mensagem, String conteudo) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(mensagem);
        alert.setContentText(conteudo);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }




}
