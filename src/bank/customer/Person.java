package bank.customer;

import database.Database;
import database.DbModel;

import java.time.LocalDate;
import java.time.Period;


/**
 * Abstract base class for any Person
 */
public abstract class Person implements DbModel {
    public static final String tableName = "customer";
    public static final String idColumn = "id";
    private final int id;
    private final String firstName;
    private final String lastName;
    private final int age;
    // don't ask age from person compute it from dob
    private final LocalDate dateOfBirth;
    private final boolean isManager;

    public Person(int id, String firstName, String lastName, LocalDate dob, boolean isManager){
        this.id = id != 0 ? id : getNewId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = Period.between(dob, LocalDate.now()).getYears();
        this.dateOfBirth = dob;
        this.isManager = isManager;
    }

    public int getId() {
        return id;
    }

    private int getNewId() {
        return Database.getNewId(tableName, idColumn);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public boolean isManager() {
        return isManager;
    }
}
