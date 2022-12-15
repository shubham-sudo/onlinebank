package bank.factories;

import bank.trades.Stock;

/**
 * Stock Factory
 */
public class StockFactory {
    public Stock createStock(String name, double value) {
        Stock stock;
        stock = new Stock(0, name, value);
        return stock;
    }

    public Stock getStock(int id, String name, double value) {
        Stock stock;
        stock = new Stock(id, name, value);
        return stock;
    }
}
