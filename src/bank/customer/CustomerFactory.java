package bank.customer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomerFactory {

    public Customer createCustomer(int id, String firstName, String lastName, String dob, String email, boolean isManager, String phoneNumber, String ssn) {
        Customer customer = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        customer = new Customer(
                id, firstName, lastName, LocalDate.parse(dob, formatter), email, isManager
        );
        if (phoneNumber != null && !phoneNumber.equals("")) {
            customer.setPhoneNumber(Integer.parseInt(phoneNumber));
        }
        if (ssn != null && !ssn.equals("")) {
            customer.setSSN(ssn);
        }
        return customer;
    }
}
