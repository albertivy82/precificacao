package org.openjfx.precificacao.database;

import org.openjfx.precificacao.models.Projeto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjetoSQLite{
    
    public void NovoProjeto(Projeto projeto) throws SQLException{
    	
    	
           Connection conn = SQLiteConnection.connect();
	        try {
		            PreparedStatement pstmt = conn.prepareStatement(
		                    "INSERT INTO projeto (nome_projeto, id_cliente, tipo, status ) VALUES (?, ?, ?, ?, ?)");
		            pstmt.setString(1, projeto.getNomeProjeto());
		            pstmt.setInt(2, projeto.getIdCliente());
                    pstmt.setString(3, projeto.getTipo());
		            pstmt.setString(4, projeto.getStatus());
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
                    "UPDATE projeto SET nome_projeto=?, id_cliente=?, tipo=?, status=?, precificacao=? WHERE ID=?");
            System.out.println(projeto.getId());
            pstmt.setString(1, projeto.getNomeProjeto());
		    pstmt.setInt(2, projeto.getIdCliente());
            pstmt.setString(3, projeto.getTipo());
		    pstmt.setString(4, projeto.getStatus());
            pstmt.setDouble(5, projeto.getPrecificacao());
            pstmt.setInt(6, projeto.getId());
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
            System.out.println(id + " " + codProjeto);
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

    public int projetoPorNome(String nome) {
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

    
    public void deletarProjeto(int projetoId) {
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM projeto WHERE ID=?");
            pstmt.setInt(1, projetoId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
    }


    public List<Integer> buscarProjetosPorCliente(int idCliente) {
        List<Integer> projetosIds = new ArrayList<>();
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT id FROM projeto WHERE id_cliente=?");
            pstmt.setInt(1, idCliente);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                projetosIds.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
        return projetosIds;
    }
    
    
    
    public List<Projeto> all() {
        List<Projeto> result = new ArrayList<>();  
        Connection conn = SQLiteConnection.connect();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("SELECT id, cod_projeto, nome_projeto, id_cliente, tipo, status, precificacao FROM projeto");
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Projeto p = new Projeto();
                p.setId(rs.getInt("id"));
                p.setCodProjeto(rs.getString("cod_projeto"));
                p.setNomeProjeto(rs.getString("nome_projeto"));
                p.setIdCliente(rs.getInt("id_cliente"));
                p.setTipo(rs.getString("tipo"));
                p.setStatus(rs.getString("status"));
                p.setPrecificacao(rs.getDouble("precificacao"));
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

    public Map<String, Integer> contarProjetosPorStatus() {
        Map<String, Integer> statusCount = new HashMap<>();
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT status, COUNT(*) AS total FROM projeto GROUP BY status");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String status = rs.getString("status");
                int count = rs.getInt("total");
                status = status +": " + count;
                statusCount.put(status, count);  // Adiciona ao mapa
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
        return statusCount;
    }


    public List<String> buscarTotalPorCliente() {
        List<String> totalPorCliente = new ArrayList<>();
        Connection conn = SQLiteConnection.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT c.nome AS nome_cliente, SUM(p.precificacao) AS total_precificacao " +
                            "FROM projeto p " +
                            "JOIN cliente c ON p.id_cliente = c.id " +
                            "GROUP BY c.nome " +
                            "ORDER BY total_precificacao DESC"
            );
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String nomeCliente = rs.getString("nome_cliente");
                double totalPrecificacao = rs.getDouble("total_precificacao");

                // Montar a string com as informações que precisamos (cliente e valor total)
                String info = "Cliente: " + nomeCliente + ", Valor Total: " + totalPrecificacao;
                totalPorCliente.add(info);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            SQLiteConnection.closeConnection(conn);
        }
        return totalPorCliente;
    }






}