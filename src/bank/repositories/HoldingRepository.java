package bank.repositories;

import bank.trades.Holding;

import java.util.List;


/**
 * Holding repository extend repository
 */
public interface HoldingRepository extends Repository<Holding, Integer> {
    @Override
    Integer genNewId();

    @Override
    List<Holding> read();

    @Override
    Holding readById(Integer id);

    List<Holding> readByCustomerId(Integer id);

    @Override
    Holding create(Holding holding);

    @Override
    Holding update(Holding holding);

    @Override
    boolean delete(Holding holding);

    @Override
    boolean exists(Integer id);
}
