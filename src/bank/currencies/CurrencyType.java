package bank.currencies;

/**
 * Type of currency
 */
public enum CurrencyType {
    USD("USD"),
    EURO("EURO"),
    CND("CND");

    private final String typeName;

    CurrencyType(String name) {
        this.typeName = name;
    }

    @Override
    public String toString() {
        return this.typeName;
    }
}
