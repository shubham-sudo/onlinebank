package bank.repositories;

import bank.factories.HoldingFactory;
import bank.trades.Holding;
import bank.trades.Stock;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HoldingAdapter implements HoldingRepository{
    private static final HoldingFactory holdingFactory = new HoldingFactory();
    private static HoldingAdapter holdingAdapter = null;
    private final Connection dbConnection;

    private HoldingAdapter(Connection connection) {
        this.dbConnection = connection;
    }

    public static HoldingRepository getInstance(Connection connection) {
        if (holdingAdapter == null) {
            holdingAdapter = new HoldingAdapter(connection);
        }
        return holdingAdapter;
    }


    @Override
    public Integer genNewId() {
        String query = "SELECT MAX(" + Holding.idColumn + ") AS max_id FROM " + Holding.tableName + " ;";
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
    public List<Holding> read() {
        String query = "SELECT * FROM " + Stock.tableName;
        return getHoldings(query);
    }

    @Override
    public Holding readById(Integer id) {
        String query = "SELECT * FROM " + Holding.tableName + " WHERE " + Holding.idColumn + " = '" + id + "';";
        return getHolding(query);

    }

    @Override
    public List<Holding> readByCustomerId(Integer id) {
        String query = "SELECT * FROM " + Holding.tableName + " WHERE " + Holding.cidColumn + " = '" + id + "';";
        return getHoldings(query);
    }

    @Override
    public Holding create(Holding holding) {
        int id = genNewId();
        StringBuilder query = new StringBuilder("INSERT INTO " + Holding.tableName);
        HashMap<String, String> requiredColumns = new HashMap<String, String>() {
            {
                put(Holding.idColumn, String.valueOf(id));
                put(Holding.cidColumn, String.valueOf(holding.getCid()));
                put(Holding.sidColumn, String.valueOf(holding.getSid()));
                put("quantity", String.valueOf(holding.getQuantity()));
                put("base_value", String.valueOf(holding.getBaseValue()));
            }
        };

        if (Repository.prepareAndExecuteQuery(dbConnection, Repository.prepareInsertQuery(query, requiredColumns)) >= 1) {
            return holdingFactory.getHolding(
                    id,
                    holding.getCid(),
                    holding.getSid(),
                    holding.getQuantity(),
                    holding.getBaseValue()
            );
        }
        return holding;

    }

    @Override
    public Holding update(Holding holding) {
        StringBuilder query = new StringBuilder("UPDATE " + Holding.tableName);

        HashMap<String, String> columnsToUpdate = new HashMap<String, String>() {
            {
                put("quantity", String.valueOf(holding.getQuantity()));
                put("base_value", String.valueOf(holding.getBaseValue()));
            }
        };
        Repository.prepareAndExecuteQuery(dbConnection, Repository.prepareUpdateQuery(query, columnsToUpdate, Holding.idColumn, holding.getId()));
        return holding;
    }

    @Override
    public boolean delete(Holding holding) {
        StringBuilder query = new StringBuilder("DELETE FROM " + Holding.tableName);
        return Repository.prepareAndExecuteQuery(dbConnection, Repository.prepareDeleteQuery(query, Holding.idColumn, holding.getId())) >= 1;
    }

    @Override
    public boolean exists(Integer id) {
        String query = "SELECT COUNT(*) AS total_count FROM " + Holding.tableName + " WHERE " + Holding.idColumn + "=" + id + ";";
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

    private Holding getHolding(String query) {
        Holding holding = null;

        try {
            ResultSet resultSet = Repository.executeQuery(dbConnection, query);

            while (resultSet.next()) {
                holding = holdingFactory.getHolding(
                        resultSet.getInt(1),      // id
                        resultSet.getInt(2),      // cid
                        resultSet.getInt(3),      // sid
                        resultSet.getInt(4),      // quantity
                        resultSet.getDouble(5)    // baseValue
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return holding;
    }

    private List<Holding> getHoldings(String query) {
        List<Holding> holdings = new ArrayList<>();

        try {
            ResultSet resultSet = Repository.executeQuery(dbConnection, query);

            while (resultSet.next()) {
                holdings.add(holdingFactory.getHolding(
                        resultSet.getInt(1),      // id
                        resultSet.getInt(2),      // cid
                        resultSet.getInt(3),      // sid
                        resultSet.getInt(4),      // quantity
                        resultSet.getDouble(5)    // baseValue
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return holdings;
    }
}
