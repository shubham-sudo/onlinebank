package bank.events;


/**
 * All events receiver should implement this interface
 */
public interface EventReceiver {
    void receiveEvents(Event event);
    void applyAction(Event event);
}
