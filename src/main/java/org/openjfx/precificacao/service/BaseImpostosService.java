package org.openjfx.precificacao.service;

import org.openjfx.precificacao.database.BaseImpostosSQLite;
import org.openjfx.precificacao.models.BaseImpostos;

import java.sql.SQLException;

public class BaseImpostosService {

    private BaseImpostosSQLite base;

    public BaseImpostosService(){
        this.base = new BaseImpostosSQLite();
    }

    public void cadImpostos(BaseImpostos impostos) throws SQLException {
        this.base.cadastroImpostos(impostos);
    }

    public BaseImpostos buscarBaseImpostos(){
        return this.base.Impostos();
    }


}
