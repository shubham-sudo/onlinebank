package bank.atm;

import bank.accounts.*;
import bank.customers.assets.Collateral;
import bank.currencies.Currency;
import bank.customers.Customer;
import bank.trades.Holding;

import java.util.List;


/**
 * CustomerATM is an interface used for the customer session
 * ATM can have specific sessions for different customers and
 * one session for ManagerATM.
 */
public interface CustomerATM {

    List<Account> getAccounts();
    List<Collateral> getCollaterals();
    List<Holding> getHoldings();
    List<Transaction> getLatestTransactions();
    Customer getLoggedInCustomer();

    /**
     * Customer can open a new account, the account
     * type would mention the type of account it is.
     * @param accountType Type of account Customer wants to open
     * @param balance starting balance for this account.
     * @return true if created successfully, false otherwise
     * @throws IllegalStateException exception if invalid state
     */
    boolean openAccount(AccountType accountType, double balance) throws IllegalStateException;

    /**
     * Customer can request for closing this account
     * @param account Account to be closed
     * @param password to double check
     * @return true if closed successfully, false otherwise
     * @throws IllegalStateException exception if illegal state
     */
    boolean closeAccount(Account account, String password) throws IllegalStateException;

    /**
     * Add new collateral for requesting loan
     * @param name Name of the collateral
     * @param value total value of collateral
     * @return true if added successfully
     */
    boolean addCollateral(String name, double value);

    /**
     * Customer can request loans
     * @param customer Customer object
     * @param collateral using collateral
     * @param value amount for loan
     * @return true if success, false otherwise
     */
    boolean requestLoan(Customer customer, Collateral collateral, double value) throws IllegalStateException;

    /**
     * View all transactions for any of the account
     * @param account Account object
     * @return List of transactions
     */
    List<Transaction> viewTransactions(Account account);

    /**
     * Check the current balance of an account
     * @param account Account object
     * @return current balance
     */
    double currentBalance(Account account);

    /**
     * Add some money into account
     * @param account Account object
     * @param amount amount value to be added
     * @param currency Currency which is to be added
     * @return true if transaction successful, false otherwise
     * @throws IllegalStateException exception if invalid
     */
    boolean deposit(Account account, double amount, Currency currency) throws IllegalStateException;

    /**
     * Withdrawal the money from the customer account
     * @param account Account object
     * @param amount amount to be withdrawal
     * @param currency Currency to be withdrawn
     * @return true if done successfully, false otherwise
     */
    boolean withdrawal(Account account, double amount, Currency currency) throws IllegalStateException;

    /**
     * Transfer amount from one account to another, If from account
     * is savings the minimum cap is upto $ 2500. Should not go below.
     * @param from source account
     * @param to target account
     * @param amount amount to be transferred
     * @return true if transaction successful, false otherwise
     * @throws IllegalStateException if not possible
     */
    boolean transferAmount(Account from, Account to, double amount) throws IllegalStateException;

    /**
     * Change password of the customer
     * @return true if changed
     */
    boolean changePassword();

    /**
     * Buy stock using securities account
     * @param customer Customer object
     * @param account Account object
     * @return true if bought success, false otherwise
     */
    boolean buyStock(Customer customer, SecuritiesAccount account);

    /**
     * Sell stock using securities account
     * @param customer Customer object
     * @param account Account object
     * @return true if sold success, false otherwise
     */
    boolean sellStock(Customer customer, SecuritiesAccount account);
}
