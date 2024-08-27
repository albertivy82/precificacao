package org.openjfx.precificacao.shared;

import org.openjfx.precificacao.models.Projeto;

public class ProjetoSingleton {

    private static ProjetoSingleton instance;
    private Projeto projeto;

    private ProjetoSingleton(){}

    public static synchronized ProjetoSingleton getInstance(){
        if(instance ==null){
            instance = new ProjetoSingleton();
        }
        return instance;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }
}
