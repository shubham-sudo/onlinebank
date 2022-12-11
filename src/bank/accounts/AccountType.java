package bank.accounts;

public enum AccountType {
    SAVING("Saving"),
    CHECKING("Checking"),
    SECURITIES("Securities"),
    LOAN("Loan");

    private final String typeVal;

    AccountType(String val){
        this.typeVal = val;
    }

    @Override
    public String toString() {
        return typeVal;
    }
}
