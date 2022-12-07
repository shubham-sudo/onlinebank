package bank.trade;

import bank.account.SecuritiesAccount;
import bank.customer.Customer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TradingStocks {

    // customer id > stock
    // customer id > stock > transaction
    Map<Integer, ArrayList<StockPrice>> stocks = new HashMap<Integer, ArrayList<StockPrice>>();
    Map<Integer, ArrayList<StockPrice>> soldStocks = new HashMap<Integer, ArrayList<StockPrice>>();

    Customer p = new Customer(7, "greesh", "yal", LocalDate.of(2017, 1, 13), "greesh@bu.edu", false);
    Integer customer_id = p.getId();
    //Should insert the customer data from database
    SecuritiesAccount securtities_acc;
    StockPrice stc;

    public TradingStocks(SecuritiesAccount securtities_acc)
    {
        this.securtities_acc = securtities_acc;
    }



//    buyingand selling
//    check&thenupdatingit
//    returning the list of stock of a customer

    HashMap<Integer, ArrayList<Stock>> customer_list=new HashMap<Integer,ArrayList<Stock>>();

    public boolean buystock(int quantity, StockPrice stock, Customer customer){
        double current_balance=securtities_acc.getbalance();
        stock.setQuantity(quantity);

        if(current_balance>1500 && current_balance-stock.getPrice()*quantity>0)
            return false;
        else
        {
            if(stocks.get(customer.getId())==null)
            {
                ArrayList<StockPrice> temp = new ArrayList<>();
                temp.add(stock);
                stocks.put(customer.getId(),temp);
            }
            else {
                stocks.get(customer.getId()).add(stock);
            }


            double stock_price=stc.getPrice();
            securtities_acc.setBalance(current_balance-stock_price);
            return true;
        }
    }

    public void sellstock(int quantity,StockPrice stock,Customer customer){

        stocks.get(customer.getId()).remove(stock);
        stock.setQuantity(quantity);

        double current_balance=securtities_acc.getbalance();
        double stock_price=stock.getPrice();
        securtities_acc.setBalance(current_balance+stock_price*quantity);

        if(soldStocks.get(customer.getId())==null)
        {
            ArrayList<StockPrice> temp = new ArrayList<>();
            temp.add(stock);
            soldStocks.put(customer.getId(),temp);
        }
        else {
            soldStocks.get(customer.getId()).add(stock);
        }
    }

    public Map<Integer, ArrayList<StockPrice>> getStocks() {
        return stocks;
    }

    public ArrayList<StockPrice> getStockspercustomer(int customer_id) {
        return stocks.get(customer_id);
    }


    public double RealisedProfit(int customer_id){
        ArrayList<StockPrice> b = stocks.get(customer_id);
        ArrayList<StockPrice> s = soldStocks.get(customer_id);

        HashMap<String, StockPrice> buyPosition = new HashMap<>();

        for(StockPrice p : b)
        {
            if(buyPosition.get(p.getTicker())==null)
            {
                buyPosition.put(p.getTicker(), p);
            }
            else {
                int quantity = buyPosition.get(p.getTicker()).getQuantity() + p.getQuantity();
                double price = buyPosition.get(p.getTicker()).getPrice() + p.getPrice();
                buyPosition.get(p.getTicker()).setPrice(price/quantity);
            }
        }

        HashMap<String, StockPrice> sellPosition = new HashMap<>();
        for(StockPrice p : s)
        {
            if(sellPosition.get(p.getTicker())==null)
            {
                sellPosition.put(p.getTicker(), p);
            }
            else {
                int quantity = sellPosition.get(p.getTicker()).getQuantity() + p.getQuantity();
                double price = sellPosition.get(p.getTicker()).getPrice() + p.getPrice();
                sellPosition.get(p.getTicker()).setPrice(price/quantity);
            }
        }
        double sold=0.0, bought=0.0;
        for (Map.Entry<String, StockPrice> set : sellPosition.entrySet()) {
            sold = set.getValue().getPrice()*set.getValue().getQuantity();
            bought = buyPosition.get(set.getKey()).getPrice()*set.getValue().getQuantity();

        }

        return sold-bought;


    }


    public double UnRealisedProfit(int customer_id){
        ArrayList<StockPrice> b = stocks.get(customer_id);
        ArrayList<StockPrice> s = soldStocks.get(customer_id);

        HashMap<String, StockPrice> buyPosition = new HashMap<>();

        for(StockPrice p : b)
        {
            if(buyPosition.get(p.getTicker())==null)
            {
                buyPosition.put(p.getTicker(), p);
            }
            else {
                int quantity = buyPosition.get(p.getTicker()).getQuantity() + p.getQuantity();
                double price = buyPosition.get(p.getTicker()).getPrice() + p.getPrice();
                buyPosition.get(p.getTicker()).setPrice(price/quantity);
            }
        }

        HashMap<String, StockPrice> sellPosition = new HashMap<>();
        for(StockPrice p : s)
        {
            if(sellPosition.get(p.getTicker())==null)
            {
                sellPosition.put(p.getTicker(), p);
            }
            else {
                int quantity = sellPosition.get(p.getTicker()).getQuantity() + p.getQuantity();
                double price = sellPosition.get(p.getTicker()).getPrice() + p.getPrice();
                sellPosition.get(p.getTicker()).setPrice(price/quantity);
            }
        }
        double sold=0.0, bought=0.0;
        for (Map.Entry<String, StockPrice> set : sellPosition.entrySet()) {
//            sold = current value of stocks *  buyPosition.get(set.getKey()).getQuantity()-set.getValue().getQuantity();
            bought = buyPosition.get(set.getKey()).getPrice()*(buyPosition.get(set.getKey()).getQuantity()-set.getValue().getQuantity());

        }

        return sold-bought;


    }


}











