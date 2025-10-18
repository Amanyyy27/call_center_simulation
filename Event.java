public class Event implements Comparable<Event> {
    public final double time;
    public final EventType type;
    public final Customer customer;
    public final Agent agent;

    // constructor to create a new event
    public Event(double time, EventType type, Customer customer, Agent agent) {
        this.time = time;
        this.type = type;
        this.customer = customer;
        this.agent = agent;
    }

    // used to sort events by time in priority queue
    @Override
    public int compareTo(Event x) {
        return Double.compare(this.time, x.time);
    }

    // print event details
    @Override
    public String toString() {
        return String.format("Event[%s t=%.4f cust=%s agent=%s]", type, time,
                customer == null ? "null" : customer.id,
                agent == null ? "null" : agent.id);
    }
}
