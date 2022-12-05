package database;

import onlinebank.account.Account;
import onlinebank.account.Transaction;
import onlinebank.customer.Customer;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


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

        try { 
            Statement stmt = connection.createStatement();
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

        try {
            Statement stmt = connection.createStatement();
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
    public static int addCustomer(Customer customer, String ssn, String password) {
        StringBuilder query = new StringBuilder("INSERT INTO customer (");
        HashMap<String, String> requiredColumns = new HashMap<String, String>(){
            {
                put("id", String.valueOf(customer.getId()));
                put("firstname", customer.getFirstName());
                put("lastname", customer.getLastName());
                put("email", customer.getEmail());
                put("age", String.valueOf(customer.getAge()));
                put("date_of_birth", String.valueOf(customer.getDateOfBirth()));
                put("password", password);
            }
        };

        if (customer.getPhoneNumber() != 0) {
            requiredColumns.put("phone_number", String.valueOf(customer.getPhoneNumber()));
        }
        if (ssn != null){
            requiredColumns.put("SSN", ssn);
        }

        return addRecord(prepareInsertQuery(query, requiredColumns));
    }

    /**
     * Get customer from the database upon login
     * @param email email entered by user
     * @param password password entered by user
     * @return Customer object
     */
    public static Customer getCustomer(String email, String password) {
        String query = "SELECT * FROM customer WHERE email='" + email + "' AND password='" + password + "';";
        Customer customer = null;
        
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                if (resultSet.getRow() == 1) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    customer = new Customer(
                            resultSet.getInt(1),  // id
                            resultSet.getString(2),  // firstname
                            resultSet.getString(3),  // lastname
                            LocalDate.parse(resultSet.getString(5), formatter),  // dob
                            resultSet.getString(4)  // email
                    );
                    if (resultSet.getString(7) != null && !resultSet.getString(7).equals("")) {
                        customer.setPhoneNumber(resultSet.getInt(7));
                    }
                    if (resultSet.getString(8) != null && !resultSet.getString(8).equals("")) {
                        customer.setSSN(resultSet.getString(8));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());  // TODO (shubham): Implement logger
        }
        return customer;
    }

    /**
     * Add a new account the account table in database
     * @param account Account object
     * @return id of the inserted record
     */
    public static int addAccount(Account account) {
        StringBuilder query = new StringBuilder("INSERT INTO account (");
        HashMap<String, String> requiredColumns = new HashMap<String, String>(){
            {
                put("id", String.valueOf(account.getId()));
                put("cid", String.valueOf(account.getCid()));
                put("account_no", String.valueOf(account.getAccountNo()));
                put("account_type", account.getAccountType().toString());
                put("balance", String.valueOf(account.getBalance()));
            }
        };

        return addRecord(prepareInsertQuery(query, requiredColumns));
    }

    // ================
    // Private Methods
    // ================
    /**
     * Execute query to add new record into database
     * @param query string query to execute
     * @return id of the inserted record
     */
    private static int addRecord(String query) {
        int id = -1;

        try {
            PreparedStatement pstmt = connection.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try {
                    ResultSet resultSet = pstmt.getGeneratedKeys();
                    if (resultSet.next()) {
                        id = resultSet.getInt(1);
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return id;
    }

    /**
     * Prepare insert query using the required columns HashMap
     * @param query query part to prepare
     * @param requiredColumns required column & values
     * @return String of prepared query
     */
    private static String prepareInsertQuery(StringBuilder query, HashMap<String, String> requiredColumns) {
        List<String> columns = new ArrayList<String>(requiredColumns.keySet());
        query.append(String.join(", ", columns)).append(") VALUES (");

        for (String col : columns) {
            query.append("'").append(requiredColumns.get(col)).append("'").append(",");
        }
        query.deleteCharAt(query.length() - 1);
        query.append(");");

        return query.toString();
    }

    public static int addTransaction(Transaction transaction) {
        return 0;  // TODO (shubham): add new transaction to the transaction table
    }
}
