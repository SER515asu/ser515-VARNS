package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.Player;
import com.groupesan.project.java.scrumsimulator.mainpackage.core.Simulation;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationSingleton;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.utils.DataModel;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.ResuableHeader;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.SpinnerInput;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.TextInput;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.LongInput;
import com.groupesan.project.java.scrumsimulator.mainpackage.utils.RandomUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class NewSimulationPane extends JFrame implements BaseComponent {
    private final DataModel<String> simulationModel;
    private final DataModel<Object> sprintModel;
    private final DataModel<Object> sprintLengthModel;
    private final DataModel<Long> seedModel;
    private final DataModel<List<Player>> users;
    private SpinnerInput sprintInput;
    private SpinnerInput sprintLengthInput;
    private LongInput seedInput;
    private JToggleButton autoFillToggleButton;
    private JSlider sprintLengthStartSlider;
    private JSlider sprintLengthEndSlider;
    private JSlider sprintNumberStartSlider;
    private JSlider sprintNumberEndSlider;
    private JButton submitAutoFillButton;
    private JButton cancelButton;
    private JButton submitButton;
    private JPanel autoFillPanel;
    private JFrame parent;

    public NewSimulationPane(JFrame parent) {
        this.parent = parent;
        this.simulationModel = new DataModel<>("New Simulation");
        this.sprintModel = new DataModel<>(1);
        this.sprintLengthModel = new DataModel<>(14);
        this.seedModel = new DataModel<>(RandomUtils.getInstance().getRandomLong());
        this.users = new DataModel<>(new ArrayList<>());
        init();
    }

    @Override
    public void init() {
        setTitle("New Simulation");
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel container = new JPanel(new BorderLayout());

        ResuableHeader resuableHeader = new ResuableHeader("General", "General simulation settings");

        JPanel inputs = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        TextInput simulationInput = new TextInput(
                "Name: ", new JTextField(simulationModel.getData()), simulationModel);

        sprintInput = new SpinnerInput(
                "Sprints: ",
                new JSpinner(new SpinnerNumberModel(1, 1, 20, 1)),
                sprintModel);

        sprintLengthInput = new SpinnerInput(
                "Sprint Length (days): ",
                new JSpinner(new SpinnerNumberModel(14, 1, 30, 1)),
                sprintLengthModel);

        seedInput = new LongInput(
                "Seed: ", new JTextField(seedModel.getData().toString()), seedModel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputs.add(resuableHeader, gbc);

        gbc.gridy++;
        inputs.add(simulationInput, gbc);

        gbc.gridy++;
        inputs.add(sprintInput, gbc);

        gbc.gridy++;
        inputs.add(sprintLengthInput, gbc);

        gbc.gridy++;
        inputs.add(seedInput, gbc);

        autoFillToggleButton = new JToggleButton("Auto Fill OFF");
        autoFillToggleButton.addActionListener(e -> toggleAutoFillPanel());

        gbc.gridy++;
        inputs.add(autoFillToggleButton, gbc);

        createAutoFillPanel();
        gbc.gridy++;
        inputs.add(autoFillPanel, gbc);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            Simulation simulation = new Simulation(UUID.randomUUID(), simulationModel.getData(),
                    (int) sprintModel.getData(), (int) sprintLengthModel.getData(), seedModel.getData());
            for (Player player : users.getData()) {
                player.doRegister();
                simulation.addPlayer(player);
            }

            SimulationStateManager.getInstance().setCurrentSimulation(simulation);
            SimulationSingleton.getInstance().addSimulation(simulation);
            dispose();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(cancelButton);
        buttonPanel.add(submitButton);

        gbc.gridy++;
        inputs.add(buttonPanel, gbc);

        container.add(inputs, BorderLayout.NORTH);
        getContentPane().add(container);
    }

    private void createAutoFillPanel() {
        autoFillPanel = new JPanel(new GridBagLayout());
        autoFillPanel.setBorder(BorderFactory.createTitledBorder("Auto Fill Settings"));
        autoFillPanel.setVisible(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        JLabel sprintLengthRangeLabel = new JLabel("Sprint Length Range (Days):");
        sprintLengthStartSlider = new JSlider(JSlider.HORIZONTAL, 1, 99, 1);
        sprintLengthEndSlider = new JSlider(JSlider.HORIZONTAL, 2, 100, 100);
        setupSlider(sprintLengthStartSlider, "Start", 20);
        setupSlider(sprintLengthEndSlider, "End", 20);

        gbc.gridy = 0;
        autoFillPanel.add(sprintLengthRangeLabel, gbc);
        gbc.gridy++;
        autoFillPanel.add(sprintLengthStartSlider, gbc);
        gbc.gridy++;
        autoFillPanel.add(sprintLengthEndSlider, gbc);

        JLabel sprintNumberRangeLabel = new JLabel("Sprint Number Range:");
        sprintNumberStartSlider = new JSlider(JSlider.HORIZONTAL, 1, 99, 1);
        sprintNumberEndSlider = new JSlider(JSlider.HORIZONTAL, 2, 100, 100);
        setupSlider(sprintNumberStartSlider, "Start", 20);
        setupSlider(sprintNumberEndSlider, "End", 20);

        gbc.gridy++;
        autoFillPanel.add(sprintNumberRangeLabel, gbc);
        gbc.gridy++;
        autoFillPanel.add(sprintNumberStartSlider, gbc);
        gbc.gridy++;
        autoFillPanel.add(sprintNumberEndSlider, gbc);

        submitAutoFillButton = new JButton("Submit Auto Fill");
        submitAutoFillButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitAutoFillButton.addActionListener(e -> applyAutoFillValues());

        gbc.gridy++;
        autoFillPanel.add(submitAutoFillButton, gbc);
    }

    private void setupSlider(JSlider slider, String labelText, int tickSpacing) {
        slider.setMajorTickSpacing(tickSpacing);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setBorder(BorderFactory.createTitledBorder(labelText));
    }

    private void toggleAutoFillPanel() {
        if (autoFillToggleButton.isSelected()) {
            autoFillToggleButton.setText("Auto Fill ON");
            autoFillPanel.setVisible(true);
        } else {
            autoFillToggleButton.setText("Auto Fill OFF");
            autoFillPanel.setVisible(false);
        }
        pack();
    }

    private void applyAutoFillValues() {
        int randomSprintLength = RandomUtils.getInstance().getRandomInt(sprintLengthStartSlider.getValue(),
                sprintLengthEndSlider.getValue());
        int randomSprintNumber = RandomUtils.getInstance().getRandomInt(sprintNumberStartSlider.getValue(),
                sprintNumberEndSlider.getValue());

        sprintLengthModel.setData(randomSprintLength);
        sprintModel.setData(randomSprintNumber);
        updateUI();

        JOptionPane.showMessageDialog(this,
                "Randomly selected values:\n" +
                        "Sprint Length: " + randomSprintLength + " Days\n" +
                        "Sprint Number: " + randomSprintNumber);
    }

    public void updateUI() {
        if (sprintInput != null) {
            sprintInput.updateFromModel();
        }
        if (sprintLengthInput != null) {
            sprintLengthInput.updateFromModel();
        }
    }
}
