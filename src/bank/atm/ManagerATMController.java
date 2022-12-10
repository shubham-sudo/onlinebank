package bank.atm;

import bank.account.Account;
import bank.account.SavingAccount;
import bank.account.Transaction;
import bank.account.AccountType;
import bank.customer.Customer;
import bank.customer.CustomerFactory;
import bank.trade.Stock;
import database.Database;
import database.DbConnection;
import bank.currency.*;

import java.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class ManagerATMController implements ManagerATM{
    private static final Connection connection = DbConnection.getConnection();
    private static final CustomerFactory customerFactory = new CustomerFactory();
    private Customer manager;

    @Override
    public Customer getCustomer(String email){
        return Database.getCustomer(email);
    }

    @Override
    public List<Customer> getCustomers(){
        List<Customer> list = new ArrayList();
        String query = "SELECT * FROM customer;";
        Customer customer = null;
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {

                    customer = customerFactory.createCustomer(
                            resultSet.getInt(1),      // id
                            resultSet.getString(2),   // firstname
                            resultSet.getString(3),   // lastname
                            resultSet.getString(5),   // dob
                            resultSet.getString(4),   // email
                            resultSet.getBoolean(5),  // is manager
                            resultSet.getString(7),   // phoneNumber
                            resultSet.getString(8)    // ssn
                    );
                    list.add(customer);


            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    @Override
    public List<Transaction> getTransaction(LocalDate todayDate){
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
        List<Transaction> transofdate = new ArrayList();
        for(Transaction tr : transactions){
            if(tr.getTodayDate().equals(todayDate)) transofdate.add(tr);
        }
        return transofdate;
    }

    @Override
    public List<Transaction> getTransactions(){
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

    @Override
    public boolean addStock(Stock stock){
        // Todo
        return false;
    }

    @Override
    public boolean removeStock(Stock stock){
        // Todo
        return false;
    }

    @Override
    public boolean updateStock(Stock stock){
        // Todo
        return false;
    }

    @Override
    public void payInterest(double rate){
        // Find all the saving accounts
        String query = "SELECT * FROM " + Account.tableName;
        Account account = null;
        List<Account> list = new ArrayList();

        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                if (resultSet.getRow() == 1) {
                    int pk_id = resultSet.getInt(1);
                    int customer_id = resultSet.getInt(2);
                    long account_no = resultSet.getLong(3);
                    double balance = resultSet.getDouble(5);
                    AccountType accountType = AccountType.valueOf(resultSet.getString(4));
                    if(accountType.toString().equals("saving")){
                        account = new SavingAccount(pk_id, customer_id, account_no, balance);
                        list.add(account);
                    }

                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        for(Account acc : list){
            double bal = acc.getBalance();
            acc.deposit(bal * rate, new USDollar(1.0));
            Database.updateAccount(acc);
        }
    }

    @Override
    public void chargeInterest(double rate){
        String query = "SELECT * FROM " + Account.tableName;
        Account account = null;
        List<Account> list = new ArrayList();

        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {

                    int pk_id = resultSet.getInt(1);
                    int customer_id = resultSet.getInt(2);
                    long account_no = resultSet.getLong(3);
                    double balance = resultSet.getDouble(5);
                    AccountType accountType = AccountType.valueOf(resultSet.getString(4));
                    if(accountType.toString().equals("loan")){
                        account = new SavingAccount(pk_id, customer_id, account_no, balance);
                        list.add(account);
                    }

                
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        for(Account acc : list){
            double bal = acc.getBalance();
            acc.deposit(bal * rate, new USDollar(1.0));
            Database.updateAccount(acc);
        }
    }
}
