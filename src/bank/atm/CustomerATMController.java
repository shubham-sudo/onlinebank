package bank.atm;

import bank.currencies.USDollar;
import bank.factories.AccountFactory;
import bank.accounts.*;
import bank.currencies.Currency;
import bank.customers.Customer;
import bank.customers.assets.Collateral;
import bank.factories.CollateralFactory;
import bank.factories.LoanFactory;
import bank.loans.Loan;
import bank.trades.Holding;
import bank.trades.Stock;

import java.util.*;


/**
 * Customer controller is responsible for managing all customer operations
 */
public class CustomerATMController extends ATM implements CustomerATM{
    private static final AccountFactory accountFactory = new AccountFactory();
    private static final LoanFactory loanFactory = new LoanFactory();
    private static final CollateralFactory collateralFactory = new CollateralFactory();
    private static CustomerATMController customerATM = null;
    private HashMap<Integer, Account> accounts;
    private HashMap<Integer, Collateral> collaterals;
    private HashMap<Integer, Holding> holdings;
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
        for (Collateral collateral : collateralRepository.readByCustomerId(this.loggedInPerson.getId())){
            collaterals.put(collateral.getId(), collateral);
        }
    }

    private void pullCustomerHoldings() {
        for (Holding holding : holdingRepository.readByCustomerId(this.loggedInPerson.getId())) {
            holdings.put(holding.getId(), holding);
        }
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

        if (account.getAccountType() == AccountType.LOAN) {
            if (account.getBalance() < ((LoanAccount)account).getLoan().getAmount()) {
                throw new IllegalStateException("Account can't be closed\nPending due amount " + ((((LoanAccount)account).getLoan().getAmount()) - account.getBalance()));
            }
        }

        if (accountRepository.delete(account)) {
            if (account.getAccountType() == AccountType.LOAN) {
                ((LoanAccount)account).getLoan().getCollateral().setNotInUse();
            }
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
    public boolean requestLoan(String collateralName, double collateralValue, double value) throws IllegalStateException {
        if (collateralValue < value) {
            throw new IllegalStateException("Can't issue loan higher than collateral value");
        }
        Account account = accountFactory.createAccount(this.loggedInPerson, AccountType.LOAN, value);
        Collateral newCollateral = collateralFactory.createCollateral(this.loggedInPerson, collateralName, collateralValue);
        newCollateral.setInUse();
        newCollateral = collateralRepository.create(newCollateral);
        Loan loan = loanFactory.createLoan(this.loggedInPerson, value, newCollateral);
        if (account instanceof LoanAccount) {
            ((LoanAccount) account).setLoan(loan);
        }
        account = accountRepository.create(account);
        loan.setAid(account.getId());
        loan = loanRepository.create(loan);
        accounts.put(account.getId(), account);
        collaterals.put(newCollateral.getId(), newCollateral);
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
        double oldAmount = from.getBalance();
        Transaction fromAccountTransaction = transactionFactory.createTransaction(
                from.getId(),
                "Account debited with $" + amount,
                oldAmount,
                from.getBalance()
        );
        fromAccountTransaction = transactionRepository.create(fromAccountTransaction);
        transactions.add(fromAccountTransaction);
        accountRepository.update(from);
        double toOldAmount = from.getBalance();
        Transaction toAccountTransaction = transactionFactory.createTransaction(
                to.getId(),
                "Account credited with $" + amount,
                toOldAmount,
                to.getBalance()
        );
        toAccountTransaction = transactionRepository.create(toAccountTransaction);
        transactions.add(toAccountTransaction);
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
        this.collaterals = null;
        this.holdings = null;
        this.transactions = null;
    }

    @Override
    public boolean changePassword(String email, String ssn, String newPassword) throws IllegalStateException{
        Customer customer = customerRepository.readByEmail(email);
        if (customer == null) {
            throw new IllegalStateException("This email doesn't exists");
        } else if (customer.getPlainSSN() != ssn) {
            throw new IllegalStateException("SSN provided doesn't match");
        } else {
            customerRepository.update(customer, newPassword);
        }
        return true;
    }

    @Override
    public boolean buyStock(SecuritiesAccount account, Stock stock, int quantity) throws IllegalStateException {
        if (account.getBalance() < (stock.getValue() * quantity)) {
            throw new IllegalStateException("Not enough funds to buy!!");
        }

        boolean alreadyHolding = false;

        for (Holding holding : holdings.values()) {
            if (holding.getSid() == stock.getId()) {
                alreadyHolding = true;
                holding.setQuantity(holding.getQuantity() + quantity);
                Holding updatedHolding = holdingRepository.update(holding);
                holdings.put(updatedHolding.getId(), updatedHolding);
                break;
            }
        }
        if (!alreadyHolding) {
            Holding holding = holdingFactory.createHolding(this.loggedInPerson.getId(), stock.getId(), quantity, stock.getValue());
            holding = holdingRepository.create(holding);
            holdings.put(holding.getId(), holding);
        }
        double oldAmount = account.getBalance();
        account.debit(stock.getValue() * quantity, new USDollar(stock.getValue() * quantity));
        Transaction buyStock = transactionFactory.createTransaction(
                account.getId(),
                "Account debited with $" + stock.getValue() * quantity,
                oldAmount,
                account.getBalance()
        );
        buyStock = transactionRepository.create(buyStock);
        transactions.add(buyStock);
        accountRepository.update(account);
        return true;
    }

    @Override
    public boolean sellStock(SecuritiesAccount account, Stock stock, int quantity) throws IllegalStateException {
        boolean hasStock = false;
        Holding holdingStock = null;

        for (Holding holding : holdings.values()) {
            if (holding.getSid() == stock.getId()) {
                hasStock = true;
                holdingStock = holding;
                break;
            }
        }

        if (hasStock) {
            if (quantity > holdingStock.getQuantity()) {
                throw new IllegalStateException("Invalid quantity mentioned");
            } else {
                if (quantity == holdingStock.getQuantity()) {
                    double currentValue = holdingStock.getCurrentValue();
                    holdingRepository.delete(holdingStock);
                    double oldAmount = account.getBalance();
                    account.credit(currentValue, new USDollar(currentValue));
                    Transaction stockSold = transactionFactory.createTransaction(
                            account.getId(),
                            "Account credited with $" + currentValue,
                            oldAmount,
                            account.getBalance()
                    );
                    stockSold = transactionRepository.create(stockSold);
                    transactions.add(stockSold);
                } else {
                    double valueToSell = holdingStock.getBaseValue() * quantity;
                    holdingStock.setQuantity(holdingStock.getQuantity() - quantity);
                    holdingRepository.update(holdingStock);
                    double oldAmount = account.getBalance();
                    account.credit(valueToSell, new USDollar(valueToSell));
                    Transaction stockSold = transactionFactory.createTransaction(
                            account.getId(),
                            "Account credited with $" + valueToSell,
                            oldAmount,
                            account.getBalance()
                    );
                    stockSold = transactionRepository.create(stockSold);
                    transactions.add(stockSold);
                }
                accountRepository.update(account);
                return true;
            }
        }
        throw new IllegalStateException("Stock Not found in holdings!");
    }
}
