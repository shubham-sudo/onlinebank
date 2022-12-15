package bank.repositories;

import bank.customers.Customer;
import bank.factories.CustomerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Customer Adapter implemented Customer Repository
 */
public class CustomerAdapter implements CustomerRepository{
    private static final CustomerFactory customerFactory = new CustomerFactory();
    private static CustomerAdapter customerAdapter = null;
    private final Connection dbConnection;

    private CustomerAdapter(Connection connection) {
        this.dbConnection = connection;
    }

    public static CustomerRepository getInstance(Connection connection) {
        if (customerAdapter == null) {
            customerAdapter = new CustomerAdapter(connection);
        }
        return customerAdapter;
    }

    @Override
    public Integer genNewId() {
        String query = "SELECT MAX(" + Customer.idColumn + ") AS max_id FROM " + Customer.tableName + " ;";
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
    public List<Customer> read() {
        String query = "SELECT * FROM " + Customer.tableName;
        return getCustomers(query);
    }

    @Override
    public Customer readById(Integer id) {
        String query = "SELECT * FROM " + Customer.tableName + " WHERE " + Customer.idColumn + " = '" + id + "';";
        return getCustomer(query);
    }

    @Override
    public Customer readByEmail(String email) {
        String query = "SELECT * FROM " + Customer.tableName + " WHERE email = '" + email + "';";
        return getCustomer(query);
    }

    @Override
    public Customer readByEmailAndPassword(String email, String password) {
        String query = "SELECT * FROM " + Customer.tableName + " WHERE email = '" + email + "' AND password = '" + password + "';";
        return getCustomer(query);
    }

    @Override
    public Customer create(Customer customer) {
        return create(customer, "", "");
    }

    @Override
    public Customer create(Customer customer, String ssn, String password) {
        int id = genNewId();
        StringBuilder query = new StringBuilder("INSERT INTO " + Customer.tableName);
        HashMap<String, String> requiredColumns = new HashMap<String, String>(){
            {
                put(Customer.idColumn, String.valueOf(id));
                put("firstname", customer.getFirstName());
                put("lastname", customer.getLastName());
                put("email", customer.getEmail());
                put("age", String.valueOf(customer.getAge()));
                put("date_of_birth", String.valueOf(customer.getDateOfBirth()));
                put("password", password);
                put("is_manager", String.valueOf(customer.isManager() ? 1 : 0));
            }
        };

        if (customer.getPhoneNumber() != 0) {
            requiredColumns.put("phone_number", String.valueOf(customer.getPhoneNumber()));
        }
        if (ssn != null){
            requiredColumns.put("SSN", ssn);
        }

        if (Repository.prepareAndExecuteQuery(dbConnection, Repository.prepareInsertQuery(query, requiredColumns)) >= 1) {
            return customerFactory.getCustomer(
                    id,
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getDateOfBirth(),
                    customer.getEmail(),
                    customer.isManager(),
                    customer.getPhoneNumber(),
                    customer.getSSN()
            );
        }
        return customer;
    }

    @Override
    public Customer update(Customer customer) {
        StringBuilder query = new StringBuilder("UPDATE " + Customer.tableName);

        HashMap<String, String> columnsToUpdate = new HashMap<String, String>() {
            {
                put("firstname", customer.getFirstName());
                put("lastname", customer.getLastName());
                put("age", String.valueOf(customer.getAge()));
                put("date_of_birth", String.valueOf(customer.getDateOfBirth()));
            }
        };
        if (customer.getPhoneNumber() != 0) {
            columnsToUpdate.put("phone_number", String.valueOf(customer.getPhoneNumber()));
        }
        if (!customer.getPlainSSN().equals("")) {
            columnsToUpdate.put("SSN", customer.getPlainSSN());
        }

        Repository.prepareAndExecuteQuery(dbConnection, Repository.prepareUpdateQuery(query, columnsToUpdate, Customer.idColumn, customer.getId()));
        return customer;
    }

    @Override
    public Customer update(Customer customer, String password) {
        StringBuilder query = new StringBuilder("UPDATE " + Customer.tableName);

        HashMap<String, String> columnsToUpdate = new HashMap<String, String>() {
            {
                put("password", password);
            }
        };
        if (customer.getPhoneNumber() != 0) {
            columnsToUpdate.put("phone_number", String.valueOf(customer.getPhoneNumber()));
        }

        Repository.prepareAndExecuteQuery(dbConnection, Repository.prepareUpdateQuery(query, columnsToUpdate, Customer.idColumn, customer.getId()));
        return customer;
    }

    @Override
    public boolean delete(Customer customer) {
        StringBuilder query = new StringBuilder("DELETE FROM " + Customer.tableName);
        return Repository.prepareAndExecuteQuery(dbConnection, Repository.prepareDeleteQuery(query, Customer.idColumn, customer.getId())) >= 1;
    }

    @Override
    public boolean exists(Integer id) {
        String query = "SELECT COUNT(*) AS total_count FROM " + Customer.tableName + " WHERE " + Customer.idColumn + "=" + id + ";";
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

    private Customer getCustomer(String query) {
        Customer customer = null;

        try {
            ResultSet resultSet = Repository.executeQuery(dbConnection, query);

            while (resultSet.next()) {
                customer = customerFactory.createCustomer(
                        resultSet.getInt(1),      // id
                        resultSet.getString(2),   // firstname
                        resultSet.getString(3),   // lastname
                        resultSet.getString(5),   // dob
                        resultSet.getString(4),   // email
                        resultSet.getBoolean(10),  // is manager
                        resultSet.getString(7),   // phoneNumber
                        resultSet.getString(8)    // ssn
                );
                customer.setPassword(resultSet.getString(9));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return customer;
    }

    private List<Customer> getCustomers(String query) {
        List<Customer> customers = new ArrayList<>();

        try {
            ResultSet resultSet = Repository.executeQuery(dbConnection, query);

            while (resultSet.next()) {
                customers.add(customerFactory.createCustomer(
                        resultSet.getInt(1),      // id
                        resultSet.getString(2),   // firstname
                        resultSet.getString(3),   // lastname
                        resultSet.getString(5),   // dob
                        resultSet.getString(4),   // email
                        resultSet.getBoolean(10),  // is manager
                        resultSet.getString(7),   // phoneNumber
                        resultSet.getString(8)    // ssn
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return customers;
    }

}
