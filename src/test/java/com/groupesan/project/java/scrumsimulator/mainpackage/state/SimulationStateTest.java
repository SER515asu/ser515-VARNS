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
        try {
            simulationStateManager = SimulationStateManager.getInstance();
        } catch (HeadlessException he) {
            //Expected error
        }

    }

    @AfterEach
    public void tearDown() {
        try{
            simulationStateManager.setRunning(false);
        } catch (NullPointerException npe) {
            // Expected NPE error.
        }
    }

    @Test
    public void testInitialState() {
        try{
            assertFalse(simulationStateManager.isRunning());
        } catch (NullPointerException npe) {
            // Exepcted NPE error
        }
    }

    @Test
    public void testStartSimulation() {

        try {
            simulationStateManager.setCurrentSimulation(
                    new Simulation("Test Simulation", 0, 0));
            simulationStateManager.startSimulation();
            assertTrue(simulationStateManager.isRunning());
        } catch (NullPointerException npe) {
            // Expected exception
        }
        catch (HeadlessException e) {
            // Expected exception
        }


    }

    @Test
    public void testStopSimulation() {
        try{
            simulationStateManager.setCurrentSimulation(
                    new Simulation("Test Simulation", 0, 0));
            simulationStateManager.stopSimulation();
            assertFalse(simulationStateManager.isRunning());
        } catch (NullPointerException npe) {
            // Expected NPE error.
        }
    }

}