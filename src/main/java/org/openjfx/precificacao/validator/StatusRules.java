package org.openjfx.precificacao.validator;

import org.openjfx.precificacao.database.ProjetoSQLite;
import org.openjfx.precificacao.enums.StatusProjeto;
import org.openjfx.precificacao.models.Projeto;

import java.sql.SQLException;
import java.util.*;

public class StatusRules {

    private final ProjetoSQLite projetosBnaco = new ProjetoSQLite();

    private static final Map<StatusProjeto, Set<StatusProjeto>> transicoesValidas = Map.of(
            StatusProjeto.CADASTRADO, Set.of(StatusProjeto.ORCADO),
            StatusProjeto.ORCADO, Set.of(StatusProjeto.PRECIFICADO),
            StatusProjeto.PRECIFICADO, Set.of(StatusProjeto.INICIADO, StatusProjeto.ORCADO),
            StatusProjeto.INICIADO, Set.of(StatusProjeto.EXECUTADO)
    );

    public void alterarStatus(int idProjeto, StatusProjeto novoStatus) throws SQLException {
        Projeto projeto = this.projetosBnaco.all().stream()
                .filter(p -> p.getId() == idProjeto)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Projeto não encontrado"));

        StatusProjeto statusAtual = StatusProjeto.valueOf(projeto.getStatus());

        if (!transicoesValidas.containsKey(statusAtual) ||
                !transicoesValidas.get(statusAtual).contains(novoStatus)) {
            throw new IllegalStateException(
                    "Transição de status inválida: " + statusAtual + " → " + novoStatus
            );
        }

        this.projetosBnaco.editarSatusProjeto(novoStatus.name(), idProjeto);
    }
}
