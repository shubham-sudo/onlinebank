package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DbConnection {

    // DbConnection is singleton class
    private DbConnection(){}

    public static Connection getConnection(){
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:onlinebank.sqlite";  // TODO (shubham): should be relative path
            conn = DriverManager.getConnection(url);
            System.out.println("Connection Established");  // TODO (shubham): remove this
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return conn;
    }

    // TODO (shubham): remove this main, only for testing db connection
    public static void main(String[] args) {
        getConnection();
    }
}
