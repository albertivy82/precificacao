package org.openjfx.precificacao.database;


import org.openjfx.precificacao.models.ConsolidaProjeto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConsolidaProjetoSQLite {

    // Método para adicionar uma nova Consolida
    public void novaConsolida(ConsolidaProjeto consolidaProjeto) throws SQLException {
        Connection conn = SQLiteConnection.connect();

        try {
            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO consolida_projetos (id_projeto, id_cliente, ano_inicio,  mes_inicio, valor_inicial, ano_fim, mes_final, valor_final) VALUES (?,?,? ,?, ?, ?, ?, ?)");
            pstmt.setInt(1, consolidaProjeto.getIdProjeto());
            pstmt.setInt(2, consolidaProjeto.getIdCliente());
            pstmt.setInt(3, consolidaProjeto.getAnoInicio());
            pstmt.setString(4, consolidaProjeto.getMesInicio());
            pstmt.setDouble(5, consolidaProjeto.getValorInicial());
            pstmt.setInt(6, consolidaProjeto.getAnoFim());
            pstmt.setString(7, consolidaProjeto.getMesFinal());
            pstmt.setDouble(8, consolidaProjeto.getValorFinal());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
    }


    public void editarConsolida(ConsolidaProjeto consolidaProjeto) throws SQLException {
        Connection conn = SQLiteConnection.connect();

        try {
            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE consolida_projetos SET id_projeto=?, id_cliente=?, ano_inicio=?, mes_inicio=?, valor_inicial=?, ano_fim=?, mes_final=?, valor_final=? WHERE id=?");
            pstmt.setInt(1, consolidaProjeto.getIdProjeto());
            pstmt.setInt(2, consolidaProjeto.getIdCliente());
            pstmt.setInt(3, consolidaProjeto.getAnoInicio());
            pstmt.setString(4, consolidaProjeto.getMesInicio());
            pstmt.setDouble(5, consolidaProjeto.getValorInicial());
            pstmt.setInt(6, consolidaProjeto.getAnoFim());
            pstmt.setString(7, consolidaProjeto.getMesFinal());
            pstmt.setDouble(8, consolidaProjeto.getValorFinal());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
    }


    public List<ConsolidaProjeto> all() {
        List<ConsolidaProjeto> result = new ArrayList<>();
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("SELECT id, id_projeto, id_cliente, ano_inicio, mes_inicio, valor_inicial, ano_fim, mes_final, valor_final FROM consolida_projetos");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ConsolidaProjeto concolidados = new ConsolidaProjeto();
                concolidados.setId(rs.getInt("id"));
                concolidados.setIdProjeto(rs.getInt("id_projeto"));
                concolidados.setIdCliente(rs.getInt("id_cliente"));
                concolidados.setAnoInicio(rs.getInt("ano_inicio"));
                concolidados.setMesInicio(rs.getString("mes_inicio"));
                concolidados.setValorInicial(rs.getDouble("valor_inicial"));
                concolidados.setAnoFim(rs.getInt("ano_fim"));
                concolidados.setMesFinal(rs.getString("mes_final"));
                concolidados.setValorFinal(rs.getDouble("valor_final"));


                result.add(concolidados);
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


    public void deletarAtividde(int id) {
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM consolida_projetos WHERE ID=?");
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
    }

    public Boolean buscarRegistroExistente(int idProjeto) {
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT id FROM consolida_projetos WHERE id_projeto=? LIMIT 1"
            );
            pstmt.setInt(1, idProjeto);
            ResultSet result = pstmt.executeQuery();

            return result.next(); // já retorna true ou false
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }

        return false;
    }






}
