package com.groupesan.project.java.scrumsimulator.mainpackage.state;

import java.util.*;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.Simulation;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationFileHandler.getSimulationData;
import static com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationFileHandler.updateSimulationData;

public class SimulationSingleton {

    private static final List<Simulation> simulations = new ArrayList<>();
    private static SimulationSingleton instance;

    private SimulationSingleton() {
        // empty for now as methods in 'SimulationStateManager' are static
    }

    public static synchronized SimulationSingleton getInstance() {
        if (instance == null) {
            instance = new SimulationSingleton();
            loadSimulations();
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
    public void createSimulation(UUID simId, String simName, Integer numberOfSprints, Integer sprintDuration) {
        simulations.add(new Simulation(simId, simName, numberOfSprints, sprintDuration));
        saveSimulationDetails();
    }

    public void addSimulation(Simulation simulation) {
        simulations.add(simulation);
    }

    /**
     * Saves the details of a new simulation to a JSON file.
     *
     */
    public void saveSimulationDetails() {
        JSONArray simulationsArray = new JSONArray();
        simulations.forEach(simulation -> simulationsArray.put(jsonMapper(simulation)));

        updateSimulationData(simulationsArray);
    }

    private static void loadSimulations() {
        JSONArray simulationsFromFile = getSimulationData();
        if (simulationsFromFile != null) {
            simulationsFromFile.forEach(simulation -> simulations.add((Simulation) simulation));
        }
    }

    private static JSONObject jsonMapper(Simulation simulation) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ID", simulation.getSimulationId());
        jsonObject.put("Name", simulation.getSimulationName());
        jsonObject.put("Status", "New");
        jsonObject.put("SprintDuration", simulation.getSprintDuration());
        jsonObject.put("NumberOfSprints", simulation.getSprintCount());
        jsonObject.put("Sprints", simulation.getSprints());
        return jsonObject;
    }
}
