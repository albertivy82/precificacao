package org.openjfx.precificacao.database;

import org.openjfx.precificacao.models.BaseImpostos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseImpostosSQLite {


    public void cadastroImpostos(BaseImpostos base) throws SQLException {
        String sql = "INSERT INTO base_impostos (id, iss, simples_nac) " +
                "VALUES (?, ?, ?) " +
                "ON CONFLICT(id) DO UPDATE SET iss = excluded.iss, simples_nac = excluded.simples_nac";

        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // O ID será sempre 1 para garantir que só exista uma tupla na tabela
            pstmt.setInt(1, 1);  // ID fixo, usado tanto para inserção quanto para atualização
            pstmt.setDouble(2, base.getIss());
            pstmt.setDouble(3, base.getSimplesNac());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    public BaseImpostos Impostos() {
        BaseImpostos result = new BaseImpostos();
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            if (conn != null) {
                pstmt = conn.prepareStatement("SELECT id, iss, simples_nac FROM base_impostos");
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    result.setId(rs.getInt("id"));
                    result.setIss(rs.getDouble("iss"));
                    result.setSimplesNac(rs.getDouble("simples_nac"));
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



}
