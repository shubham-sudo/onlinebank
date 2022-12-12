package bank.atm;

import bank.factories.AccountFactory;
import bank.accounts.*;
import bank.currencies.Currency;
import bank.customers.Customer;
import bank.customers.assets.Collateral;
import bank.factories.CollateralFactory;
import bank.factories.LoanFactory;
import bank.loans.Loan;
import bank.trades.Holding;

import java.util.*;


public class CustomerATMController extends ATM implements CustomerATM{
    private static final AccountFactory accountFactory = new AccountFactory();
    private static final LoanFactory loanFactory = new LoanFactory();
    private static final CollateralFactory collateralFactory = new CollateralFactory();
    private static CustomerATMController customerATM = null;
    private HashMap<Integer, Account> accounts;
    private final HashMap<Integer, Collateral> collaterals;
    private final HashMap<Integer, Holding> holdings;
    private List<Transaction> transactions;

    private CustomerATMController() {
        this.accounts = new HashMap<>();
        this.collaterals = new HashMap<>();
        this.holdings = new HashMap<>();
        this.transactions = new ArrayList<>();
    }

    public static CustomerATMController getInstance() {
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
        List<Account> accounts = accountRepository.readByCustomerId(this.loggedInPerson.getId());
        for (Account account : accounts) {
            this.accounts.put(account.getId(), account);
        }
    }

    private void pullLatestTransactions() {
        this.transactions = transactionRepository.readByCustomerId(this.loggedInPerson.getId());
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
        if (this.transactions.size() > 10) {
            return this.transactions.subList(0, 10);
        }
        return this.transactions;
    }

    @Override
    public Customer getLoggedInCustomer(){
        return this.loggedInPerson;
    }

    @Override
    public boolean openAccount(AccountType accountType, double balance) throws IllegalStateException{
        Account account = accountFactory.createAccount(this.loggedInPerson, accountType, balance);
        account = accountRepository.create(account);
        accounts.put(account.getId(), account);
        return true;
    }

    @Override
    public boolean closeAccount(Account account, String password) throws IllegalStateException {
        if (!loggedInPerson.verifyPassword(password)) {
            throw new IllegalStateException("Password didn't match, Try again!");
        }
        if (accountRepository.delete(account)) {
            accounts.remove(account.getId());
            return true;
        }
        return false;
    }

    @Override
    public boolean addCollateral(String name, double value) {  // Assuming the customer will add true value
        Collateral  collateral = collateralFactory.createCollateral(loggedInPerson, name, value);
        collateralRepository.create(collateral);
        return true;
    }

    @Override
    public boolean requestLoan(Customer customer, Collateral collateral, double value) throws IllegalStateException {
        if (collateral.getValue() < value) {
            throw new IllegalStateException("Can't issue loan higher than collateral value");
        }
        Account account = accountFactory.createAccount(customer, AccountType.LOAN, value);
        Loan loan = loanFactory.createLoan(customer, value, collateral);
        account = accountRepository.create(account);
        loan.setAid(account.getId());
        loan = loanRepository.create(loan);
        accounts.put(account.getId(), account);
        return true;
    }

    @Override
    public List<Transaction> viewTransactions(Account account) {
        return transactionRepository.readByAccountIds(new ArrayList<>(Collections.singletonList(account.getId())));
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
        double oldAmount = account.getBalance();
        account.credit(amount, currency);
        accountRepository.update(account);
        Transaction depositTransaction = transactionFactory.createTransaction(
                account.getId(),
                "Account credited with $" + currency.baseValue(),
                oldAmount,
                account.getBalance()
        );
        depositTransaction = transactionRepository.create(depositTransaction);
        transactions.add(depositTransaction);
        return true;
    }

    @Override
    public boolean withdrawal(Account account, double amount, Currency currency) throws IllegalStateException{
        if (account.getAccountType() == AccountType.SECURITIES) {
            throw new IllegalStateException("Can't withdrawal money from Securities account");
        }
        double oldAmount = account.getBalance();
        account.debit(amount, currency);
        accountRepository.update(account);
        Transaction withdrawalTransaction = transactionFactory.createTransaction(
                account.getId(),
                "Account debited with $" + currency.baseValue(),
                oldAmount,
                account.getBalance()
        );
        withdrawalTransaction = transactionRepository.create(withdrawalTransaction);
        transactions.add(withdrawalTransaction);
        return true;
    }

    @Override
    public boolean transferAmount(Account from, Account to, double amount) throws IllegalStateException {
        from.transfer(amount, to);
        accountRepository.update(from);
        accountRepository.update(to);
        return true;
    }

    @Override
    public String greet() {
        return "Hi " + this.loggedInPerson.getLastName();
    }

    @Override
    public void startSession(Customer customer) {
        this.loggedInPerson = customer;
        setUpDashBoard();
    }

    @Override
    public void endSession() {
        customerATM = null;
        this.loggedInPerson = null;
        this.accounts = null;
    }

    @Override
    public boolean changePassword() {
        // TODO (shubham) Update this !!!
        customerRepository.update(this.loggedInPerson, "");
        return true;
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
