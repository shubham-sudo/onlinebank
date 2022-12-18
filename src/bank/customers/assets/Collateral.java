package bank.customers.assets;


/**
 * Collateral is to store details of collateral a customer gives
 */
public class Collateral {
    public static final String tableName = "collateral";
    public static final String idColumn = "id";
    public static final String cidColumn = "cid";
    private final int id;
    private final int cid;
    private final String name;
    private final double value;
    private boolean inUse;

    public Collateral(int id, int cid, String name, double value) {
        this.id = id;
        this.cid = cid;
        this.name = name;
        this.value = value;
        this.inUse = false;
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
    }

    public void setNotInUse(){
        this.inUse = false;
    }

    @Override
    public String toString() {
        return "Name: " + name + "| Value: " + value + "| In Use: " + inUse;
    }
}
