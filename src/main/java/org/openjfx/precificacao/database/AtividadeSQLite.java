package org.openjfx.precificacao.database;

import org.openjfx.precificacao.models.Atividade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AtividadeSQLite {

    public List<Atividade> atividadePorEtapa(int idEtapa) {

        List<Atividade> etapas = new ArrayList<>();
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet result = null;

        try{
            pstmt = conn.prepareStatement("SELECT id, atividade FROM atividades WHERE etapa=?");
            pstmt.setInt(1, idEtapa);
            result = pstmt.executeQuery();

            while(result.next()){
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


    public String atividadePorNome(int idAtividade) {
        String nome = "";
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT nome FROM atividade WHERE ID=?");
            pstmt.setInt(1, idAtividade);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                nome = rs.getString("nome");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }

        return nome;
    }
}
