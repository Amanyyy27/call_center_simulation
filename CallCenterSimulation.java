import java.util.*;
import java.util.stream.Collectors;

public class CallCenterSimulation {
    // parameters for the simulation
    private final int numAgents;
    private final double meanArrivalRate;
    private final double meanServiceTime;
    private final double meanPatienceTime;
    private final double simulationEndTime;
    private final long seed;

    // simulaiton states
    private final PriorityQueue<Event> eventQueue = new PriorityQueue<>();
    private final Queue<Customer> waitingQueue = new LinkedList<>();
    private final List<Agent> agents = new ArrayList<>();
    private final Random rng;

    // performance metrics
    private int totalArrived = 0;
    private int totalServed = 0;
    private int totalAbandoned = 0;
    private double totalWaitTime = 0.0;
    private final List<Double> waitTimes = new ArrayList<>();
    private double lastEventTime = 0.0;
    private double areaQueueTime = 0.0;
    private double currentQueueLength = 0.0;

    // creating the constructor
    public CallCenterSimulation(int numAgents, double meanArrivalRate, double meanServiceTime,
            double meanPatienceTime, double simulationEndTime, long seed) {
        this.numAgents = numAgents;
        this.meanArrivalRate = meanArrivalRate;
        this.meanServiceTime = meanServiceTime;
        this.meanPatienceTime = meanPatienceTime;
        this.simulationEndTime = simulationEndTime;
        this.seed = seed;
        this.rng = new Random(seed);
        for (int i = 0; i < numAgents; i++)
            agents.add(new Agent(i + 1));
    }

    // generating exponential random variables
    private double sampleExponential(double mean) {
        if (mean <= 0)
            return 0.0;
        double u = rng.nextDouble();
        while (u <= 0.0)
            u = rng.nextDouble();
        return -mean * Math.log(u);
    }

    // main simulation run
    public void run() {
        // Schedule first arrival
        double firstInter = sampleExponential(1.0 / meanArrivalRate);
        scheduleEvent(new Event(firstInter, EventType.ARRIVAL, null, null));

        // process events until the end time of simulation
        while (!eventQueue.isEmpty()) {
            Event ev = eventQueue.poll(); // get next even acc to chronological order
            if (ev.time > simulationEndTime)
                break;

            // update time weighted queue length calculation
            double dt = ev.time - lastEventTime;
            areaQueueTime += currentQueueLength * dt;
            lastEventTime = ev.time;

            // handling each event type
            switch (ev.type) {
                case ARRIVAL:
                    handleArrival(ev);
                    break;
                case SERVICE_START:
                    handleServiceStart(ev);
                    break;
                case SERVICE_END:
                    handleServiceEnd(ev);
                    break;
                case ABANDON:
                    handleAbandon(ev);
                    break;
            }
        }

        // update busy times of agents
        for (Agent a : agents) {
            if (a.busy)
                a.endBusy(Math.min(lastEventTime, simulationEndTime));
        }
    }

    // scheduling new events
    private void scheduleEvent(Event e) {
        eventQueue.add(e);
    }

    // finding a free agent
    private Agent findFreeAgent() {
        for (Agent a : agents)
            if (!a.busy)
                return a;
        return null;
    }

    // HANDLING ARRIVAL EVENT
    // -----------------------------------------------------------------------------------------
    private void handleArrival(Event ev) {
        double t = ev.time;
        double serviceTime = sampleExponential(meanServiceTime);
        double patience = meanPatienceTime <= 0 ? Double.POSITIVE_INFINITY : sampleExponential(meanPatienceTime);
        Customer cust = new Customer(t, serviceTime, patience);
        totalArrived++;

        // schedule next arrival
        double nextInter = sampleExponential(1.0 / meanArrivalRate);
        double nextTime = t + nextInter;
        if (nextTime <= simulationEndTime)
            scheduleEvent(new Event(nextTime, EventType.ARRIVAL, null, null));

        // check for free agent
        Agent free = findFreeAgent();
        if (free != null) {
            // if free, start service immediately
            scheduleEvent(new Event(t, EventType.SERVICE_START, cust, free));
        } else {
            // else add to queue
            waitingQueue.add(cust);
            currentQueueLength = waitingQueue.size();

            // schedule abandon eventif not patient
            if (!Double.isInfinite(cust.patienceTime)) {
                double abandonTime = t + cust.patienceTime;
                if (abandonTime <= simulationEndTime) {
                    scheduleEvent(new Event(abandonTime, EventType.ABANDON, cust, null));
                }
            }
        }
    }

