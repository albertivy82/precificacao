package org.openjfx.precificacao.database;

import org.openjfx.precificacao.models.Lucro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LucroSQLite {


    public void cadastroLucro(Lucro lucro) throws SQLException {
        String sql = "INSERT INTO quota_sobre_despesas (id_projeto, margem_lucro, lucro) " +
                "VALUES (?, ?, ?) " +
                "ON CONFLICT(id_projeto) DO UPDATE SET " +
                "margem_lucro = excluded.margem_lucro, " +
                "lucro = excluded.lucro;";

        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, lucro.getIdProjeto());
            pstmt.setDouble(2, lucro.getMargemLucro());
            pstmt.setDouble(3, lucro.getLucro());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }




    public List<Lucro> all() {
        List<Lucro> result = new ArrayList<>();
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            if (conn != null) {
                pstmt = conn.prepareStatement("SELECT id, id_projeto, margem_lucro, lucro FROM quota_sobre_despesas");
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    Lucro lucro = new Lucro();
                    lucro.setId(rs.getInt("id"));
                    lucro.setIdProjeto(rs.getInt("id_projeto"));
                    lucro.setMargemLucro(rs.getDouble("margem_lucro"));
                    lucro.setLucro(rs.getDouble("lucro"));
                    result.add(lucro);
                }
            }  else {
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


    public Float totalLucroPorProjeto(int idProjeto) {
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet result = null;
        Float totalLucro = 0.0f;

        try {
            pstmt = conn.prepareStatement("SELECT lucro AS total_lucro FROM quota_sobre_despesas WHERE id_projeto = ?");
            pstmt.setInt(1, idProjeto);
            result = pstmt.executeQuery();
            if (result.next()) {
                totalLucro = result.getFloat("total_lucro");
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
        return totalLucro;
    }

    public double margemLucroPorProjeto(int idProjeto) {
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet result = null;
        double margemLucro = 0.00f;

        try {
            pstmt = conn.prepareStatement("SELECT margem_lucro AS margem_lucro FROM quota_sobre_despesas WHERE id_projeto = ?");
            pstmt.setInt(1, idProjeto);
            result = pstmt.executeQuery();
            if (result.next()) {
                margemLucro = result.getFloat("margem_lucro");
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
        return margemLucro;
    }

    public void deletarLucroPorProjeto(int idProjeto) {
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM quota_sobre_despesas WHERE id_projeto=?");
            pstmt.setInt(1, idProjeto);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
    }
}
