package bank.events;

import bank.repositories.HoldingAdapter;
import bank.repositories.HoldingRepository;
import bank.trades.Holding;
import databases.DbConnection;

import java.sql.Connection;
import java.util.List;


/**
 * Stock Update event Receiver applies the action
 * to the holding of customer when price updates
 */
public class StockUpdateEventReceiver implements EventReceiver{
    private static final Connection connection = DbConnection.getConnection();
    private static final HoldingRepository holdingRepository = HoldingAdapter.getInstance(connection);

    @Override
    public void receiveEvents(Event event) {
        applyAction(event);
    }

    /**
     * This method is to apply actions for this particular event
     * @param event StockUpdateEvent
     */
    @Override
    public void applyAction(Event event) {
        StockUpdateEvent stockEvent = (StockUpdateEvent) event;

        List<Holding> holdings = holdingRepository.read();
        for (Holding holding : holdings) {
            holding.setBaseValue(stockEvent.getStock().getValue());
            holdingRepository.update(holding);
        }
    }
}
