package bank.atm;

import bank.customer.Customer;
import bank.trade.Stock;
import database.Database;
import java.util.List;


public interface ATM {
    
    /**
     * Fetch all stocks list from database
     * @return List of stock
     */
    default List<Stock> pullAllStocks() {
        return Database.fetchAllStocks();
    }
    
     /**
     * Greet upon login!
     * @return Greeting message
     */
    String greet();
    
    /**
     * Login anyone from ATM
     * @param email Email address of user
     * @param password of user
     * 
     * @return Customer if successful
     */
    default Customer login(String email, String password) {
        Customer customer = Database.getCustomer(email, password);
        if (customer != null) {
            startSession(customer);
        }
        return customer;
    }

    /**
     * Logout anyone from ATM
     */
    default void logout() {
        endSession();
    }

    void startSession(Customer customer);

    void endSession();
}
