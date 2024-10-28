package com.groupesan.project.java.scrumsimulator.mainpackage.state;

import java.util.Map;
import java.util.HashMap;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.Simulation;

/**
 * SimulationManager acts as an intermediary between the UI and
 * SimulationStateManager. It handles the creation and updating of simulations.
 */
public class SimulationManager {

    Map<String, Simulation> simulations = new HashMap<String, Simulation>();

    public SimulationManager() {
        // empty for now as methods in 'SimulationStateManager' are static
    }

    /**
     * Creates a simulation with the provided simulation ID, name and sprint count.
     *
     * @param simId           The simulation ID.
     * @param simName         The simulation name.
     * @param numberOfSprints The total sprint count.
     * @param sprintDuration  The duration of each sprint.
     */
    public void createSimulation(String simId, String simName, Integer numberOfSprints, Integer sprintDuration, long seed) {
        simulations.put(simId, new Simulation(simName, numberOfSprints, sprintDuration, seed));
        SimulationStateManager.saveNewSimulationDetails(simId, simName, numberOfSprints, sprintDuration, seed);
    }
}
