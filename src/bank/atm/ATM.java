package bank.atm;

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
     * @return true if successful
     */
    boolean login(String email, String password);

    /**
     * Logout anyone from ATM
     * @return true
     */
    boolean logout();

}
