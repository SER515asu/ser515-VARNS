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
import com.groupesan.project.java.scrumsimulator.mainpackage.state.*;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager.SprintStateEnum;


public class SimulationProgressPane {
    private JPanel simPan;
    private JLabel jimPan;
    private JLabel currentProgressValue;
    private JProgressBar jimProg;
    private JButton pauseSimulationButton;

    private JScrollPane userStoryScrollPane;

    private  DefaultTableModel model = null;
    private JTable userStoryContainer;





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
        model = new DefaultTableModel(userStoryColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
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
        String status = (((USText.getUserStoryState() instanceof UserStoryUnselectedState)) ? "N/A" : "Added");

        model.addRow(new Object[] { USText.getName(), status});
        userStoryContainer.revalidate();
        userStoryContainer.repaint();
    }

    public void changeState(UserStory userStory) {
        UserStoryState userStoryState = userStory.getUserStoryState();

        System.out.println(userStory.getName());
        System.out.println(userStoryState instanceof UserStorySelectedState);

        if(userStoryState instanceof UserStorySelectedState) {
                setStatus(userStory, "Selected");
        }
        else if(userStoryState instanceof UserStoryCompletedState) {
            setStatus(userStory, "Completed");
        }
        userStoryContainer.repaint();
    }

    /**
     * Set the user stories in place to yellow.
     */
    public void inProgressState() {
        userStoryContainer.getColumn("Status").setCellRenderer(
                new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object progress, boolean isSelected, boolean hasFocus, int row, int column) {
                        Component userStoryCell = super.getTableCellRendererComponent(table, progress, isSelected, hasFocus, row, column);

                        if ("Added".equals(progress)) {
                            userStoryCell.setForeground(Color.ORANGE);
                        }
                        else if ("Selected".equals(progress)) {
                            userStoryCell.setForeground(Color.BLUE);
                        }
                        else if ("Completed".equals(progress)) {
                            userStoryCell.setForeground(Color.GREEN);
                        } else {
                            userStoryCell.setForeground(Color.BLACK);
                        }

                        return userStoryCell;
                    }
                }
         );
        userStoryContainer.repaint();
    }



    public void resetPanel() {
        // Had to remove SwingUtilities to be able to refresh the panel.

        for(int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
        }
        userStoryContainer.revalidate();
        userStoryContainer.repaint();
    }

    private void setStatus(UserStory US, String status) {

        int rowCount = model.getRowCount();
        int userStoryRow = 0;
        int statusColumn = 1;
        String selectedUS = US.getName();

        System.out.println("Story selected: " + selectedUS);

        for(int i = 0; i < rowCount; i++){
            String currentUS = (String) model.getValueAt(i, userStoryRow);
            System.out.println("Story selected: " + selectedUS);
            System.out.println("Current user story: " + currentUS);
            if(currentUS.equals(selectedUS)) {
                model.setValueAt(status, i, statusColumn);
                System.out.println("Story found");
                break;
            }
        }

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

    // TODO - I want to keep this because it can refactored later to be reused to display blockers within the story
//    public void addBlocker(BlockerObject blocker) {
//        JPanel blockerTextPanel = new JPanel(new BorderLayout());
//        JLabel blockerText = new JLabel(blocker.getType().getName());
//        JCheckBox checkBoxButton = new JCheckBox();
//
//        blockerCheckBoxMap.put(blocker, checkBoxButton);
//
//        blockerTextPanel.add(blockerText, BorderLayout.WEST);
//        blockerTextPanel.add(checkBoxButton, BorderLayout.EAST);
//
//        SwingUtilities.invokeLater(() -> {
//            blockerContainer.add(blockerTextPanel);
//            blockerContainer.revalidate();
//            blockerContainer.repaint();
//        });
//    }

    //    public void addBlockers(List<BlockerObject> blockers) {
//        for (BlockerObject blocker : blockers) {
//            addBlocker(blocker);
//        }
//    }
//
//    public void removeBlocker(BlockerObject blocker) {
//        JCheckBox checkBox = blockerCheckBoxMap.get(blocker);
//        if (checkBox != null) {
//            JPanel parentPanel = (JPanel) checkBox.getParent();
//            blockerCheckBoxMap.remove(blocker);
//
//            SwingUtilities.invokeLater(() -> {
//                blockerContainer.remove(parentPanel);
//                blockerContainer.revalidate();
//                blockerContainer.repaint();
//            });
//        }
//    }
//
//    public void removeBlockers(List<BlockerObject> blockers) {
//        for (BlockerObject blocker : blockers) {
//            removeBlocker(blocker);
//        }
//    }

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

