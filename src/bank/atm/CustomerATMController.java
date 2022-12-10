package bank.atm;

import bank.factory.AccountFactory;
import bank.account.*;
import bank.currency.Currency;
import bank.customer.Customer;
import bank.customer.assets.Collateral;
import bank.factory.CollateralFactory;
import bank.trade.Holding;
import database.Database;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;


public class CustomerATMController implements CustomerATM{
    private static final AccountFactory accountFactory = new AccountFactory();
    private static final CollateralFactory collateralFactory = new CollateralFactory();
    private static CustomerATM customerATM = null;
    private HashMap<Integer, Account> accounts;
    private HashMap<Integer, Collateral> collaterals;
    private HashMap<Integer, Holding> holdings;
    private List<Transaction> transactions;
    private Customer customer;

    private CustomerATMController() {
        this.accounts = new HashMap<>();
        this.collaterals = new HashMap<>();
        this.holdings = new HashMap<>();
        this.transactions = new ArrayList<>();
    }

    public static CustomerATM getInstance() {
        if (customerATM == null) {
            customerATM = new CustomerATMController();
        }
        return customerATM;
    }

    private void setUpDashBoard() {
        pullCustomerAccounts();
        pullCustomerCollaterals();
        pullCustomerHoldings();
        pullLatestTransactions();
        pullAllStocks();
    }

    private void pullCustomerCollaterals() {
        // TODO : pull customer collaterals
    }
    
    private void pullCustomerHoldings() {
        // TODO: pull customer bought holdings
    }
    
    private void pullCustomerAccounts() {
        List<Account> accounts = Database.getAccounts(this.customer.getId());
        for (Account account : accounts) {
            this.accounts.put(account.getId(), account);
        }
    }
    
    private void pullLatestTransactions() {
        // TOOD: pull latest transactions of the customer
    }
    
    @Override
    public List<Account> getAccounts() {
        return new ArrayList<>(this.accounts.values());
    }
    
    @Override
    public List<Collateral> getCollaterals() {
        return new ArrayList<>(this.collaterals.values());
    }
    
    @Override
    public List<Holding> getHoldings() {
        return new ArrayList<>(this.holdings.values());
    }
    
    @Override
    public List<Transaction> getLatestTransactions() {
        return this.transactions;
    }
    
    @Override
    public Customer getLoggedInCustomer(){
        return this.customer;
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
        if (account.getAccountType() == AccountType.SECURITIES) {
            throw new IllegalStateException("Can't withdrawal money from Securities account");
        }
        account.debit(amount, currency);
        return true;
    }

    @Override
    public boolean transferAmount(Account from, Account to, double amount) throws IllegalStateException {
        from.transfer(amount, to);
        return true;
    }
    
    @Override
    public String greet() {
        return "Hi " + this.customer.getLastName();
    }
    
    @Override
    public boolean login(String email, String password) {
        Customer customer = Database.getCustomer(email, password);
        if (customer != null) {
            this.customer = customer;
            setUpDashBoard();
            return true;
        }
        return false;
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
