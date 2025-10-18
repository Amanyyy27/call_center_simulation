public class Customer {
    private static long next_id = 1;
    public final long id;
    public final double arrivalTime;
    public final double serviceTime; // amount of time customer will take with agent
    public final double patienceTime; // max wait time in queue
    public double serviceStartTime = Double.NaN;
    public double serviceEndTime = Double.NaN;
    public boolean abandoned = false;

    // constructor to create a new customer
    public Customer(double arrivalTime, double serviceTime, double patienceTime) {
        this.id = next_id++;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.patienceTime = patienceTime;
    }

    // calculates how long the customer waited before being served
    public double getWaitingTime() {
        if (Double.isNaN(serviceStartTime))
            return Double.NaN; // not served yet
        return serviceStartTime - arrivalTime;
    }
}
