package bank.factory;

import bank.customer.Customer;
import bank.customer.assets.Collateral;

public class CollateralFactory {
    public Collateral createCollateral(Customer customer, String name, double value) {
        return new Collateral(0, customer.getId(), name, value);
    }
}
