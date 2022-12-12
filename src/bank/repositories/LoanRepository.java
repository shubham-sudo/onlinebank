package bank.repositories;

import bank.loans.Loan;

import java.util.List;


public interface LoanRepository extends Repository<Loan, Integer> {
    Integer genNewId();
    List<Loan> read();
    Loan readById(Integer id);
    Loan readByLoanAccountId(Integer aid);
    Loan create(Loan loan);
    Loan update(Loan loan);
    boolean delete(Loan loan);
    boolean exists(Integer id);
}
