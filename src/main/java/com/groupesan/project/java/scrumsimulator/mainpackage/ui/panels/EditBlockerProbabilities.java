package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.BlockerType;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.BlockerTypeStore;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;
import com.groupesan.project.java.scrumsimulator.mainpackage.utils.CustomConstraints;
import com.groupesan.project.java.scrumsimulator.mainpackage.utils.RandomUtils;

import javax.swing.*;
import java.awt.*;

public class EditBlockerProbabilities extends JFrame implements BaseComponent {

    private final String blockerName;
    private final int encounterChance;
    private final int resolveChance;
    private boolean randomMode;

    private JLabel nameLabel;
    private JLabel encounterLabel;
    private JLabel resolveLabel;
    private JLabel lowerBoundEncounterValueLabel;
    private JLabel upperBoundEncounterValueLabel;
    private JLabel lowerBoundResolveValueLabel;
    private JLabel upperBoundResolveValueLabel;

    private JPanel myPanel;
    private JCheckBox randomModeCheckBox;
    private JTextField nameField;
    private JTextField encounterChanceField;
    private JTextField resolveChanceField;
    private JSlider lowerBoundRandomEncounterSlider;
    private JSlider upperBoundRandomEncounterSlider;
    private JSlider lowerBoundRandomResolveSlider;
    private JSlider upperBoundRandomResolveSlider;
    private JButton saveButton;

    public EditBlockerProbabilities(String blockerName, int encounterChance, int resolveChance) {
        this.blockerName = blockerName;
        this.encounterChance = encounterChance;
        this.resolveChance = resolveChance;
        this.randomMode = false;
        this.init();
    }

    @Override
    public void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Edit Blocker Probabilities");
        setSize(400, 400);

        myPanel = new JPanel();
        myPanel.setLayout(new GridBagLayout());

        nameLabel = new JLabel("Blocker Name:");
        encounterLabel = new JLabel("Encounter Chance (%):");
        resolveLabel = new JLabel("Resolve Chance (%):");

        randomModeCheckBox = new JCheckBox("Random Mode");
        randomModeCheckBox.setName("randomModeCheckBox");

        nameField = new JTextField(blockerName, 20);
        nameField.setName("nameField");
        encounterChanceField = new JTextField(String.valueOf(encounterChance), 5);
        encounterChanceField.setName("encounterChanceField");
        resolveChanceField = new JTextField(String.valueOf(resolveChance), 5);
        resolveChanceField.setName("resolveChanceField");

        lowerBoundRandomEncounterSlider = new JSlider(0, 100, 0);
        upperBoundRandomEncounterSlider = new JSlider(0, 100, 100);

        lowerBoundEncounterValueLabel = new JLabel(String.valueOf(lowerBoundRandomEncounterSlider.getValue()));
        upperBoundEncounterValueLabel = new JLabel(String.valueOf(upperBoundRandomEncounterSlider.getValue()));

        lowerBoundRandomResolveSlider = new JSlider(0, 100, 0);
        upperBoundRandomResolveSlider = new JSlider(0, 100, 100);

        lowerBoundResolveValueLabel = new JLabel(String.valueOf(lowerBoundRandomResolveSlider.getValue()));
        upperBoundResolveValueLabel = new JLabel(String.valueOf(upperBoundRandomResolveSlider.getValue()));

        lowerBoundRandomEncounterSlider.addChangeListener(e -> {
            if (lowerBoundRandomEncounterSlider.getValue() > upperBoundRandomEncounterSlider.getValue()) {
                lowerBoundRandomEncounterSlider.setValue(upperBoundRandomEncounterSlider.getValue());
            }
            lowerBoundEncounterValueLabel.setText(String.valueOf(lowerBoundRandomEncounterSlider.getValue()));
            encounterChanceField.setText(String.valueOf(lowerBoundRandomEncounterSlider.getValue()));
        });

