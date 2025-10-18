public class Agent {
    public final int id;
    public boolean busy = false;
    public double busyStart = 0.0;
    public double totalBusyTime = 0.0;

    // assigning unique id to every agent
    public Agent(int id) {
        this.id = id;
    }

    // marking agent as busy with start time t
    public void startBusy(double t) {
        busy = true;
        busyStart = t;
    }

    // marking agent as free and update total busy time
    public void endBusy(double t) {
        if (busy == true) {
            totalBusyTime += (t - busyStart);
            busy = false;
        }
    }
}
