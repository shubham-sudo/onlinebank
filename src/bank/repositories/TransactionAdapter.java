package bank.repositories;

import bank.accounts.Account;
import bank.accounts.Transaction;
import bank.factories.TransactionFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class TransactionAdapter implements TransactionRepository{
    private static final TransactionFactory transactionFactory = new TransactionFactory();
    private final AccountRepository accountRepository;
    private static TransactionAdapter transactionAdapter = null;
    private final Connection dbConnection;

    private TransactionAdapter(Connection connection) {
        this.dbConnection = connection;
        this.accountRepository = AccountAdapter.getInstance(connection);
    }

    public static TransactionRepository getInstance(Connection connection) {
        if (transactionAdapter == null) {
            transactionAdapter = new TransactionAdapter(connection);
        }
        return transactionAdapter;
    }

    @Override
    public Integer genNewId() {
        String query = "SELECT MAX(" + Transaction.idColumn + ") AS max_id FROM " + Transaction.tableName + " ;";
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
    public List<Transaction> read() {
        String query = "SELECT * FROM " + Transaction.tableName;
        return getTransactions(query);
    }

    @Override
    public Transaction readById(Integer id) {
        String query = "SELECT * FROM " + Transaction.tableName + " WHERE " + Transaction.idColumn + " = '" + id + "';";
        return getTransaction(query);
    }

    @Override
    public List<Transaction> readByAccountIds(List<Integer> aids) {
        String query = "SELECT * FROM " + Transaction.tableName + " WHERE " + Transaction.aidColumn + " IN (" + aids.stream().map(String::valueOf).collect(Collectors.joining(", ")) + ");";
        return getTransactions(query);
    }

    @Override
    public List<Transaction> readByCustomerId(Integer cid) {
        List<Account> accounts = accountRepository.readByCustomerId(cid);
        List<Integer> aids = accounts.stream().map(Account::getId).collect(Collectors.toList());
        return readByAccountIds(aids);
    }

    @Override
    public Transaction create(Transaction transaction) {
        int id = genNewId();
        StringBuilder query = new StringBuilder("INSERT INTO " + Transaction.tableName);
        HashMap<String, String> requiredColumns = new HashMap<String, String>(){
            {
                put(Transaction.idColumn, String.valueOf(id));
                put("aid", String.valueOf(transaction.getAid()));
                put("message", transaction.getMessage());
                put("today_date", transaction.getTodayDate().toString());
                put("old_value", String.valueOf(transaction.getOldValue()));
                put("new_value", String.valueOf(transaction.getNewValue()));
            }
        };
        if (Repository.prepareAndExecuteQuery(dbConnection, Repository.prepareInsertQuery(query, requiredColumns)) >= 1) {
            return transactionFactory.getTransaction(
                    id,
                    transaction.getAid(),
                    transaction.getMessage(),
                    transaction.getOldValue(),
                    transaction.getNewValue(),
                    transaction.getTodayDate()
            );
        }
        return  transaction;
    }

    @Override
    public Transaction update(Transaction transaction) {
        return transaction;
    }

    @Override
    public boolean delete(Transaction transaction) {
        return false;
    }

    @Override
    public boolean exists(Integer id) {
        String query = "SELECT COUNT(*) AS total_count FROM " + Transaction.tableName + " WHERE " + Transaction.idColumn + "=" + id + ";";
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

    private Transaction getTransaction(String query) {
        Transaction transaction = null;

        try {
            ResultSet resultSet = Repository.executeQuery(dbConnection, query);

            while (resultSet.next()) {
                int tid = resultSet.getInt(1);
                int aid = resultSet.getInt(2);
                String message = resultSet.getString(3);
                double oldValue = resultSet.getDouble(5);
                double newValue = resultSet.getDouble(6);
                LocalDate date = LocalDate.parse(resultSet.getString(4));
                transaction = transactionFactory.getTransaction(tid, aid, message, oldValue, newValue, date);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return transaction;

    }

    private List<Transaction> getTransactions(String query) {
        List<Transaction> transactions = new ArrayList<>();

        try {
            ResultSet resultSet = Repository.executeQuery(dbConnection, query);

            while (resultSet.next()) {
                int tid = resultSet.getInt(1);
                int aid = resultSet.getInt(2);
                String message = resultSet.getString(3);
                double oldValue = resultSet.getDouble(5);
                double newValue = resultSet.getDouble(6);
                LocalDate date = LocalDate.parse(resultSet.getString(4));
                transactions.add(transactionFactory.getTransaction(tid, aid, message, oldValue, newValue, date));

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return transactions;
    }
}
