package onlinebank.account;

import java.util.ArrayList;
import onlinebank.currency.Currency;
import onlinebank.person.Person;
import onlinebank.transaction.Transaction;
public interface Loan {
    public Person person;
    public string password;
    public ArrayList<Collateral> collaterals;
    public ArrayList<Currency> debt;
    public ArraList<Transaction> history;
    public Transaction Execute(Transaction t);// will record the transaction,
    // only mark the transaction to false when debt is clear.
    //should return the remaining when there is more Currency than debt.
    //so the transaction returned is the actuall currency paid.
    //by this you can calculate the extra money
    public ArrayList<Currency> viewDebt();
    public void printDebt();
    //public Currency MorethanDebt();
    public void getInterest(float InterestRate);
}
