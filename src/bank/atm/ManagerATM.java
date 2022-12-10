package bank.atm;

import bank.account.Transaction;
import bank.customer.Customer;
import bank.trade.Stock;

import java.time.LocalDate;
import java.util.List;


/**
 * ManagerATM represents the manager session to view all customers
 * check specific customer, should see the daily reports on transactions.
 */
public interface ManagerATM extends ATM {
    /**
     * Get a customer based on the customer email id
     * @param email email id
     * @return Customer object
     */
    Customer getCustomer(String email);

    /**
     * List all the customers
     * @return List of Customer
     */
    List<Customer> getCustomers();

    /**
     * Fetch all transactions made today
     * @return List of all Transactions
     */
    List<Transaction> getTransaction(LocalDate todayDate);

    /**
     * Fetch all transactions, it should pull
     * like last 2 years transaction to not load
     * the database for pulling all past transactions
     * @return List of transactions
     */
    List<Transaction> getTransactions();

    /**
     * Add a new stock to database
     * @param stock Stock object
     * @return true if success, false otherwise
     */
    boolean addStock(Stock stock);

    /**
     * Remove stock from database
     * This should also pay all customers
     * who was holding this stock
     * @param stock Stock object
     * @return true if success, false otherwise
     */
    boolean removeStock(Stock stock);

    /**
     * Update stock value
     * @param stock Stock object
     * @return true if success, false otherwise
     */
    boolean updateStock(Stock stock);

    /**
     * Manager would pay interest on all saving accounts
     * IDEALLY, this should be automatic
     */
    void payInterest(double rate);

    /**
     * Charge interest on all loans
     * IDEALLY, this should be automatic
     */
    void chargeInterest(double rate);
}
