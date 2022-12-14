package bank.factories;

import bank.trades.Holding;


public class HoldingFactory {
    public Holding createHolding(int cid, int sid, int quantity,  double baseValue) {
        return new Holding(0, cid, sid, quantity, baseValue);
    }

    public Holding getHolding(int id, int cid, int sid, int quantity,  double baseValue) {
        return new Holding(id, cid, sid, quantity, baseValue);
    }
}
