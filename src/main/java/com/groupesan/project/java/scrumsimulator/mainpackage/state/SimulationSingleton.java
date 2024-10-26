package com.groupesan.project.java.scrumsimulator.mainpackage.state;

import java.util.Map;
import java.util.HashMap;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.Simulation;

/**
 * SimulationManager acts as an intermediary between the UI and
 * SimulationStateManager. It handles the creation and updating of simulations.
 */
public class SimulationSingleton {

    Map<String, Simulation> simulations = new HashMap<>();
    private static SimulationSingleton instance;

    private SimulationSingleton() {
        // empty for now as methods in 'SimulationStateManager' are static
    }

    public static synchronized SimulationSingleton getInstance() {
        if (instance == null) {
            instance = new SimulationSingleton();
        }
        return instance;
    }

    /**
     * Creates a simulation with the provided simulation ID, name and sprint count.
     *
     * @param simId           The simulation ID.
     * @param simName         The simulation name.
     * @param numberOfSprints The total sprint count.
     * @param sprintDuration  The duration of each sprint.
     */
    public void createSimulation(String simId, String simName, Integer numberOfSprints, Integer sprintDuration) {
        simulations.put(simId, new Simulation(simName, numberOfSprints, sprintDuration));
        SimulationStateManager.saveNewSimulationDetails(simId, simName, numberOfSprints, sprintDuration);
    }
}
