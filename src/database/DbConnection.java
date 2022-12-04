package database;

import java.nio.file.Paths;
import java.sql.*;


public class DbConnection {
    private static final String dbFile = "onlinebank.sqlite";
    private static Connection conn = null;

    // DbConnection is singleton class
    private DbConnection(){}

    public static Connection getConnection(){
        String dbFilePath = Paths.get(".").normalize().toAbsolutePath() + "/" + dbFile;

        try {
            if (conn == null || conn.isClosed()) {
                String url = "jdbc:sqlite:" + dbFilePath;
                conn = DriverManager.getConnection(url);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());  // TODO (shubham): Implement logging
        }
        return conn;
    }

    public static void closeConnection(){
        try {
            if (!conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());  // TODO (shubham): Implement logging
        }
    }
}
