package onlinebank.account;

import java.util.ArrayList;
public interface Loan {
    public string name;
    public string password;
    public ArrayList<Collateral> collaterals;
    public ArrayList<Currency> debt;
    public boolean PayFinished(Currency c);
    public ArrayList<Currency> viewDebt();
    public void printDebt();
    public void getInterest(float InterestRate);
}
