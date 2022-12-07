package bank.customer.assets;

import bank.customer.Customer;

public class CollateralFactory {
    public Collateral createCollateral(Customer customer, String name, double value) {
        return new Collateral(0, customer.getId(), name, value);
    }
}
