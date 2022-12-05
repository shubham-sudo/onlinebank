package onlineback.currency;

import onlinebank.currency.currency;

import java.util.ArrayList;

public class Euro implements currency {


    @Override
    public boolean setAmount(float a) {
        return false;
    }

    @Override
    public float getAmount() {
        return 0;
    }

    @Override
    public float getExchanges(int index) {
        return 0;
    }

    @Override
    public float getExchanges(String name) {
        return 0;
    }
    public double Euro_to_USD(double amount){
        return amount*1.056;
    }
    public double USD_to_Euro(double amount)
    {
        return amount*0.946;
    }
}