package bank.repositories;


import bank.trades.Stock;

import java.util.List;


/**
 * Stock Repository extends Repository
 */
public interface StockRepository extends Repository<Stock, Integer> {
    Integer genNewId();
    List<Stock> read();
    Stock readById(Integer id);
    Stock create(Stock item);
    Stock update(Stock item);
    boolean delete(Stock item);
    boolean exists(Integer id);
}
