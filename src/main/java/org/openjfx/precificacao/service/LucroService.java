package org.openjfx.precificacao.service;

import org.openjfx.precificacao.database.ClienteSQLite;
import org.openjfx.precificacao.database.LucroSQLite;
import org.openjfx.precificacao.models.Lucro;

import java.sql.SQLException;
import java.util.List;

public class LucroService {

    private LucroSQLite lucro;

    public LucroService(){
        this.lucro = new LucroSQLite();
    }

    public void lancarLucro(Lucro lucro) throws SQLException {
        this.lucro.cadastroLucro(lucro);
    }

    public Float buscarLucro(int projetoId){
        return this.lucro.totalLucroPorProjeto(projetoId);
    }

    public void deletarLucroDoProjeto(int projetoId){
        this.lucro.deletarLucroPorProjeto(projetoId);
    }

    public double buscarMargemLucro(int projetoId){
        return this.lucro.margemLucroPorProjeto(projetoId);
    }



}
