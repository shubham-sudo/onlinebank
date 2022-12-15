package bank.trades;


/**
 * Holding represents the current holding of the customer
 */
public class Holding {
    public static final String tableName = "holding";
    public static final String idColumn = "id";
    public static final String cidColumn = "cid";
    public static final String sidColumn = "sid";
    private final int id;
    private final int cid;
    private final int sid;
    private int quantity;
    private double baseValue;

    public Holding (int id, int cid, int sid, int quantity, double baseValue) {
        this.id = id;
        this.cid = cid;
        this.sid = sid;
        this.quantity = quantity;
        this.baseValue = baseValue;
    }

    public int getId() {
        return id;
    }

    public int getCid() {
        return cid;
    }

    public int getSid() {
        return sid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCurrentValue() {
        return this.baseValue * this.quantity;
    }

    public double getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(double baseValue) {
        this.baseValue = baseValue;
    }
}
