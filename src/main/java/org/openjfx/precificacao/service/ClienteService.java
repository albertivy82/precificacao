package org.openjfx.precificacao.service;

import org.openjfx.precificacao.database.ClienteSQLite;

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


       String nomeCleinte =  this.clientes.buscarClientePorId(id);

        return nomeCleinte;
    }

    public void gerarCodCliente(String cpf){

             int id =  this.clientes.clientePorCpf(cpf);
            LocalDate hoje = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMyyyy");
            String dataFormatada = hoje.format(formatter);
            String cod = String.format("%d%s", id, dataFormatada);
            this.clientes.gerarCodCliente(id, cod);

    }



}
