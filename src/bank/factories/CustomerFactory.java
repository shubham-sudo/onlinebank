package bank.factories;

import bank.customers.Customer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Customer Factory is very helpful for formatting dates and everything
 */
public class CustomerFactory {

    public Customer createCustomer(int id, String firstName, String lastName, String dob, String email, boolean isManager, String phoneNumber, String ssn) {
        Customer customer = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        customer = new Customer(
                id, firstName, lastName, LocalDate.parse(dob, formatter), email, isManager
        );
        if (phoneNumber != null && !phoneNumber.equals("")) {
            customer.setPhoneNumber(Long.parseLong(phoneNumber));
        }
        if (ssn != null && !ssn.equals("")) {
            customer.setSSN(ssn);
        }
        return customer;
    }

    public Customer getCustomer(int id, String firstName, String lastName, LocalDate dob, String email, boolean isManager, long phoneNo, String ssn) {
        Customer customer = new Customer(id, firstName, lastName, dob, email, isManager);
        customer.setPhoneNumber(phoneNo);
        customer.setSSN(ssn);
        return customer;
    }
}
