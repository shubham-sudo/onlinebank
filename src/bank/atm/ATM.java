package bank.atm;

import bank.customers.Customer;
import bank.factories.*;
import bank.repositories.*;
import bank.trades.Stock;
import databases.DbConnection;

import java.sql.Connection;
import java.util.List;


public abstract class ATM {
    protected Customer loggedInPerson;

    private static final Connection connection = DbConnection.getConnection();

    protected final AccountRepository accountRepository;
    protected final LoanRepository loanRepository;
    protected final CustomerRepository customerRepository;
    protected final CollateralRepository collateralRepository;
    protected final TransactionRepository transactionRepository;
    protected final StockRepository stockRepository;
    protected final HoldingRepository holdingRepository;

    protected static final AccountFactory accountFactory = new AccountFactory();
    protected static final LoanFactory loanFactory = new LoanFactory();
    protected static final CollateralFactory collateralFactory = new CollateralFactory();
    protected static final TransactionFactory transactionFactory = new TransactionFactory();
    protected static final StockFactory stockFactory = new StockFactory();
    protected static final HoldingFactory holdingFactory = new HoldingFactory();

    public ATM() {
        this.accountRepository = AccountAdapter.getInstance(connection);
        this.loanRepository = LoanAdapter.getInstance(connection);
        this.collateralRepository = CollateralAdapter.getInstance(connection);
        this.transactionRepository = TransactionAdapter.getInstance(connection);
        this.customerRepository = CustomerAdapter.getInstance(connection);
        this.stockRepository = StockAdapter.getInstance(connection);
        this.holdingRepository = HoldingAdapter.getInstance(connection);
    }

    /**
     * Fetch all stocks list from database
     * @return List of stock
     */
    public List<Stock> pullAllStocks() {
        return stockRepository.read();
    }

     /**
     * Greet upon login!
     * @return Greeting message
     */
    abstract String greet();

    /**
     * Login anyone from ATM
     * @param email Email address of user
     * @param password of user
     *
     * @return Customer if successful
     */
    public Customer login(String email, String password) {
        Customer customer = customerRepository.readByEmailAndPassword(email, password);
        if (customer != null) {
            startSession(customer);
        }
        return customer;
    }

    /**
     * Logout anyone from ATM
     */
    public void logout() {
        endSession();
    }

    abstract void startSession(Customer customer);

    abstract void endSession();
}
