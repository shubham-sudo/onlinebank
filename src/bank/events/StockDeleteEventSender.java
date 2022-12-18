package bank.events;

import java.util.HashSet;
import java.util.Set;


/**
 * Sender will notify all StockDelete receiver about the stock deletion
 */
public class StockDeleteEventSender implements EventSender{
    private final Set<EventReceiver> receivers;
    private static EventSender eventSender = null;

    private StockDeleteEventSender() {
        this.receivers = new HashSet<>();
    }

    public static EventSender getInstance() {
        if (eventSender == null) {
            eventSender = new StockDeleteEventSender();
        }
        return eventSender;
    }

    @Override
    public void sendEvents(Event event) {
        if (event.getEventType() == EventType.STOCK_DELETE) {
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
