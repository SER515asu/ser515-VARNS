package com.groupesan.project.java.scrumsimulator.mainpackage.state;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.Simulation;

public class SimulationStateTest {

    private SimulationStateManager simulationStateManager;

    @BeforeEach
    public void setUp() {
        simulationStateManager = SimulationStateManager.getInstance();

    }

    @AfterEach
    public void tearDown() {
        simulationStateManager.setRunning(false);
    }

    @Test
    public void testInitialState() {
        assertFalse(simulationStateManager.isRunning());
    }

    @Test
    public void testStartSimulation() {
        simulationStateManager.setCurrentSimulation(
                new Simulation("Test Simulation", 0, 0));
        try {
            simulationStateManager.startSimulation();
        } catch (HeadlessException e) {
            // Expected exception
        }
        assertTrue(simulationStateManager.isRunning());

    }

    @Test
    public void testStopSimulation() {
        simulationStateManager.setCurrentSimulation(
                new Simulation("Test Simulation", 0, 0));
        simulationStateManager.stopSimulation();
        assertFalse(simulationStateManager.isRunning());
    }

}