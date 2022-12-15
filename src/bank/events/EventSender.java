package bank.events;


/**
 * All events sender should implement this interface
 */
public interface EventSender {
    void sendEvents(Event event);
    void addReceiver(EventReceiver eventReceiver);
}
