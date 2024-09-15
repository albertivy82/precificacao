package org.openjfx.precificacao.service;

import org.openjfx.precificacao.database.ClienteSQLite;
import org.openjfx.precificacao.database.CustosVariaveisSQLite;
import org.openjfx.precificacao.models.CustosVariaveis;
import org.openjfx.precificacao.models.Profissionais;

import java.util.List;

public class CustosService {

    private CustosVariaveisSQLite custosvariaveisDB;


    public CustosService(){
        this.custosvariaveisDB = new CustosVariaveisSQLite();
    }


    public List<CustosVariaveis> listaCustos(){
        return this.custosvariaveisDB.all();
    }



}
