package onlinebank.account;

public interface Transaction {
    public void saveMoney(SavingAccount account,Currency c);
    public void saveMoney(CheckingAccount account,Currency c,float FeeRate);

    public boolean WithdrawMoney(SavingAccount account,Currency c);
    public boolean WithdrawMoney(CheckingAccount account,Currency c,float FeeRate);

    public boolean PayLoan(Loan loan, Currency c);
}
