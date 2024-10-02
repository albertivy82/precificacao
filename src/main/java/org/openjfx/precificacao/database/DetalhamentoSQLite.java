package org.openjfx.precificacao.database;

import org.openjfx.precificacao.dtos.totalProfissionalPorProjetoDTO;
import org.openjfx.precificacao.models.Detalhamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DetalhamentoSQLite {

    public void CadastroDetalhamentoEtapas(Set<Detalhamento> listaDetalhemnto) throws SQLException {

        Connection conn = SQLiteConnection.connect();

        try {
                //verificar a existência de registros idênticos

                for(Detalhamento item : listaDetalhemnto) {
                    PreparedStatement pstmt = conn.prepareStatement(
                            "INSERT INTO detalhamento (id_projeto, id_etapa, id_atividade, id_profissional, valor_hora, horas) VALUES (?, ?, ?, ?, ?, ?)");
                    pstmt.setInt(1, item.getIdProjeto());
                    pstmt.setInt(2, item.getIdEtapa());
                    pstmt.setInt(3, item.getIdAtividade());
                    pstmt.setInt(4, item.getIdProfissional());
                    pstmt.setFloat(5, item.getValorHora());
                    pstmt.setFloat(6, item.getHoras());
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                SQLiteConnection.closeConnection(conn);
            }
    }

    public List<Detalhamento> all() {
        List<Detalhamento> result = new ArrayList<>();
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("SELECT id, id_projeto, id_etapa, id_atividade, id_profissional, valor_hora, horas FROM detalhamento");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Detalhamento  d = new Detalhamento();
                d.setId(rs.getInt("id"));
                d.setIdProjeto(rs.getInt("id_projeto"));
                d.setIdEtapa(rs.getInt("id_etapa"));
                d.setIdAtividade(rs.getInt("id_atividade"));
                d.setIdProfissional(rs.getInt("id_profissional"));
                d.setValorHora(rs.getFloat("valor_hora"));
                d.setHoras(rs.getFloat("horas"));
                result.add(d);
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

    public List<Detalhamento> detalhementoPorProjeto(int idProjeto) {
        List<Detalhamento> result = new ArrayList<>();
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("SELECT id, id_projeto, id_etapa, id_atividade, id_profissional, valor_hora, horas " +
                    "FROM detalhamento WHERE id_projeto=? ORDER BY id_etapa ASC");
            pstmt.setInt(1, idProjeto);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Detalhamento  d = new Detalhamento();
                d.setId(rs.getInt("id"));
                d.setIdProjeto(rs.getInt("id_projeto"));
                d.setIdEtapa(rs.getInt("id_etapa"));
                d.setIdAtividade(rs.getInt("id_atividade"));
                d.setIdProfissional(rs.getInt("id_profissional"));
                d.setValorHora(rs.getFloat("valor_hora"));
                d.setHoras(rs.getFloat("horas"));
                result.add(d);
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

    public Float totalDeServicosPorProjeto(int idProjeto) {

        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet result = null;
        Float totalProjeto = 0.0f;

        try {
            pstmt = conn.prepareStatement("SELECT SUM(valor_hora*horas) AS total_projeto FROM detalhamento WHERE id_projeto = ?");
            pstmt.setInt(1, idProjeto);
            result = pstmt.executeQuery();
           if (result.next()) {
                totalProjeto = result.getFloat("total_projeto");
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

        return totalProjeto;
    }


    public List<totalProfissionalPorProjetoDTO> totalPorProfissional(int idProjeto) {
        List<totalProfissionalPorProjetoDTO> result = new ArrayList<>();
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("SELECT p.nome, d.id_profissional, SUM(d.horas) AS total_por_profissional" +
                    "FROM detalhamento d" +
                    "INNER JOIN profissionais p ON d.id_profissional = p.id_profissional" +
                    "WHERE d.id_projeto = ?" +
                    "GROUP BY p.nome, d.id_profissional");
            pstmt.setInt(1, idProjeto);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                totalProfissionalPorProjetoDTO t = new totalProfissionalPorProjetoDTO();
                t.setNome(rs.getString("nome"));
                t.setTotalPorProfissional(rs.getFloat("total"));
                result.add(t);
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

    public void deletarRegistro(int idProjeto, int idEtapa, int idAtividade, int idProfissional, float valorHoras, float horas) {
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;

        try {
            // Prepara a query DELETE com todos os parâmetros no WHERE
            pstmt = conn.prepareStatement("DELETE FROM detalhamento WHERE id_projeto = ? AND id_etapa = ? AND id_atividade = ? AND id_profissional = ? AND valor_hora = ? AND horas = ?");

            // Define os valores dos parâmetros
            pstmt.setInt(1, idProjeto);
            pstmt.setInt(2, idEtapa);
            pstmt.setInt(3, idAtividade);
            pstmt.setInt(4, idProfissional);
            pstmt.setFloat(5, valorHoras);
            pstmt.setFloat(6, horas);

            // Executa a query
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
               // System.out.println("Registro deletado com sucesso.");
            } else {
               // System.out.println("Nenhum registro encontrado para deletar.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                SQLiteConnection.closeConnection(conn);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void deletarDetalhamentoPorProjeto(int idProjeto) {
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM detalhamento WHERE id_projeto=?");
            pstmt.setInt(1, idProjeto);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
    }

    public boolean deletarEtapa(int idProjeto, int idEtapa) {
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        boolean success = false;

        try {

            pstmt = conn.prepareStatement("DELETE FROM detalhamento WHERE id_Projeto = ? AND  id_etapa = ?");

            pstmt.setInt(1, idProjeto);
            pstmt.setInt(2, idEtapa);
            // Executa a query
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                success = true;
               // System.out.println("Registro deletado com sucesso.");
            } else {
               // System.out.println("Nenhum registro encontrado para deletar.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                SQLiteConnection.closeConnection(conn);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return success;
    }

    public boolean deletarAtividade(int idProjeto, int idAtividade) {
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        boolean success = false;

        try {

            pstmt = conn.prepareStatement("DELETE FROM detalhamento WHERE idProjeto = ? AND id_atividade = ?");

            pstmt.setInt(1, idProjeto);
            pstmt.setInt(2, idAtividade);

            System.out.println("RESULTADO DA QUERY :" +pstmt.toString());
            // Executa a query
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                success = true;
               // System.out.println("Registro deletado com sucesso.");
            } else {
              //  System.out.println("Nenhum registro encontrado para deletar.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                SQLiteConnection.closeConnection(conn);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return success;
    }



}
