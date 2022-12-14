package bank.events;


/**
 * All events happening in the bank application
 * should be of event type. This is base abstract class
 */
public abstract class Event {
    private final EventType eventType;

    public Event(EventType eventType) {
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return eventType;
    }
}
