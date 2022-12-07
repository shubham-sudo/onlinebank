package bank.trade;

public class StockPrice {
    private Stock stock;
    private String ticker;
    private double price;
    private int quantity;
    public StockPrice(Stock stock,double price)
    {
        this.stock=stock;
        this.price=price;

    }
    public Stock getStock(){
        return stock;
    }
    public double getPrice(){
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    //bank manager updates the price
//    public void Update_price(double price) {
//        this.price = price;
//    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getTicker() {
        return ticker;
    }
}
