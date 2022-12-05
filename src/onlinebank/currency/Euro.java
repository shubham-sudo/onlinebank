package onlineback.currency;

import java.util.ArrayList;

public class Euro implements Currency{
    public float Euro_to_USD(float amount){
        return amount*1.056
    }
   public float USD_to_Euro(float amount)
   {
       return amount*0.946
   }
}