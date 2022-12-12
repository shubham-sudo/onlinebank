package bank.accounts;

import bank.currencies.Currency;
import bank.currencies.USDollar;


public class SecuritiesAccount extends Account{
    private static final double MINIMUM_SAVING_ACCOUNT_BALANCE = 5000;  // minimum $ for opening security account

    public SecuritiesAccount(int id, int cid, long accountNo, double balance) {
        super(id, cid, accountNo, balance);
        this.accountType = AccountType.SECURITIES;
    }

    @Override
    protected boolean isSafeToDebit(double amount, Currency currency) {
        return this.balance > currency.baseValue();
    }

    @Override
    protected double creditAmount(double amount, Currency currency) {
        // TODO (shubham): add this service charge value to bank wallet
        return currency.baseValue() - TRANSACTION_SERVICE_CHARGE;
    }

    @Override
    protected double debitAmount(double amount, Currency currency) {
        // TODO (shubham): add this service charge value to bank wallet
        return currency.baseValue() + TRANSACTION_SERVICE_CHARGE;
    }

    @Override
    public boolean transfer(double amount, Account account) throws IllegalStateException {
        if (!(account instanceof SavingAccount)) {
            throw new IllegalStateException("Can only transfer to Saving Account");  // can only transfer to Saving Account from Securities Account
        }
        synchronized (this) {
            if (this.debit(amount, new USDollar(amount))) {
                account.credit(amount, new USDollar(amount));
                return true;
            }
        }
        return false;
    }
}
