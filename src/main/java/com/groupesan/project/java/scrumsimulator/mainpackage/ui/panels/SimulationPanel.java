package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

public class SimulationPanel extends JPanel implements BaseComponent {

    private JButton startSimulationButton;
    private JButton stopSimulationButton;

    /** Simulation Panel Initialization. */
    protected SimulationPanel() {
        this.init();
    }

    @Override
    public void init() {
        startSimulationButton = new JButton("Start Simulation");
        stopSimulationButton = new JButton("Stop Simulation");

        stopSimulationButton.setVisible(false);

        startSimulationButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SimulationStateManager.getInstance().startSimulation();
                        updateButtonVisibility();
                    }
                });

        stopSimulationButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SimulationStateManager.getInstance().stopSimulation();
                        updateButtonVisibility();
                    }
                });

        add(startSimulationButton);
        add(stopSimulationButton);
    }

    private void updateButtonVisibility() {
        if (SimulationStateManager.getInstance().isRunning()) {
            stopSimulationButton.setVisible(true);
            startSimulationButton.setVisible(false);
        } else {
            stopSimulationButton.setVisible(false);
            startSimulationButton.setVisible(true);
        }
    }
}