        upperBoundRandomEncounterSlider.addChangeListener(e -> {
            if (upperBoundRandomEncounterSlider.getValue() < lowerBoundRandomEncounterSlider.getValue()) {
                upperBoundRandomEncounterSlider.setValue(lowerBoundRandomEncounterSlider.getValue());
            }
            upperBoundEncounterValueLabel.setText(String.valueOf(upperBoundRandomEncounterSlider.getValue()));
            encounterChanceField.setText(String.valueOf(upperBoundRandomEncounterSlider.getValue()));
        });

        lowerBoundRandomResolveSlider.addChangeListener(e -> {
            if (lowerBoundRandomResolveSlider.getValue() > upperBoundRandomResolveSlider.getValue()) {
                lowerBoundRandomResolveSlider.setValue(upperBoundRandomResolveSlider.getValue());
            }
            lowerBoundResolveValueLabel.setText(String.valueOf(lowerBoundRandomResolveSlider.getValue()));
            resolveChanceField.setText(String.valueOf(lowerBoundRandomResolveSlider.getValue()));
        });

        upperBoundRandomResolveSlider.addChangeListener(e -> {
            if (upperBoundRandomResolveSlider.getValue() < lowerBoundRandomResolveSlider.getValue()) {
                upperBoundRandomResolveSlider.setValue(lowerBoundRandomResolveSlider.getValue());
            }
            upperBoundResolveValueLabel.setText(String.valueOf(upperBoundRandomResolveSlider.getValue()));
            resolveChanceField.setText(String.valueOf(upperBoundRandomResolveSlider.getValue()));
        });

        this.saveButton = getSaveButton(encounterChanceField, resolveChanceField, nameField);
        saveButton.setName("saveButton");

        randomModeCheckBox.addItemListener(e -> {
            randomMode = randomModeCheckBox.isSelected();
            rebuildPanel();
        });

