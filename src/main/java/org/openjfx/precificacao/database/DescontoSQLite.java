package org.openjfx.precificacao.database;

import org.openjfx.precificacao.models.Desconto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DescontoSQLite {


    public void cadastroDesconto(Desconto Desconto) throws SQLException {
        String sql = "INSERT INTO descontos (id_projeto, margem_desconto, desconto) " +
                "VALUES (?, ?, ?) " +
                "ON CONFLICT(id_projeto) DO UPDATE SET " +
                "margem_desconto = excluded.margem_desconto, " +
                "desconto = excluded.desconto;";

        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Desconto.getIdProjeto());
            pstmt.setDouble(2, Desconto.getMargemDesconto());
            pstmt.setDouble(3, Desconto.getDesconto());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }




    public List<Desconto> all() {
        List<Desconto> result = new ArrayList<>();
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            if (conn != null) {
                pstmt = conn.prepareStatement("SELECT id, id_projeto, margem_desconto, desconto FROM descontos");
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    Desconto Desconto = new Desconto();
                    Desconto.setId(rs.getInt("id"));
                    Desconto.setIdProjeto(rs.getInt("id_projeto"));
                    Desconto.setMargemDesconto(rs.getDouble("margem_desconto"));
                    Desconto.setDesconto(rs.getDouble("desconto"));
                    result.add(Desconto);
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


    public double totalDescontoPorProjeto(int idProjeto) {
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet result = null;
        double totalDesconto = 0.00f;

        try {
            pstmt = conn.prepareStatement("SELECT desconto AS total_desconto FROM descontos WHERE id_projeto = ?");
            pstmt.setInt(1, idProjeto);
            result = pstmt.executeQuery();
            if (result.next()) {
                totalDesconto = result.getDouble("total_desconto");
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

    public double margemDescontoPorProjeto(int idProjeto) {
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet result = null;
        double margemDesconto = 0.00f;

        try {
            pstmt = conn.prepareStatement("SELECT margem_desconto AS margem_desconto FROM descontos WHERE id_projeto = ?");
            pstmt.setInt(1, idProjeto);
            result = pstmt.executeQuery();
            if (result.next()) {
                margemDesconto = result.getFloat("margem_desconto");
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

        return margemDesconto;
    }

    public void deletarDescontoPorProjeto(int idProjeto) {
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM descontos WHERE id_projeto=?");
            pstmt.setInt(1, idProjeto);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
    }
}
