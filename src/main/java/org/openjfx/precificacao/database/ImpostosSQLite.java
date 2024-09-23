package org.openjfx.precificacao.database;

import org.openjfx.precificacao.models.Impostos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImpostosSQLite {


    public void cadastroIpostos(Impostos impostos) throws SQLException {
        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO impostos_projeto (id_projeto, iss, simples_nac) " +
                             "VALUES (?, ?, ?) " +
                             "ON CONFLICT(id_projeto) DO UPDATE " +
                             "SET iss = excluded.iss, simples_nac = excluded.simples_nac;")) {

            pstmt.setInt(1, impostos.getIdProjeto());
            pstmt.setDouble(2, impostos.getIss());
            pstmt.setDouble(3, impostos.getSimplesNac());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    public List<Impostos> all() {
        List<Impostos> result = new ArrayList<>();
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            if (conn != null) {
                pstmt = conn.prepareStatement("SELECT id, id_projeto, iss, simples_nac FROM impostos_projeto");
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    Impostos impostos = new Impostos();
                    impostos.setId(rs.getInt("id"));
                    impostos.setIdProjeto(rs.getInt("id_projeto"));
                    impostos.setIss(rs.getFloat("iss"));
                    impostos.setSimplesNac(rs.getFloat("simples_nac"));
                    result.add(impostos);
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


    public Float totalImpostosPorProjeto(int idProjeto) {
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet result = null;
        Float totalImpostos = null;

        try {
            pstmt = conn.prepareStatement("SELECT (iss + simples_nac) AS total_impostos FROM impostos_projeto WHERE id_projeto = ?");
            pstmt.setInt(1, idProjeto);
            result = pstmt.executeQuery();
            if (result.next()) {
                totalImpostos = result.getFloat("total_impostos");
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
        return totalImpostos;
    }


}
