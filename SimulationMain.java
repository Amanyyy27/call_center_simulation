public class SimulationMain {

    public static void main(String[] args) {

        // Scenario 1 – Low Load
        runScenario("Scenario 1A - Low Load", 3, 0.5, 2.0, 10.0, 10000, 42);
        runScenario("Scenario 1B - Low Load", 3, 0.6, 2.0, 10.0, 10000, 42);
        runScenario("Scenario 1C - Low Load", 3, 0.5, 1.8, 10.0, 10000, 42);
        runScenario("Scenario 1D - Low Load", 3, 0.5, 2.0, 9.0, 10000, 42);
        runScenario("Scenario 1E - Low Load", 3, 0.55, 2.0, 10.0, 10000, 42);

        // Scenario 2 – High Load
        runScenario("Scenario 2A - High Load", 4, 2.0, 2.0, 10.0, 10000, 42);
        runScenario("Scenario 2B - High Load", 4, 2.2, 2.0, 10.0, 10000, 42);
        runScenario("Scenario 2C - High Load", 4, 2.0, 1.8, 10.0, 10000, 42);
        runScenario("Scenario 2D - High Load", 4, 2.0, 2.0, 9.0, 10000, 42);
        runScenario("Scenario 2E - High Load", 4, 1.9, 2.0, 10.0, 10000, 42);

        // Scenario 3 – Increased Staffing
        runScenario("Scenario 3A - Increased Staffing", 6, 2.0, 2.0, 10.0, 10000, 42);
        runScenario("Scenario 3B - Increased Staffing", 6, 2.2, 2.0, 10.0, 10000, 42);
        runScenario("Scenario 3C - Increased Staffing", 6, 2.0, 1.8, 10.0, 10000, 42);
        runScenario("Scenario 3D - Increased Staffing", 6, 2.0, 2.0, 9.0, 10000, 42);
        runScenario("Scenario 3E - Increased Staffing", 6, 1.8, 2.0, 10.0, 10000, 42);

        // Scenario 4 – Low Patience
        runScenario("Scenario 4A - Low Patience", 4, 2.0, 2.0, 1.0, 10000, 42);
        runScenario("Scenario 4B - Low Patience", 4, 2.0, 2.0, 1.2, 10000, 42);
        runScenario("Scenario 4C - Low Patience", 4, 1.8, 2.0, 1.0, 10000, 42);
        runScenario("Scenario 4D - Low Patience", 4, 2.0, 1.8, 1.0, 10000, 42);
        runScenario("Scenario 4E - Low Patience", 4, 2.2, 2.0, 1.0, 10000, 42);

        // Scenario 5 – Faster Service Rate
        runScenario("Scenario 5A - Faster Service Rate", 4, 2.0, 1.0, 10.0, 10000, 42);
        runScenario("Scenario 5B - Faster Service Rate", 4, 2.2, 1.0, 10.0, 10000, 42);
        runScenario("Scenario 5C - Faster Service Rate", 4, 2.0, 0.9, 10.0, 10000, 42);
        runScenario("Scenario 5D - Faster Service Rate", 4, 1.8, 1.0, 10.0, 10000, 42);
        runScenario("Scenario 5E - Faster Service Rate", 4, 2.0, 1.0, 9.0, 10000, 42);
    }

    private static void runScenario(String name, int numAgents, double arrivalRate, double meanService,
            double meanPatience, double simDuration, long seed) {
        System.out.println("\n" + name);
        CallCenterSimulation sim = new CallCenterSimulation(numAgents, arrivalRate, meanService, meanPatience,
                simDuration, seed);
        sim.run();
        sim.printReport();
    }
}
