package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


import javax.swing.JButton;
import javax.swing.JPanel;

import static com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager.getSimulationData;

public class SimulationPanel extends JPanel implements BaseComponent {

    private JButton startSimulationButton;
    private JButton stopSimulationButton;

    public static SimulationPanel simPan;

    /** Simulation Panel Initialization. */
    protected SimulationPanel() {
        this.init();
    }

    /**
     * Method to get the instance of simulation panel. Made to update button status.
     * @return
     */
    public static synchronized SimulationPanel getSimPan() {
        if (simPan == null) {
            simPan = new SimulationPanel();
        }
        return simPan;
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


        Runnable r = new Runnable() {
            public void run() {
            JSONObject simulationData = getSimulationData();
            JSONArray simulations = simulationData.optJSONArray("Simulations");
            for (int i = 0; i < simulations.length(); i++) {
                JSONObject simulation = simulations.getJSONObject(i);
                if (simulation.getString("Status").equals("Complete")) {
                    stopSimulationButton.setVisible(true);
                    startSimulationButton.setVisible(false);
                }
             }
            }
        };

        new Thread(r).start();
    }

    // Used the same method to update status here.


    public void updateButtonVisibility() {
        if (SimulationStateManager.getInstance().isRunning()) {
            stopSimulationButton.setVisible(true);
            startSimulationButton.setVisible(false);
        } else {
            stopSimulationButton.setVisible(false);
            startSimulationButton.setVisible(true);
        }
    }
}
