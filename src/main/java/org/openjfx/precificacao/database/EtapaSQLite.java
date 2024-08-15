package org.openjfx.precificacao.database;
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
}
