package com.groupesan.project.java.scrumsimulator.mainpackage.ui.dialogs.simulation;

import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.BlockerObject;
import com.groupesan.project.java.scrumsimulator.mainpackage.core.User;
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

    private DefaultTableModel model;
    private static JTable userStoryContainer;


    private ArrayList<String> userStoryList;



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


        String[] userStoryColumnNames = { "User Story Name", "Status" };
        model = new DefaultTableModel(userStoryColumnNames, 0);
        userStoryContainer = new JTable(model);

        userStoryScrollPane = new JScrollPane(userStoryContainer);
        // userStoryScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        simPan.add(jimPan);
        simPan.add(currentProgressValue);
        simPan.add(jimProg);
        simPan.add(pauseSimulationButton);
        simPan.add(userStoryScrollPane);
    }



    public void addUserStory(UserStory USText) {
        model.addRow(new Object[] { USText.getName(), "In Progress"});
        userStoryContainer.revalidate();
        userStoryContainer.repaint();
    }

    /**
     * Set the user stories in place to yellow.
     */
    public void inProgressState() {
        System.out.println("Setting colour");
        userStoryContainer.getColumnModel().getColumn(1).setCellRenderer(new StatusCellRender());
    }



    public void resetPanel() {
        // Had to remove SwingUtilities to be able to refresh the panel.

        for(int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
        }
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

class StatusCellRender extends DefaultTableCellRenderer {
    /**
     * Class created to set the status of each individual user story and to give them a colour. - Suparno
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object progress, boolean isSelected, boolean hasFocus, int row, int column) {
        Component userStoryCell = super.getTableCellRendererComponent(table, progress, isSelected, hasFocus, row, column);

        if ("In Progress".equals(progress)) {
            userStoryCell.setForeground(Color.YELLOW);
        }
        if ("Completed".equals(progress)) {
            userStoryCell.setForeground(Color.GREEN);
        } else {
            userStoryCell.setForeground(Color.BLACK);
        }

        return userStoryCell;
    }
}