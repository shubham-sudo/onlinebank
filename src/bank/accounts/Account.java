package bank.accounts;

import bank.currencies.Currency;


/**
 * Account is an abstract base class for Checking, Saving & Securities
 * @see CheckingAccount
 * @see SavingAccount
 * @see SecuritiesAccount
 * @see LoanAccount
 */
public abstract class Account {
    protected static final double TRANSACTION_SERVICE_CHARGE = 0.10;
    protected static final double ACCOUNT_STARTING_MIN_BALANCE = 1;
    public static final long ACCOUNT_NO_BASE = 1000;
    public static final String tableName = "account";
    public static final String idColumn = "id";
    public static final String cidColumn = "cid";
    public static final String accountNoColumn = "account_no";
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
            throw new IllegalArgumentException("Starting balance should be at-least $ 2!");
        }
        this.id = id;
        this.cid = cid;
        this.accountNo = accountNo;
        this.balance = balance;
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
        this.balance += amountToCredit;
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
            throw new IllegalStateException("Not Safe to debit!");
        }

        double amountToDebit = debitAmount(amount, currency);
        this.balance -= amountToDebit;
        return true;
    }

    protected abstract boolean isSafeToDebit(double amount, Currency currency);
    protected abstract double creditAmount(double amount, Currency currency);
    protected abstract double debitAmount(double amount, Currency currency);
    public abstract boolean transfer(double amount, Account account) throws IllegalStateException;

    @Override
    public String toString() {
        return "Account No.<" + getAccountNo() + ">";
    }
}
