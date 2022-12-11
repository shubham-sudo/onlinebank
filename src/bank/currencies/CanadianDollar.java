package bank.currencies;


public class CanadianDollar implements Currency{
    private static final double BASE_CURRENCY_RATE = 0.74;  // IDEALLY, should be fetched from web
    private static final double BASE_TO_THIS_RATE = 1.33;   // IDEALLY, should be fetched from web
    private final double value;
    private final CurrencyType currencyType;

    public CanadianDollar(double value) {
        this.value = value;
        currencyType = CurrencyType.EURO;
    }

    @Override
    public double baseValue() {
        return this.value * BASE_CURRENCY_RATE;
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