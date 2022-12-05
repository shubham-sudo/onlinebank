package onlinebank.currency;

import onlinebank.currency.currency;

public class CanadaDollar implements currency {
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
    public double CND_to_USD(float amount){
        return amount*0.74;
    }
    public double USD_to_CND(float amount){
        return amount*1.33;
    }


}