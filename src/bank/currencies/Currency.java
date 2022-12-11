package bank.currencies;


/**
 * Currency interface provides an interface to all
 * types of currency. The base value would be USD
 * The conversion rates would be applied based on
 * base value. The Account always stores value in
 * base USD. The conversion happens when customer
 * withdraw/deposit different currency.
 */
public interface Currency {
    CurrencyType BASE_CURRENCY = CurrencyType.USD;

    /**
     * Base value would always do conversion
     * from any currency to USD.
     * @return converted value in USD
     */
    double baseValue();

    /**
     * Should return the type of currency is this.
     * @return enum constant for currency
     */
    CurrencyType getCurrencyType();

    /**
     * Should return the current currency value
     * NOT CONVERTED TO ANY ANOTHER CURRENCY
     * @return double value
     */
    double getCurrencyValue();

    /**
     * Get converted value to this currency
     * @param usd usd value of any other currency
     * @return double converted value
     */
    double getConversion(USDollar usd);
}