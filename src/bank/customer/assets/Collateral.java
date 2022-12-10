package bank.customer.assets;

import database.Database;
import database.DbModel;

public class Collateral implements DbModel {
    public static final String tableName = "collateral";
    public static final String idColumn = "id";
    public static final String cidColumn = "cid";
    private final int id;
    private final int cid;
    private final String name;
    private final double value;
    private boolean inUse;

    public Collateral(int id, int cid, String name, double value) {
        this.id = id != 0 ? id : getNewId();
        this.cid = cid;
        this.name = name;
        this.value = value;
        this.inUse = false;
    }

    private int getNewId(){
        return Database.getNewId(tableName, idColumn);
    }

    public int getId() {
        return id;
    }

    public int getCid() {
        return cid;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public boolean inUse() {
        return inUse;
    }

    public void setInUse(){
        this.inUse = true;
        this.update();
    }

    public void setNotInUse(){
        this.inUse = false;
        this.update();
    }

    @Override
    public boolean isValid() {
        return !Database.isIdExists(tableName, idColumn, getId());
    }

    @Override
    public int create() {
        if (!isValid()) {
            throw new IllegalStateException("Collateral already exists!");
        }
        return Database.addCollateral(this);
    }

    @Override
    public void delete() {
        Database.deleteCollateral(this);
    }

    @Override
    public int update() {
        return Database.updateCollateral(this);
    }
}
