package bank.repositories;

import bank.customers.assets.Collateral;
import bank.factories.LoanFactory;
import bank.loans.Loan;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Loan Adapter implements Loan Repository
 */
public class LoanAdapter implements LoanRepository{
    private static final LoanFactory loanFactory = new LoanFactory();
    private final CollateralRepository collateralRepository;
    private static LoanAdapter loanAdapter = null;
    private final Connection dbConnection;

    private LoanAdapter(Connection connection) {
        this.dbConnection = connection;
        collateralRepository = CollateralAdapter.getInstance(connection);
    }

    public static LoanRepository getInstance(Connection connection) {
        if (loanAdapter == null) {
            loanAdapter = new LoanAdapter(connection);
        }
        return loanAdapter;
    }

    @Override
    public Integer genNewId() {
        String query = "SELECT MAX(" + Loan.idColumn + ") AS max_id FROM " + Loan.tableName + " ;";
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
    public List<Loan> read() {
        String query = "SELECT * FROM " + Loan.tableName + ";";
        return getLoans(query);
    }

    @Override
    public Loan readById(Integer id) {
        String query = "SELECT * FROM " + Loan.tableName + " WHERE " + Loan.idColumn + " = '" + id + "';";
        return getLoan(query);
    }

    @Override
    public Loan readByLoanAccountId(Integer aid) {
        String query = "SELECT * FROM " + Loan.tableName + " WHERE " + Loan.aidColumn + " = '" + aid + "';";
        return getLoan(query);
    }

    @Override
    public Loan create(Loan loan) {
        int id = genNewId();
        StringBuilder query = new StringBuilder("INSERT INTO " + Loan.tableName);
        HashMap<String, String> requiredColumns = new HashMap<String, String>(){
            {
                put(Loan.idColumn, String.valueOf(id));
                put(Loan.aidColumn, String.valueOf(loan.getAid()));
                put(Loan.cidColumn, String.valueOf(loan.getCid()));
                put("name", loan.getName());
                put("amount", String.valueOf(loan.getAmount()));
                put("collateral_id", String.valueOf(loan.getCollateral().getId()));
            }
        };

        if (Repository.prepareAndExecuteQuery(dbConnection, Repository.prepareInsertQuery(query, requiredColumns)) >= 1){
            return loanFactory.getLoan(
                    id,
                    loan.getAid(),
                    loan.getCid(),
                    loan.getName(),
                    loan.getAmount(),
                    loan.getCollateral()
            );
        }
        return loan;
    }

    @Override
    public Loan update(Loan loan) {
        StringBuilder query = new StringBuilder("UPDATE " + Loan.tableName);

        HashMap<String, String> columnsToUpdate = new HashMap<String, String>() {
            {
                put("amount", String.valueOf(loan.getAmount()));
            }
        };

        Repository.prepareAndExecuteQuery(dbConnection, Repository.prepareUpdateQuery(query, columnsToUpdate, Loan.idColumn, loan.getId()));
        return loan;
    }

    @Override
    public boolean delete(Loan loan) {
        StringBuilder query = new StringBuilder("DELETE FROM " + Loan.tableName);
        return Repository.prepareAndExecuteQuery(dbConnection, Repository.prepareDeleteQuery(query, Loan.idColumn, loan.getId())) >= 1;
    }

    @Override
    public boolean exists(Integer id) {
        String query = "SELECT COUNT(*) AS total_count FROM " + Loan.tableName + " WHERE " + Loan.idColumn + "=" + id + ";";
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

    private List<Loan> getLoans(String query) {
        List<Loan> loans = new ArrayList<>();

        try {
            ResultSet resultSet = Repository.executeQuery(dbConnection, query);

            while (resultSet.next()) {
                int pk_id = resultSet.getInt(1);
                int aid = resultSet.getInt(2);
                int cid = resultSet.getInt(3);
                String name = resultSet.getString(4);
                double amount = resultSet.getDouble(5);
                Collateral collateral = collateralRepository.readById(resultSet.getInt(6));
                loans.add(loanFactory.getLoan(pk_id, aid, cid, name, amount, collateral));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return loans;
    }

    private Loan getLoan(String query) {
        Loan loan = null;

        try {
            ResultSet resultSet = Repository.executeQuery(dbConnection, query);

            while (resultSet.next()) {
                int pk_id = resultSet.getInt(1);
                int aid = resultSet.getInt(2);
                int cid = resultSet.getInt(3);
                String name = resultSet.getString(4);
                double amount = resultSet.getDouble(5);
                Collateral collateral = collateralRepository.readById(resultSet.getInt(6));
                loan = loanFactory.getLoan(pk_id, aid, cid, name, amount, collateral);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return loan;
    }
}
