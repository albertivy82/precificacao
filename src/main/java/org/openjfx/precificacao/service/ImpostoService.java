package org.openjfx.precificacao.service;

import org.openjfx.precificacao.database.ImpostosSQLite;
import org.openjfx.precificacao.models.Impostos;

import java.sql.SQLException;

public class ImpostoService {

    private ImpostosSQLite impostos;


   public ImpostoService(){
       this.impostos = new ImpostosSQLite();
   }

public void lancarImpostos(Impostos novosImposto ) throws SQLException {
       this.impostos.cadastroIpostos(novosImposto);
}

public Float buscarImpostos(int projetoId){
    return this.impostos.totalImpostosPorProjeto(projetoId);
}

}
