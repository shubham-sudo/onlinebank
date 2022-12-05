package database;

import bank.account.Account;
import bank.account.Transaction;
import bank.customer.Customer;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        StringBuilder query = new StringBuilder("INSERT INTO customer");
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
     * Update Customer record into database
     * @param customer Customer object to be updated
     * @param idColumn id column of the customer table
     * @return number of records updated
     */
    public static int updateCustomer(Customer customer, String idColumn) {
        StringBuilder query = new StringBuilder("UPDATE customer");

        HashMap<String, String> columnsToUpdate = new HashMap<String, String>() {
            {
                put("firstname", customer.getFirstName());
                put("lastname", customer.getLastName());
                put("age", String.valueOf(customer.getAge()));
                put("date_of_birth", String.valueOf(customer.getDateOfBirth()));
            }
        };
        if (customer.getPhoneNumber() != 0) {
            columnsToUpdate.put("phone_number", String.valueOf(customer.getPhoneNumber()));
        }

        return updateRecord(prepareUpdateQuery(query, columnsToUpdate, idColumn, customer.getId()));
    }

    /**
     * Delete a customer from database
     * @param customer Customer object
     * @param idColumn id column name
     */
    public static void deleteCustomer(Customer customer, String idColumn) {
        StringBuilder query = new StringBuilder("DELETE FROM customer");
        deleteRecord(prepareDeleteQuery(query, idColumn, customer.getId()));
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
        StringBuilder query = new StringBuilder("INSERT INTO account");
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

    /**
     * Update Account record into database
     * @param account Account object to be updated
     * @param idColumn id column of the account table
     * @return number of records updated
     */
    public static int updateAccount(Account account, String idColumn) {
        StringBuilder query = new StringBuilder("UPDATE account");

        HashMap<String, String> columnsToUpdate = new HashMap<String, String>() {
            {
                put("balance", String.valueOf(account.getBalance()));
            }
        };

        return updateRecord(prepareUpdateQuery(query, columnsToUpdate, idColumn, account.getId()));
    }

    /**
     * Delete an account from database
     * @param account Account object
     * @param idColumn id column name
     */
    public static void deleteAccount(Account account, String idColumn) {
        StringBuilder query = new StringBuilder("DELETE FROM account");
        deleteRecord(prepareDeleteQuery(query, idColumn, account.getId()));
    }

    /**
     * Add new transaction record to the transaction table
     * @param transaction Transaction object
     * @return id of the inserted record
     */
    public static int addTransaction(Transaction transaction) {
        StringBuilder query = new StringBuilder("INSERT INTO transaction");
        HashMap<String, String> requiredColumns = new HashMap<String, String>(){
            {
                put("id", String.valueOf(transaction.getId()));
                put("aid", String.valueOf(transaction.getAid()));
                put("message", transaction.getMessage());
                put("date", transaction.getTodayDate().toString());
                put("old_value", String.valueOf(transaction.getOldValue()));
                put("new_value", String.valueOf(transaction.getNewValue()));
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
        return executeQuery(query);
    }

    /**
     * Execute query to update records into database
     * @param query string query to execute
     * @return number of records affected
     */
    private static int updateRecord(String query) {
        return executeQuery(query);
    }

    /**
     * Execute query to delete records from database
     * @param query string query to execute
     * @return number of records affected
     */
    private static int deleteRecord(String query) {
        return executeQuery(query);
    }

    /**
     * Execute given query string
     * @param query query to execute
     * @return rows affected when update
     * or inserted id when insert
     */
    private static int executeQuery(String query) {
        int id = -1;

        try {
            PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
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
        query.append(" (").append(String.join(", ", columns)).append(") VALUES (");

        for (String col : columns) {
            query.append("'").append(requiredColumns.get(col)).append("'").append(",");
        }
        query.deleteCharAt(query.length() - 1);
        query.append(");");

        return query.toString();
    }

    /**
     * Prepare update query using columns to update HashMap
     * @param query query part to prepare
     * @param columnToUpdate columns to update
     * @param idColumn id column name
     * @param id id of the record to update
     * @return String of prepared query
     */
    private static String prepareUpdateQuery(StringBuilder query, HashMap<String, String> columnToUpdate, String idColumn, int id) {
        List<String> columns = new ArrayList<>(columnToUpdate.keySet());
        query.append(" SET ");

        for (String col : columns) {
            query.append(col).append(" = '").append(columnToUpdate.get(col)).append("', ");
        }
        query.deleteCharAt(query.length() - 1);
        query.append("WHERE ").append(idColumn).append(" = '").append(id).append("';");

        return query.toString();
    }

    /**
     * Prepare delete query using id column
     * @param query query part to prepare
     * @param idColumn id column name
     * @param id id of the record to delete
     * @return String of prepared query
     */
    private static String prepareDeleteQuery(StringBuilder query, String idColumn, int id) {
        query.append(" WHERE ").append(idColumn).append(" = '").append(id).append("';");
        return query.toString();
    }
}
