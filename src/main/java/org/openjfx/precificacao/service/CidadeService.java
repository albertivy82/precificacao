package org.openjfx.precificacao.service;

import org.openjfx.precificacao.database.CidadeSQLite;
import org.openjfx.precificacao.models.Cidade;

import java.util.List;

public class CidadeService {

    private CidadeSQLite cidadeSQLite;

    public CidadeService() {
        this.cidadeSQLite = new CidadeSQLite();
    }

    public List<Cidade> buscarTodasCidades() {
        return this.cidadeSQLite.all();
    }

    public List<Cidade> buscarCidadesPorEstado(int estadoId) {
        return this.cidadeSQLite.cidadePorEstado(estadoId);
    }

}
