package bank.repositories;

import bank.accounts.Transaction;

import java.util.List;


public interface TransactionRepository extends Repository<Transaction, Integer> {
    Integer genNewId();
    List<Transaction> read();
    Transaction readById(Integer id);
    List<Transaction> readByAccountIds(List<Integer> aids);
    List<Transaction> readByCustomerId(Integer cid);
    Transaction create(Transaction transaction);
    Transaction update(Transaction transaction);
    boolean delete(Transaction transaction);
    boolean exists(Integer id);
}
