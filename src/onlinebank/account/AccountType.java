package onlinebank.account;

public enum AccountType {
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
