package bank.account;

import database.Database;
import database.DbModel;
import bank.currency.Currency;

import java.util.ArrayList;
import java.util.List;


/**
 * Account is an abstract base class for Checking, Saving & Securities
 * @see CheckingAccount
 * @see SavingAccount
 * @see SecuritiesAccount
 */
public abstract class Account implements DbModel {
    protected static final String tableName = "account";
    protected static final String idColumn = "id";
    private final int id;
    private final int cid;
    private final long accountNo;
    protected AccountType accountType;
    private double balance;

    public Account(int id, int cid, long accountNo, double balance){
        this.id = id != 0 ? id : getNewId();
        this.cid = cid;
        this.accountNo = accountNo;
        this.balance = balance;
    }

    private int getNewId() {
        return Database.getNewId(tableName, idColumn);
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
                this.balance, this.balance + amountToCredit);
        transaction.save();
        this.balance += amountToCredit;
        this.update();
    }

    /**
     * Debit account with given amount
     * @param amount amount value
     * @param currency Currency object
     * @return true if successful, false otherwise
     */
    public boolean debit(double amount, Currency currency) {
        if (!isSafeToDebit(amount, currency)) {
            return false;
        }

        double amountToDebit = debitAmount(amount, currency);
        Transaction transaction = new Transaction(0, id, "debit amount in " + currency.toString(),
                this.balance, this.balance - amountToDebit);
        transaction.save();
        this.balance -= amountToDebit;
        return true;
    }

    protected abstract boolean isSafeToDebit(double amount, Currency currency);
    protected abstract double creditAmount(double amount, Currency currency);
    protected abstract double debitAmount(double amount, Currency currency);

    public List<Transaction> history(){
        List<Transaction> transactions = new ArrayList<>();
        // TODO (shubham): add transaction table & fetch Database.getTransactions(this);
        return transactions;
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
        return Database.addAccount(this);
    }

    /**
     * Update the value in database
     */
    @Override
    public int update() {
        return Database.updateAccount(this, idColumn);
    }

    @Override
    public void delete() {
        Database.deleteAccount(this, idColumn);
    }

    @Override
    public String toString() {
        return "Account<" + this.accountNo + ">";
    }
}
