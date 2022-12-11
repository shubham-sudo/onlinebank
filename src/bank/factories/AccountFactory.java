package bank.factories;

import bank.accounts.Account;
import bank.accounts.AccountType;
import bank.accounts.CheckingAccount;
import bank.accounts.LoanAccount;
import bank.accounts.SavingAccount;
import bank.accounts.SecuritiesAccount;
import bank.customers.Customer;
import bank.loans.Loan;


public class AccountFactory {

    public Account createAccount(Customer customer, AccountType accountType, double balance) {
        return getAccount(0, customer.getId(), 0, balance, accountType, null);
    }

//    public Account createLoanAccount(Customer customer, double amount, Collateral collateral) {
//        Loan loan = new Loan(0, customer.getId(), 0, "New Loan", amount, collateral);
//        Account account = new LoanAccount(0, customer.getId(), 0, amount);
//        account.create();
//        loan.setAid(account.getId());
//        loan.create();
//        return account;
//    }

    public Account getAccount(int id, int cid, long accountNo, double balance, AccountType accountType, Loan loan) {
        Account account = null;

        switch (accountType) {
            case SAVING:
                account = new SavingAccount(id, cid, accountNo, balance);
                break;
            case CHECKING:
                account = new CheckingAccount(id, cid, accountNo, balance);
                break;
            case SECURITIES:
                account = new SecuritiesAccount(id, cid, accountNo, balance);
                break;
            case LOAN:
                account = new LoanAccount(id, cid, accountNo, balance);
                break;
        }
        return account;
    }
}
