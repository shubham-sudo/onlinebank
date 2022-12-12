package bank.repositories;

import bank.accounts.Account;
import bank.accounts.AccountType;

import java.util.List;


public interface AccountRepository extends Repository<Account, Integer> {
    Integer genNewId();
    List<Account> read();
    List<Account> readByAccountTypes(List<AccountType> accountTypes);
    Account readByAccountNo(Long accountNo);
    Account readById(Integer id);
    List<Account> readByCustomerId(Integer cid);
    Account create(Account account);
    Account update(Account account);
    boolean delete(Account account);
    boolean exists(Integer id);
}
