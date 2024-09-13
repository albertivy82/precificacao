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
import org.openjfx.precificacao.database.ClienteSQLite;
import org.openjfx.precificacao.models.Cliente;
import org.openjfx.precificacao.shared.CPFMaskedTextField;
import org.openjfx.precificacao.shared.CPFValidator;
import org.openjfx.precificacao.shared.TelefoneMaskedTextField;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public class ClienteController {
	 
	private ClienteSQLite clientes; 
	
	private int id = -1;
	
	@FXML
	private CPFMaskedTextField cpfInput;
	
	@FXML
	private TextField emailInput;

	@FXML
	private TextField nomeClienteInput;
	
	@FXML
	private TelefoneMaskedTextField telefoneInput;


	@FXML
	private TextField bairroClienteInput;

	@FXML
	private TextField cepClienteInput;

	@FXML
	private TextField enderecoClienteInput;

	@FXML
	private ListView<Cliente> LvClientes;

		
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
	protected void btnProfissionais(ActionEvent e) {
		App.mudarTela("Profissionais");
	};

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
	protected void btnCadastrarCliente(ActionEvent e) {
		
		    if (camposEstaoValidos()) {
	        Cliente novoCliente = new Cliente();
	        novoCliente.setNome(nomeClienteInput.getText());
	        novoCliente.setEmail(emailInput.getText());
	        novoCliente.setCpf(cpfInput.getText());
	        novoCliente.setTelefone(telefoneInput.getText());
			novoCliente.setEndereco(enderecoClienteInput.getText());
			novoCliente.setBairro(bairroClienteInput.getText());
			novoCliente.setCep(cepClienteInput.getText());

	        try {
	            this.clientes = new ClienteSQLite();
		            if(this.id==-1) {
		            clientes.novoCliente(novoCliente);
		            }else {
		            novoCliente.setId(this.id);	
		           	clientes.editarCliente(novoCliente);
		            }
	            nomeClienteInput.clear();
	            emailInput.clear();
	            cpfInput.clear();
	            telefoneInput.clear();
				enderecoClienteInput.clear();
				bairroClienteInput.clear();
				cepClienteInput.clear();
	            updateList();
	        } catch (SQLException ex) {
	            showAlert("Erro ao Cadastrar Cliente", "Não foi possível cadastrar o cliente: " + ex.getMessage());
	        }
	    } else {
	        showAlert("Erro de Validação", "Por favor, corrija os campos destacados antes de enviar.");
	    }
	
	};

	
	private boolean camposEstaoValidos() {
	    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
	    String cpfRegex = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}";
	    String telefoneRegex = "^\\(\\d{2}\\) \\d{5}-\\d{4}$";

	    boolean valid = true;
	    // Verificação para nome (não pode estar vazio)
	    if (nomeClienteInput.getText().trim().isEmpty()) {
	        showAlert("Nome Vazio", "O campo nome não pode estar vazio.");
	        nomeClienteInput.requestFocus();
	        valid = false;
	    }
	    // Verificação para e-mail (não pode estar vazio e deve seguir o regex)
	    if (emailInput.getText().trim().isEmpty() || !emailInput.getText().matches(emailRegex)) {
	        showAlert("E-mail Inválido", "O campo de e-mail está vazio ou não está no formato correto.");
	        emailInput.requestFocus();
	        valid = false;
	    }
	    // Verificação para CPF (não pode estar vazio e deve seguir o regex)
	    if (cpfInput.getText().trim().isEmpty() || !cpfInput.getText().matches(cpfRegex) || !CPFValidator.isValidCPF(cpfInput.getText())){
	        showAlert("CPF Inválido", "O campo CPF está vazio ou não está no formato XXX.XXX.XXX-XX.");
	        cpfInput.requestFocus();
	        valid = false;
	    }
	    // Verificação para telefone (não pode estar vazio e deve seguir o regex)
	    if (telefoneInput.getText().trim().isEmpty() || !telefoneInput.getText().matches(telefoneRegex)) {
	        showAlert("Telefone Inválido", "O campo de telefone está vazio ou não está no formato (XX) XXXXX-XXXX.");
	        telefoneInput.requestFocus();
	        valid = false;
	    }

		if (enderecoClienteInput.getText().trim().isEmpty()) {
			showAlert("Endereço Vazio", "O campo Endereço não pode estar vazio.");
			enderecoClienteInput.requestFocus();
			valid = false;
		}

		if (bairroClienteInput.getText().trim().isEmpty()) {
			showAlert("Bairro Vazio", "O campo Bairro não pode estar vazio.");
			bairroClienteInput.requestFocus();
			valid = false;
		}

		if (cepClienteInput.getText().trim().isEmpty()) {
			showAlert("CEP Vazio", "O campo CEP não pode estar vazio.");
			cepClienteInput.requestFocus();
			valid = false;
		}


	    return valid;
	}

	
	@FXML
	void btnDeletarCliente(ActionEvent event) {
	    this.clientes = new ClienteSQLite();
	    ObservableList<Cliente> escolhido = LvClientes.getSelectionModel().getSelectedItems();

	    if (!escolhido.isEmpty()) {
	        Cliente clienteEscolhido = escolhido.get(0);

	        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	        alert.setTitle("ATENÇÃO");
	        alert.setHeaderText("Deseja realmente excluir o cliente selecionado?");
	        alert.setContentText(clienteEscolhido.toString());

	        Optional<ButtonType> result = alert.showAndWait();
	        if (result.isPresent() && result.get() == ButtonType.OK) {
	            System.out.println("Deletando cliente com ID: " + clienteEscolhido.getId());
	            clientes.deletarCliente(clienteEscolhido);
	            updateList();
	        } else {
	            System.out.println("Deleção cancelada ou janela fechada");
	        }

	    } else {
	        Alert alert = new Alert(Alert.AlertType.WARNING);
	        alert.setTitle("ATENÇÃO!!!!");
	        alert.setHeaderText("Nenhum cliente foi selecionado");
	        alert.showAndWait();
	    }
	}

	
	@FXML
	void btnEditarCliente(ActionEvent event) {
		
		ObservableList<Cliente> editar = LvClientes.getSelectionModel().getSelectedItems();
		 
		 	if(!editar.isEmpty()) {
		 		
		 		Cliente clienteEscolhido = editar.get(0);
		 		
		 		Alert alert = new Alert(AlertType.CONFIRMATION);
		 		alert.setTitle("Atenção");
		 		alert.setHeaderText("Deseja realmente editar o cliente selecionado?");
		 		alert.setContentText(clienteEscolhido.toString());
		 		
		 		Optional<ButtonType> result = alert.showAndWait();
			 		if(result.get()==ButtonType.OK) {
			 			nomeClienteInput.setText(clienteEscolhido.getNome());
			 			emailInput.setText(clienteEscolhido.getEmail());
			 			cpfInput.setText(clienteEscolhido.getCpf());
			 			telefoneInput.setText(clienteEscolhido.getTelefone());
						enderecoClienteInput.setText(clienteEscolhido.getTelefone());
						bairroClienteInput.setText(clienteEscolhido.getTelefone());
						cepClienteInput.setText(clienteEscolhido.getTelefone());
			 			this.id = clienteEscolhido.getId();
			 			System.out.println(this.id);
			 		}
		 		
		 	}else{

		 		Alert alert = new Alert(AlertType.CONFIRMATION);
		 		alert.setTitle("Atenção");
		 		alert.setHeaderText("Nenhum cliente foi selecionado");
		 		alert.showAndWait();
		 			 		
		 	}

	 }
	
	private void updateList() {
		this.clientes = new ClienteSQLite();
		LvClientes.getItems().clear();
		List<Cliente> listaClientes = this.clientes.all();
		listaClientes.stream().forEach(c->LvClientes.getItems().add(c));
		
	}
	


}
