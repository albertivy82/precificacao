package org.openjfx.precificacao.service;

import org.openjfx.precificacao.database.EtapaSQLite;
import org.openjfx.precificacao.models.Etapa;

import java.util.List;

public class ProjetoService {

    private EtapaSQLite etapas;

    public List<Etapa> listaEtapas(){


        this.etapas = new EtapaSQLite();
        List<Etapa> etapasBanco = this.etapas.all();

        return etapasBanco;

    }
}
