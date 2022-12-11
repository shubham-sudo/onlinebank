package bank.factories;

import bank.customers.Customer;
import bank.customers.assets.Collateral;
import bank.loans.Loan;

public class LoanFactory {

    public Loan createLoan(Customer customer, double amount, Collateral collateral) {
        return new Loan(0, customer.getId(), 0, "New Loan", amount, collateral);
    }

    public Loan getLoan(int id, int aid, int cid, String name, double amount, Collateral collateral) {
        return new Loan(id, cid, aid, name, amount, collateral);
    }
}
