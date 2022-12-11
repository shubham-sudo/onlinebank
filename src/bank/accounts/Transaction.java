package bank.accounts;

import databases.DbModel;

import java.time.LocalDate;


/**
 * Transaction keeps all the history for credit and debit
 * happens on any of the account. It also stores tax info
 * This works similar to the logs of an application.
 */
public class Transaction implements DbModel {
    public static final String tableName = "operations";
    public static final String idColumn = "id";
    public static final String aidColumn = "aid";
    public static final String cidColumn = "cid";
    private final int id;
    private final int aid;
    private final String message;
    private final LocalDate todayDate;
    private final double oldValue;
    private final double newValue;

    public Transaction(int id, int aid, String message, double oldValue, double newValue, LocalDate date) {
        this.id = id;
        this.aid = aid;
        this.message = message;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.todayDate = date;
    }

    public int getId() {
        return id;
    }

    public int getAid() {
        return aid;
    }

    public LocalDate getTodayDate() {
        return todayDate;
    }

    public String getMessage() {
        return message;
    }

    public double getOldValue() {
        return oldValue;
    }

    public double getNewValue() {
        return newValue;
    }

    @Override
    public String toString() {
        return "" + id +" | " + message;
    }
}
