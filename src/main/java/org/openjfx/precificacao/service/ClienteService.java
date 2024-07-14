package org.openjfx.precificacao.service;

import org.openjfx.precificacao.database.ClienteSQLite;
import org.openjfx.precificacao.models.Cliente;

import java.util.List;

public class ClienteService {

    private ClienteSQLite clientes;


    public List<String> clientesPorNome(){
        this.clientes = new ClienteSQLite();

        List<String> clientes = this.clientes.nomesClientes();

        return clientes;
    }

}
