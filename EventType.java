// defining the types of event that can occue within the simulation
public enum EventType {
    ARRIVAL, // customer arrival
    SERVICE_START, // agent starts serving cusotmer
    SERVICE_END, // agent finishes serving customer
    ABANDON, // customer leaves queue without being served
}
