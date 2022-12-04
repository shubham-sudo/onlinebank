package onlinebank.customer;

import database.Database;

import java.time.LocalDate;


/**
 * Customer class represent the customers of the bank
 * It extends the Person class which is basic for any Person
 */
public class Customer extends Person {
    private final String email;
    private int phoneNumber;
    private String SSN;
    private String password;

    public Customer(int id, String firstName, String lastName, LocalDate dob, String email) {
        super(id, firstName, lastName, dob);
        this.email = email;
    }

    /**
     * Set the phone number of the customer, though it is optional
     * @param phoneNumber integer
     * @throws IllegalArgumentException exception if invalid
     */
    public void setPhoneNumber(int phoneNumber) throws IllegalArgumentException {
        if (String.valueOf(phoneNumber).length() != 10) {
            throw new IllegalArgumentException("Invalid Phone Number!");
        }
        this.phoneNumber = phoneNumber;
    }

    /**
     * This will set the SSN of the customer, though it is optional
     * but this would be required for all the customer who do trading.
     *
     * @param ssn ssn string
     * @throws IllegalArgumentException exception if invalid
     */
    public void setSSN(String ssn) throws IllegalArgumentException {
        if (ssn.length() != 9) {
            throw new IllegalArgumentException("Invalid SSN!");
        }
        this.SSN = ssn.trim();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * This wil mask the SSN to display on Customer profile.
     * @return eg - XXX-XX-1234
     */
    public String getSSN() {
        if (SSN == null) {
            return "";
        }
        char[] ssn = new char[11];
        char[] ssnToChar = SSN.toCharArray();

        for (int i = 0; i < ssn.length; i++) {
            if (i == 3 || i == 6) {
                ssn[i] = '-';
            } else if (i > 6) {
                ssn[i] = ssnToChar[i];
            } else {
                ssn[i] = 'X';
            }
        }

        return String.valueOf(ssn);
    }

    public boolean validate() {
        return !Database.isIdExists(tableName, idColumn, getId());
    }

    public int save() {
        if (!validate()) {
            throw new IllegalStateException("Customer already exists!");
        }
        return Database.addCustomer(this, this.SSN, this.password);
    }
}
