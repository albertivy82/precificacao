package org.openjfx.precificacao.database;

import org.openjfx.precificacao.models.Cidade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CidadeSQLite {


    public List<Cidade> all() {
        List<Cidade> result = new ArrayList<>();
        String sql = "SELECT id, cidade, id_estado FROM municipios";

        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Cidade cidade = new Cidade();
                cidade.setId(rs.getInt("id"));
                cidade.setCidade(rs.getString("cidade"));
                cidade.setIdEstado(rs.getInt("id_estado"));
                result.add(cidade);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao executar consulta: " + e.getMessage());
        }

        return result;
    }


    public List<Cidade> cidadePorEstado(int idEstado) {
        List<Cidade> result = new ArrayList<>();
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            if (conn != null) {
                pstmt = conn.prepareStatement("SELECT id, cidade, id_estado FROM cidades WHERE id_estado = ?");
                pstmt.setInt(1, idEstado); // Define o parâmetro idEstado no PreparedStatement
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    Cidade cidade = new Cidade();
                    cidade.setId(rs.getInt("id"));
                    cidade.setCidade(rs.getString("cidade"));
                    cidade.setIdEstado(rs.getInt("id_estado"));
                    result.add(cidade);
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
