package org.openjfx.precificacao.database;

import org.openjfx.precificacao.models.Cliente;
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

}
