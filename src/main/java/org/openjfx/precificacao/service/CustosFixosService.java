package org.openjfx.precificacao.service;

import org.openjfx.precificacao.database.CustosFixosSQLite;
import org.openjfx.precificacao.database.LancamentoCFSQLite;
import org.openjfx.precificacao.models.LancamentoCF;

import java.sql.SQLException;

public class CustosFixosService {

    private LancamentoCFSQLite lancamentoCF;
    private CustosFixosSQLite custosFixos;

    public CustosFixosService(){
        this.lancamentoCF = new LancamentoCFSQLite();
        this.custosFixos = new CustosFixosSQLite();
    }


    public Float totalCustosFixos(){
        return this.custosFixos.totalCustosFixos();
    }

    public Float TotalDeLancamentos(){
        return this.lancamentoCF.totalDesconto();
    }

    public void lancarCusto(LancamentoCF lancamento) throws SQLException {
        this.lancamentoCF.cadastroLancamento(lancamento);
    }


}
