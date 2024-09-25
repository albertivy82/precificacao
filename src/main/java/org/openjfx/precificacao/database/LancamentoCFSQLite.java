package org.openjfx.precificacao.database;

import org.openjfx.precificacao.models.LancamentoCF;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LancamentoCFSQLite {



    public void cadastroLancamento(LancamentoCF lancamento) throws SQLException {
        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO lancamento_cf (id_projeto, desconto, data) " +
                             "VALUES (?, ?, ?) " +
                             "ON CONFLICT(id_projeto) DO UPDATE " +
                             "SET desconto = excluded.desconto, data = excluded.data;")) {

            pstmt.setInt(1, lancamento.getIdProjeto());
            pstmt.setDouble(2, lancamento.getDesconto());
            pstmt.setString(3, lancamento.getData().toString()); // Usando LocalDate.toString()

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void deletarLancamentoCF(LancamentoCF lancamento) {
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM lancamento_cf WHERE id=?");
            pstmt.setInt(1, lancamento.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
    }


    public List<LancamentoCF> all() {
        List<LancamentoCF> result = new ArrayList<>();
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            if (conn != null) {
                pstmt = conn.prepareStatement("SELECT id, id_projeto, desconto, data FROM lancamento_cf");
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    LancamentoCF lcf = new LancamentoCF();
                    lcf.setId(rs.getInt("id"));
                    lcf.setIdProjeto(rs.getInt("id_projeto"));
                    lcf.setDesconto(rs.getFloat("desconto"));
                    lcf.setData(LocalDate.parse(rs.getString("data")));
                    result.add(lcf);
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

    public Float lancamentoCFPorProjeto(int idProjeto) {
        float lancamentoProjeto = 0.0f;
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement("SELECT id, id_projeto, desconto, data FROM lancamento_cf WHERE id_projeto=?");
            pstmt.setInt(1, idProjeto);
            rs = pstmt.executeQuery();

            while (rs.next()) {
               lancamentoProjeto = rs.getFloat("desconto");
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
        return lancamentoProjeto;
    }

    public Float totalDescontoPorProjeto(int idProjeto) {
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet result = null;
        Float totalDesconto = null;

        try {
            pstmt = conn.prepareStatement("SELECT desconto AS total_descontos FROM lancamento_cf WHERE id_projeto = ?");
            pstmt.setInt(1, idProjeto);
            result = pstmt.executeQuery();
            if (result.next()) {
                totalDesconto = result.getFloat("total_descontos");
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
        return totalDesconto;
    }

    public Float totalDesconto() {
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet result = null;
        Float totalDesconto = null;

        try {
            pstmt = conn.prepareStatement("SELECT SUM(desconto) AS total_descontos FROM lancamento_cf");
            result = pstmt.executeQuery();
            if (result.next()) {
                totalDesconto = result.getFloat("total_descontos");
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
        return totalDesconto;
    }


}
