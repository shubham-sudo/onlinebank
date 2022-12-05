package onlinebank.currency;


public class USD implements Currency {
    private final double value;
    private final CurrencyType currencyType;

    public USD(double value) {
        this.value = value;
        this.currencyType = CurrencyType.EURO;
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
    public double getConversion(USD usd) {
        return this.value;
    }
}