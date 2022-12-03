package onlinebank.atm;

import onlinebank.account.Account;
import onlinebank.account.Loan;
import onlinebank.account.Transaction;
import onlinebank.assets.Collateral;
import onlinebank.person.Person;

import java.util.List;

public interface ATM {
    boolean createAccount(Person person, Account account);
    boolean requestLoan(Person person, Loan loan);
    List<Transaction> viewTransactions(Person person, Account account);
    double currentBalance(Person person, Account account);
    boolean addCollateral(Person person, Collateral collateral);
}
