package com.groupesan.project.java.scrumsimulator.mainpackage.state;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import javax.swing.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.Simulation;

/**
 * SimulationStateManager manages the state of a simulation, including whether
 * it is running and saving its ID.
 */
public class SimulationStateManager {
    private boolean running;
    private static final String JSON_FILE_PATH = "src/main/resources/simulation.JSON";

    private Simulation currentSimultation;
    private JPanel simPan = new JPanel();
    private JLabel jimPan = new JLabel("This is the UI!");
    private JProgressBar jimProg = new JProgressBar();

    JFrame framePan = new JFrame();

    private Integer day = 1;
    private Integer sprint = 1;

    private Integer progressValue;

    private static SimulationStateManager instance;

    private SimulationStateManager() {
        this.running = false;
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
     */
    public void setCurrentSimulation(Simulation simulation) {
        this.currentSimultation = simulation;
    }

    /**
     * Returns the current state of the simulation.
     *
     * @return boolean running
     */
    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    private void runSimulation() {

        try {
            // Instead of sleeping for the full second, we sleep for 100ms and check if the simulation is still running
            // This allows the simulation to be stopped more responsively
            for (int i = 0; i < 10; i++) {
                Thread.sleep(100);
                if (!isRunning()) {
                    return;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Logic of running the simulation goes here
        // I've tailored the logic to display the progress of the simulation through these lines.
        progressValue = (int)Math.round(100.0 / currentSimultation.getSprintDuration() * day );
        jimPan.setText("Running simulation for day "
                + day
                + " of " + currentSimultation.getSprintDuration()
                + " of sprint " + sprint
                + progressValue + " %");

        // Extremely long message, changed them to be in new lines with each for clarity's sake + - Suparno

        System.out.println(progressValue);
        jimProg.setValue(progressValue);


        if (sprint >= currentSimultation.getSprintCount() && day >= currentSimultation.getSprintDuration()) {
            completeSimulation();
        } else {
            day++;
            if (day > currentSimultation.getSprintDuration()) {
                day = 1;
                sprint++;
            }
            runSimulation();
        }
    }

    /** Method to set the simulation state to running. */
    public void startSimulation() {
        if (currentSimultation == null) {
            JOptionPane.showMessageDialog(null, "No simulation selected");
            return;
        }

        progressValue = 0;
        /**
         * This is the part where the UI for the progress bar pops up. - Suparno
         */
        setRunning(true);
        simPan.add(jimPan);
        simPan.add(jimProg); // progress bar is added here - Suparno
        framePan.add(simPan);
        framePan.setSize(300,300);
        framePan.setVisible(true);


        new Thread(() -> runSimulation()).start();

        JOptionPane.showMessageDialog(null, "Simulation started!");
    }

    /** Method to set the simulation state to not running. */
    public void stopSimulation() {
        if (currentSimultation == null) {
            JOptionPane.showMessageDialog(null, "No simulation selected");
            return;
        }

        setRunning(false);
        // Add other logic for stopping the simulation
    }

    private void completeSimulation() {
        setRunning(false);

        // save simulation data
        JSONObject simulationData = getSimulationData();

        if (simulationData != null) {
            JSONArray simulations = simulationData.optJSONArray("Simulations");
            if (simulations != null) {
                for (int i = 0; i < simulations.length(); i++) {
                    JSONObject simulation = simulations.getJSONObject(i);
                    if (simulation.getString("Status").equals("Running")) {
                        simulation.put("Status", "Completed");
                        break;
                    }
                }
                updateSimulationData(simulationData);
            }
        }

        JOptionPane.showMessageDialog(null, "Simulation completed!");
    }

    /**
     * Saves the details of a new simulation to a JSON file.
     *
     * @param simId           The ID of the simulation.
     * @param simName         The name of the simulation.
     * @param numberOfSprints The number of sprints in the simulation.
     * @param sprintDuration  The duration of each sprint in the simulation.
     */
    public static void saveNewSimulationDetails(String simId, String simName, Integer numberOfSprints, Integer sprintDuration) {
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
}
