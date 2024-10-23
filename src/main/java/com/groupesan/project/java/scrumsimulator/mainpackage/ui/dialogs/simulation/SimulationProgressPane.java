package com.groupesan.project.java.scrumsimulator.mainpackage.ui.dialogs.simulation;

import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;
import java.awt.*;
import javax.swing.*;
import com.groupesan.project.java.scrumsimulator.mainpackage.core.BlockerObject;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.UserStory;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager.SprintStateEnum;

public class SimulationProgressPane {
    private JPanel simPan;
    private JLabel jimPan;
    private JLabel currentProgressValue;
    private JProgressBar jimProg;
    private JButton pauseSimulationButton;

    private JScrollPane userStoryScrollPane;
    private static JPanel userStoryContainer;

    private Map<BlockerObject, JCheckBox> blockerCheckBoxMap = new HashMap<>();

    public SimulationProgressPane() {
        simPan = new JPanel();
        simPan.setLayout(new BoxLayout(simPan, BoxLayout.Y_AXIS));
        simPan.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        jimPan = new JLabel();
        currentProgressValue = new JLabel();
        jimProg = new JProgressBar(0, 100);

        pauseSimulationButton = new JButton("Pause Simulation");
        pauseSimulationButton.addActionListener(this::handlePauseSimulation);

        userStoryContainer = new JPanel();
        userStoryContainer.setLayout(new BoxLayout(userStoryContainer, BoxLayout.Y_AXIS));

        JLabel userStoryHeader = new JLabel("User Story");
        JLabel userProgressHeader = new JLabel("Progress");

        userStoryContainer.add(userStoryHeader);
        userStoryContainer.add(userProgressHeader);

        userStoryScrollPane = new JScrollPane(userStoryContainer);
        userStoryScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        simPan.add(jimPan);
        simPan.add(currentProgressValue);
        simPan.add(jimProg);
        simPan.add(pauseSimulationButton);
        simPan.add(userStoryScrollPane);
    }



    public void addUserStory(UserStory USText) {
        JPanel userStoryTextPanel = new JPanel();
        userStoryTextPanel.setLayout(new BorderLayout());


        JLabel userStoryText = new JLabel(USText.getName());
        userStoryTextPanel.add(userStoryText, BorderLayout.WEST);


        userStoryContainer.add(userStoryText);
        userStoryContainer.revalidate();
        userStoryContainer.repaint();
    }



    public void resetPanel() {
        // Had to remove SwingUtilities to be able to refresh the panel. 
        userStoryContainer.removeAll();
        userStoryContainer.revalidate();
        userStoryContainer.repaint();
    }

    public boolean checkResolved() {
        for (JCheckBox checkBox : blockerCheckBoxMap.values()) {
            if (!checkBox.isSelected()) {
                SimulationStateManager.getInstance().setState(SprintStateEnum.RUNNING);
                return false;
            }
        }
        return true;
    }

    private void handlePauseSimulation(ActionEvent e) {
        SimulationStateManager stateManager = SimulationStateManager.getInstance();
        SprintStateEnum state = stateManager.getState();

        if (state == SprintStateEnum.RUNNING) {
            stateManager.setState(SprintStateEnum.PAUSED);
            pauseSimulationButton.setText("Start Simulation");
        } else if (state == SprintStateEnum.PAUSED) {
            stateManager.setState(SprintStateEnum.RUNNING);
            pauseSimulationButton.setText("Pause Simulation");
        }
    }

    public JPanel getSimPan() {
        return simPan;
    }

    public void updateProgress(int progressValue, int day, int sprint, int sprintDuration) {
        SwingUtilities.invokeLater(() -> {
            jimPan.setText("Running simulation for day " + day + " of " + sprintDuration + " of sprint " + sprint);
            currentProgressValue.setText("Progress: " + progressValue + "%");
            jimProg.setValue(progressValue);
        });
    }

    public void resetProgress() {
        SwingUtilities.invokeLater(() -> {
            currentProgressValue.setText("Progress: 0%");
            jimProg.setValue(0);
        });
    }
}