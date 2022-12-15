package bank.atm;

import bank.accounts.*;
import bank.customers.Customer;
import bank.events.*;
import bank.trades.Stock;
import bank.currencies.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;


/**
 * Manager controller is responsible for managing all manager operations
 */
public class ManagerATMController extends ATM implements ManagerATM{
    private static ManagerATMController managerATM = null;
    private static final EventSender stockUpdateEvent = StockUpdateEventSender.getInstance();
    private static final EventSender stockDeleteEvent = StockDeleteEventSender.getInstance();

    private ManagerATMController() {
        stockUpdateEvent.addReceiver(new StockUpdateEventReceiver());
        stockDeleteEvent.addReceiver(new StockDeleteEventReceiver());
    }

    public static ManagerATMController getInstance() {
        if (managerATM == null) {
            managerATM = new ManagerATMController();
        }
        return managerATM;
    }

    private void setUpDashBoard() {
        // TODO Set up manager dashboard
    }

    @Override
    public Customer getCustomer(String email){
        return customerRepository.readByEmail(email);
    }

    @Override
    public List<Customer> getCustomers(){
        return customerRepository.read();
    }

    @Override
    public List<Transaction> getTransaction(LocalDate todayDate){
        List<Transaction> transactions = transactionRepository.read();
        List<Transaction> transactionOfDate = new ArrayList<>();
        for(Transaction transaction : transactions){
            if(transaction.getTodayDate().equals(todayDate)) {
                transactionOfDate.add(transaction);
            }
        }
        return transactionOfDate;
    }

    @Override
    public List<Transaction> getTransactions(){
        return transactionRepository.read();
    }

    @Override
    public boolean addStock(String stockName, double stockValue){
        Stock stock = stockFactory.createStock(stockName, stockValue);
        stockRepository.create(stock);
        return true;
    }

    @Override
    public boolean removeStock(int stockId){
        Stock stock = stockRepository.readById(stockId);
        if (stock != null) {
            stockDeleteEvent.sendEvents(new StockDeleteEvent(stock));
            stockRepository.delete(stock);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateStock(int stockId, double stockValue){
        Stock stock = stockRepository.readById(stockId);
        if (stock == null) {
            return false;
        }
        stock.setValue(stockValue);
        stockRepository.update(stock);
        stockUpdateEvent.sendEvents(new StockUpdateEvent(stock));
        return true;
    }

    @Override
    public void payInterest(double rate){
        List<? extends Account> savingAccounts = accountRepository.readByAccountTypes(new ArrayList<>(Collections.singletonList(AccountType.SAVING)));

        if (rate == 0) {
            rate = SAVING_INTEREST_RATE;
        }

        for(Account account : savingAccounts){
            SavingAccount savingAccount = (SavingAccount) account;
            if (account.getBalance() > 2) {  // if balance is more than 2 pay interest
                savingAccount.credit((savingAccount.getBalance() * rate), new USDollar((savingAccount.getBalance() * rate)));
                accountRepository.update(savingAccount);
            }
        }
    }

    @Override
    public void chargeInterest(double rate){
        List<? extends Account> loanAccounts = accountRepository.readByAccountTypes(new ArrayList<>(Collections.singletonList(AccountType.LOAN)));

        if (rate == 0) {
            rate = LOAN_INTEREST_RATE;  // default rate
        }

        for(Account account : loanAccounts){
            LoanAccount loanAccount = (LoanAccount) account;
            if (account.getBalance() < loanAccount.getLoan().getAmount()) {
                double usedAmount = loanAccount.getLoan().getAmount() - account.getBalance();
                loanAccount.getLoan().addInterest(usedAmount * rate);
                loanRepository.update(loanAccount.getLoan());
            }
        }
    }

    @Override
    public String greet() {
        return "Hi, " + loggedInPerson.getLastName();
    }

    @Override
    public void endSession() {
        this.loggedInPerson = null;
        // TODO (shubham) make rest of the stuff also null
    }

    @Override
    public void startSession(Customer manager) {
        this.loggedInPerson = manager;
    }
}
