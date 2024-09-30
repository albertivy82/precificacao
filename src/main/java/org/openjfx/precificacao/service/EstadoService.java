package org.openjfx.precificacao.service;


import org.openjfx.precificacao.database.EstadoSQLite;
import org.openjfx.precificacao.models.Estado;

import java.util.List;

public class EstadoService {

    private EstadoSQLite estadoSQLite;

    public EstadoService() {
        this.estadoSQLite = new EstadoSQLite();
    }

    public List<Estado> buscarTodosEstados() {
        return this.estadoSQLite.all();
    }

}
