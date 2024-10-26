package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;
import com.groupesan.project.java.scrumsimulator.mainpackage.utils.CustomConstraints;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.sql.Date;
import java.util.UUID;
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

    private SimulationManager simulationManager;
    private JTextField simulationNameField;
    private JTextField numberOfSprintsField;
    private JTextField sprintLengthCycleField;
    private JTextArea simulationIdDisplay;
    private JTextField randomSeedField;

    private JFrame parent;

    public ModifySimulationPane(JFrame parent) {
        this.parent = parent;

        this.simulationManager = new SimulationManager();
        this.init();
    }

    /** Initializes the UI components of the ModifySimulationPane. */
    @Override
    public void init() {
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setTitle("Create Simulation");

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        simulationIdDisplay = new JTextArea(2, 20);
        simulationIdDisplay.setEditable(false);

        simulationNameField = new JTextField(20);
        numberOfSprintsField = new JTextField(20);
        sprintLengthCycleField = new JTextField(20);

        JLabel nameLabel = new JLabel("Simulation Name:");
        JLabel sprintsLabel = new JLabel("Number of Sprints:");
        JLabel sprintLengthLabel = new JLabel("Length of Sprint:");
        JLabel randomSeedLabel = new JLabel("Random Seed:");

        panel.add(
                nameLabel,
                new CustomConstraints(
                        0, 0, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));
        panel.add(
                simulationNameField,
                new CustomConstraints(
                        1, 0, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        panel.add(
                sprintsLabel,
                new CustomConstraints(
                        0, 1, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));
        panel.add(
                numberOfSprintsField,
                new CustomConstraints(
                        1, 1, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));
        panel.add(
                sprintLengthLabel,
                new CustomConstraints(
                        0, 2, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));
        panel.add(
                sprintLengthCycleField,
                new CustomConstraints(
                        1, 2, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));
        
        panel.add(
                randomSeedLabel,
                new CustomConstraints(
                        0, 3, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        JButton submitButton = new JButton("Create Simulation");
        submitButton.addActionListener(e -> {
            String simId = UUID.randomUUID().toString();
            String simName = simulationNameField.getText();
            Integer sprintLengthCycle = 0;
            try {
                sprintLengthCycle = sprintLengthCycleField.getText().isEmpty() ? 0
                        : Integer.parseInt(sprintLengthCycleField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        ModifySimulationPane.this,
                        "Length of a sprint must be an integer.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            Integer numberOfSprints = 0;
            try {
                numberOfSprints = numberOfSprintsField.getText().isEmpty() ? 0
                        : Integer.parseInt(numberOfSprintsField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        ModifySimulationPane.this,
                        "Number of sprints must be an integer.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            long seed = 0;
            try {
                seed = Long.parseLong(randomSeedField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        ModifySimulationPane.this,
                        "Random seed must be an integer.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            simulationManager.createSimulation(simId, simName, numberOfSprints, sprintLengthCycle, seed);

            // Prepare a JTextField to display the Simulation ID
            JTextField simIdField = new JTextField(simId);
            simIdField.setEditable(false);
            Object[] message = {
                    "A new simulation has been generated.\nSimulation ID:", simIdField
            };

            // Show a dialog with the JTextField containing the Simulation ID
            JOptionPane.showMessageDialog(
                    ModifySimulationPane.this,
                    message,
                    "Simulation Created",
                    JOptionPane.INFORMATION_MESSAGE);

            // Reset fields and simulation ID display to blank
            simulationNameField.setText("");
            numberOfSprintsField.setText("");
            sprintLengthCycleField.setText("");
            simulationIdDisplay.setText("");
        });

        panel.add(
                submitButton,
                new CustomConstraints(
                        0, 3, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));
        panel.add(
                simulationIdDisplay,
                new CustomConstraints(
                        1, 3, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        add(panel);
    }
}
