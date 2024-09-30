package org.openjfx.precificacao.database;

import org.openjfx.precificacao.models.Estado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstadoSQLite {


   public List<Estado> all() {
        List<Estado> result = new ArrayList<>();
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            if (conn != null) {
                pstmt = conn.prepareStatement("SELECT id, estado FROM estados");
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    Estado estado = new Estado();
                    estado.setId(rs.getInt("id"));
                    estado.setEstado(rs.getString("estado"));
                    result.add(estado);
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
