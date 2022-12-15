package bank.trades;


public class Stock {
    public static final String tableName = "stock";
    public static final String idColumn = "id";
    private final int id;
    private final String name;
    private double value;

    public Stock(int id, String name, double value){
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Stock<" + id + ", " + name + ">";
    }
}

