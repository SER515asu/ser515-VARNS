package com.groupesan.project.java.scrumsimulator.mainpackage.state;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

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

    private Simulation currentSimulation;
    private SprintStateEnum state;
    private Integer day;
    private Integer sprint;
    private Integer progressValue;

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
        SimulationSingleton.getInstance().saveSimulationDetails();
    }

    private void runSimulation() {
        while (true) {
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

            if (sprint >= currentSimulation.getSprintCount() && day >= currentSimulation.getSprintDuration()) {
                stopSimulation();
            } else {
                day++;
                if (day > currentSimulation.getSprintDuration()) {
                    day = 1;
                    sprint++;
                }
            }
        }
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
                    notifyBlockerResolved(blocker);
                }
            }
        }
    }

    public SprintStateEnum getState() {
        return state;
    }

    public void setState(SprintStateEnum state) {
        this.state = state;
    }
}
