package org.openjfx.precificacao.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.util.StringConverter;
import org.openjfx.precificacao.App;
import org.openjfx.precificacao.database.ClienteSQLite;
import org.openjfx.precificacao.models.Cidade;
import org.openjfx.precificacao.models.Cliente;
import org.openjfx.precificacao.models.Estado;
import org.openjfx.precificacao.service.CidadeService;
import org.openjfx.precificacao.service.ClienteService;
import org.openjfx.precificacao.service.EstadoService;
import org.openjfx.precificacao.shared.CPFMaskedTextField;
import org.openjfx.precificacao.shared.CPFValidator;
import org.openjfx.precificacao.shared.ClienteSingleton;
import org.openjfx.precificacao.shared.TelefoneMaskedTextField;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public class ClienteController {
	 
	private ClienteSQLite clientes;
	private ClienteService clienteService;
	private EstadoService estadoService;
	private CidadeService cidadeService;
	
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
	private ComboBox<Estado> estadoClienteCB;

	@FXML
	private ComboBox<Cidade> cidadeClienteCB;

	@FXML
	protected void btnImpostos(ActionEvent e) {
		App.mudarTela("Impostos");
	}
		
	@FXML
	void initialize() {
		updateList();
		this.clienteService = new ClienteService();
		this.estadoService = new EstadoService();
		this.cidadeService = new CidadeService();
		estadoClienteCB();
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
				// Verifica se um estado foi selecionado antes de atribuir
				Estado estadoSelecionado = estadoClienteCB.getSelectionModel().getSelectedItem();
				if (estadoSelecionado != null) {
					novoCliente.setEstado(estadoSelecionado.getEstado());
				} else {
					novoCliente.setEstado(null); // Se não selecionado, o valor será nulo
				}

				// Verifica se uma cidade foi selecionada antes de atribuir
				Cidade cidadeSelecionada = cidadeClienteCB.getSelectionModel().getSelectedItem();
				if (cidadeSelecionada != null) {
					novoCliente.setCidade(cidadeSelecionada.getCidade());
				} else {
					novoCliente.setCidade(null); // Se não selecionado, o valor será nulo
				}			novoCliente.setCep(cepClienteInput.getText());
	        try {
	            this.clientes = new ClienteSQLite();
		            if(this.id==-1) {
		            clientes.novoCliente(novoCliente);
		            }else {
		            novoCliente.setId(this.id);	
		           	clientes.editarCliente(novoCliente);
		            }
				this.clienteService.gerarCodCliente(cpfInput.getText());
	            nomeClienteInput.clear();
	            emailInput.clear();
	            cpfInput.clear();
	            telefoneInput.clear();
				enderecoClienteInput.clear();
				bairroClienteInput.clear();
				estadoClienteCB.getSelectionModel().clearSelection();
				cidadeClienteCB.getSelectionModel().clearSelection();
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
		if (!emailInput.getText().trim().isEmpty() && !emailInput.getText().matches(emailRegex)) {
			showAlert("E-mail Inválido", "O campo de e-mail não está no formato correto.");
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

		 return valid;
	}

	
	@FXML
	void btnDeletarCliente(ActionEvent event) throws SQLException {
	    this.clientes = new ClienteSQLite();
	    ObservableList<Cliente> escolhido = LvClientes.getSelectionModel().getSelectedItems();

	    if (!escolhido.isEmpty()) {
	        Cliente clienteEscolhido = escolhido.get(0);

	        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	        alert.setTitle("ATENÇÃO");
	        alert.setHeaderText("Deseja realmente excluir o cliente selecionado?\nTodos os projetos do cliente serão apagados também.");
	        alert.setContentText(clienteEscolhido.toString());

	        Optional<ButtonType> result = alert.showAndWait();
	        if (result.isPresent() && result.get() == ButtonType.OK) {
	           this.clienteService.deletarCliente(clienteEscolhido);
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
						enderecoClienteInput.setText(clienteEscolhido.getEndereco());
						bairroClienteInput.setText(clienteEscolhido.getBairro());
						cepClienteInput.setText(clienteEscolhido.getCep());
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


	@FXML
	protected void btnDetalhes(ActionEvent e) {

		ObservableList<Cliente> clienteClickado = LvClientes.getSelectionModel().getSelectedItems();

		if(!clienteClickado.isEmpty()) {

			Cliente clienteSelecionado = clienteClickado.get(0);

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Atenção");
			alert.setHeaderText("Deseja ver mais detalhes do cliente selecionado?");
			alert.setContentText(clienteSelecionado.toString());

			Optional<ButtonType> result = alert.showAndWait();
			if(result.get()==ButtonType.OK) {
				   ClienteSingleton clienteSingleton = ClienteSingleton.getInstance();
				   clienteSingleton.setCliente(clienteSelecionado);
					App.mudarTela("ClientesDetalhes");

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


	@FXML
	private void estadoClienteCB(){

		List<Estado> estadosList = this.estadoService.buscarTodosEstados();
		ObservableList<Estado> estados = FXCollections.observableArrayList(estadosList);
		estadoClienteCB.setItems(estados);

		// Ajustando o StringConverter para evitar problemas de null
		estadoClienteCB.setConverter(new StringConverter<Estado>() {
			@Override
			public String toString(Estado object) {
				// Verifique se o object não é nulo antes de acessá-lo
				return (object != null && object.getEstado() != null) ? object.getEstado() : "Selecione o estado";
			}

			@Override
			public Estado fromString(String string) {
				// Não precisamos implementar conversão reversa no seu caso
				return null;
			}
		});

		estadoClienteCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				List<Cidade> cidade = this.cidadeService.buscarCidadesPorEstado(newValue.getId());
				ObservableList<Cidade> cidades = FXCollections.observableArrayList(cidade);
				cidadeClienteCB.setItems(cidades);
				cidadeClienteCB.setConverter(new StringConverter<Cidade>() {
					@Override
					public String toString(Cidade object) {
						return (object != null && object.getCidade() != null) ? object.getCidade() : "Selecione o cidade";
					}

					@Override
					public Cidade fromString(String string) {
						// Não precisamos implementar conversão reversa no seu caso
						return null;
					}
				});

			}
		});
	}




}
