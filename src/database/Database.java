package database;

import bank.account.*;
import bank.customer.Customer;
import bank.factory.CustomerFactory;
import bank.customer.assets.Collateral;
import bank.loan.Loan;
import bank.trade.Stock;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Database is a helper class for online bank
 */
public class Database {
    private static final CustomerFactory customerFactory = new CustomerFactory();
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
        StringBuilder query = new StringBuilder("INSERT INTO " + Customer.tableName);
        HashMap<String, String> requiredColumns = new HashMap<String, String>(){
            {
                put(Customer.idColumn, String.valueOf(customer.getId()));
                put("firstname", customer.getFirstName());
                put("lastname", customer.getLastName());
                put("email", customer.getEmail());
                put("age", String.valueOf(customer.getAge()));
                put("date_of_birth", String.valueOf(customer.getDateOfBirth()));
                put("password", password);
                put("is_manager", String.valueOf(customer.isManager() ? 1 : 0));
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
     * Update password of the customer
     * @param customer Customer object
     * @param password new password
     * @return
     */
    public static boolean updatePassword(Customer customer, String password) {
        StringBuilder query = new StringBuilder("UPDATE " + Customer.tableName);

        HashMap<String, String> columnsToUpdate = new HashMap<String, String>() {
            {
                put("password", password);
            }
        };
        if (customer.getPhoneNumber() != 0) {
            columnsToUpdate.put("phone_number", String.valueOf(customer.getPhoneNumber()));
        }

        return updateRecord(prepareUpdateQuery(query, columnsToUpdate, Customer.idColumn, customer.getId())) == 1;
    }

    /**
     * Update Customer record into database
     * @param customer Customer object to be updated
     * @return number of records updated
     */
    public static int updateCustomer(Customer customer) {
        StringBuilder query = new StringBuilder("UPDATE " + Customer.tableName);

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

        return updateRecord(prepareUpdateQuery(query, columnsToUpdate, Customer.idColumn, customer.getId()));
    }

    /**
     * Delete a customer from database
     * @param customer Customer object
     */
    public static void deleteCustomer(Customer customer) {
        StringBuilder query = new StringBuilder("DELETE FROM " + Customer.tableName);
        deleteRecord(prepareDeleteQuery(query, Customer.idColumn, customer.getId()));
    }

    /**
     * Get customer from the database upon whenever required
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
                    customer = customerFactory.createCustomer(
                            resultSet.getInt(1),      // id
                            resultSet.getString(2),   // firstname
                            resultSet.getString(3),   // lastname
                            resultSet.getString(5),   // dob
                            resultSet.getString(4),   // email
                            resultSet.getInt(10) > 0,  // is manager
                            resultSet.getString(7),   // phoneNumber
                            resultSet.getString(8)    // ssn
                    );
                    customer.setPassword(resultSet.getString(9));  // password
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());  // TODO (shubham): Implement logger
        }
        return customer;
    }

    public static List<Customer> getCustomers() {
        String query = "SELECT * FROM " + Customer.tableName + ";";
        List<Customer> customers = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {

                Customer customer = customerFactory.createCustomer(
                        resultSet.getInt(1),      // id
                        resultSet.getString(2),   // firstname
                        resultSet.getString(3),   // lastname
                        resultSet.getString(5),   // dob
                        resultSet.getString(4),   // email
                        resultSet.getBoolean(10),  // is manager
                        resultSet.getString(7),   // phoneNumber
                        resultSet.getString(8)    // ssn
                );
                if (!customer.isManager()) {
                    customers.add(customer);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return customers;
    }

    /**
     * Get customer from database using email
     * @param email email id to look for
     */
    public static Customer getCustomer(String email) {
        String query = "SELECT * FROM customer WHERE email='" + email + "';";
        Customer customer = null;

        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                if (resultSet.getRow() == 1) {
                    customer = customerFactory.createCustomer(
                            resultSet.getInt(1),      // id
                            resultSet.getString(2),   // firstname
                            resultSet.getString(3),   // lastname
                            resultSet.getString(5),   // dob
                            resultSet.getString(4),   // email
                            resultSet.getBoolean(10),  // is manager
                            resultSet.getString(7),   // phoneNumber
                            resultSet.getString(8)    // ssn
                    );
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
        StringBuilder query = new StringBuilder("INSERT INTO " + Account.tableName);
        HashMap<String, String> requiredColumns = new HashMap<String, String>(){
            {
                put(Account.idColumn, String.valueOf(account.getId()));
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
     * @return number of records updated
     */
    public static int updateAccount(Account account) {
        StringBuilder query = new StringBuilder("UPDATE " + Account.tableName);

        HashMap<String, String> columnsToUpdate = new HashMap<String, String>() {
            {
                put("balance", String.valueOf(account.getBalance()));
            }
        };

        return updateRecord(prepareUpdateQuery(query, columnsToUpdate, Account.idColumn, account.getId()));
    }

    /**
     * Delete an account from database
     * @param account Account object
     */
    public static void deleteAccount(Account account) {
        StringBuilder query = new StringBuilder("DELETE FROM " + Account.tableName);
        deleteRecord(prepareDeleteQuery(query, Account.idColumn, account.getId()));
    }

    /**
     * Get account with account number from database
     * @param accountNo account number
     * @return Account object
     */
    public static Account getAccount(long accountNo) {
        int id = (int) (accountNo - Account.ACCOUNT_NO_BASE);
        String query = "SELECT * FROM " + Account.tableName + " WHERE " + Account.accountNoColumn + " = '" + id + "';";
        Account account = null;

        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                if (resultSet.getRow() == 1) {
                    int pk_id = resultSet.getInt(1);
                    int customer_id = resultSet.getInt(2);
                    long account_no = resultSet.getLong(3);
                    double balance = resultSet.getDouble(5);
                    AccountType accountType = AccountType.valueOf(resultSet.getString(4).toUpperCase());

                    switch (accountType) {
                        case SAVING:
                            account = new SavingAccount(pk_id, customer_id, account_no, balance);
                            break;
                        case CHECKING:
                            account = new CheckingAccount(pk_id, customer_id, account_no, balance);
                            break;
                        case SECURITIES:
                            account = new SecuritiesAccount(pk_id, customer_id, account_no, balance);
                            break;
                        case LOAN:
                            Loan loan = getLoanWithAcId(pk_id);
                            account = new LoanAccount(pk_id, customer_id, account_no, balance, loan);
                            break;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());  // TODO (shubham): Implement logger
        }
        return account;
    }

    /**
     * Fetch all accounts for a customer
     * @param customerId customer id
     * @return list of all accounts
     */
    public static List<Account> getAccounts(int customerId) {
        String query = "SELECT * FROM " + Account.tableName + " WHERE " + Account.cidColumn + " = '" + customerId + "';";
        List<Account> accounts = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                int pkId = resultSet.getInt(1);
                int cid = resultSet.getInt(2);
                long accountNo = resultSet.getLong(3);
                double balance = resultSet.getDouble(5);
                AccountType accountType = AccountType.valueOf(resultSet.getString(4).toUpperCase());

                switch (accountType) {
                    case SAVING:
                        accounts.add(new SavingAccount(pkId, cid, accountNo, balance));
                        break;
                    case CHECKING:
                        accounts.add(new CheckingAccount(pkId, cid, accountNo, balance));
                        break;
                    case SECURITIES:
                        accounts.add(new SecuritiesAccount(pkId, cid, accountNo, balance));
                        break;
                    case LOAN:
                        Loan loan = getLoanWithAcId(pkId);
                        accounts.add(new LoanAccount(pkId, cid, accountNo, balance, loan));
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());  // TODO (shubham): Implement logger
        }

        return accounts;
    }

    /**
     * Fetch all accounts of a type
     * @param accountTypes types of account
     * @return list of all accounts
     */
    public static List<Account> getAccounts(List<AccountType> accountTypes) {
        String inTypes = "'" +
                accountTypes.stream().map(AccountType::toString).collect(Collectors.joining("', '"))
                + "'";
        String query = "SELECT * FROM " + Account.tableName + " WHERE account_type IN [" + inTypes + "];";
        List<Account> accounts = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                int pkId = resultSet.getInt(1);
                int cid = resultSet.getInt(2);
                long accountNo = resultSet.getLong(3);
                double balance = resultSet.getDouble(5);
                AccountType accountType = AccountType.valueOf(resultSet.getString(4).toUpperCase());

                switch (accountType) {
                    case SAVING:
                        accounts.add(new SavingAccount(pkId, cid, accountNo, balance));
                        break;
                    case CHECKING:
                        accounts.add(new CheckingAccount(pkId, cid, accountNo, balance));
                        break;
                    case SECURITIES:
                        accounts.add(new SecuritiesAccount(pkId, cid, accountNo, balance));
                        break;
                    case LOAN:
                        Loan loan = getLoanWithAcId(pkId);
                        accounts.add(new LoanAccount(pkId, cid, accountNo, balance, loan));
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());  // TODO (shubham): Implement logger
        }

        return accounts;
    }

    /**
     * Add new transaction record to the transaction table
     * @param transaction Transaction object
     * @return id of the inserted record
     */
    public static int addTransaction(Transaction transaction) {
        StringBuilder query = new StringBuilder("INSERT INTO " + Transaction.tableName);
        HashMap<String, String> requiredColumns = new HashMap<String, String>(){
            {
                put(Transaction.idColumn, String.valueOf(transaction.getId()));
                put("aid", String.valueOf(transaction.getAid()));
                put("message", transaction.getMessage());
                put("date", transaction.getTodayDate().toString());
                put("old_value", String.valueOf(transaction.getOldValue()));
                put("new_value", String.valueOf(transaction.getNewValue()));
            }
        };
        return addRecord(prepareInsertQuery(query, requiredColumns));
    }

    /**
     * Fetch all transactions of a particular account
     * @param accounts list of Account objects
     * @return list of transactions
     */
    public static List<Transaction> getTransactions(List<Account> accounts) {
        StringBuilder aids = new StringBuilder();

        for (Account account : accounts) {
            aids.append("'").append(account.getId()).append("', ");
        }
        aids.deleteCharAt(aids.length() - 1);

        String query = "SELECT * FROM " + Transaction.tableName + " WHERE " + Transaction.aidColumn + " IN (" + aids + ");";
        List<Transaction> transactions = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                if (resultSet.getRow() == 1) {
                    int tid = resultSet.getInt(1);
                    int aid = resultSet.getInt(2);
                    String message = resultSet.getString(3);
                    double oldValue = resultSet.getDouble(5);
                    double newValue = resultSet.getDouble(6);
                    LocalDate date = LocalDate.parse(resultSet.getString(4));

                    transactions.add(new Transaction(tid, aid, message, oldValue, newValue, date));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());  // TODO (shubham): Implement logger
        }
        return transactions;
    }

    /**
     * Fetch all transactions of a particular customer
     * @param customerId Customer id
     * @return list of transactions
     */
    public static List<Transaction> getTransactions(int customerId) {
        List<Account> accounts = getAccounts(customerId);
        return getTransactions(accounts);
    }

    /**
     * Add loan as a new record in database
     * @param loan Loan object
     * @return inserted record id
     */
    public static int addLoan(Loan loan) {
        StringBuilder query = new StringBuilder("INSERT INTO " + Loan.tableName);
        HashMap<String, String> requiredColumns = new HashMap<String, String>(){
            {
                put(Loan.idColumn, String.valueOf(loan.getId()));
                put(Loan.aidColumn, String.valueOf(loan.getAid()));
                put(Loan.cidColumn, String.valueOf(loan.getCid()));
                put("name", loan.getName());
                put("amount", String.valueOf(loan.getAmount()));
                put("collateral_id", String.valueOf(loan.getCollateral().getId()));
            }
        };

        return addRecord(prepareInsertQuery(query, requiredColumns));
    }

    public static List<Transaction> getTransactions() {
        String query = "SELECT * FROM " + Transaction.tableName;
        List<Transaction> transactions = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                int tid = resultSet.getInt(1);
                int aid = resultSet.getInt(2);
                String message = resultSet.getString(3);
                double oldValue = resultSet.getDouble(5);
                double newValue = resultSet.getDouble(6);
                LocalDate date = LocalDate.parse(resultSet.getString(4));
                transactions.add(new Transaction(tid, aid, message, oldValue, newValue, date));

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return transactions;
    }

    /**
     * Update the loan object
     * @param loan Loan object to be updated
     * @return number of records updated
     */
    public static int updateLoan(Loan loan) {
        StringBuilder query = new StringBuilder("UPDATE " + Loan.tableName);

        HashMap<String, String> columnsToUpdate = new HashMap<String, String>() {
            {
                put("amount", String.valueOf(loan.getAmount()));
            }
        };

        return updateRecord(prepareUpdateQuery(query, columnsToUpdate, Loan.idColumn, loan.getId()));
    }

    /**
     * Get loan object with account id
     * @param aid Account id
     * @return Loan object
     */
    public static Loan getLoanWithAcId(int aid) {
        return getLoan(Loan.aidColumn, aid);
    }

    /**
     * Get loan from database using idColumn name and id
     * @param idColumn String name of the id column
     * @param id integer id to be looked for
     * @return Loan object
     */
    public static Loan getLoan(String idColumn, int id) {
        String query = "SELECT * FROM " + Loan.tableName + " WHERE " + idColumn + " = '" + id + "';";
        Loan loan = null;

        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                if (resultSet.getRow() == 1) {
                    int pk_id = resultSet.getInt(1);
                    int aid = resultSet.getInt(2);
                    int cid = resultSet.getInt(3);
                    String name = resultSet.getString(4);
                    double amount = resultSet.getDouble(5);
                    Collateral collateral = getCollateral(resultSet.getInt(6));
                    loan = new Loan(pk_id, cid, aid, name, amount, collateral);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());  // TODO (shubham): Implement logger
        }
        return loan;
    }

    /**
     * Get loan from database
     * @param id id of the loan
     * @return return Loan object
     */
    public static Loan getLoanWithId(int id) {
        return getLoan(Loan.idColumn, id);
    }

    /**
     * Delete an loan entry from database
     * @param loan Loan object
     */
    public static void deleteLoan(Loan loan) {
        StringBuilder query = new StringBuilder("DELETE FROM " + Loan.tableName);
        deleteRecord(prepareDeleteQuery(query, Loan.idColumn, loan.getId()));
    }

    /**
     * Add collateral as a new record in database
     * @param collateral Collateral object
     * @return inserted record id
     */
    public static int addCollateral(Collateral collateral) {
        StringBuilder query = new StringBuilder("INSERT INTO " + Collateral.tableName);
        HashMap<String, String> requiredColumns = new HashMap<String, String>(){
            {
                put(Collateral.idColumn, String.valueOf(collateral.getId()));
                put(Collateral.cidColumn, String.valueOf(collateral.getCid()));
                put("name", collateral.getName());
                put("value", String.valueOf(collateral.getValue()));
                put("in_use", String.valueOf(collateral.inUse()));
            }
        };

        return addRecord(prepareInsertQuery(query, requiredColumns));
    }

    /**
     * Update the collateral object
     * @param collateral Loan object to be updated
     * @return number of records updated
     */
    public static int updateCollateral(Collateral collateral) {
        StringBuilder query = new StringBuilder("UPDATE " + Collateral.tableName);

        HashMap<String, String> columnsToUpdate = new HashMap<String, String>() {
            {
                put("in_use", String.valueOf(collateral.inUse()));
            }
        };

        return updateRecord(prepareUpdateQuery(query, columnsToUpdate, Collateral.idColumn, collateral.getId()));
    }

    /**
     * Delete an collateral entry from database
     * @param collateral Collateral object
     */
    public static void deleteCollateral(Collateral collateral) {
        StringBuilder query = new StringBuilder("DELETE FROM " + Collateral.tableName);
        deleteRecord(prepareDeleteQuery(query, Collateral.idColumn, collateral.getId()));
    }

    /**
     * Get collateral from database using given id
     * @param id integer id to be looked for
     * @return Collateral object
     */
    public static Collateral getCollateral(int id) {
        String query = "SELECT * FROM " + Collateral.tableName + " WHERE " + Collateral.idColumn + " = '" + id + "';";
        Collateral collateral = null;

        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                if (resultSet.getRow() == 1) {
                    int pk_id = resultSet.getInt(1);
                    int cid = resultSet.getInt(2);
                    String name = resultSet.getString(3);
                    double value = resultSet.getDouble(4);
                    boolean inUse = resultSet.getBoolean(5);
                    collateral = new Collateral(pk_id, cid, name, value);
                    if (inUse) {
                        collateral.setInUse();
                    } else {
                        collateral.setNotInUse();
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());  // TODO (shubham): Implement logger
        }
        return collateral;
    }
    
    public static List<Stock> fetchAllStocks() {
        return new ArrayList<Stock>();
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
