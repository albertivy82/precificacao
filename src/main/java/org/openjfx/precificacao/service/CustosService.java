package org.openjfx.precificacao.service;

import org.openjfx.precificacao.database.ClienteSQLite;
import org.openjfx.precificacao.database.CustosVariaveisSQLite;
import org.openjfx.precificacao.database.LancamentoCVSQLite;
import org.openjfx.precificacao.models.CustosVariaveis;
import org.openjfx.precificacao.models.LancamentoCV;
import org.openjfx.precificacao.models.Profissionais;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class CustosService {

    private CustosVariaveisSQLite custosvariaveisDB;
    private LancamentoCVSQLite lancamentosDB;




    public List<CustosVariaveis> listaCustos(){
        this.custosvariaveisDB = new CustosVariaveisSQLite();
        return this.custosvariaveisDB.all();
    }

    public void cadastrarLancamento(Set<LancamentoCV> lancamentos) throws SQLException {
        this.lancamentosDB = new LancamentoCVSQLite();
        this.lancamentosDB.CadastroLancamento(lancamentos);


    }

    public List<LancamentoCV> listagem() throws SQLException {
        this.lancamentosDB = new LancamentoCVSQLite();
        return this.lancamentosDB.all();

    }

}
