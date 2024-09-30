package org.openjfx.precificacao.shared;

import org.openjfx.precificacao.models.Cliente;

public class ClienteSingleton {

    private static ClienteSingleton instance;
    private Cliente cliente;

    private ClienteSingleton(){}

    public static synchronized ClienteSingleton getInstance(){
        if(instance ==null){
            instance = new ClienteSingleton();
        }
        return instance;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void clearCliente() {
        this.cliente = null;
    }
}
