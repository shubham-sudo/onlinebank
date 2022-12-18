package bank.events;

import bank.trades.Stock;


/**
 * Stock Update event only bothers about the
 * stock prices going up or down, This will update
 * the prices of customer holdings
 */
public class StockUpdateEvent extends Event{
    private final Stock stock;

    public StockUpdateEvent(Stock stock) {
        super(EventType.STOCK_UPDATE);
        this.stock = stock;
    }

    public Stock getStock() {
        return stock;
    }
}
