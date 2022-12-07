package bank.loan;

import bank.customer.assets.Collateral;
import database.Database;
import database.DbModel;


public class Loan implements DbModel {
    public static final String tableName = "loan";
    public static final String idColumn = "id";
    public static final String aidColumn = "aid";
    public static final String cidColumn = "cid";
    private int aid;
    private final int id;
    private final int cid;
    private final String name;
    private double amount;
    private final Collateral collateral;  // TODO (shubham): create table for collateral [id, name, value, isUsed, aid(if under use)]

    public Loan(int id, int cid, int aid, String name, double amount, Collateral collateral) {
        this.id = id != 0 ? id : getNewId();
        this.cid = cid;
        this.name = name;
        this.amount = amount;
        this.aid = aid != 0 ? aid : -1;  // default to -1
        // account id would be set afterwards of creating loan account
        this.collateral = collateral;
    }

    private int getNewId() {
        return Database.getNewId(tableName, idColumn);
    }

    public int getId() {
        return id;
    }

    public int getCid() {
        return cid;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public void addInterest(double amount) {
        this.amount += amount;
        this.update();
    }

    public Collateral getCollateral() {
        return collateral;
    }

    @Override
    public boolean isValid() {
        return !Database.isIdExists(tableName, idColumn, getId());
    }

    @Override
    public int save() {
        if (!isValid()) {
            throw new IllegalStateException("Loan already exists!");
        }
        return Database.addLoan(this);
    }

    @Override
    public void delete() {
        Database.deleteLoan(this);
    }

    @Override
    public int update() {
        return Database.updateLoan(this);
    }
}
