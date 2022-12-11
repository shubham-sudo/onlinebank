package bank.accounts;

import bank.currencies.Currency;
import bank.currencies.USDollar;


public class CheckingAccount extends Account{
    public CheckingAccount(int id, int cid, long accountNo, double balance) {
        super(id, cid, accountNo, balance);
        this.accountType = AccountType.CHECKING;
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
