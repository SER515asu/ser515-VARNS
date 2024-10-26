package com.groupesan.project.java.scrumsimulator.mainpackage.state;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.Simulation;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.*;

import static com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationFileHandler.getSimulationData;
import static com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationFileHandler.updateSimulationData;

/**
 * SimulationManager acts as an intermediary between the UI and
 * SimulationStateManager. It handles the creation and updating of simulations.
 */
public class SimulationSingleton {

    private static final List<Simulation> simulations = new ArrayList<>();
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
    public static void saveSimulationDetails() {
        JSONArray simulationsArray = new JSONArray();
        simulations.forEach(simulation -> simulationsArray.put(jsonMapper(simulation)));

        updateSimulationData(simulationsArray);
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
