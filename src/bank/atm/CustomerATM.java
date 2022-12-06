package bank.atm;

import bank.account.*;
import bank.customer.assets.Collateral;
import bank.currency.Currency;
import bank.customer.Customer;
import bank.loan.Loan;

import java.util.List;


/**
 * CustomerATM is an interface used for the customer session
 * ATM can have specific sessions for different customers and
 * one session for ManagerATM.
 */
public interface CustomerATM extends ATM {
    /**
     * Customer can open a new account, the account
     * type would mention the type of account it is.
     * @param customer Customer object
     * @param account Account object
     * @return true if created successfully, false otherwise
     */
    boolean openAccount(Customer customer, Account account);

    /**
     * Customer can request for closing this account
     * @param customer Customer object
     * @param account Account to be closed
     * @return true if closed successfully, false otherwise
     */
    boolean closeAccount(Customer customer, Account account);

    /**
     * Customer can request loans
     * @param customer Customer object
     * @param loan Loan object
     * @return true if success, false otherwise
     */
    boolean requestLoan(Customer customer, Loan loan);

    /**
     * View all transactions for any of the account
     * @param customer Customer object
     * @param account Account object
     * @return List of transactions
     */
    List<Transaction> viewTransactions(Customer customer, Account account);

    /**
     * Check the current balance of an account
     * @param customer Customer object
     * @param account Account object
     * @return current balance
     */
    double currentBalance(Customer customer, Account account);

    /**
     * Add Collateral for the customer to request for loans
     * @param customer Customer object
     * @param collateral Collateral object
     * @return true if added successfully, false otherwise
     */
    boolean addCollateral(Customer customer, Collateral collateral);

    /**
     * Add some money into account
     * @param customer Customer object
     * @param account Account object
     * @param amount amount value to be added
     * @param currency Currency which is to be added
     * @return true if transaction successful, false otherwise
     */
    boolean deposit(Customer customer, Account account, double amount, Currency currency);

    /**
     * Withdrawal the money from the customer account
     * @param customer Customer object
     * @param account Account object
     * @param amount amount to be withdrawal
     * @param currency Currency to be withdrawn
     * @return true if done successfully, false otherwise
     */
    boolean withdrawal(Customer customer, Account account, double amount, Currency currency);

    /**
     * Transfer amount from one account to another, If from account
     * is savings the minimum cap is upto $ 2500. Should not go below.
     * @param customer Customer object
     * @param from source account
     * @param to target account
     * @param amount amount to be transferred
     * @return true if transaction successful, false otherwise
     */
    boolean transferAmount(Customer customer, Account from, Account to, double amount);

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
