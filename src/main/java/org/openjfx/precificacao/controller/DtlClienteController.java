package org.openjfx.precificacao.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.openjfx.precificacao.App;
import org.openjfx.precificacao.models.Cliente;
import org.openjfx.precificacao.shared.ClienteSingleton;

public class DtlClienteController {

    Cliente cliente = ClienteSingleton.getInstance().getCliente();

    @FXML
    protected void btnMain(ActionEvent e) {
        App.mudarTela("DashBoard");
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
    protected void btnImpostos(ActionEvent e) {
        App.mudarTela("Impostos");
    }

    @FXML
    protected void btnClientes(ActionEvent e) {

        ClienteSingleton.getInstance().clearCliente();

        App.mudarTela("Clientes");
    }


    @FXML
    private Label codClienteLabel;

    @FXML
    private Label nomeCliente;

    @FXML
    private Label cpfLabel;

    @FXML
    private Label telefoneLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label enderecoLabel;

    @FXML
    private Label bairroLabel;

    @FXML
    private Label estadoLabel;

    @FXML
    private Label cidadeLabel;

    @FXML
    private Label cepLabel;

    @FXML
    void initialize() {
        setLabels();
    }

    public void setLabels() {
        if (cliente != null) {
            codClienteLabel.setText(getValueOrPlaceholder(cliente.getCodCliente()));
            nomeCliente.setText(getValueOrPlaceholder(cliente.getNome()));
            cpfLabel.setText(getValueOrPlaceholder(cliente.getCpf()));
            telefoneLabel.setText(getValueOrPlaceholder(cliente.getTelefone()));
            emailLabel.setText(getValueOrPlaceholder(cliente.getEmail()));
            enderecoLabel.setText(getValueOrPlaceholder(cliente.getEndereco()));
            bairroLabel.setText(getValueOrPlaceholder(cliente.getBairro()));
            cidadeLabel.setText(getValueOrPlaceholder(cliente.getCidade()));
            estadoLabel.setText(getValueOrPlaceholder(cliente.getEstado()));
            cepLabel.setText(getValueOrPlaceholder(cliente.getCep()));
        }
    }

    private String getValueOrPlaceholder(String value) {
        return value != null && !value.isEmpty() ? value : "não informado";
    }
}
