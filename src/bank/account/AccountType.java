package bank.account;

public enum AccountType {
    LOAN("loan"),
    SAVING("saving"),
    CHECKING("checking"),
    SECURITIES("securities");

    private final String typeVal;

    AccountType(String val){
        this.typeVal = val;
    }

    @Override
    public String toString() {
        return typeVal;
    }
}
