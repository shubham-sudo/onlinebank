package bank.accounts;

import bank.currencies.Currency;
import bank.currencies.USDollar;


public class SavingAccount extends Account {
    protected static final double ACCOUNT_MIN_BALANCE = 2500;

    public SavingAccount(int id, int cid, long accountNo, double balance) {
        super(id, cid, accountNo, balance);
        this.accountType = AccountType.SAVING;
    }

    @Override
    protected boolean isSafeToDebit(double amount, Currency currency) {
        // Saving account should maintain the minimum balance of $ 2500.
        return this.balance > currency.baseValue() && (this.balance - currency.baseValue()) > ACCOUNT_MIN_BALANCE;
    }

    @Override
    protected double creditAmount(double amount, Currency currency) {
        return currency.baseValue();
    }

    @Override
    protected double debitAmount(double amount, Currency currency) {
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
