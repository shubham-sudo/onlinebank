package bank.factories;

import bank.customers.Customer;
import bank.customers.assets.Collateral;

/**
 * Collateral Factory
 */
public class CollateralFactory {
    public Collateral createCollateral(Customer customer, String name, double value) {
        return new Collateral(0, customer.getId(), name, value);
    }

    public Collateral getCollateral(int id, int cid, String name, double value, boolean inUse) {
        Collateral collateral = new Collateral(id, cid, name, value);
        if (inUse) {
            collateral.setInUse();
        }
        return collateral;
    }
}
