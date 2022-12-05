package onlinebank.currency;

import java.util.ArrayList;

public class USdollar implements currency{

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
}