package com.groupesan.project.java.scrumsimulator.mainpackage.ui.dialogs.simulation;

import java.awt.event.ActionEvent;
import java.util.*;
import java.awt.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import com.groupesan.project.java.scrumsimulator.mainpackage.core.BlockerObject;
import com.groupesan.project.java.scrumsimulator.mainpackage.core.BlockerType;
import com.groupesan.project.java.scrumsimulator.mainpackage.core.Simulation;
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
        System.out.println("State when added: " + USText.getUserStoryState());
//        String status = (((USText.getUserStoryState() instanceof UserStoryUnselectedState)) ? "N/A" : "New");
        String status = "";
        if(USText.getUserStoryState() instanceof UserStoryUnselectedState) {
            status = "New";
        } 


        model.addRow(new Object[] { USText.getName(), status, "In Progress", "Blocked" , "Spiked", "Completed"});
        userStoryContainer.revalidate();
        userStoryContainer.repaint();
    }

    public void changeState(UserStory userStory) {
        UserStoryState userStoryState = userStory.getUserStoryState();


        if(userStoryState instanceof UserStoryInProgressState) {
                setStatus(userStory, "In Progress");
        }
        else if(userStoryState instanceof UserStoryBlockedState) {
            int recentBlocker = userStory.getBlockers().size();
            String blocker = String.valueOf(userStory.getBlockers().get(recentBlocker-1));

            Pattern pattern = Pattern.compile(".*\\[Blocker\\]\\s*\\[Blocker\\]\\s*(.*)");
            Matcher matcher = pattern.matcher(blocker);


            if(matcher.matches()) {
                setStatus(userStory, "Blocked - " + matcher.group(1).trim());

            }
        }
        else if(userStoryState instanceof UserStorySpikedState) {
            setStatus(userStory, "SPIKED");
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

                        System.out.println("Status type: " + progress.toString());
                        if ("New".equals(progress)) {
                            userStoryCell.setForeground(Color.ORANGE);
                        }
                        else if ("In Progress".equals(progress)) {
                            userStoryCell.setForeground(Color.BLUE);
                        }
                        else if (progress.toString().contains("Blocked")) {
                            userStoryCell.setForeground(Color.RED);
                        }
                        else if ("SPIKED".equals(progress)) {
                            userStoryCell.setForeground(Color.MAGENTA);
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

        for(int i = 0; i < rowCount; i++){
            String currentUS = (String) model.getValueAt(i, userStoryRow);
            if(currentUS.equals(selectedUS)) {
                model.setValueAt(status, i, statusColumn);
                break;
            }
        }

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

        userStoryContainer.revalidate();
        userStoryContainer.repaint();

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

class ButtonRenderer extends JButton implements TableCellRenderer {


    public ButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "Actions" : value.toString());
        SimulationStateManager stateManager = SimulationStateManager.getInstance();
        SprintStateEnum state = stateManager.getState();
        Simulation currentSimulation = stateManager.getCurrentSimulation();
        int currentSprint = stateManager.getSprintNum();

        setEnabled(state != SprintStateEnum.RUNNING);
        setBackground(state == SprintStateEnum.RUNNING ? Color.LIGHT_GRAY : UIManager.getColor("Button.background"));

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
    private JTable table;
    private DefaultTableModel tabModel;
    private int column;
    private int row;
    public ButtonEditor(JCheckBox checkBox, DefaultTableModel model) {
        super(checkBox);
        button = new JButton();
        tabModel = model;
        button.setOpaque(true);
        button.addActionListener(e -> {
            if (button.isEnabled()) {
                fireEditingStopped();
            }
        });
        //button.setVisible(false);
    }


    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        SimulationStateManager stateManager = SimulationStateManager.getInstance();
        SprintStateEnum state = stateManager.getState();
        label = (value == null) ? "Actions" : value.toString();

        this.table = table;
        this.row = row;
        this.column = column;
        boolean x = state != SprintStateEnum.RUNNING;
        button.setEnabled(state != SprintStateEnum.RUNNING);

        if(state == SprintStateEnum.RUNNING) {
            button.setBackground(Color.LIGHT_GRAY);
            return null;
        }

        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        if(isPushed) {
            super.fireEditingStopped();
            Object c1 = tabModel.getValueAt(row, 0);
            Object c2 = tabModel.getValueAt(row, 1);

            Object c3 = tabModel.getValueAt(row, column);



            isPushed = false;

            SimulationStateManager stateManager = SimulationStateManager.getInstance();
            Simulation currentSimulation = stateManager.getCurrentSimulation();
            int currentSprint = stateManager.getSprintNum();
            for (UserStory userStory : currentSimulation.getSprints().get(currentSprint-1).getUserStories()) {
                if(userStory.getName().equals(c1)) {
                    switch (c3.toString()) {
                        case "In Progress":
                            if(userStory.isBlocked()) {
                                userStory.resolveBlockers();
                            }
                            userStory.changeState(new UserStoryInProgressState(userStory));
                            tabModel.setValueAt("In Progress", row, 1);
                            break;
                        case "Completed":
                            if(userStory.isBlocked()) {
                                userStory.resolveBlockers();
                            }
                            userStory.changeState(new UserStoryCompletedState(userStory));
                            tabModel.setValueAt("Completed", row, 1);
                            break;
                        case "Blocked":
                            userStory.changeState(new UserStoryBlockedState(userStory));
                            BlockerType blockerTypeManual = new BlockerType("Manual", 0, 90, 10);
                            BlockerObject blockerManual = new BlockerObject(blockerTypeManual);
                            userStory.setBlocker(blockerManual);
                            tabModel.setValueAt("Blocked - Manually", row, 1);
                            break;
                        case "Spiked":
                            userStory.changeState(new UserStorySpikedState(userStory));
                            tabModel.setValueAt("SPIKED", row, 1);
                            break;
                    }
                }
            }

            System.out.println("C1 " + c1);
            System.out.println("C2 " + c2);
            System.out.println("C3 " + c3.toString());

        }
    }
}