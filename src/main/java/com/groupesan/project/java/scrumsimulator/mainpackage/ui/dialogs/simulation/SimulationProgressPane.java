package com.groupesan.project.java.scrumsimulator.mainpackage.ui.dialogs.simulation;

import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;
import java.awt.*;
import javax.swing.*;
import com.groupesan.project.java.scrumsimulator.mainpackage.core.BlockerObject;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager.SprintStateEnum;

public class SimulationProgressPane {
    private JPanel simPan;
    private JLabel jimPan;
    private JLabel currentProgressValue;
    private JProgressBar jimProg;
    private JButton pauseSimulationButton;

    private JScrollPane blockerScrollPane;
    private JPanel blockerContainer;

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

        blockerContainer = new JPanel();
        blockerContainer.setLayout(new BoxLayout(blockerContainer, BoxLayout.Y_AXIS));

        blockerScrollPane = new JScrollPane(blockerContainer);
        blockerScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        simPan.add(jimPan);
        simPan.add(currentProgressValue);
        simPan.add(jimProg);
        simPan.add(pauseSimulationButton);
        simPan.add(blockerScrollPane);
    }

    public void addBlocker(BlockerObject blocker) {
        JPanel blockerTextPanel = new JPanel(new BorderLayout());
        JLabel blockerText = new JLabel(blocker.getType().getName());
        JCheckBox checkBoxButton = new JCheckBox();

        blockerCheckBoxMap.put(blocker, checkBoxButton);

        blockerTextPanel.add(blockerText, BorderLayout.WEST);
        blockerTextPanel.add(checkBoxButton, BorderLayout.EAST);

        SwingUtilities.invokeLater(() -> {
            blockerContainer.add(blockerTextPanel);
            blockerContainer.revalidate();
            blockerContainer.repaint();
        });
    }

    public void addBlockers(List<BlockerObject> blockers) {
        for (BlockerObject blocker : blockers) {
            addBlocker(blocker);
        }
    }

    public void removeBlocker(BlockerObject blocker) {
        JCheckBox checkBox = blockerCheckBoxMap.get(blocker);
        if (checkBox != null) {
            JPanel parentPanel = (JPanel) checkBox.getParent();
            blockerCheckBoxMap.remove(blocker);

            SwingUtilities.invokeLater(() -> {
                blockerContainer.remove(parentPanel);
                blockerContainer.revalidate();
                blockerContainer.repaint();
            });
        }
    }

    public void removeBlockers(List<BlockerObject> blockers) {
        for (BlockerObject blocker : blockers) {
            removeBlocker(blocker);
        }
    }

    public void resetPanel() {
        SwingUtilities.invokeLater(() -> {
            blockerContainer.removeAll();
            blockerCheckBoxMap.clear();
            blockerContainer.revalidate();
            blockerContainer.repaint();
        });
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
