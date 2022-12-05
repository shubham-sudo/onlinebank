package onlineback.currency;

import java.util.ArrayList;

public class CanadaDollar implements Currency{
    public float CND_to_USD(float amount){
        return amount*0.74
    }
    public float USD_to_CND(float amount){
        return amount*1.33
    }
}