package onlinebank.person;

import java.time.LocalDate;
import java.time.Period;


public abstract class Person {
    private final String firstName;
    private final String lastName;
    private final int age;
    private final LocalDate dateOfBrith;

    public Person(String firstName, String lastName, LocalDate dob){
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = Period.between(dob, LocalDate.now()).getYears();
        this.dateOfBrith = dob;
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

    public LocalDate getDateOfBrith() {
        return dateOfBrith;
    }
}
