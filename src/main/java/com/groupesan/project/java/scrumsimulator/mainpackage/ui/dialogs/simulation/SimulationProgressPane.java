package com.groupesan.project.java.scrumsimulator.mainpackage.ui.dialogs.simulation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
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



        String[] userStoryColumnNames = { "User Story Name", "Status", "Set In Progress", "Set Blocked" , "Set Spiked", "Set Completed"};
        model = new DefaultTableModel(userStoryColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 2;
            }
        };
        userStoryContainer = new JTable(model);

        userStoryScrollPane = new JScrollPane(userStoryContainer);
        for(int i = 2; i < userStoryColumnNames.length; i++) {
            userStoryContainer.getColumn(userStoryColumnNames[i]).setCellRenderer(new ButtonRenderer());
            userStoryContainer.getColumn(userStoryColumnNames[i]).setCellEditor(new ButtonEditor(new JCheckBox(), model));
        }
        // userStoryScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        simPan.add(jimPan);
        simPan.add(currentProgressValue);
        simPan.add(jimProg);
        simPan.add(pauseSimulationButton);
        simPan.add(userStoryScrollPane);
    }



    public void addUserStory(UserStory USText) {
        String status = (((USText.getUserStoryState() instanceof UserStoryUnselectedState)) ? "N/A" : "Added");

        JButton inProg = new JButton("In Progress");
        JButton blocked = new JButton("Blocked");
        JButton spiked = new JButton("Spiked");
        JButton completed = new JButton("Completed");

        model.addRow(new Object[] { USText.getName(), status, inProg, blocked, spiked, completed});
        userStoryContainer.revalidate();
        userStoryContainer.repaint();
    }

    public void changeState(UserStory userStory) {
        UserStoryState userStoryState = userStory.getUserStoryState();

        System.out.println(userStory.getName());
        System.out.println(userStoryState instanceof UserStorySelectedState);

        if(userStoryState instanceof UserStoryNewState) {
                setStatus(userStory, "In Progress");
        }
        else if(userStoryState instanceof UserStoryCompletedState) {
            setStatus(userStory, "Completed");
        }
        userStoryContainer.revalidate();
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

                        if ("New".equals(progress)) {
                            userStoryCell.setForeground(Color.ORANGE);
                        }
                        else if ("Spiked".equals(progress)) {
                            userStoryCell.setForeground(Color.RED);
                        }
                        else if ("Blocked".equals(progress)) {
                            userStoryCell.setForeground(Color.PINK);
                        }
                        else if ("In Progress".equals(progress)) {
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
        //userStoryContainer.revalidate();
        userStoryContainer.repaint();
    }



    public void resetPanel() {
        // Had to remove SwingUtilities to be able to refresh the panel.

        for(int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
        }
        //userStoryContainer.revalidate();
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

        System.out.println("Pause button pressed");
        System.out.println(state);
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

class ButtonRenderer extends JButton implements TableCellRenderer {

    public ButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "Actions" : value.toString());
        return this;
    }
}

/**
 * Borrowing elements of PotentialBlockerSolutionsPane
 */
class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private DefaultTableModel tabModel;
    private int column;
    private int row;
    public ButtonEditor(JCheckBox checkBox, DefaultTableModel model) {
        super(checkBox);
        button = new JButton();
        tabModel = model;
        button.setVisible(false);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
                if(column == 2) {
                    model.setValueAt("In Progress", row, 1);
                }
                if(column == 3) {
                    model.setValueAt("Blocked", row, 1);
                }
                if(column == 4) {
                    model.setValueAt("Spiked", row, 1);
                }
                if(column == 5) {
                    model.setValueAt("Completed", row, 1);
                }
            }
        });
    }


    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof JButton) {
            button = (JButton) value;
            this.isPushed = true;
            this.row = row;
            this.column = column;
            return button;
        }
        return null;
    }

    @Override
    public Object getCellEditorValue() {
        isPushed = false;
        return button;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}