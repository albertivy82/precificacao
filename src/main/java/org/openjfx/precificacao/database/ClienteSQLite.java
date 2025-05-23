package org.openjfx.precificacao.database;

import org.openjfx.precificacao.models.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClienteSQLite{
    
    public void novoCliente(Cliente cliente) throws SQLException{
    	
    	
           Connection conn = SQLiteConnection.connect();
	        try {
                PreparedStatement checkStmt;

                if (cliente.getCpf() != null && !cliente.getCpf().trim().isEmpty()) {
                    checkStmt = conn.prepareStatement("SELECT id FROM cliente WHERE cpf = ?");
                    checkStmt.setString(1, cliente.getCpf());
                } else {
                    checkStmt = conn.prepareStatement("SELECT id FROM cliente WHERE uuid = ?");
                    checkStmt.setString(1, cliente.getUuid());
                }

                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    throw new SQLException("Já existe cliente cadastrado com o mesmo CPF ou UUID.");
                }


                PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO cliente (nome, telefone, email, cpf, endereco, bairro, cep, estado, cidade, uuid, perfil) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                pstmt.setString(1, cliente.getNome());
                pstmt.setString(2, cliente.getTelefone());
                pstmt.setString(3, cliente.getEmail());
                pstmt.setString(4, cliente.getCpf());
                pstmt.setString(5, cliente.getEndereco());
                pstmt.setString(6, cliente.getBairro());
                pstmt.setString(7, cliente.getCep());
                pstmt.setString(8, cliente.getEstado());
                pstmt.setString(9, cliente.getCidade());
                pstmt.setString(10, cliente.getUuid());     // novo campo
                pstmt.setString(11, cliente.getPerfil());   // novo campo
                pstmt.executeUpdate();

            } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        } finally {
	            SQLiteConnection.closeConnection(conn);
	        }
    }
    
    
    
    public void editarCliente(Cliente cliente) {
        System.out.println("editarCliente é chamado?" +  cliente.getId());
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE cliente SET nome=?, telefone=?, email=?, cpf=?, endereco=?, bairro=?, cep=?, estado=?, cidade=?, uuid=?, perfil=? WHERE ID=?");
            System.out.println("editarCliente é chamado?" + " " + cliente.getNome() + " " + cliente.getTelefone()+ " " + cliente.getEmail()+ " " +cliente.getCpf()+cliente.getEndereco()+
                    cliente.getBairro()+cliente.getCep()+ " " + cliente.getEstado()+ " " +cliente.getCidade()+ " " + cliente.getUuid()+ " " +cliente.getPerfil()+ " " +cliente.getId());
            pstmt.setString(1, cliente.getNome());
            pstmt.setString(2, cliente.getTelefone());
            pstmt.setString(3, cliente.getEmail());
            pstmt.setString(4, cliente.getCpf());
            pstmt.setString(5, cliente.getEndereco());
            pstmt.setString(6, cliente.getBairro());
            pstmt.setString(7, cliente.getCep());
            pstmt.setString(8, cliente.getEstado());
            pstmt.setString(9, cliente.getCidade());
            pstmt.setString(10, cliente.getUuid());
            pstmt.setString(11, cliente.getPerfil());
            pstmt.setInt(12, cliente.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
    }

    public void gerarCodCliente(int id, String codCliente) {
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement("UPDATE cliente SET cod_cliente = ? WHERE id = ?");
            pstmt.setString(1, codCliente);
            pstmt.setInt(2, id);
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

    public String buscarNomeClientePorId(int id) {

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

    public Cliente buscarClientePorId(int id) {
        Cliente cliente = new Cliente();
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT id, cod_cliente, nome, cpf, telefone, email, endereco, bairro, estado, cidade, cep, uuid, perfil FROM cliente WHERE ID=?"
            );
            pstmt.setInt(1, id);
            ResultSet result = pstmt.executeQuery();

            if(result.next()){
                cliente.setId(result.getInt("id"));
                cliente.setCodCliente(result.getString("cod_cliente"));
                cliente.setNome(result.getString("nome"));
                cliente.setCpf(result.getString("cpf"));
                cliente.setTelefone(result.getString("telefone"));
                cliente.setEmail(result.getString("email"));
                cliente.setEndereco(result.getString("endereco"));
                cliente.setBairro(result.getString("bairro"));
                cliente.setEstado(result.getString("estado"));
                cliente.setCidade(result.getString("cidade"));
                cliente.setCep(result.getString("cep"));
                cliente.setUuid(result.getString("uuid"));
                cliente.setPerfil(result.getString("perfil"));

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }

        return cliente;
    }
    
    
    
    public List<Cliente> all() {
        List<Cliente> result = new ArrayList<>();  
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("SELECT id, cod_cliente, nome, telefone, email, cpf, endereco, bairro, cep, estado, cidade, uuid, perfil FROM cliente");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setCodCliente(rs.getString("cod_cliente"));
                c.setNome(rs.getString("nome"));
                c.setTelefone(rs.getString("telefone"));
                c.setEmail(rs.getString("email"));
                c.setCpf(rs.getString("cpf"));
                c.setEndereco(rs.getString("endereco"));
                c.setBairro(rs.getString("bairro"));
                c.setCep(rs.getString("cep"));
                c.setEstado(rs.getString("estado")); // Adiciona o estado
                c.setCidade(rs.getString("cidade")); // Adiciona a cidade
                c.setUuid(rs.getString("uuid"));
                c.setPerfil(rs.getString("perfil"));
                result.add(c);
            }

        }catch (SQLException e) {
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

    public int clientePorCpf(String cpf) {
        int idCliente = 0;
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet result = null;

        try{
            pstmt = conn.prepareStatement("SELECT id FROM cliente WHERE cpf=?");
            pstmt.setString(1, cpf);
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

    public int clientePorUuid(String uuid) {
        int idCliente = 0;
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet result = null;

        try {
            pstmt = conn.prepareStatement("SELECT id FROM cliente WHERE uuid = ?");
            pstmt.setString(1, uuid);
            result = pstmt.executeQuery();

            if (result.next()) {
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
