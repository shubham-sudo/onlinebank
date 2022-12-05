package bank.account;

import bank.assets.Collateral;
import bank.currency.Currency;


public class LoanAccount extends Account {
    private final Collateral collateral;  // TODO (shubham): create table for collateral [id, name, value, isUsed, aid(if under use)]

    public LoanAccount(int id, int cid, long accountNo, double balance, Collateral collateral) {
        super(id, cid, accountNo, balance);
        this.accountType = AccountType.LOAN;
        this.collateral = collateral;
    }

    public Collateral getUsedCollateral() {
        return collateral;
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
