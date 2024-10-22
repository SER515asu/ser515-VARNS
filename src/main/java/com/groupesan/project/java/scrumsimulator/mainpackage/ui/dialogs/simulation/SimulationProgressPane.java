package com.groupesan.project.java.scrumsimulator.mainpackage.ui.dialogs.simulation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

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

    private static volatile JPanel checkboxGroup = new JPanel();

    private static volatile JPanel userStoryContainer = new JPanel();

    private static ArrayList<JCheckBox> checkBoxList = new ArrayList<>();

    public SimulationProgressPane() {
        simPan = new JPanel();
        simPan.setLayout(new BoxLayout(simPan, BoxLayout.Y_AXIS));


        simPan.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        jimPan = new JLabel();
        currentProgressValue = new JLabel();
        jimProg = new JProgressBar();
        pauseSimulationButton = new JButton("Pause Simulation");
        pauseSimulationButton.addActionListener(this::handlePauseSimulation);



        userStoryContainer.setLayout(new BoxLayout(userStoryContainer, BoxLayout.Y_AXIS));



        blockerScrollPane = new JScrollPane(userStoryContainer);
        blockerScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);



        simPan.add(jimPan);
        simPan.add(currentProgressValue);
        simPan.add(jimProg);
        simPan.add(pauseSimulationButton);
        simPan.add(blockerScrollPane);

    }

//    public static void addBlocker(String blocker) {
//        JPanel blockerTextPanel = new JPanel();
//        blockerTextPanel.setLayout(new BorderLayout());
//
//        JLabel blockerText = new JLabel(blocker);
//        JCheckBox checkBoxButton = new JCheckBox();
//
//        checkBoxList.add(checkBoxButton);
//        blockerTextPanel.add(blockerText, BorderLayout.WEST);
//        blockerTextPanel.add(checkBoxButton, BorderLayout.EAST);
//
//        blockerContainer.add(blockerTextPanel);
//        blockerContainer.revalidate();
//        blockerContainer.repaint();
//
//    }

    public static void addUserStory(String USText) {
        JPanel userStoryTextPanel = new JPanel();
        userStoryTextPanel.setLayout(new BorderLayout());

        JLabel userStoryText = new JLabel(USText);
        userStoryTextPanel.add(userStoryText, BorderLayout.WEST);
        JLabel userStoryHeader = new JLabel("User Story");
        JLabel userProgressHeader = new JLabel("Progress");
        userStoryTextPanel.add(userStoryHeader, BorderLayout.WEST);
        userStoryTextPanel.add(userProgressHeader, BorderLayout.EAST);
        // checkBoxList.add(checkBoxButton);

        // blockerTextPanel.add(checkBoxButton, BorderLayout.EAST);

        userStoryContainer.add(userStoryText);
        userStoryContainer.add(userStoryHeader);
        userStoryContainer.add(userProgressHeader);
        userStoryContainer.revalidate();
        userStoryContainer.repaint();

    }

    public static void resetPanel() {
        userStoryContainer.removeAll();
        userStoryContainer.revalidate();
        userStoryContainer.repaint();
    }

    public static boolean checkResolved(){
        SimulationStateManager stateManager = SimulationStateManager.getInstance();

        for(JCheckBox checkBox : checkBoxList) {
            if(!checkBox.isSelected()) {
                stateManager.setState(SprintStateEnum.START_SPRINT);
                return false;
            }
        }
        return true;
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

