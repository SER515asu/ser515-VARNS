package com.groupesan.project.java.scrumsimulator.mainpackage.state;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.*;

import org.assertj.swing.edt.GuiActionRunner;
import org.junit.jupiter.api.*;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.Simulation;

public class SimulationStateTest {

    private static SimulationStateManager simulationStateManager;

    @BeforeAll
    public static void setUp() {
        GuiActionRunner.execute(() -> {
            SimulationStateManager.setTestMode(true);
            simulationStateManager = SimulationStateManager.getInstance();
        });
    }

    @AfterAll
    public static void testtearDown() {
        GuiActionRunner.execute(() -> {
            if (simulationStateManager != null) {
                simulationStateManager.setRunning(false);
            }
        });
    }

    @Test
    public void testSimulationState() {

        GuiActionRunner.execute(() -> {
            // Test Initial State
            if (simulationStateManager != null) {
                assertFalse(simulationStateManager.isRunning());
            } else {
                System.out.println("simulationStateManager is null");
            }

            // Test Start Simulation
            if (simulationStateManager != null) {
                try {
                    simulationStateManager.setCurrentSimulation(
                            new Simulation("Test Simulation", 0, 0));
                    simulationStateManager.startSimulation();
                    assertTrue(simulationStateManager.isRunning());
                } catch (HeadlessException e) {
                    // Expected exception
                }
            }

            // Test Stop Simulation
            if (simulationStateManager != null) {
                simulationStateManager.setCurrentSimulation(
                        new Simulation("Test Simulation", 0, 0));
                simulationStateManager.stopSimulation();

                assertFalse(simulationStateManager.isRunning());
            }
        });
    }
}