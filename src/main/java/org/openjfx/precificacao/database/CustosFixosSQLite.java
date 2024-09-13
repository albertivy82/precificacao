package org.openjfx.precificacao.database;

import org.openjfx.precificacao.models.CustosFixos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustosFixosSQLite{
    
    public void NovoCustosFixos(CustosFixos custo) throws SQLException{
    	
    	
           Connection conn = SQLiteConnection.connect();
	        try {
	        	
	        	
		            PreparedStatement pstmt = conn.prepareStatement(
		                    "INSERT INTO custos_fixos (item, valor) VALUES (?, ?)");
		            pstmt.setString(1, custo.getItem());
		            pstmt.setFloat(2, custo.getValor());
		            pstmt.executeUpdate();
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        } finally {
	            SQLiteConnection.closeConnection(conn);
	        }
    }
    
    
    
    public void editarCustosFixos(CustosFixos custo) {
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE custos_fixos SET item=?, valor=? WHERE ID=?");
            pstmt.setString(1, custo.getItem());
		    pstmt.setFloat(2, custo.getValor());
            pstmt.setFloat(3, custo.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
    }

    
    public void deletarCustosFixos(CustosFixos custo) {
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM custos_fixos WHERE ID=?");
            pstmt.setInt(1, custo.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
    }
    
    
    
    public List<CustosFixos> all() {
        List<CustosFixos> result = new ArrayList<>();  
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("SELECT id, item, valor FROM custos_fixos");
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                CustosFixos c = new CustosFixos();
                c.setId(rs.getInt("id"));
                c.setItem(rs.getString("item"));
                c.setValor(rs.getFloat("valor"));
               
                result.add(c);
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