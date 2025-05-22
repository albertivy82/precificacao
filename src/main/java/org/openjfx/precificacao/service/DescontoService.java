package org.openjfx.precificacao.service;

import org.openjfx.precificacao.database.DescontoSQLite;
import org.openjfx.precificacao.models.Desconto;

import java.sql.SQLException;

public class DescontoService {

    private DescontoSQLite desconto;

    public DescontoService(){
        this.desconto = new DescontoSQLite();
    }

    public void lancardesconto(Desconto desconto) throws SQLException {
        this.desconto.cadastroDesconto(desconto);
    }

    public double buscardesconto(int projetoId){
        return this.desconto.totalDescontoPorProjeto(projetoId);
    }

    public void deletardescontoDoProjeto(int projetoId){
        this.desconto.deletarDescontoPorProjeto(projetoId);
    }

    public double buscarMargemDesconto(int projetoId){
        return this.desconto.margemDescontoPorProjeto(projetoId);
    }





}
