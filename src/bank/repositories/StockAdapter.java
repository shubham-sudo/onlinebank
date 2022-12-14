package bank.repositories;

import bank.factories.StockFactory;
import bank.trades.Stock;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StockAdapter implements StockRepository{
    private static final StockFactory stockFactory = new StockFactory();
    private static StockAdapter stockAdapter = null;
    private final Connection dbConnection;

    private StockAdapter(Connection connection) {
        this.dbConnection = connection;
    }

    public static StockRepository getInstance(Connection connection) {
        if (stockAdapter == null) {
            stockAdapter = new StockAdapter(connection);
        }
        return stockAdapter;
    }

    @Override
    public Integer genNewId() {
        String query = "SELECT MAX(" + Stock.idColumn + ") AS max_id FROM " + Stock.tableName + " ;";
        int id = 0;

        try {
            ResultSet resultSet = Repository.executeQuery(dbConnection, query);
            while (resultSet.next()) {
                id = resultSet.getInt("max_id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return id + 1;
    }

    @Override
    public List<Stock> read() {
        String query = "SELECT * FROM " + Stock.tableName;
        return getStocks(query);
    }

    @Override
    public Stock readById(Integer id) {
        String query = "SELECT * FROM " + Stock.tableName + " WHERE " + Stock.idColumn + " = '" + id + "';";
        return getStock(query);
    }

    @Override
    public Stock create(Stock stock) {
        int id = genNewId();
        StringBuilder query = new StringBuilder("INSERT INTO " + Stock.tableName);
        HashMap<String, String> requiredColumns = new HashMap<String, String>() {
            {
                put(Stock.idColumn, String.valueOf(id));
                put("name", stock.getName());
                put("value", String.valueOf(stock.getValue()));
            }
        };

        if (Repository.prepareAndExecuteQuery(dbConnection, Repository.prepareInsertQuery(query, requiredColumns)) >= 1) {
            return stockFactory.getStock(
                    id,
                    stock.getName(),
                    stock.getValue()
            );
        }
        return stock;
    }

    @Override
    public Stock update(Stock stock) {
        StringBuilder query = new StringBuilder("UPDATE " + Stock.tableName);

        HashMap<String, String> columnsToUpdate = new HashMap<String, String>() {
            {
                put("value", String.valueOf(stock.getValue()));
            }
        };
        Repository.prepareAndExecuteQuery(dbConnection, Repository.prepareUpdateQuery(query, columnsToUpdate, Stock.idColumn, stock.getId()));
        return stock;
    }

    @Override
    public boolean delete(Stock stock) {
        StringBuilder query = new StringBuilder("DELETE FROM " + Stock.tableName);
        return Repository.prepareAndExecuteQuery(dbConnection, Repository.prepareDeleteQuery(query, Stock.idColumn, stock.getId())) >= 1;
    }

    @Override
    public boolean exists(Integer id) {
        String query = "SELECT COUNT(*) AS total_count FROM " + Stock.tableName + " WHERE " + Stock.idColumn + "=" + id + ";";
        boolean exists = false;

        try {
            ResultSet resultSet = Repository.executeQuery(dbConnection, query);
            while (resultSet.next()) {
                if (resultSet.getInt("total_count") > 0) {
                    exists = true;
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return exists;
    }

    private Stock getStock(String query) {
        Stock stock = null;

        try {
            ResultSet resultSet = Repository.executeQuery(dbConnection, query);

            while (resultSet.next()) {
                stock = stockFactory.getStock(
                        resultSet.getInt(1),      // id
                        resultSet.getString(2),   // name
                        resultSet.getDouble(3)   // value
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return stock;
    }

    private List<Stock> getStocks(String query) {
        List<Stock> stocks = new ArrayList<>();

        try {
            ResultSet resultSet = Repository.executeQuery(dbConnection, query);

            while (resultSet.next()) {
                stocks.add(stockFactory.getStock(
                        resultSet.getInt(1),      // id
                        resultSet.getString(2),   // name
                        resultSet.getDouble(3)   // value
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return stocks;
    }
}
