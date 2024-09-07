package org.openjfx.precificacao.database;

import org.openjfx.precificacao.models.Atividade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AtividadeSQLite {

    // Método existente para listar atividades por etapa
    public List<Atividade> atividadePorEtapa(int idEtapa) {
        List<Atividade> etapas = new ArrayList<>();
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet result = null;

        try {
            pstmt = conn.prepareStatement("SELECT id, atividade FROM atividades WHERE etapa=?");
            pstmt.setInt(1, idEtapa);
            result = pstmt.executeQuery();

            while (result.next()) {
                Atividade et = new Atividade();
                et.setId(result.getInt("id"));
                et.setAtividade(result.getString("atividade"));
                etapas.add(et);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (result != null) result.close();
                if (pstmt != null) pstmt.close();
                SQLiteConnection.closeConnection(conn);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return etapas;
    }

    // Método para buscar o nome da atividade pelo ID
    public String buscaNomeAtividadePorId(int idAtividade) {
        String atividade = "";
        Connection conn = SQLiteConnection.connect();
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT atividade FROM atividades WHERE ID=?")) {
            pstmt.setInt(1, idAtividade);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    atividade = rs.getString("atividade");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
        return atividade;
    }

    // Método para buscar o ID da atividade pelo nome
    public int buscaIdAtividadePorNome(String atividade) {
        int idAtividade = 0;
        Connection conn = SQLiteConnection.connect();
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT id FROM atividades WHERE atividade =?")) {
            pstmt.setString(1, atividade);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    idAtividade = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
        return idAtividade;
    }

    // Método solicitado para buscar o total por atividade para um projeto específico
    public List<Atividade> totalPorAtividade(int idProjeto) {
        List<Atividade> atividades = new ArrayList<>();
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet result = null;

        try {
            pstmt = conn.prepareStatement("SELECT id_atividade, SUM(valor_hora * horas) AS total_por_atividade " +
                    "FROM detalhamento WHERE id_projeto = ? GROUP BY id_atividade");
            pstmt.setInt(1, idProjeto);
            result = pstmt.executeQuery();

            while (result.next()) {
                Atividade atividade = new Atividade();
                atividade.setId(result.getInt("id_atividade"));
               // atividade.setTotalPorAtividade(result.getFloat("total_por_atividade")); // Supondo que a classe Atividade tenha esse campo
                atividades.add(atividade);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (result != null) result.close();
                if (pstmt != null) pstmt.close();
                SQLiteConnection.closeConnection(conn);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return atividades;
    }

}
