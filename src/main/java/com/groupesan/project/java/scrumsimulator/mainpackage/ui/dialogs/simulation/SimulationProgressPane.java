package com.groupesan.project.java.scrumsimulator.mainpackage.ui.dialogs.simulation;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager.SprintStateEnum;

public class SimulationProgressPane {
    private JPanel simPan; // changed to static retrieve and place text values of blockers detected.
    private JLabel jimPan;
    private JLabel currentProgressValue;
    private JProgressBar jimProg;
    private JButton pauseSimulationButton;

    private JScrollPane blockerScrollPane;

    private static final JPanel blockerContainer = new JPanel();

    public SimulationProgressPane() {
        simPan = new JPanel();
        simPan.setLayout(new BoxLayout(simPan, BoxLayout.Y_AXIS));


        simPan.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        jimPan = new JLabel();
        currentProgressValue = new JLabel();
        jimProg = new JProgressBar();
        pauseSimulationButton = new JButton("Pause Simulation");
        pauseSimulationButton.addActionListener(this::handlePauseSimulation);



        blockerContainer.setLayout(new BoxLayout(blockerContainer, BoxLayout.Y_AXIS));

        blockerScrollPane = new JScrollPane(blockerContainer);
        blockerScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);



        simPan.add(jimPan);
        simPan.add(currentProgressValue);
        simPan.add(jimProg);
        simPan.add(pauseSimulationButton);
        simPan.add(blockerScrollPane);

    }

    public static void addBlocker(String blocker) {
        JPanel blockerTextPanel = new JPanel();
        blockerTextPanel.setLayout(new BorderLayout());

        JLabel blockerText = new JLabel(blocker);
        JToggleButton toggleButton = new JToggleButton("Toggle");

        blockerTextPanel.add(blockerText, BorderLayout.WEST);
        blockerTextPanel.add(toggleButton, BorderLayout.EAST);

        blockerContainer.add(blockerTextPanel);
        blockerContainer.revalidate();
        blockerContainer.repaint();

        //JLabel blockerText = new JLabel(blocker);
        //blockerContainer.add(blockerText);
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

