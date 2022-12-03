package onlineback.curency;

import java.util.ArrayList;

public interface Currency {
    public string CurrencyName;
    public int CurrencyIndex;
    public float amount;
    public ArrayList<ArrayList<float>> exchanges;
    public boolean setAmount(float a);
    public float getAmount();
    public float getExchanges(int index);
    public float getExchanges(string name);
}