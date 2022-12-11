package bank.loans;

import bank.customers.assets.Collateral;
import databases.DbModel;


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
        this.id = id;
        this.cid = cid;
        this.name = name;
        this.amount = amount;
        this.aid = aid != 0 ? aid : -1;  // default to -1
        // account id would be set afterwards of creating loan account
        this.collateral = collateral;
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
    }

    public Collateral getCollateral() {
        return collateral;
    }
}
