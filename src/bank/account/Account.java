package bank.account;

import database.Database;
import database.DbModel;
import bank.currency.Currency;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Account is an abstract base class for Checking, Saving & Securities
 * @see CheckingAccount
 * @see SavingAccount
 * @see SecuritiesAccount
 * @see LoanAccount
 */
public abstract class Account implements DbModel {
    protected static final double TRANSACTION_SERVICE_CHARGE = 0.10;
    protected static final double ACCOUNT_STARTING_MIN_BALANCE = 1;
    public static final long ACCOUNT_NO_BASE = 1000;
    public static final String tableName = "account";
    public static final String idColumn = "id";
    public static final String cidColumn = "cid";
    private final int id;
    private final int cid;
    private final long accountNo;
    protected AccountType accountType;
    protected double balance;

    /**
     * Create a new account object with provided fields
     * @param id 0 if creating new account
     * @param cid customer id is required
     * @param accountNo 0 if creating new account
     * @param balance starting balance
     */
    public Account(int id, int cid, long accountNo, double balance){
        if (balance < ACCOUNT_STARTING_MIN_BALANCE) {
            throw new IllegalArgumentException("Starting balance should be at-least $ 1!");
        }
        this.id = id != 0 ? id : getNewId();
        this.cid = cid;
        this.accountNo = accountNo != 0 ? accountNo : getNewAcNo();
        this.balance = balance;
    }

    private int getNewId() {
        return Database.getNewId(tableName, idColumn);
    }

    private long getNewAcNo(){
        return ACCOUNT_NO_BASE + id;
    }

    public int getId() {
        return id;
    }

    public int getCid() {
        return cid;
    }

    public long getAccountNo() {
        return accountNo;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    /**
     * Credit account with given amount
     * @param amount amount value
     * @param currency Currency Object
     */
    public void credit(double amount, Currency currency) {
        double amountToCredit = creditAmount(amount, currency);
        Transaction transaction = new Transaction(0, id, "credited amount in " + currency.toString(),
                this.balance, this.balance + amountToCredit, LocalDate.now());
        transaction.save();
        this.balance += amountToCredit;
        this.update();
    }

    /**
     * Debit account with given amount
     * @param amount amount value
     * @param currency Currency object
     * @return true if successful, false otherwise
     * @throws IllegalStateException operation not possible
     */
    public boolean debit(double amount, Currency currency) throws IllegalStateException{
        if (!isSafeToDebit(amount, currency)) {
            throw new IllegalStateException("Operation not possible!");
        }

        double amountToDebit = debitAmount(amount, currency);
        Transaction transaction = new Transaction(0, id, "debit amount in " + currency.toString(),
                this.balance, this.balance - amountToDebit, LocalDate.now());
        transaction.save();
        this.balance -= amountToDebit;
        this.update();
        return true;
    }

    protected abstract boolean isSafeToDebit(double amount, Currency currency);
    protected abstract double creditAmount(double amount, Currency currency);
    protected abstract double debitAmount(double amount, Currency currency);
    public abstract boolean transfer(double amount, Account account) throws IllegalStateException;

    public List<Transaction> history(){
        return Database.getTransactions(new ArrayList<>(Collections.singletonList(this)));
    }

    @Override
    public boolean isValid() {
        return !Database.isIdExists(tableName, idColumn, getId());
    }

    /**
     * Save new record in database
     * @return id of newly inserted record
     */
    @Override
    public int save() {
        if (!isValid()) {
            throw new IllegalStateException("Account already exists!");
        }
        this.balance -= TRANSACTION_SERVICE_CHARGE;  // new account open service
        // TODO (shubham): add this amount to bank wallet/ account.
        return Database.addAccount(this);
    }

    /**
     * Update the value in database
     */
    @Override
    public int update() {
        return Database.updateAccount(this);
    }

    @Override
    public void delete() {
        Database.deleteAccount(this);
    }

    @Override
    public String toString() {
        return "Account<" + this.accountNo + ">";
    }
}
