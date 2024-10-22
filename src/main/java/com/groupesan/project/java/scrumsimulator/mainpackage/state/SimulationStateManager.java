package com.groupesan.project.java.scrumsimulator.mainpackage.state;

import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

import javax.swing.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.BlockerObject;
import com.groupesan.project.java.scrumsimulator.mainpackage.core.Simulation;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.BlockerTypeStore;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.UserStory;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.dialogs.simulation.SimulationProgressPane;

/**
 * SimulationStateManager manages the state of a simulation, including whether
 * it is running and saving its ID.
 */
public class SimulationStateManager {

    public enum SprintStateEnum {
        RUNNING,
        STOPPED,
        PAUSED
    }

    private static final String JSON_FILE_PATH = "src/main/resources/simulation.JSON";
    private Simulation currentSimultation;
    private SprintStateEnum state;
    private Integer day = 1;
    private Integer sprint = 1;
    private Integer progressValue;

    private static SimulationStateManager instance;

    private SimulationProgressPane progressPane = new SimulationProgressPane();
    private JFrame framePan = new JFrame();

    private SimulationStateManager() {
        this.state = SprintStateEnum.STOPPED;
    }

    /**
     * Returns the singleton instance of SimulationStateManager.
     *
     * @return the singleton instance
     */
    public static synchronized SimulationStateManager getInstance() {
        if (instance == null) {
            instance = new SimulationStateManager();
        }
        return instance;
    }

    /**
     * Sets the current simulation.
     *
     * @param simulation The simulation to set as the current simulation.
     * @return the SimulationStateManager
     */
    public SimulationStateManager setCurrentSimulation(Simulation simulation) {
        this.currentSimultation = simulation;
        return this;
    }

    /**
     * Gets the current simulation
     *
     * @return current simulation
     */
    public Simulation getCurrentSimulation() {
        return this.currentSimultation;
    }

