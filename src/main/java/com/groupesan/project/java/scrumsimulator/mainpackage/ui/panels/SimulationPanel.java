package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class SimulationPanel extends JPanel implements BaseComponent {

    private SimulationStateManager simulationStateManager;
    private JButton startSimulationButton;
    private JButton stopSimulationButton;

    /** Simulation Panel Initialization. */
    protected SimulationPanel(SimulationStateManager simulationStateManager) {
        this.simulationStateManager = simulationStateManager;
        this.init();
    }

    @Override
    public void init() {
        startSimulationButton = new JButton("Start Simulation");
        stopSimulationButton = new JButton("Stop Simulation");

        stopSimulationButton.setVisible(false);


        JPanel simPan = new JPanel();
        JLabel jimPan = new JLabel("This is the UI!");

        JFrame framePan = new JFrame();
        simPan.add(jimPan);
        framePan.add(simPan);

        startSimulationButton.addActionListener(


                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        simulationStateManager.startSimulation();
                        //JOptionPane.showMessageDialog(null, "Simulation started!");
                        System.out.println("Am being pressed!");
                        framePan.setVisible(true);
                        updateButtonVisibility();

                    }
                });

        stopSimulationButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        simulationStateManager.stopSimulation();
                        JOptionPane.showMessageDialog(null, "Simulation stopped!");
                        updateButtonVisibility();
                    }
                });

        add(startSimulationButton);
        add(stopSimulationButton);
    }

    private void updateButtonVisibility() {
        // Show/hide buttons based on the simulation state
        if (simulationStateManager.isRunning()) {
            stopSimulationButton.setVisible(true);
            startSimulationButton.setVisible(false);
        } else {
            stopSimulationButton.setVisible(false);
            startSimulationButton.setVisible(true);
        }
    }
}
