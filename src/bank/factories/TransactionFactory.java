package bank.factories;

import bank.accounts.Transaction;

import java.time.LocalDate;

public class TransactionFactory {

    public Transaction createTransaction(int aid, String message, double oldValue, double newValue){
        return new Transaction(0, aid, message, oldValue, newValue,  LocalDate.now());
    }

    public Transaction getTransaction(int id, int aid, String message, double oldValue, double newValue, LocalDate date) {
        return new Transaction(id, aid, message, oldValue, newValue, date);
    }
}
