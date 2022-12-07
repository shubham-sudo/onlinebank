package bank.atm;

import bank.account.*;
import bank.currency.Currency;
import bank.customer.Customer;
import bank.customer.assets.Collateral;
import bank.customer.assets.CollateralFactory;
import database.Database;

import java.util.HashMap;
import java.util.List;

public class CustomerATMController implements CustomerATM{
    private static final AccountFactory accountFactory = new AccountFactory();
    private static final CollateralFactory collateralFactory = new CollateralFactory();
    private CustomerATM customerATM = null;
    private HashMap<Integer, Account> accounts;
    private Customer customer;

    private CustomerATMController(Customer customer) {
        this.customer = customer;
        this.accounts = new HashMap<>();
        setUpDashBoard();
    }

    public CustomerATM getInstance(Customer customer) {
        if (customerATM == null) {
            customerATM = new CustomerATMController(customer);
        }
        return customerATM;
    }

    private void setUpDashBoard() {
        pullCustomerAccounts();
    }

    private void pullCustomerAccounts() {
        List<Account> accounts = Database.getAccounts(this.customer.getId());
        for (Account account : accounts) {
            this.accounts.put(account.getId(), account);
        }
    }

    @Override
    public boolean openAccount(AccountType accountType, double balance) throws IllegalStateException{
        Account account = accountFactory.createAccount(this.customer, accountType, balance);
        account.save();
        accounts.put(account.getId(), account);
        return true;
    }

    @Override
    public boolean closeAccount(Account account) throws IllegalStateException {
        account.delete();
        accounts.remove(account.getId());
        return true;
    }

    @Override
    public boolean addCollateral(String name, double value) {  // Assuming the customer will add true value
        Collateral  collateral = collateralFactory.createCollateral(customer, name, value);
        collateral.save();
        return true;
    }

    @Override
    public boolean requestLoan(Customer customer, Collateral collateral, double value) throws IllegalStateException {
        if (collateral.getValue() < value) {
            throw new IllegalStateException("Can't issue loan higher than collateral value");
        }
        Account account = accountFactory.createLoanAccount(customer, value, collateral);
        accounts.put(account.getId(), account);
        return true;
    }

    @Override
    public List<Transaction> viewTransactions(Account account) {
        return account.history();
    }

    @Override
    public double currentBalance(Account account) {
        return account.getBalance();
    }

    @Override
    public boolean deposit(Account account, double amount, Currency currency) throws IllegalStateException {
        if (amount < 0) {
            throw new IllegalStateException("Credit amount can't be less than zero");
        }
        account.credit(amount, currency);
        return true;
    }

    @Override
    public boolean withdrawal(Account account, double amount, Currency currency) throws IllegalStateException{
        account.debit(amount, currency);
        return true;
    }

    @Override
    public boolean transferAmount(Account from, Account to, double amount) throws IllegalStateException {
        from.transfer(amount, to);
        return true;
    }

    @Override
    public boolean logout() {
        this.customerATM = null;
        this.customer = null;
        this.accounts = null;
        return true;
    }

    @Override
    public boolean changePassword() {
        return customer.changePassword();
    }

    @Override
    public boolean buyStock(Customer customer, SecuritiesAccount account) {
        return false;
    }

    @Override
    public boolean sellStock(Customer customer, SecuritiesAccount account) {
        return false;
    }
}