    // HANDLING SERVICE START EVENT
    // -----------------------------------------------------------------------------------------
    private void handleServiceStart(Event ev) {
        double t = ev.time;
        Customer cust = ev.customer;
        Agent agent = ev.agent;

        // if customer was waiting, remove from queue
        if (waitingQueue.remove(cust))
            currentQueueLength = waitingQueue.size();

        // agents start servicing
        agent.startBusy(t);
        cust.serviceStartTime = t;

        // schedule service end
        double endTime = t + cust.serviceTime;
        scheduleEvent(new Event(endTime, EventType.SERVICE_END, cust, agent));
    }

    // HANDLING SERVICE END EVENT
    // -----------------------------------------------------------------------------------------
    private void handleServiceEnd(Event ev) {
        double t = ev.time;
        Customer cust = ev.customer;
        Agent agent = ev.agent;

        // marking service as complete
        cust.serviceEndTime = t;
        totalServed++;
        double wait = cust.getWaitingTime();
        if (!Double.isNaN(wait)) {
            totalWaitTime += wait;
            waitTimes.add(wait);
        } else {
            waitTimes.add(0.0);
        }

        // marking agent as free
        agent.endBusy(t);

        // if queue non-empty, start next service
        Customer next = waitingQueue.poll();
        currentQueueLength = waitingQueue.size();
        if (next != null) {
            // schedule immediate service start with this agent
            scheduleEvent(new Event(t, EventType.SERVICE_START, next, agent));
        }
    }

    // HANDLING ABANDON EVENT
    // -----------------------------------------------------------------------------------------
    private void handleAbandon(Event ev) {
        double t = ev.time;
        Customer cust = ev.customer;

        // if customer still in queue, remove and mark abandoned
        boolean removed = waitingQueue.remove(cust);
        if (removed) {
            cust.abandoned = true;
            totalAbandoned++;
            currentQueueLength = waitingQueue.size();
        }
    }

    // PPRINT REPORT
    // -----------------------------------------------------------------------------------------
    public void printReport() {
        double simTime = Math.max(lastEventTime, simulationEndTime);
        double avgWait = totalServed == 0 ? 0.0 : totalWaitTime / totalServed;
        double avgQueueLength = simTime == 0 ? 0.0 : areaQueueTime / simTime;
        double throughput = simTime == 0 ? 0.0 : (double) totalServed / simTime;

        System.out.println("----------------------Simulation Report----------------------");
        System.out.printf("Simulation time: %.3f\n", simTime);
        System.out.printf("Total arrived: %d\n", totalArrived);
        System.out.printf("Total served: %d\n", totalServed);
        System.out.printf("Total abandoned: %d\n", totalAbandoned);
        System.out.printf("Average wait time (served): %.4f\n", avgWait);
        System.out.printf("95th percentile wait time (served): %.4f\n", percentile(waitTimes, 95));
        System.out.printf("Avg queue length (time-weighted): %.4f\n", avgQueueLength);
        System.out.printf("Throughput (served per time unit): %.4f\n", throughput);

        // agent utilizations
        for (Agent a : agents) {
            double util = simTime == 0 ? 0.0 : a.totalBusyTime / simTime;
            System.out.printf("Agent %d utilization: %.4f (busyTime=%.4f)\n", a.id, util, a.totalBusyTime);
        }
    }

    private static double percentile(List<Double> data, double p) {
        if (data.isEmpty())
            return 0.0;
        List<Double> sorted = data.stream().sorted().collect(Collectors.toList());
        double rank = p / 100.0 * (sorted.size() - 1);
        int low = (int) Math.floor(rank);
        int high = (int) Math.ceil(rank);
        if (low == high)
            return sorted.get(low);
        double d = rank - low;
        return sorted.get(low) * (1 - d) + sorted.get(high) * d;
    }
}
