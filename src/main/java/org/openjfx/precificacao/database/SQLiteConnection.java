package org.openjfx.precificacao.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {
    private static final String URL = "jdbc:sqlite:precificacao.db";

    public static Connection connect() {
    	Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("Conexão estabelecida com o banco de dados SQLite.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Conexão com o banco de dados foi fechada.");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
