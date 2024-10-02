package org.openjfx.precificacao.database;

import org.openjfx.precificacao.models.LancamentoCV;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LancamentoCVSQLite {

    public void CadastroLancamento(Set<LancamentoCV> lancamentos) throws SQLException {

        Connection conn = SQLiteConnection.connect();

        try {
                for(LancamentoCV item : lancamentos) {
                    PreparedStatement pstmt = conn.prepareStatement(
                            "INSERT INTO lancamento_cv (id_projeto, id_custo_variavel, nome_custo, valor_unitario, quantidade, obs) VALUES (?, ?, ?, ?, ?, ?)");
                    pstmt.setInt(1, item.getIdProjeto());
                    pstmt.setInt(2, item.getIdCustoVariavel());
                    pstmt.setString(3, item.getNomeCusto());
                    pstmt.setFloat(4, item.getValorUnitario());
                    pstmt.setFloat(5, item.getQuantidade());
                    pstmt.setString(6, item.getObs());
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                SQLiteConnection.closeConnection(conn);
            }
    }

    public List<LancamentoCV> all() {
        List<LancamentoCV> result = new ArrayList<>();
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            if (conn != null) {
                pstmt = conn.prepareStatement("SELECT id, id_projeto, id_custo_variavel, nome_custo, valor_unitario, quantidade, obs FROM lancamento_cv");
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    LancamentoCV lcv = new LancamentoCV();
                    lcv.setId(rs.getInt("id"));
                    lcv.setIdProjeto(rs.getInt("id_projeto"));
                    lcv.setIdCustoVariavel(rs.getInt("id_custo_variavel"));
                    lcv.setNomeCusto(rs.getString("nome_custo"));
                    lcv.setValorUnitario(rs.getFloat("valor_unitario"));
                    lcv.setQuantidade(rs.getFloat("quantidade"));
                    lcv.setObs(rs.getString("obs"));
                    result.add(lcv);
                }
            } else {
                System.out.println("A conexão com o banco de dados falhou.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao executar consulta: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                SQLiteConnection.closeConnection(conn);
            } catch (SQLException ex) {
                System.out.println("Erro ao fechar recursos: " + ex.getMessage());
            }
        }
        return result;
    }


    public List<LancamentoCV> lancamentoPorProjeto(int idProjeto) {
        List<LancamentoCV> result = new ArrayList<>();
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("SELECT id, id_projeto, id_custo_variavel, valor_unitario, quantidade, obs FROM lancamento_cv" +
                    " WHERE id_projeto=?");
            pstmt.setInt(1, idProjeto);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                LancamentoCV  lcv = new LancamentoCV();
                lcv.setId(rs.getInt("id"));
                lcv.setIdProjeto(rs.getInt("id_projeto"));
                lcv.setIdCustoVariavel(rs.getInt("id_custo_variavel"));
                lcv.setNomeCusto(rs.getString("nome_custo"));
                lcv.setValorUnitario(rs.getFloat("valor_unitario"));
                lcv.setQuantidade(rs.getFloat("quantidade"));
                lcv.setObs(rs.getNString("obs"));
                result.add(lcv);
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

    public Float totalCVPorProjeto(int idProjeto) {

        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet result = null;
        Float totalProjeto = 0.0f;

        try {
            pstmt = conn.prepareStatement("SELECT SUM(valor_unitario*quantidade) AS total_lancamentos FROM lancamento_cv WHERE id_projeto = ?");
            pstmt.setInt(1, idProjeto);
            result = pstmt.executeQuery();
           if (result.next()) {
                totalProjeto = result.getFloat("total_lancamentos");
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

        return totalProjeto;
    }


    public void deletarLancamentoCV(LancamentoCV lancamento) {
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM lancamento_cv WHERE ID=?");
            pstmt.setInt(1, lancamento.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
    }

    public void deletarLancamentoCVPorProjeto(int idProjeto) {
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM lancamento_cv WHERE id_projeto=?");
            pstmt.setInt(1, idProjeto);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
    }







}
