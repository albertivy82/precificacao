package org.openjfx.precificacao.database;

import org.openjfx.precificacao.models.CustosVariaveis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustosVariaveisSQLite {
    
    public void NovoCustosVariaveis(CustosVariaveis custo) throws SQLException{
    	
    	
           Connection conn = SQLiteConnection.connect();
	        try {
	        	
	        	
		            PreparedStatement pstmt = conn.prepareStatement(
		                    "INSERT INTO custos_variaveis (item, valor) VALUES (?, ?)");
		            pstmt.setString(1, custo.getItem());
		            pstmt.setFloat(2, custo.getValor());
		            pstmt.executeUpdate();
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        } finally {
	            SQLiteConnection.closeConnection(conn);
	        }
    }
    
    
    
    public void editarCustosVariaveis(CustosVariaveis custo) {
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE custos_variaveis SET item=?, valor=? WHERE ID=?");
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

    
    public void deletarCustosVariaveis(CustosVariaveis custo) {
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM custos_variaveis WHERE ID=?");
            pstmt.setInt(1, custo.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
    }
    
    
    
    public List<CustosVariaveis> all() {
        List<CustosVariaveis> result = new ArrayList<>();  
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("SELECT id, item, valor FROM custos_variaveis");
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                CustosVariaveis c = new CustosVariaveis();
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