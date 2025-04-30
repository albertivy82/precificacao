package org.openjfx.precificacao.service;

import org.openjfx.precificacao.database.ClienteSQLite;
import org.openjfx.precificacao.models.Cliente;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ClienteService {

    private ClienteSQLite clientes;

    public ClienteService(){
        this.clientes = new ClienteSQLite();
    }
    public List<String> clientesPorNome(){

        List<String> clientes = this.clientes.nomesClientes();

        return clientes;
    }

    public int idCliente(String nome){

        int idCliente =  this.clientes.clientePorNome(nome);

        return idCliente;
    }

    public String nomeCliente(int id){


       String nomeCleinte =  this.clientes.buscarNomeClientePorId(id);

        return nomeCleinte;
    }

    public void gerarCodCliente(Cliente cliente){

        int id;

            if (cliente.getCpf() != null && !cliente.getCpf().trim().isEmpty()) {
                id = this.clientes.clientePorCpf(cliente.getCpf());
            } else {
                id = this.clientes.clientePorUuid(cliente.getUuid()); // <- precisamos criar esse método
            }

            if (id == 0) {
                System.out.println("Erro: Não foi possível localizar o cliente para gerar código.");
                return;
            }

            LocalDate hoje = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMyyyy");
            String dataFormatada = hoje.format(formatter);
            String cod = String.format("%d%s", id, dataFormatada);
            this.clientes.gerarCodCliente(id, cod);

    }

    public String gerarUuid() {
        return java.util.UUID.randomUUID().toString();
    }


    public void deletarCliente(Cliente clienteEscolhido) throws SQLException {
        ProjetoService projetosRelaciodados = new ProjetoService();
        List<Integer> idProjetosRelacaionados = projetosRelaciodados.idsProejoPorCliente(clienteEscolhido.getId());
        this.clientes.deletarCliente(clienteEscolhido);
        for(Integer item: idProjetosRelacaionados){
            projetosRelaciodados.deletaProjetoEDependencias(item);
        }
    }

    private void apagarProjetosAssociados(int idCliemte) throws SQLException {
        ProjetoService projetoService = new ProjetoService();
        List<Integer> idsProjetos = projetoService.idsProejoPorCliente(idCliemte);
        for (int projetoId : idsProjetos) {
            projetoService.deletaProjetoEDependencias(projetoId);
        }

    }

    public List<Cliente> listarClientes(){
       return this.clientes.all();
    }


    public Cliente clientePorId(int id){
        return this.clientes.buscarClientePorId(id);
    }

    public String possuiUuid(int id) {
        Cliente cliente = clientePorId(id);
        return cliente.getUuid();
    }


    public int cpfRepetido(String cpf) {
        return this.clientes.clientePorCpf(cpf);

    }





}
