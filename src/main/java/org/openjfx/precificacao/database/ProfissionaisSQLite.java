package org.openjfx.precificacao.database;

import org.openjfx.precificacao.models.Cliente;
import org.openjfx.precificacao.models.Profissionais;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProfissionaisSQLite {

    public void novoProfissional (Profissionais profissional) throws SQLException {

        Connection conn = SQLiteConnection.connect();

        try{

            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO profissionais (nome, profissional, valor_Hora ) VALUES (?, ?, ?)");
            pstmt.setString(1, profissional.getNome());
            pstmt.setString(2, profissional.getProfissional());
            pstmt.setFloat(3, profissional.getValorHora());

            pstmt.executeUpdate();

        }catch (SQLException e){
            System.out.println(e.getMessage());
        } finally {
        SQLiteConnection.closeConnection(conn);
        }
    }

    public void editarProfissionais(Profissionais profissional) {
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE profissionais SET nome=?, profissional=?, valor_hora=? WHERE ID=?");

            pstmt.setString(1, profissional.getNome());
            pstmt.setString(2, profissional.getProfissional());
            pstmt.setFloat(3, profissional.getValorHora());
            pstmt.setInt(3, profissional.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
    }


    public void deletarProfissional(Profissionais profissional) {
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM profissionais WHERE ID=?");
            pstmt.setInt(1, profissional.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
    }



    public List<Profissionais> all() {
        List<Profissionais> result = new ArrayList<>();
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("SELECT id, nome, profissional, valor_hora FROM profissionais");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Profissionais p = new Profissionais();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setProfissional(rs.getString("profissional"));
                p.setValorHora(rs.getFloat("valor_hora"));
                result.add(p);
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
}
