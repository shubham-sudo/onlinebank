package bank.repositories;

import bank.customers.Customer;

import java.util.List;


public interface CustomerRepository extends Repository<Customer, Integer>{
    Integer genNewId();
    List<Customer> read();
    Customer readById(Integer id);
    Customer readByEmail(String email);
    Customer readByEmailAndPassword(String email, String password);
    Customer create(Customer customer);
    Customer create(Customer customer, String ssn, String password);
    Customer update(Customer customer);
    Customer update(Customer customer, String password);
    boolean delete(Customer customer);
    boolean exists(Integer id);
}
