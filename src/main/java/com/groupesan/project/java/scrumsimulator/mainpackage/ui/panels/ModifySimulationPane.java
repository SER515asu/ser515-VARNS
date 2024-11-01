package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.Simulation;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationSingleton;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.ResuableHeader;
import com.groupesan.project.java.scrumsimulator.mainpackage.utils.RandomUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.UUID;

class ModifySimulationPane extends JFrame implements BaseComponent {
    private JTextField simulationNameField;
    private JSpinner numberOfSprintsField;
    private JSpinner sprintLengthCycleField;
    private JTextField randomSeedField;
    
    private final ModifyMode mode;
    private final JFrame parent;
    private Simulation simulation;

    private enum ModifyMode {
        CREATE_NEW, MODIFY_EXISTING
    }

    public ModifySimulationPane(JFrame parent) {
        this.mode = ModifyMode.CREATE_NEW;
        this.parent = parent;
        init();
    }

    public ModifySimulationPane(JFrame parent, Simulation simulation) {
        this.mode = ModifyMode.MODIFY_EXISTING;
        this.parent = parent;
        this.simulation = simulation;
        init();
    }

    @Override
    public void init() {
        setTitle(mode == ModifyMode.CREATE_NEW ? "New Simulation" : "Edit Simulation");
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel container = new JPanel(new BorderLayout());
        ResuableHeader reusableHeader = new ResuableHeader("General", "General simulation settings");

        JPanel inputs = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // Initialize fields with either default values (for NEW) or existing values (for EDIT)
        String name = mode == ModifyMode.CREATE_NEW ? "New Simulation" : simulation.getName();
        int numberOfSprints = mode == ModifyMode.CREATE_NEW ? 1 : simulation.getSprintCount();
        int sprintDuration = mode == ModifyMode.CREATE_NEW ? 14 : simulation.getSprintDuration();
        long seed = mode == ModifyMode.CREATE_NEW ? RandomUtils.getInstance().getRandomLong() : simulation.getRandomSeed();

        simulationNameField = new JTextField(name, 20);
        JLabel nameLabel = new JLabel("Simulation Name:");
        gbc.gridy = 0;
        inputs.add(nameLabel, gbc);
        gbc.gridx = 1;
        inputs.add(simulationNameField, gbc);

        numberOfSprintsField = new JSpinner(new SpinnerNumberModel(numberOfSprints, 1, 20, 1));
        JLabel sprintLabel = new JLabel("Number of Sprints:");
        gbc.gridx = 0;
        gbc.gridy++;
        inputs.add(sprintLabel, gbc);
        gbc.gridx = 1;
        inputs.add(numberOfSprintsField, gbc);

        sprintLengthCycleField = new JSpinner(new SpinnerNumberModel(sprintDuration, 1, 30, 1));
        JLabel sprintDurationLabel = new JLabel("Sprint Duration (days):");
        gbc.gridx = 0;
        gbc.gridy++;
        inputs.add(sprintDurationLabel, gbc);
        gbc.gridx = 1;
        inputs.add(sprintLengthCycleField, gbc);

        randomSeedField = new JTextField(String.valueOf(seed), 20);
        randomSeedField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '-' || (c == '-' && randomSeedField.getText().contains("-"))) {
                    e.consume();
                }
            }
        });

        JLabel seedLabel = new JLabel("Random Seed:");
        gbc.gridx = 0;
        gbc.gridy++;
        inputs.add(seedLabel, gbc);
        gbc.gridx = 1;
        inputs.add(randomSeedField, gbc);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        JButton submitButton = new JButton(mode == ModifyMode.CREATE_NEW ? "Create Simulation" : "Update Simulation");
        submitButton.addActionListener(e -> handleSubmission());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(cancelButton);
        buttonPanel.add(submitButton);

        container.add(reusableHeader, BorderLayout.NORTH);
        container.add(inputs, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);
        getContentPane().add(container);
    }

    private void handleSubmission() {
        String simName = simulationNameField.getText();
        int sprintLengthCycle = (int) sprintLengthCycleField.getValue();
        int numberOfSprints = (int) numberOfSprintsField.getValue();
        long seed;

        try {
            seed = Long.parseLong(randomSeedField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Random seed must be a valid number and less than " + Long.toString(Long.MAX_VALUE).length());
            return;
        }

        if (mode == ModifyMode.CREATE_NEW) {
            Simulation newSimulation = new Simulation(UUID.randomUUID(), simName, numberOfSprints, sprintLengthCycle, seed);
            SimulationSingleton.getInstance().addSimulation(newSimulation);
        } else {
            simulation.setName(simName);
            simulation.setSprintDuration(sprintLengthCycle);
            simulation.setSprintCount(numberOfSprints);
            simulation.setRandomSeed(seed);

            SimulationSingleton.getInstance().saveSimulationDetails();
        }

        dispose();
    }
}
