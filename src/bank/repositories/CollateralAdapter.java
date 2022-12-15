package bank.repositories;

import bank.customers.assets.Collateral;
import bank.factories.CollateralFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Collateral Adapter implements collateral repository
 */
public class CollateralAdapter implements CollateralRepository{
    private static final CollateralFactory collateralFactory = new CollateralFactory();
    private static CollateralAdapter collateralAdapter = null;
    private final Connection dbConnection;

    private CollateralAdapter(Connection connection) {
        this.dbConnection = connection;
    }

    public static CollateralRepository getInstance(Connection connection) {
        if (collateralAdapter == null) {
            collateralAdapter = new CollateralAdapter(connection);
        }
        return collateralAdapter;
    }

    @Override
    public Integer genNewId() {
        String query = "SELECT MAX(" + Collateral.idColumn + ") AS max_id FROM " + Collateral.tableName + " ;";
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
    public List<Collateral> read() {
        String query = "SELECT * FROM " + Collateral.tableName + ";";
        return getCollaterals(query);
    }

    @Override
    public Collateral readById(Integer id) {
        String query = "SELECT * FROM " + Collateral.tableName + " WHERE " + Collateral.idColumn + " = '" + id + "';";
        return getCollateral(query);
    }

    @Override
    public List<Collateral> readByCustomerId(Integer id) {
        String query = "SELECT * FROM " + Collateral.tableName + " WHERE " + Collateral.cidColumn + " = '" + id + "';";
        return getCollaterals(query);
    }

    @Override
    public Collateral create(Collateral collateral) {
        int id = genNewId();
        StringBuilder query = new StringBuilder("INSERT INTO " + Collateral.tableName);
        HashMap<String, String> requiredColumns = new HashMap<String, String>(){
            {
                put(Collateral.idColumn, String.valueOf(id));
                put(Collateral.cidColumn, String.valueOf(collateral.getCid()));
                put("name", collateral.getName());
                put("value", String.valueOf(collateral.getValue()));
                put("in_use", String.valueOf(collateral.inUse()));
            }
        };

        if (Repository.prepareAndExecuteQuery(dbConnection, Repository.prepareInsertQuery(query, requiredColumns)) >= 1) {
            return collateralFactory.getCollateral(id, collateral.getCid(), collateral.getName(), collateral.getValue());
        }
        return collateral;
    }

    @Override
    public Collateral update(Collateral collateral) {
        StringBuilder query = new StringBuilder("UPDATE " + Collateral.tableName);

        HashMap<String, String> columnsToUpdate = new HashMap<String, String>() {
            {
                put("in_use", String.valueOf(collateral.inUse()));
            }
        };

        if (Repository.prepareAndExecuteQuery(dbConnection, Repository.prepareUpdateQuery(query, columnsToUpdate, Collateral.idColumn, collateral.getId())) >= 1) {
            Collateral updatedCollateral =  collateralFactory.getCollateral(collateral.getId(), collateral.getCid(), collateral.getName(), collateral.getValue());
            if (collateral.inUse()) {
                updatedCollateral.setInUse();
            }
        }
        return collateral;
    }

    @Override
    public boolean delete(Collateral collateral) {
        StringBuilder query = new StringBuilder("DELETE FROM " + Collateral.tableName);
        return Repository.prepareAndExecuteQuery(dbConnection, Repository.prepareDeleteQuery(query, Collateral.idColumn, collateral.getId())) >= 1;
    }

    @Override
    public boolean exists(Integer id) {
        String query = "SELECT COUNT(*) AS total_count FROM " + Collateral.tableName + " WHERE " + Collateral.idColumn + "=" + id + ";";
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

    private Collateral getCollateral(String query) {
        Collateral collateral = null;

        try {
            ResultSet resultSet = Repository.executeQuery(dbConnection, query);

            while (resultSet.next()) {
                if (resultSet.getRow() == 1) {
                    int pk_id = resultSet.getInt(1);
                    int cid = resultSet.getInt(2);
                    String name = resultSet.getString(3);
                    double value = resultSet.getDouble(4);
                    boolean inUse = resultSet.getBoolean(5);
                    collateral = new Collateral(pk_id, cid, name, value);
                    if (inUse) {
                        collateral.setInUse();
                    } else {
                        collateral.setNotInUse();
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());  // TODO (shubham): Implement logger
        }
        return collateral;
    }

    private List<Collateral> getCollaterals(String query) {
        List<Collateral> collaterals = new ArrayList<>();

        try {
            ResultSet resultSet = Repository.executeQuery(dbConnection, query);

            while (resultSet.next()) {
                int pk_id = resultSet.getInt(1);
                int cid = resultSet.getInt(2);
                String name = resultSet.getString(3);
                double value = resultSet.getDouble(4);
                boolean inUse = resultSet.getBoolean(5);
                Collateral collateral = new Collateral(pk_id, cid, name, value);
                if (inUse) {
                    collateral.setInUse();
                } else {
                    collateral.setNotInUse();
                }
                collaterals.add(collateral);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return collaterals;
    }
}