        rebuildPanel();
        add(myPanel);
    }

    private void rebuildPanel() {
        myPanel.removeAll();

        myPanel.add(nameLabel,
                new CustomConstraints(0, 0, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));
        myPanel.add(nameField,
                new CustomConstraints(1, 0, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        myPanel.add(encounterLabel,
                new CustomConstraints(0, 1, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        myPanel.add(resolveLabel,
                new CustomConstraints(0, 2, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        myPanel.add(randomModeCheckBox,
                new CustomConstraints(0, 3, GridBagConstraints.WEST, 0.5, 1.0, GridBagConstraints.HORIZONTAL));
        myPanel.add(saveButton,
                new CustomConstraints(1, 3, GridBagConstraints.WEST, 0.5, 1.0, GridBagConstraints.HORIZONTAL));

        if (randomMode) {
            JPanel encounterPanel = new JPanel();
            encounterPanel.setLayout(new GridBagLayout());

            encounterPanel.add(new JLabel("Lower Bound:"),
                    new CustomConstraints(0, 0, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));
            encounterPanel.add(lowerBoundRandomEncounterSlider,
                    new CustomConstraints(1, 0, GridBagConstraints.WEST, 2.0, 1.0, GridBagConstraints.HORIZONTAL));
            encounterPanel.add(lowerBoundEncounterValueLabel,
                    new CustomConstraints(2, 0, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

            encounterPanel.add(new JLabel("Upper Bound:"),
                    new CustomConstraints(0, 1, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));
            encounterPanel.add(upperBoundRandomEncounterSlider,
                    new CustomConstraints(1, 1, GridBagConstraints.WEST, 2.0, 1.0, GridBagConstraints.HORIZONTAL));
            encounterPanel.add(upperBoundEncounterValueLabel,
                    new CustomConstraints(2, 1, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

            myPanel.add(encounterPanel,
                    new CustomConstraints(1, 1, GridBagConstraints.WEST, 2.0, 1.0, GridBagConstraints.HORIZONTAL));

            JPanel resolvePanel = new JPanel();
            resolvePanel.setLayout(new GridBagLayout());

            resolvePanel.add(new JLabel("Lower Bound:"),
                    new CustomConstraints(0, 0, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));
            resolvePanel.add(lowerBoundRandomResolveSlider,
                    new CustomConstraints(1, 0, GridBagConstraints.WEST, 2.0, 1.0, GridBagConstraints.HORIZONTAL));
            resolvePanel.add(lowerBoundResolveValueLabel,
                    new CustomConstraints(2, 0, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

            resolvePanel.add(new JLabel("Upper Bound:"),
                    new CustomConstraints(0, 1, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));
            resolvePanel.add(upperBoundRandomResolveSlider,
                    new CustomConstraints(1, 1, GridBagConstraints.WEST, 2.0, 1.0, GridBagConstraints.HORIZONTAL));
            resolvePanel.add(upperBoundResolveValueLabel,
                    new CustomConstraints(2, 1, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

            myPanel.add(resolvePanel,
                    new CustomConstraints(1, 2, GridBagConstraints.WEST, 2.0, 1.0, GridBagConstraints.HORIZONTAL));
        } else {
            myPanel.add(encounterLabel,
                    new CustomConstraints(0, 1, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));
            myPanel.add(encounterChanceField,
                    new CustomConstraints(1, 1, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

            myPanel.add(resolveLabel,
                    new CustomConstraints(0, 2, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));
            myPanel.add(resolveChanceField,
                    new CustomConstraints(1, 2, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));
        }

        myPanel.revalidate();
        myPanel.repaint();
    }

    private JButton getSaveButton(JTextField encounterChanceField, JTextField resolveChanceField,
            JTextField nameField) {
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            int encounterChance;
            int resolveChance;

            if (randomMode) {
                encounterChance = RandomUtils.getRandomInt(lowerBoundRandomEncounterSlider.getValue(),
                        upperBoundRandomEncounterSlider.getValue());

                resolveChance = RandomUtils.getRandomInt(lowerBoundRandomResolveSlider.getValue(),
                        upperBoundRandomResolveSlider.getValue());

            } else {
                EncounterResolveProbabilities result = getEncounterResolveProbabilities(encounterChanceField,
                        resolveChanceField);

                if (result == null)
                    return;

                encounterChance = result.newEncounterChance();
                resolveChance = result.newResolveChance();
            }

            BlockerType blocker = BlockerTypeStore.get().getBlockerType(blockerName);
            if (blocker == null) {
                blocker = new BlockerType(nameField.getText(), encounterChance, resolveChance);
                BlockerTypeStore.get().addBlockerType(blocker);
            } else {
                blocker.setName(nameField.getText());
                blocker.setEncounterChance(encounterChance);
                blocker.setResolveChance(resolveChance);
            }
            dispose();
        });
        return saveButton;
    }

    public static EncounterResolveProbabilities getEncounterResolveProbabilities(JTextField encounterChanceField,
            JTextField resolveChanceField) {
        int newEncounterChance;
        try {
            newEncounterChance = encounterChanceField.getText().isEmpty() ? 0
                    : Integer.parseInt(encounterChanceField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Probability of encounter chance must be an integer.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        int newResolveChance;
        try {
            newResolveChance = resolveChanceField.getText().isEmpty() ? 0
                    : Integer.parseInt(resolveChanceField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Probability of resolve chance must be an integer.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        if (chanceNotInRange(newEncounterChance) || chanceNotInRange(newResolveChance)) {
            JOptionPane.showMessageDialog(
                    null,
                    "Probability must be an integer between 0 and 100.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return new EncounterResolveProbabilities(newEncounterChance, newResolveChance);
    }

    public record EncounterResolveProbabilities(int newEncounterChance, int newResolveChance) {
    }

    private static boolean chanceNotInRange(int chance) {
        return chance > 100 || chance < 0;
    }
}
