package bank.repositories;

import bank.customers.assets.Collateral;

import java.util.List;


public interface CollateralRepository extends Repository<Collateral, Integer> {
    Integer genNewId();
    List<Collateral> read();
    Collateral readById(Integer id);
    List<Collateral> readByCustomerId(Integer id);
    Collateral create(Collateral collateral);
    Collateral update(Collateral collateral);
    boolean delete(Collateral collateral);
    boolean exists(Integer id);
}
