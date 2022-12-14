package bank.repositories;

import bank.accounts.Account;
import bank.accounts.AccountType;
import bank.factories.AccountFactory;
import bank.loans.Loan;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class AccountAdapter implements AccountRepository{
    private static final AccountFactory accountFactory = new AccountFactory();
    private final LoanRepository loanRepository;
    private static AccountAdapter accountAdapter = null;
    private final Connection dbConnection;

    private AccountAdapter(Connection connection) {
        this.dbConnection = connection;
        loanRepository = LoanAdapter.getInstance(connection);
    }

    public static AccountRepository getInstance(Connection connection) {
        if (accountAdapter == null) {
            accountAdapter = new AccountAdapter(connection);
        }
        return accountAdapter;
    }

    @Override
    public Integer genNewId() {
        String query = "SELECT MAX(" + Account.idColumn + ") AS max_id FROM " + Account.tableName + " ;";
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
    public List<Account> read() {
        String query = "SELECT * FROM " + Account.tableName + ";";
        return getAccounts(query);
    }

    @Override
    public List<Account> readByAccountTypes(List<AccountType> accountTypes) {
        String inTypes = "'" +
                accountTypes.stream().map(AccountType::toString).collect(Collectors.joining("', '")) +
                "'";
        String query = "SELECT * FROM " + Account.tableName + " WHERE account_type IN (" + inTypes + ");";
        return getAccounts(query);
    }

    @Override
    public Account readByAccountNo(Long accountNum) {
        int accountNumber = (int) (accountNum - Account.ACCOUNT_NO_BASE);
        String query = "SELECT * FROM " + Account.tableName + " WHERE " + Account.accountNoColumn + " = '" + accountNumber + "';";
        return getAccount(query);
    }

    @Override
    public Account readById(Integer id) {
        String query = "SELECT * FROM " + Account.tableName + " WHERE " + Account.idColumn + " = '" + id + "';";
        return getAccount(query);
    }

    @Override
    public List<Account> readByCustomerId(Integer cId) {
        String query = "SELECT * FROM " + Account.tableName + " WHERE " + Account.cidColumn + " = '" + cId + "';";
        return getAccounts(query);
    }

    @Override
    public Account create(Account account) {
        int id = genNewId();
        StringBuilder query = new StringBuilder("INSERT INTO " + Account.tableName);
        HashMap<String, String> requiredColumns = new HashMap<String, String>(){
            {
                put(Account.idColumn, String.valueOf(id));
                put("cid", String.valueOf(account.getCid()));
                put("account_no", String.valueOf(Account.ACCOUNT_NO_BASE + id));
                put("account_type", account.getAccountType().toString());
                put("balance", String.valueOf(account.getBalance()));
            }
        };
        if (Repository.prepareAndExecuteQuery(dbConnection, Repository.prepareInsertQuery(query, requiredColumns)) >= 1){
            return accountFactory.getAccount(
                    id,
                    account.getCid(),
                    Account.ACCOUNT_NO_BASE + id,
                    account.getBalance(),
                    account.getAccountType(),
                    null
            );
        }
        return account;
    }

    @Override
    public Account update(Account account) {
        StringBuilder query = new StringBuilder("UPDATE " + Account.tableName);

        HashMap<String, String> columnsToUpdate = new HashMap<String, String>() {
            {
                put("balance", String.valueOf(account.getBalance()));
            }
        };

        Repository.prepareAndExecuteQuery(dbConnection, Repository.prepareUpdateQuery(query, columnsToUpdate, Account.idColumn, account.getId()));
        return account;
    }

    @Override
    public boolean delete(Account account) {
        StringBuilder query = new StringBuilder("DELETE FROM " + Account.tableName);
        return Repository.prepareAndExecuteQuery(dbConnection, Repository.prepareDeleteQuery(query, Account.idColumn, account.getId())) >= 1;
    }

    @Override
    public boolean exists(Integer id) {
        String query = "SELECT COUNT(*) AS total_count FROM " + Account.tableName + " WHERE " + Account.idColumn + "=" + id + ";";
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

    private Account getAccount(String query) {
        Account account = null;

        try {
            ResultSet resultSet = Repository.executeQuery(dbConnection, query);

            while (resultSet.next()) {
                int pkId = resultSet.getInt(1);
                int cid = resultSet.getInt(2);
                long accountNo = resultSet.getLong(3);
                double balance = resultSet.getDouble(5);
                AccountType accountType = AccountType.valueOf(resultSet.getString(4).toUpperCase());
                Loan loan = null;

                if (accountType == AccountType.LOAN) {
                    loan = loanRepository.readById(pkId);
                }
                account = accountFactory.getAccount(pkId, cid, accountNo, balance, accountType, loan);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return account;
    }

    private List<Account> getAccounts(String query) {
        List<Account> accounts = new ArrayList<>();

        try {
            ResultSet resultSet = Repository.executeQuery(dbConnection, query);

            while (resultSet.next()) {
                int pkId = resultSet.getInt(1);
                int cid = resultSet.getInt(2);
                long accountNo = resultSet.getLong(3);
                double balance = resultSet.getDouble(5);
                AccountType accountType = AccountType.valueOf(resultSet.getString(4).toUpperCase());
                Loan loan = null;

                if (accountType == AccountType.LOAN) {
                    loan = loanRepository.readById(pkId);
                }

                accounts.add(accountFactory.getAccount(pkId, cid, accountNo, balance, accountType, loan));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return accounts;
    }
}
