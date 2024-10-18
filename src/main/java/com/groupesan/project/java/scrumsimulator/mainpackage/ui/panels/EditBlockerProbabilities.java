package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.BlockerType;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.BlockerTypeStore;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;
import com.groupesan.project.java.scrumsimulator.mainpackage.utils.CustomConstraints;

import javax.swing.*;
import java.awt.*;

public class EditBlockerProbabilities extends JFrame implements BaseComponent {

    private final String blockerName;
    private final int encounterChance;
    private final int resolveChance;

    public EditBlockerProbabilities(String blockerName, int encounterChance, int resolveChance) {
        this.blockerName = blockerName;
        this.encounterChance = encounterChance;
        this.resolveChance = resolveChance;
        this.init();
    }

    @Override
    public void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Edit Blocker Probabilities");
        setSize(400, 400);

        JPanel myPanel = new JPanel();
        myPanel.setLayout(new GridBagLayout());

        JLabel nameLabel = new JLabel("Blocker Name:");
        JLabel encounterLabel = new JLabel("Encounter Chance (%):");
        JLabel resolveLabel = new JLabel("Resolve Chance (%):");

        JTextField nameField = new JTextField(blockerName, 20);
        nameField.setName("nameField");
        JTextField encounterChanceField = new JTextField(String.valueOf(encounterChance), 5);
        encounterChanceField.setName("encounterChanceField");
        JTextField resolveChanceField = new JTextField(String.valueOf(resolveChance), 5);
        resolveChanceField.setName("resolveChanceField");

        JButton saveButton = getSaveButton(encounterChanceField, resolveChanceField, nameField);
        saveButton.setName("saveButton");

        myPanel.add(nameLabel,
                new CustomConstraints(0, 0, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        myPanel.add(encounterLabel,
                new CustomConstraints(0, 1, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        myPanel.add(resolveLabel,
                new CustomConstraints(0, 2, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        myPanel.add(nameField,
                new CustomConstraints(1, 0, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        myPanel.add(encounterChanceField,
                new CustomConstraints(1, 1, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        myPanel.add(resolveChanceField,
                new CustomConstraints(1, 2, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        myPanel.add(saveButton,
                new CustomConstraints(1, 3, GridBagConstraints.WEST, 1.0, 1.0, GridBagConstraints.HORIZONTAL));

        add(myPanel);
    }

    private JButton getSaveButton(JTextField encounterChanceField, JTextField resolveChanceField, JTextField nameField) {
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            EncounterResolveProbabilities result = getEncounterResolveProbabilities(encounterChanceField, resolveChanceField);
            if (result == null) return;

            BlockerType blocker = BlockerTypeStore.get().getBlockerType(blockerName);
            blocker.setName(nameField.getText());
            blocker.setEncounterChance(result.newEncounterChance());
            blocker.setResolveChance(result.newResolveChance());
            dispose();
        });
        return saveButton;
    }

    public static EncounterResolveProbabilities getEncounterResolveProbabilities(JTextField encounterChanceField, JTextField resolveChanceField) {
        int newEncounterChance;
        try {
            newEncounterChance = encounterChanceField.getText().isEmpty() ? 0 : Integer.parseInt(encounterChanceField.getText());
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
            newResolveChance = resolveChanceField.getText().isEmpty() ? 0 : Integer.parseInt(resolveChanceField.getText());
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
