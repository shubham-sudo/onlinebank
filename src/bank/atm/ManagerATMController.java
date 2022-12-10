package bank.atm;

import bank.account.*;
import bank.customer.Customer;
import bank.trade.Stock;
import database.Database;
import bank.currency.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;


public class ManagerATMController implements ManagerATM{
    private static ManagerATM managerATM = null;
    private Customer manager;


    private ManagerATMController() {
        // TODO initialize anything that is required
    }

    public static ManagerATM getInstance() {
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
        return Database.getCustomer(email);
    }

    @Override
    public List<Customer> getCustomers(){
        return Database.getCustomers();
    }

    @Override
    public List<Transaction> getTransaction(LocalDate todayDate){
        List<Transaction> transactions = Database.getTransactions();
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
        return Database.getTransactions();
    }

    @Override
    public boolean addStock(Stock stock){
        // Todo
        return false;
    }

    @Override
    public boolean removeStock(Stock stock){
        // Todo
        return false;
    }

    @Override
    public boolean updateStock(Stock stock){
        // Todo
        return false;
    }

    @Override
    public void payInterest(double rate){
        List<? extends Account> savingAccounts = Database.getAccounts(new ArrayList<>(Collections.singletonList(AccountType.SAVING)));

        if (rate == 0) {
            rate = SAVING_INTEREST_RATE;
        }

        for(Account account : savingAccounts){
            SavingAccount savingAccount = (SavingAccount) account;
            if (account.getBalance() > 2) {  // if balance is more than 2 pay interest
                savingAccount.credit((savingAccount.getBalance() * rate), new USDollar((savingAccount.getBalance() * rate)));
                savingAccount.update();
            }
        }
    }

    @Override
    public void chargeInterest(double rate){
        List<? extends Account> loanAccounts = Database.getAccounts(new ArrayList<>(Collections.singletonList(AccountType.LOAN)));

        if (rate == 0) {
            rate = LOAN_INTEREST_RATE;  // default rate
        }

        for(Account account : loanAccounts){
            LoanAccount loanAccount = (LoanAccount) account;
            if (account.getBalance() < loanAccount.getLoan().getAmount()) {
                double usedAmount = loanAccount.getLoan().getAmount() - account.getBalance();
                loanAccount.getLoan().addInterest(usedAmount * rate);
                loanAccount.getLoan().update();
            }
        }
    }

    @Override
    public String greet() {
        return "Hi, " + manager.getLastName();
    }

    @Override
    public void endSession() {
        this.manager = null;
        // TODO (shubham) make rest of the stuff also null
    }

    @Override
    public void startSession(Customer manager) {
        this.manager = manager;
    }
}
