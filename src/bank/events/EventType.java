package bank.events;


/**
 * Event could be different type in bank application
 * Action taken by the sender would be smart based on type of event
 */
public enum EventType {
    STOCK_UPDATE("stock update"),
    STOCK_DELETE("stock delete");

    private final String typeName;

    EventType(String name) {
        this.typeName = name;
    }

    @Override
    public String toString() {
        return this.typeName;
    }
}
