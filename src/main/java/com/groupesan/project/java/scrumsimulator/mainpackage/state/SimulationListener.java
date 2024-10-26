package com.groupesan.project.java.scrumsimulator.mainpackage.state;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.BlockerObject;

/**
 * Interface for the simulation listener
 */
public interface SimulationListener {
    void onProgressUpdate(int progressValue, int day, int sprint, int sprintDuration);
    void onBlockerDetected(BlockerObject blocker);
    void onBlockerResolved(BlockerObject blocker);
    void onSimulationStopped();
    void onSimulationStarted();
}
