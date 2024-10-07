package org.openjfx.precificacao.database;

import org.openjfx.precificacao.dtos.ProfissionalDTO;
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
            pstmt.setInt(4, profissional.getId());
            System.out.println(pstmt.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
    }


    public void deletarProfissional(int id) {
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM profissionais WHERE ID=?");
            pstmt.setInt(1, id);
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


    public String profissionalPorNome(int idProfissional) {
        String nome = "";
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT nome FROM profissionais WHERE ID=?");
            pstmt.setInt(1, idProfissional);
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

    public int buscaIdProfissionalPorNome(String nome) {
        int idProfissional = 0;
        Connection conn = SQLiteConnection.connect();
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT id FROM profissionais WHERE nome =?")) {
            pstmt.setString(1, nome);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    idProfissional = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
        return idProfissional;
    }


    public List<ProfissionalDTO> totalPorProfissional(int idProjeto) {
        List<ProfissionalDTO> profissionais = new ArrayList<>();
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet result = null;

        try {
            pstmt = conn.prepareStatement("SELECT d.id_profissional, p.nome, SUM(d.valor_hora * d.horas) AS total_por_profissional" +
                    "FROM detalhamento d" +
                    "INNER JOIN profissionais p ON d.id_profissional = p.id" +
                    "WHERE d.id_projeto = ?" +
                    "GROUP BY d.id_etapa, p.nome;");
            pstmt.setInt(1, idProjeto);
            result = pstmt.executeQuery();

            while (result.next()) {
                ProfissionalDTO profissional = new ProfissionalDTO();
                profissional.setId(result.getInt("id_profissional"));
                profissional.setNome(result.getString("nome"));
                profissional.setTotalPorProfissionalProjeto(result.getFloat("total_por_profissional"));
                profissionais.add(profissional);
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
        return profissionais;
    }
}
