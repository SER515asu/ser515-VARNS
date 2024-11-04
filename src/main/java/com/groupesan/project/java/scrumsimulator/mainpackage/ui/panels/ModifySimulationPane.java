package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.Simulation;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;
import com.groupesan.project.java.scrumsimulator.mainpackage.utils.CustomConstraints;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * ModifySimulationPane is a UI component used by teachers to create or modify
 * simulations. It
 * allows the generation of a new simulation ID and displays it on the UI.
 */
public class ModifySimulationPane extends JFrame implements BaseComponent {

    private JTextField simulationNameField;
    private JTextField numberOfSprintsField;
    private JTextField sprintLengthCycleField;
    private JTextField randomSeedField;
    private final JFrame parent;
    private Simulation simulation;

    public ModifySimulationPane(JFrame parent, Simulation simulation) {
        this.parent = parent;
        if (simulation == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No current simulation available to modify.",
                    "Simulation Not Found",
                    JOptionPane.WARNING_MESSAGE);
            dispose();
            return;
        }

        this.simulation = simulation;
        init();
    }

    @Override
    public void init() {
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setTitle("Modify Simulation");

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextArea simulationIdDisplay = new JTextArea(2, 20);
        simulationIdDisplay.setEditable(false);

        randomSeedField = new JTextField(20);

        simulationNameField = new JTextField(20);
        numberOfSprintsField = new JTextField(20);
        sprintLengthCycleField = new JTextField(20);

        JLabel nameLabel = new JLabel("Simulation Name:");
        JLabel sprintsLabel = new JLabel("Number of Sprints:");
        JLabel sprintLengthLabel = new JLabel("Length of Sprint:");
        JLabel randomSeedLabel = new JLabel("Random Seed:");

        simulationNameField.setText(simulation.getSimulationName());
        numberOfSprintsField.setText(String.valueOf(simulation.getSprintCount()));
        sprintLengthCycleField.setText(String.valueOf(simulation.getSprintDuration()));
        randomSeedField.setText(String.valueOf(simulation.getRandomSeed()));

        int gridy = 0;

        panel.add(nameLabel, new CustomConstraints(0, gridy, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));
        panel.add(simulationNameField, new CustomConstraints(1, gridy, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));
        gridy++;

        panel.add(sprintsLabel, new CustomConstraints(0, gridy, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));
        panel.add(numberOfSprintsField, new CustomConstraints(1, gridy, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));
        gridy++;

        panel.add(sprintLengthLabel, new CustomConstraints(0, gridy, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));
        panel.add(sprintLengthCycleField, new CustomConstraints(1, gridy, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));
        gridy++;

        panel.add(randomSeedLabel, new CustomConstraints(0, gridy, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));
        panel.add(randomSeedField, new CustomConstraints(1, gridy, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));
        gridy++;

        JButton submitButton = new JButton("Update Simulation");
        submitButton.addActionListener(e -> {
            String simName = simulationNameField.getText();
            int sprintLengthCycle;
            try {
                sprintLengthCycle = sprintLengthCycleField.getText().isEmpty() ? 0
                        : Integer.parseInt(sprintLengthCycleField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Length of a sprint must be an integer.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int numberOfSprints;
            try {
                numberOfSprints = numberOfSprintsField.getText().isEmpty() ? 0
                        : Integer.parseInt(numberOfSprintsField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Number of sprints must be an integer.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            long seed;
            try {
                seed = Long.parseLong(randomSeedField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Random seed must be an integer.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            simulation.setSimulationName(simName);
            simulation.setSprintDuration(sprintLengthCycle);
            simulation.setSprintCount(numberOfSprints);

            if (simulation.getRandomSeed() != seed) {
                simulation.setRandomSeed(seed);
            }

            JOptionPane.showMessageDialog(this, "Simulation updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        });


        panel.add(submitButton, new CustomConstraints(0, gridy, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));
        panel.add(simulationIdDisplay, new CustomConstraints(1, gridy, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        add(panel);
    }
}