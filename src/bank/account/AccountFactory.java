package bank.account;

import bank.customer.Customer;
import bank.customer.assets.Collateral;
import bank.loan.Loan;


public class AccountFactory {

    public Account createAccount(Customer customer, AccountType accountType, double balance) {
        Account account = null;
        switch (accountType) {
            case SAVING:
                account = new SavingAccount(0, customer.getId(), 0, balance);
                break;
            case CHECKING:
                account = new CheckingAccount(0, customer.getId(), 0, balance);
                break;
            case SECURITIES:
                account = new SecuritiesAccount(0, customer.getId(), 0, balance);
                break;
        }
        return account;
    }

    public Account createLoanAccount(Customer customer, double amount, Collateral collateral) {
        Loan loan = new Loan(0, customer.getId(), 0, "New Loan", amount, collateral);
        Account account = new LoanAccount(0, customer.getId(), 0, amount, loan);
        account.save();
        loan.setAid(account.getId());
        loan.save();
        return account;
    }
}
