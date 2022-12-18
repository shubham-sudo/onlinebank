package bank.events;

import bank.trades.Stock;


/**
 * Stock Delete event only bothers about the
 * stock getting deleted by manager, This will
 * remove that stock from customer holding
 * and credit the amount respectively to customer account.
 */
public class StockDeleteEvent extends Event{
    private final Stock stock;

    public StockDeleteEvent(Stock stock) {
        super(EventType.STOCK_DELETE);
        this.stock = stock;
    }

    public Stock getStock() {
        return stock;
    }
}
