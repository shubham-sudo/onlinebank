package bank.events;

import java.util.HashSet;
import java.util.Set;


/**
 * Sender will notify all StockUpdate receiver about the price change with stock event
 */
public class StockUpdateEventSender implements EventSender{
    private final Set<EventReceiver> receivers;
    private static EventSender eventSender = null;

    private StockUpdateEventSender() {
        this.receivers = new HashSet<>();
    }

    public static EventSender getInstance() {
        if (eventSender == null) {
            eventSender = new StockUpdateEventSender();
        }
        return eventSender;
    }

    @Override
    public void sendEvents(Event event) {
        if (event.getEventType() == EventType.STOCK_UPDATE) {
            for (EventReceiver eventReceiver : receivers) {
                eventReceiver.receiveEvents(event);
            }
        }
    }

    @Override
    public void addReceiver(EventReceiver eventReceiver) {
        this.receivers.add(eventReceiver);
    }
}
