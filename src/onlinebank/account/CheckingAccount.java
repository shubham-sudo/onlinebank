package onlinebank.account;

public interface CheckingAccount {
    public string name;
    public string password;

    public ArrayList<Currency> money;
    public boolean CanTransaction(Currency c);
    public ArrayList<Currency> viewMoney();
    public void printMoney();
}
