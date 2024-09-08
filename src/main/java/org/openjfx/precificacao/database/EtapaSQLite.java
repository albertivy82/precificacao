package org.openjfx.precificacao.database;

import org.openjfx.precificacao.dtos.EtapaDTO;
import org.openjfx.precificacao.models.Etapa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EtapaSQLite {

    public List<Etapa> all() {
        List<Etapa> result = new ArrayList<>();
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("SELECT id, etapa FROM etapas");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Etapa et = new Etapa();
                et.setId(rs.getInt("id"));
                et.setEtapa(rs.getString("etapa"));

                result.add(et);
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

    public String etapaPorNome(int idEtapa) {
        String etapa = "";
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT etapa FROM etapas WHERE ID=?");
            pstmt.setInt(1, idEtapa);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                etapa = rs.getString("etapa");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }

        return etapa;
    }

    public void deletarEtapa(int id) {
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM etapas WHERE ID=?");
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
    }


    public int buscaIdEtapaPorNome(String etapa) {
        int idEtapa = 0;
        Connection conn = SQLiteConnection.connect();
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT id FROM etapas WHERE etapa =?")) {
            pstmt.setString(1, etapa);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    idEtapa = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
        return idEtapa;
    }


    public List<EtapaDTO> totalPorEtapa(int idProjeto) {
        List<EtapaDTO> etapas = new ArrayList<>();
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet result = null;

        try {
            pstmt = conn.prepareStatement("SELECT d.id_etapa, e.etapa, SUM(d.horas) AS total_por_etapa" +
                    "FROM detalhamento d" +
                    "INNER JOIN etapas e ON d.id_etapa = e.id" +
                    "WHERE d.id_projeto = ?" +
                    "GROUP BY d.id_etapa, e.etapa;");
            pstmt.setInt(1, idProjeto);
            result = pstmt.executeQuery();

            while (result.next()) {
                EtapaDTO etapa = new EtapaDTO();
                etapa.setId(result.getInt("id_etapa"));
                etapa.setEtapa(result.getString("etapa"));
                etapa.setTotalPorEtapaProjeto(result.getFloat("total_por_etapa"));
                etapas.add(etapa);
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


    public Float totalEtapaPorId(int idProjeto, int idEtapa) {
        Float totalEtapa = null;
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet result = null;

        try {
            pstmt = conn.prepareStatement("SELECT SUM(horas) AS total_por_etapa FROM detalhamento WHERE id_Projeto = ? AND id_etapa = ?");
            pstmt.setInt(1, idProjeto);
            pstmt.setInt(2, idEtapa);
            result = pstmt.executeQuery();

            if (result.next()) {
                totalEtapa = result.getFloat("total_por_etapa");
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
        return totalEtapa;
    }
}
