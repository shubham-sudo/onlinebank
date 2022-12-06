package bank.trade;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Stock {
public String Security_Name;
public String stock_price;
public String number_of_shares;
 Map<String,Integer> stocks=new HashMap<String,Integer>();
 stocks.put("GOOG",100.91);
 stocks.put("NASDAQ",4000);
 Collection<Integer> stock_details=stocks.values();

    public HashMap<Stock, StockPrice> getstock_price() {
        return stocks;
    }

    public




}
