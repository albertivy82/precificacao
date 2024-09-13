package org.openjfx.precificacao.database;

import org.openjfx.precificacao.dtos.AtividadeDTO;
import org.openjfx.precificacao.models.Atividade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AtividadeSQLite {

    // Método para adicionar uma nova atividade
    public void novaAtividade(Atividade atividade) throws SQLException {
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO atividades (atividade, etapa) VALUES (?, ?)");
            pstmt.setString(1, atividade.getAtividade());
            pstmt.setInt(2, atividade.getEtapa()); // Referência à etapa
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
    }

    // Método para editar uma atividade existente
    public void editarAtividade(Atividade atividade) {
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE atividade SET atividade=?, etapa=? WHERE id=?");
            pstmt.setString(1, atividade.getAtividade());
            pstmt.setInt(2, atividade.getEtapa());  // Atualiza a etapa associada
            pstmt.setInt(3, atividade.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
    }


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

    public List<Atividade> all() {
        List<Atividade> result = new ArrayList<>();
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("SELECT id, atividade, etapa FROM atividades");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Atividade at = new Atividade();
                at.setId(rs.getInt("id"));
                at.setAtividade(rs.getString("atividade"));
                at.setEtapa(rs.getInt("etapa"));

                result.add(at);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                SQLiteConnection.closeConnection(conn);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return result;
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

    public void deletarAtividde(int id) {
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM atividades WHERE ID=?");
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
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
    public List<AtividadeDTO> totalPorAtividade(int idProjeto) {
        List<AtividadeDTO> atividades = new ArrayList<>();
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet result = null;

        try {
            pstmt = conn.prepareStatement("SELECT d.id_atividade, a.atividade, SUM(d.horas) " +
                    "AS total_por_atividade FROM detalhamento INNER JOIN atividades a ON d.id_etapa = a.id " +
                    "WHERE d.id_projeto = ? GROUP BY d.id_atividade, a.atividade");
            pstmt.setInt(1, idProjeto);
            result = pstmt.executeQuery();

            while (result.next()) {
                AtividadeDTO atividade = new AtividadeDTO();
                atividade.setId(result.getInt("atividade"));
                atividade.setAtividade(result.getString("id_atividade"));
                atividade.setTotalPorAtiidadeProjeto(result.getFloat("total_por_atividade"));
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
