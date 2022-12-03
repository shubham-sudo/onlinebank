package onlinebank.account;

public interface SavingAccount {
    public Person person;
    public string password;

    public ArrayList<Currency> money;
    public ArraList<Transaction> history;
    public Transaction Execute(Transaction t);// will edit if the transcation is successed and record the transaction
    public ArrayList<Currency> viewMoney();
    public void printMoney();
    public ArraList<Transaction> getHistory();
    public void getInterest(float InterestRate);
}
