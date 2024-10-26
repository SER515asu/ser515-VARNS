package com.groupesan.project.java.scrumsimulator.mainpackage.state;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.*;

import com.groupesan.project.java.scrumsimulator.mainpackage.ui.dialogs.simulation.SimulationProgressPane;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.BlockerObject;
import com.groupesan.project.java.scrumsimulator.mainpackage.core.Simulation;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.BlockerTypeStore;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.UserStory;

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
    private Simulation currentSimulation;
    private SprintStateEnum state;
    private Integer day;
    private Integer sprint;
    private Integer progressValue;

    private SecureRandom rand = new SecureRandom();

    private static SimulationStateManager instance;
    private final List<SimulationListener> listeners = new ArrayList<>();

    private SimulationStateManager() {
        init();
    }

    private void init() {
        state = SprintStateEnum.STOPPED;
        day = 1;
        sprint = 1;
        progressValue = 0;
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
        this.currentSimulation = simulation;
        return this;
    }

    /**
     * Gets the current simulation
     *
     * @return current simulation
     */
    public Simulation getCurrentSimulation() {
        return this.currentSimulation;
    }

    public void addListener(SimulationListener listener) {
        listeners.add(listener);
    }

    public void removeListener(SimulationListener listener) {
        listeners.remove(listener);
    }

    private void notifyProgressUpdate() {
        for (SimulationListener listener : listeners) {
            listener.onProgressUpdate(progressValue, day, sprint, currentSimulation.getSprintDuration());
        }
    }

    private void notifyBlockerDetected(BlockerObject blocker) {
        for (SimulationListener listener : listeners) {
            listener.onBlockerDetected(blocker);
        }
    }

    private void notifyBlockerResolved(BlockerObject blocker) {
        for (SimulationListener listener : listeners) {
            listener.onBlockerResolved(blocker);
        }
    }

    private void notifySimulationStarted() {
        for (SimulationListener listener : listeners) {
            listener.onSimulationStarted();
        }
    }

    private void notifySimulationStopped() {
        for (SimulationListener listener : listeners) {
            listener.onSimulationStopped();
        }
    }

    private void notifyUserStory(UserStory userStory) {
        for (SimulationListener listener : listeners) {
            listener.onUserStory(userStory);
        }
    }

    private void notifyInProgressUserStory() {
        for (SimulationListener listener : listeners) {
            listener.onInProgressUserStory();
        }
    }

    private void notifyResetUserStoryPanel() {
        for (SimulationListener listener : listeners) {
            listener.onSprintCompletion();
        }
    }

    private void notifyStoryStatusChange(UserStory userStory) {
        for (SimulationListener listener : listeners) {
            listener.onUserStoryStatusChange(userStory);
        }
    }

    private void notifyUserStoryStatusUpdatePanel() {
        for (SimulationListener listener : listeners) {
            listener.onSprintCompletion();
        }
    }


    public void startSimulation() {
        if (currentSimulation == null) {
            JOptionPane.showMessageDialog(null, "No simulation selected");
            return;
        }

        state = SprintStateEnum.RUNNING;
        notifySimulationStarted();
        new Thread(this::runSimulation).start();
    }

    public void stopSimulation() {
        if (currentSimulation == null) {
            JOptionPane.showMessageDialog(null, "No simulation selected");
            return;
        }

        init();

        notifySimulationStopped();
    }

    private void runSimulation() {
        addUserStory();
        while (state == SprintStateEnum.RUNNING) {
            try {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(100);
                    if (state == SprintStateEnum.STOPPED) {
                        return;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (state == SprintStateEnum.PAUSED) {
                continue;
            }

            progressValue = (int) Math.round(100.0 / currentSimulation.getSprintDuration() * day);
            notifyProgressUpdate();

            resolveBlockers();
            detectBlockers();
            detectInProgressUserStory();


            if (sprint >= currentSimulation.getSprintCount() && day >= currentSimulation.getSprintDuration()) {
                stopSimulation();
            } else {
                day++;
                setRandomStates();
                if (day > currentSimulation.getSprintDuration()) {
                    day = 1;
                    sprint++;
                    resetPanel();
                    addUserStory();
                }
            }
        }
    }

    private void resetPanel() {
        notifyResetUserStoryPanel();
    }

    private void addUserStory(){
        for (UserStory userStory : currentSimulation.getSprints().get(sprint - 1).getUserStories()) {
            notifyUserStory(userStory);
        }
    }

    public void setRandomStates() {

        // TODO - Figure out a random or linear way of setting the status of the user story.


        try {
            List<UserStory> usList =  currentSimulation.getSprints().get(sprint - 1).getUserStories();

            int randNumb =  rand.nextInt(usList.size());


            UserStory selectedStory = usList.get(randNumb);

            if(selectedStory.getUserStoryState() instanceof UserStoryAddedState) {
                selectedStory.changeState(new UserStorySelectedState(selectedStory));
                notifyStoryStatusChange(selectedStory);
            } else if (selectedStory.getUserStoryState() instanceof UserStorySelectedState) {
                selectedStory.changeState(new UserStoryCompletedState(selectedStory));
                notifyStoryStatusChange(selectedStory);
            } else {
                notifyStoryStatusChange(selectedStory);
            }
        } catch (IllegalArgumentException ie) {
            // The code should detect the lack of assigned user stories from the backlog and send a message, before closing the simulation
            //ie.printStackTrace();
        }

    }

    /**
     * Detect the state of all user stories as the simulation is in progress.
     */
    private void detectInProgressUserStory() {
        notifyInProgressUserStory();
    }


    private void detectBlockers() {
        BlockerTypeStore blockerStore = BlockerTypeStore.get();
        for (UserStory userStory : currentSimulation.getSprints().get(sprint - 1).getUserStories()) {
            BlockerObject blocker = blockerStore.rollForBlocker();
            if (blocker != null) {
                notifyBlockerDetected(blocker);
                userStory.setBlocker(blocker);
            }
        }
    }

    private void resolveBlockers() {
        for (UserStory userStory : currentSimulation.getSprints().get(sprint - 1).getUserStories()) {
            for (BlockerObject blocker : userStory.getBlockers()) {
                if (blocker == null || blocker.isResolved()) {
                    continue;
                }

                if (blocker.attemptResolve()) {
                    System.out.println("Blocker resolved: " + blocker.getType().getName() + " by " + blocker.getSolution().getName());
                    notifyBlockerResolved(blocker);
                }
            }
        }
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