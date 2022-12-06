package bank.currency;


public class Euro implements Currency {
    private static final double BASE_CURRENCY_RATE = 1.05;  // IDEALLY, should be fetched from web
    private static final double BASE_TO_THIS_RATE = 0.94;   // IDEALLY, should be fetched from web
    private final double value;
    private final CurrencyType currencyType;

    public Euro(double value) {
        this.value = value;
        this.currencyType = CurrencyType.EURO;
    }

    @Override
    public double baseValue() {
        return value * BASE_CURRENCY_RATE;
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
        return usd.getCurrencyValue() * BASE_TO_THIS_RATE;
    }
}