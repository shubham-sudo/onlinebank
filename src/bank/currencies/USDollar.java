package bank.currencies;


public class USDollar implements Currency {
    private final double value;
    private final CurrencyType currencyType;

    public USDollar(double value) {
        this.value = value;
        this.currencyType = CurrencyType.USD;
    }

    @Override
    public double baseValue() {
        return this.value;
    }

    @Override
    public CurrencyType getCurrencyType() {
        return this.currencyType;
    }

    @Override
    public double getCurrencyValue() {
        return this.value;
    }

    @Override
    public double getConversion(USDollar usd) {
        return this.value;
    }
}