package org.openjfx.precificacao.database;

import org.openjfx.precificacao.models.Projeto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjetoSQLite{
    
    public void NovoProjeto(Projeto projeto) throws SQLException{
    	
    	
           Connection conn = SQLiteConnection.connect();
	        try {
	        	
	        	
		            PreparedStatement pstmt = conn.prepareStatement(
		                    "INSERT INTO projeto (nome_projeto, id_cliente, status) VALUES (?, ?, ?)");
		            pstmt.setString(1, projeto.getNomeProjeto());
		            pstmt.setInt(2, projeto.getIdCliente());
		            pstmt.setString(3, projeto.getStatus());
		            pstmt.executeUpdate();
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        } finally {
	            SQLiteConnection.closeConnection(conn);
	        }
    }
    
  
  public void editarProjeto(Projeto projeto) throws SQLException {
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE projeto SET nome_projeto=?, id_cliente=?, status=?, precificacao=? WHERE ID=?");
            System.out.println("testanto precificacao 2");
            pstmt.setString(1, projeto.getNomeProjeto());
		    pstmt.setInt(2, projeto.getIdCliente());
		    pstmt.setString(3, projeto.getStatus());
            pstmt.setDouble(4, projeto.getPrecificacao());
            pstmt.setInt(5, projeto.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
    }

    public void gerarCodProjeto(int id, String codProjeto) {
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement("UPDATE projeto SET cod_projeto = ? WHERE id = ?");
            pstmt.setString(1, codProjeto);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
    }

    public int clientePorNome(String nome) {
        int idProjeto = 0;
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet result = null;

        try{
            pstmt = conn.prepareStatement("SELECT id FROM projeto WHERE nome_projeto=?");
            pstmt.setString(1, nome);
            result = pstmt.executeQuery();

            if(result.next()){
                idProjeto = result.getInt("id");
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
        return idProjeto;
    }

    
    public void deletarProjeto(Projeto projeto) {
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM projeto WHERE ID=?");
            pstmt.setInt(1, projeto.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
    }
    
    
    
    public List<Projeto> all() {
        List<Projeto> result = new ArrayList<>();  
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("SELECT id, cod_projeto, nome_projeto, id_cliente, status FROM projeto");
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Projeto p = new Projeto();
                p.setId(rs.getInt("id"));
                p.setCodProjeto(rs.getString("cod_projeto"));
                p.setNomeProjeto(rs.getString("nome_projeto"));
                p.setIdCliente(rs.getInt("id_cliente"));
                p.setStatus(rs.getString("status"));
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

    public String projetoPorNome(int idProjeto) {
        String nome = "";
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT nome_projeto FROM projeto WHERE ID=?");
            pstmt.setInt(1, idProjeto);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                nome = rs.getString("nome_projeto");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }

        return nome;
    }


    public int buscaIdProjetoPorNome(String projeto) {
        int idProjeto = 0;
        Connection conn = SQLiteConnection.connect();
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT id FROM projeto WHERE nome_projeto =?")) {
            pstmt.setString(1, projeto);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    idProjeto = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
        return idProjeto;
    }

    public Float totalDoProjeto(String projeto) {
        float idProjeto = 0;
        Connection conn = SQLiteConnection.connect();
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT SUM(valor_hora * horas) AS total_projeto FROM detalhamento WHERE id_projeto = ?"))
        {
            pstmt.setString(1, "nome_projeto");
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    idProjeto = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
        return idProjeto;
    }

    public String buscarClientePorId(int id) {

        String nomeCliente = "";
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT nome FROM cliente WHERE ID=?");
            pstmt.setInt(1, id);
            ResultSet result = pstmt.executeQuery();

            if(result.next()){
                nomeCliente = result.getString("nome");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }

        return nomeCliente;
    }


}