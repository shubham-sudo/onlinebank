package bank.account;

import bank.currency.Currency;


public class CheckingAccount extends Account{
    public CheckingAccount(int id, int cid, long accountNo, double balance) {
        super(id, cid, accountNo, balance);
        this.accountType = AccountType.CHECKING;
    }

    @Override
    protected boolean isSafeToDebit(double amount, Currency currency) {
        return false;
    }

    @Override
    protected double creditAmount(double amount, Currency currency) {
        return 0;
    }

    @Override
    protected double debitAmount(double amount, Currency currency) {
        return 0;
    }
}
