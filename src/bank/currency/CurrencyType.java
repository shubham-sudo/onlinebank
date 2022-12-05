package bank.currency;

public enum CurrencyType {
    USD("usd"),
    EURO("euro"),
    CND("cnd");

    private final String typeName;

    CurrencyType(String name) {
        this.typeName = name;
    }

    @Override
    public String toString() {
        return this.typeName;
    }
}
