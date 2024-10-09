package com.groupesan.project.java.scrumsimulator.mainpackage.ui.dialogs.simulation;

import java.awt.event.ActionEvent;

import javax.swing.*;

import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager.SprintStateEnum;

public class SimulationProgressPane {
    private JPanel simPan;
    private JLabel jimPan;
    private JLabel currentProgressValue;
    private JProgressBar jimProg;
    private JButton pauseSimulationButton;

    public SimulationProgressPane() {
        simPan = new JPanel();
        jimPan = new JLabel();
        currentProgressValue = new JLabel();
        jimProg = new JProgressBar();
        pauseSimulationButton = new JButton("Pause Simulation");
        pauseSimulationButton.addActionListener(this::handlePauseSimulation);

        simPan.add(jimPan);
        simPan.add(currentProgressValue);
        simPan.add(jimProg);
        simPan.add(pauseSimulationButton);
    }


    private void handlePauseSimulation(ActionEvent e) {
        SimulationStateManager stateManager = SimulationStateManager.getInstance();
        SprintStateEnum state = stateManager.getState();

        if(state == SprintStateEnum.START_SPRINT) {
            stateManager.setState(SprintStateEnum.PAUSE_SPRINT);
            pauseSimulationButton.setText("Start Simulation");
        } else if (state == SprintStateEnum.PAUSE_SPRINT){
            stateManager.setState(SprintStateEnum.START_SPRINT);
            pauseSimulationButton.setText("Pause Simulation");
        }
    }

    public JPanel getSimPan() {
        return simPan;
    }

    public void updateProgress(int progressValue, int day, int sprint, int sprintDuration) {
        jimPan.setText("Running simulation for day " + day + " of " + sprintDuration + " of sprint " + sprint);
        currentProgressValue.setText("Progress: " + progressValue + "%");
        jimProg.setValue(progressValue);
    }

    public void resetProgress() {
        currentProgressValue.setText("Progress: 0%");
        jimProg.setValue(0);
    }
}
