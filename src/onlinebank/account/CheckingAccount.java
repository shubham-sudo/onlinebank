package onlinebank.account;

public interface CheckingAccount {
    public Person person;
    public string password;

    public ArrayList<Currency> money;
    public boolean CanTransaction(Currency c);
    public ArrayList<Currency> viewMoney();
    public void printMoney();
}
