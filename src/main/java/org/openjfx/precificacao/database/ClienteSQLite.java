package org.openjfx.precificacao.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.openjfx.precificacao.models.Cliente;

public class ClienteSQLite{
    
    public void novoCliente(Cliente cliente) throws SQLException{
    	
    	
           Connection conn = SQLiteConnection.connect();
	        try {
	        	
	        	
		            PreparedStatement pstmtCpf = conn.prepareStatement("SELECT * FROM cliente WHERE cpf = ?");
		            pstmtCpf.setString(1, cliente.getCpf());
		            ResultSet rs = pstmtCpf.executeQuery();
	
		           
		            if (rs.next()) { 
		                throw new SQLException("Já existe cliente cadastrado com o cpf informado");
		            }
	        	
		            PreparedStatement pstmt = conn.prepareStatement(
		                    "INSERT INTO cliente (nome, telefone, email, cpf, endereco, bairro, cep) VALUES (?, ?, ?, ?, ?, ?, ?)");
		            pstmt.setString(1, cliente.getNome());
		            pstmt.setString(2, cliente.getTelefone());
		            pstmt.setString(3, cliente.getEmail());
		            pstmt.setString(4, cliente.getCpf());
                    pstmt.setString(5, cliente.getEndereco());
                    pstmt.setString(6, cliente.getBairro());
                    pstmt.setString(7, cliente.getCep());
		            pstmt.executeUpdate();
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        } finally {
	            SQLiteConnection.closeConnection(conn);
	        }
    }
    
    
    
    public void editarCliente(Cliente cliente) {
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE cliente SET nome=?, telefone=?, email=?, cpf=?, endereco=?, bairro=?, cep=? WHERE ID=?");
            pstmt.setString(1, cliente.getNome());
            pstmt.setString(2, cliente.getTelefone());
            pstmt.setString(3, cliente.getEmail());
            pstmt.setString(4, cliente.getCpf());
            pstmt.setString(5, cliente.getEndereco());
            pstmt.setString(6, cliente.getBairro());
            pstmt.setString(7, cliente.getCep());
            pstmt.setInt(8, cliente.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
    }

    
    public void deletarCliente(Cliente cliente) {
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM cliente WHERE ID=?");
            pstmt.setInt(1, cliente.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
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
    
    
    
    public List<Cliente> all() {
        List<Cliente> result = new ArrayList<>();  
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("SELECT id, nome, telefone, email, cpf, endereco, bairro, cep FROM cliente");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setTelefone(rs.getString("telefone"));
                c.setEmail(rs.getString("email"));
                c.setCpf(rs.getString("cpf"));
                c.setEndereco(rs.getString("endereco"));
                c.setBairro(rs.getString("bairro"));
                c.setCep(rs.getString("cep"));
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

    public List<String> nomesClientes() {

        List<String> result = new ArrayList<>();
        String sql = "SELECT nome FROM cliente";
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                result.add(rs.getString("nome"));
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


    public int clientePorNome(String nomeCliente) {

        int idCliente = -1;
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet result = null;

        try{
            pstmt = conn.prepareStatement("SELECT id FROM cliente WHERE nome=?");
            pstmt.setString(1, nomeCliente);
            result = pstmt.executeQuery();

                if(result.next()){
                    idCliente = result.getInt("id");
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
       return idCliente;
    }

}
