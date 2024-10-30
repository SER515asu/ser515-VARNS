package com.groupesan.project.java.scrumsimulator.mainpackage.state;

import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupesan.project.java.scrumsimulator.mainpackage.core.Simulation;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.Sprint;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.UserStory;
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

    public void addSimulation(Simulation simulation) {
        simulations.add(simulation);

        saveSimulationDetails();
    }

    /**
     * Saves the details of a new simulation to a JSON file.
     *
     */
    public void saveSimulationDetails() {
        System.out.println("Saving simulation details");

        JSONArray simulationsArray = new JSONArray();
        simulations.forEach(simulation -> simulationsArray.put(simulationToJson(simulation)));

        System.out.println(simulationsArray.toString(4));

        updateSimulationData(simulationsArray);
    }

    public Simulation getLatestSimulation() {
        if (simulations.isEmpty()) return null;
        return simulations.getLast();
    }

    public void initializeDefaultSimulation() {
        Simulation simulation = new Simulation(
                UUID.randomUUID(),
                "Default",
                1,
                14,
                0
        );
        simulations.add(simulation);
        SimulationStateManager.getInstance().setCurrentSimulation(simulation);
    }

    private static void loadSimulations() {
        JSONArray simulationsFromFile = getSimulationData();

        if (!simulationsFromFile.isEmpty()) {
            try {
                simulationsFromFile.forEach(simulation -> simulations.add(jsonToSimulation((JSONObject) simulation)));
            } catch (Exception e) {
                updateSimulationData(new JSONArray());
            }
        }
    }

    private static Simulation jsonToSimulation(JSONObject simulationJson) {
        ObjectMapper mapper = new ObjectMapper();
        JSONArray sprintsFromJson = simulationJson.getJSONArray("Sprints");
        List<Sprint> sprints = new ArrayList<>();
        if (!sprintsFromJson.isEmpty()) {
            sprintsFromJson.forEach(sprintJson -> {
                try {
                    sprints.add(mapper.readValue(sprintJson.toString(), Sprint.class));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        JSONArray userStoriesFromJson = simulationJson.getJSONArray("UserStories");
        List<UserStory> userStories = new ArrayList<>();
        if (!userStoriesFromJson.isEmpty()) {
            userStoriesFromJson.forEach(userStoryJson -> {
                try {
                    userStories.add(mapper.readValue(userStoryJson.toString(), UserStory.class));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        return new Simulation(
                UUID.fromString(simulationJson.getString("ID")),
                simulationJson.getString("Name"),
                simulationJson.getInt("Count"),
                simulationJson.getInt("DurationDays"),
                sprints,
                userStories
        );
    }

    private static JSONObject simulationToJson(Simulation simulation) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ID", simulation.getSimulationId());
        jsonObject.put("Name", simulation.getSimulationName());
        jsonObject.put("Status", "New");
        jsonObject.put("DurationDays", simulation.getSprintDuration());
        jsonObject.put("Count", simulation.getSprintCount());
        jsonObject.put("Sprints", simulation.getSprints());
        jsonObject.put("UserStories", simulation.getUserStories());
        return jsonObject;
    }
}