    private void runSimulation() {
        while (state == SprintStateEnum.RUNNING) {
            try {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(100);
                    if (state != SprintStateEnum.RUNNING) {
                        return;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (state == SprintStateEnum.STOPPED) {
                return;
            }

            if (state == SprintStateEnum.PAUSED) {
                continue;
            }

            progressValue = (int) Math.round(100.0 / (currentSimultation.getSprintDuration()) * day);
            progressPane.updateProgress(progressValue, day, sprint, currentSimultation.getSprintDuration());

            System.out.println("Day: " + day + " Sprint: " + sprint);

            for (UserStory userStory : currentSimultation.getSprints().get(sprint - 1).getUserStories()) {
                System.out.println(userStory.getName() + " " + userStory.getUserStoryState().getClass().getSimpleName()
                        + (userStory.isBlocked() ? "Blocked" : ""));
            }

            resolveBlockers();
            detectBlockers();

            if (sprint >= currentSimultation.getSprintCount() && day >= currentSimultation.getSprintDuration()) {
                blockerCheck();
                stopSimulation();
            } else {
                day++;
                if (day > currentSimultation.getSprintDuration()) {
                    blockerCheck();
                    SimulationProgressPane.resetPanel();
                    day = 1;
                    sprint++;
                }
            }
        }
    }

    private void blockerCheck() {
        if (!SimulationProgressPane.checkResolved()) {
            JOptionPane.showMessageDialog(null,
                    "There are unresolved issues! Resolve them before the sprint can proceed");
            while (!SimulationProgressPane.checkResolved()) {
                continue;
            }
        }
    }

    private void resolveBlockers() {
        for (int i = 0; i < currentSimultation.getSprints().get(sprint - 1).getUserStories().size(); i++) {
            currentSimultation.getSprints().get(sprint - 1).getUserStories().get(i).resolveBlockers();
        }
    }

    private void detectBlockers() {
        BlockerTypeStore blockerStore = BlockerTypeStore.get();

        // For every unresolved user story in the current sprint, roll for blocker
        for (var userStory : currentSimultation.getSprints().get(sprint - 1).getUserStories()) {

            if (userStory.getUserStoryState() instanceof UserStoryCompletedState) {
                continue;
            }

            BlockerObject blocker = blockerStore.rollForBlocker();

            if (blocker != null) {
                System.out.println("Blocker detected: " + blocker.getType().getName());
                SimulationProgressPane.addBlocker(blocker.getType().getName());
                userStory.setBlocker(blocker);
            }
        }
    }

    /** Method to set the simulation state to running. */
    public void startSimulation() {
        if (currentSimultation == null) {
            JOptionPane.showMessageDialog(null, "No simulation selected");
            return;
        }

        progressValue = 0;
        progressPane.resetProgress();

        framePan.add(progressPane.getSimPan());
        framePan.setSize(300, 300);
        framePan.setVisible(true);

        framePan.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                if (state == SprintStateEnum.STOPPED) {
                    return;
                }

                int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to stop the simulation?",
                        "Warning", JOptionPane.YES_NO_OPTION);

                if (dialogResult == JOptionPane.YES_OPTION) {
                    stopSimulation();
                    framePan.dispose();
                }
            }
        });

        state = SprintStateEnum.RUNNING;
        new Thread(this::runSimulation).start();
    }

    /** Method to set the simulation state to not running. */
    public void resetSimulation() {
        if (currentSimultation == null) {
            JOptionPane.showMessageDialog(null, "No simulation selected");
            return;
        }

        state = SprintStateEnum.STOPPED;
        JOptionPane.showMessageDialog(null, "Simulation stopped!");
        framePan.dispatchEvent(new WindowEvent(framePan, WindowEvent.WINDOW_CLOSING));

        day = 1;
        sprint = 1;
        progressValue = 0;

        progressPane.resetProgress();
    }

    public void stopSimulation() {
        if (currentSimultation == null) {
            JOptionPane.showMessageDialog(null, "No simulation selected");
            return;
        }

        if (state != SprintStateEnum.STOPPED) {
            state = SprintStateEnum.STOPPED;
        } else {
            return;
        }

        SimulationProgressPane.resetPanel();
        // Included JSON code to indicate stopped simulations.
        JSONObject simulationData = getSimulationData();
        if (simulationData != null) {
            JSONArray simulations = simulationData.optJSONArray("Simulations");
            if (simulations != null) {
                for (int i = 0; i < simulations.length(); i++) {
                    JSONObject simulation = simulations.getJSONObject(i);
                    if (simulation.getString("Status").equals("Running")) {
                        simulation.put("Status", "Stopped");
                        break;
                    }
                }
                updateSimulationData(simulationData);
            }
        }
        JOptionPane.showMessageDialog(null, "Simulation stopped!");
        framePan.dispatchEvent(new WindowEvent(framePan, WindowEvent.WINDOW_CLOSING));
    }

    /**
     * Saves the details of a new simulation to a JSON file.
     *
     * @param simId           The ID of the simulation.
     * @param simName         The name of the simulation.
     * @param numberOfSprints The number of sprints in the simulation.
     * @param sprintDuration  The duration of each sprint in the simulation.
     */

    public static void saveNewSimulationDetails(String simId, String simName, Integer numberOfSprints,
            Integer sprintDuration) {
        JSONObject simulationData = getSimulationData();
        if (simulationData == null) {
            simulationData = new JSONObject();
        }

        JSONObject newSimulation = new JSONObject();
        newSimulation.put("ID", simId);
        newSimulation.put("Name", simName);
        newSimulation.put("Status", "New");
        newSimulation.put("SprintDuration", sprintDuration);
        newSimulation.put("NumberOfSprints", numberOfSprints);
        newSimulation.put("sprintDuration", sprintDuration);
        newSimulation.put("Sprints", new JSONArray());
        newSimulation.put("Events", new JSONArray());
        newSimulation.put("Users", new JSONArray());

        JSONArray simulations = simulationData.optJSONArray("Simulations");
        if (simulations == null) {
            simulations = new JSONArray();
            simulationData.put("Simulations", simulations);
        }
        simulations.put(newSimulation);

        updateSimulationData(simulationData);
    }

    private static JSONObject getSimulationData() {
        try (FileInputStream fis = new FileInputStream(JSON_FILE_PATH)) {
            JSONTokener tokener = new JSONTokener(fis);
            return new JSONObject(tokener);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading from simulation.JSON");
            return null;
        }
    }

    private static void updateSimulationData(JSONObject updatedData) {
        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(JSON_FILE_PATH), StandardCharsets.UTF_8)) {
            writer.write(updatedData.toString(4));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error writing to simulation.JSON");
        }
    }

    public SprintStateEnum getState() {
        return state;
    }

    public void setState(SprintStateEnum state) {
        this.state = state;
    }
}
