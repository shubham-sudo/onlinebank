package database;

import onlinebank.person.Customer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Database is a helper class for online bank
 */
public class Database {
    private static final Connection connection = DbConnection.getConnection();

    /**
     * Get new id (Primary Key) for the given table
     * @param tableName table name
     * @param idColumn id column name of table
     * @return integer next id
     */
    public static int getNewId(String tableName, String idColumn) {
        String query = "SELECT MAX("+ idColumn +") AS max_id FROM " + tableName + " ;";
        int id = 0;

        try (Statement stmt = connection.createStatement()) {
            ResultSet resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                id = resultSet.getInt("max_id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());  // TODO (shubham): Implement logger
        }

        return id + 1;
    }

    /**
     * Checks if the id already exists before inserting new records
     * @param tableName table name
     * @param idColumn id column name of table
     * @param id id to check in table
     * @return true if exists, false otherwise
     */
    public static boolean isIdExists(String tableName, String idColumn, int id) {
        String query = "SELECT COUNT(*) AS total_count FROM " + tableName + " WHERE " + idColumn + "=" + id + ";";
        boolean found = false;

        try (Statement stmt = connection.createStatement()) {
            ResultSet resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                if (resultSet.getInt("total_count") > 0) {
                    found = true;
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());  // TODO (shubham): Implement logger
        }

        return found;
    }

    /**
     * Add new customer to the customer table
     * @param customer Customer object
     * @return id of the inserted record
     */
    public static int addCustomer(Customer customer) {
        // add new customer into database
        return 0;
    }
}
