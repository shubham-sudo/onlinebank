package bank.account;

import bank.currency.Currency;
import bank.currency.USDollar;
import database.Database;

import java.util.List;


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
    protected boolean transfer(double amount, Account account) {
        if (!(account instanceof SavingAccount)) {
            return false;  // can only transfer to Saving Account from Securities Account
        }
        synchronized (this) {
            if (this.debit(amount, new USDollar(amount))) {
                account.credit(amount, new USDollar(amount));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isValid() {
        boolean isValid = false;
        List<Account> accounts = Database.getAccounts(getCid());
        for (Account account : accounts) {
            if (account instanceof SavingAccount && account.getBalance() >= MINIMUM_SAVING_ACCOUNT_BALANCE) {
                isValid = true;
                break;
            }
        }
        return isValid;
    }

    @Override
    public int save() {
        if (!super.isValid()) {
            throw new IllegalStateException("Account already exists!");
        } else if (!isValid()) {
            throw new IllegalStateException("Not have enough funds in Saving Account!");
        } else {
            return super.save();
        }
    }

    @Override
    public void delete() {
        if (this.balance > 0) {
            Account account = getSavingAccount();
            if (account == null) {
                throw new IllegalStateException("No Saving account Found to Transfer funds!");
            }
            this.transfer(this.balance, account);
        }
        super.delete();
    }

    private Account getSavingAccount() {
        Account savingAccount = null;
        List<Account> accounts = Database.getAccounts(getCid());
        for (Account account : accounts) {
            if (account instanceof SavingAccount) {
                savingAccount = account;
                break;
            }
        }
        return savingAccount;
    }
}
