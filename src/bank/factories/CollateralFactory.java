package bank.factories;

import bank.customers.Customer;
import bank.customers.assets.Collateral;

public class CollateralFactory {
    public Collateral createCollateral(Customer customer, String name, double value) {
        return new Collateral(0, customer.getId(), name, value);
    }

    public Collateral getCollateral(int id, int cid, String name, double value) {
        return new Collateral(id, cid, name, value);
    }
}
