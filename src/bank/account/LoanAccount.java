package bank.account;

import bank.currency.Currency;
import bank.currency.USDollar;
import bank.loan.Loan;


public class LoanAccount extends Account {
    // keeping fixed interest rate for all type of loans for simplicity.
    protected static final double LOAN_INTEREST_RATE = 0.08;  // IDEALLY, should be on pro rata basis.

    private final Loan loan;

    public LoanAccount(int id, int cid, long accountNo, double balance, Loan loan) {
        super(id, cid, accountNo, balance);
        this.accountType = AccountType.LOAN;
        this.loan = loan;
    }

    public Loan getLoan() {
        return loan;
    }

    @Override
    protected boolean isSafeToDebit(double amount, Currency currency) {
        return this.balance > currency.baseValue();
    }

    @Override
    protected double creditAmount(double amount, Currency currency) {
        return currency.baseValue();
    }

    @Override
    protected double debitAmount(double amount, Currency currency) {
        // calculate the interest and add to the loan for simplicity
        double interest = currency.baseValue() * LOAN_INTEREST_RATE;
        this.loan.addInterest(interest);
        return currency.baseValue();
    }

    @Override
    public boolean transfer(double amount, Account account) {
        synchronized (this) {
        if (this.debit(amount, new USDollar(amount))) {
            account.credit(amount, new USDollar(amount));
            return true;
        }
    }
        return false;
    }

    @Override
    public void delete() {
        if (balance >= loan.getAmount()) {
            super.delete();
            return;
        }
        throw new IllegalStateException("Can't close Loan Account with pending due");
    }
}
