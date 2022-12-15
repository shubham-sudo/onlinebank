package bank.accounts;

import bank.currencies.Currency;
import bank.currencies.USDollar;
import bank.loans.Loan;


/**
 * Creates a new loan account and also put interest rate on each transaction made through it
 * The interest rate is fixed for simplicity and it will be added to the loan value.
 */
public class LoanAccount extends Account {
    // keeping fixed interest rate for all type of loans for simplicity.
    protected static final double LOAN_INTEREST_RATE = 0.08;  // IDEALLY, should be on pro rata basis.

    private Loan loan;

    public LoanAccount(int id, int cid, long accountNo, double balance) {
        super(id, cid, accountNo, balance);
        this.accountType = AccountType.LOAN;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        if (this.loan != null) {
            throw new IllegalStateException("Can't reset the loan!");
        }
        this.loan = loan;
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
}
