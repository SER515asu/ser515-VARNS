package com.groupesan.project.java.scrumsimulator.mainpackage.ui.dialogs.simulation;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AutoFillToggleButton {
    private final JToggleButton toggleButton;
    private JSlider sprintLengthStartSlider;
    private JSlider sprintLengthEndSlider;
    private JSlider sprintNumberStartSlider;
    private JSlider sprintNumberEndSlider;
    private JLabel sprintLengthRangeLabel;
    private JLabel sprintNumberRangeLabel;
    private JButton submitButton;
    private JFrame rangeSelectionFrame;

    public AutoFillToggleButton() {
        JLabel autoFillLabel = new JLabel("Auto Fill:");
        toggleButton = new JToggleButton("OFF");
        toggleButton.addActionListener(e -> {
            if (toggleButton.isSelected()) {
                toggleButton.setText("ON");
                showRangeSelectionWindow();
            } else {
                toggleButton.setText("OFF");
                if (rangeSelectionFrame != null) {
                    rangeSelectionFrame.dispose();
                }
            }
        });

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(autoFillLabel);
        panel.add(toggleButton);
    }

    private void showRangeSelectionWindow() {
        rangeSelectionFrame = new JFrame("Select Ranges");
        rangeSelectionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        rangeSelectionFrame.setSize(500, 400);
        rangeSelectionFrame.setLocationRelativeTo(null);

        rangeSelectionFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                toggleButton.setSelected(false);
                toggleButton.setText("OFF");
            }
        });

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Sprint Length Range
        sprintLengthRangeLabel = new JLabel("Sprint Length Range (weeks):");
        mainPanel.add(sprintLengthRangeLabel);
        sprintLengthStartSlider = new JSlider(JSlider.HORIZONTAL, 1, 99, 1);
        sprintLengthEndSlider = new JSlider(JSlider.HORIZONTAL, 2, 100, 100);
        setupSlider(sprintLengthStartSlider, "Start", 20);
        setupSlider(sprintLengthEndSlider, "End", 20);
        mainPanel.add(sprintLengthStartSlider);
        mainPanel.add(sprintLengthEndSlider);

        // Sprint Number Range
        sprintNumberRangeLabel = new JLabel("Sprint Number Range:");
        mainPanel.add(sprintNumberRangeLabel);
        sprintNumberStartSlider = new JSlider(JSlider.HORIZONTAL, 1, 99, 1);
        sprintNumberEndSlider = new JSlider(JSlider.HORIZONTAL, 2, 100, 100);
        setupSlider(sprintNumberStartSlider, "Start", 20);
        setupSlider(sprintNumberEndSlider, "End", 20);
        mainPanel.add(sprintNumberStartSlider);
        mainPanel.add(sprintNumberEndSlider);

        ChangeListener sliderListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                adjustSliderValues();
                updateSprintLengthRangeLabel();
                updateSprintNumberRangeLabel();
                updateSubmitButtonState();
            }
        };

        sprintLengthStartSlider.addChangeListener(sliderListener);
        sprintLengthEndSlider.addChangeListener(sliderListener);
        sprintNumberStartSlider.addChangeListener(sliderListener);
        sprintNumberEndSlider.addChangeListener(sliderListener);

        submitButton = new JButton("Submit");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(rangeSelectionFrame,
                    "Ranges submitted:\n" +
                            "Sprint Length Range: " + sprintLengthStartSlider.getValue() + " - "
                            + sprintLengthEndSlider.getValue() + " weeks\n" +
                            "Sprint Number Range: " + sprintNumberStartSlider.getValue() + " - "
                            + sprintNumberEndSlider.getValue());
            toggleButton.setSelected(false);
            toggleButton.setText("OFF");
            rangeSelectionFrame.dispose();
        });
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(submitButton);

        rangeSelectionFrame.add(mainPanel);
        rangeSelectionFrame.setVisible(true);

        updateSubmitButtonState();
    }

    private void setupSlider(JSlider slider, String labelText, int tickSpacing) {
        slider.setMajorTickSpacing(tickSpacing);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setBorder(BorderFactory.createTitledBorder(labelText));
    }

    private void adjustSliderValues() {

        if (sprintLengthStartSlider.getValue() >= sprintLengthEndSlider.getValue()) {
            sprintLengthEndSlider
                    .setValue(Math.min(sprintLengthStartSlider.getValue() + 1, sprintLengthEndSlider.getMaximum()));
        }
        if (sprintNumberStartSlider.getValue() >= sprintNumberEndSlider.getValue()) {
            sprintNumberEndSlider
                    .setValue(Math.min(sprintNumberStartSlider.getValue() + 1, sprintNumberEndSlider.getMaximum()));
        }
    }

    private void updateSprintLengthRangeLabel() {
        sprintLengthRangeLabel.setText("Sprint Length Range: " + sprintLengthStartSlider.getValue() + " - "
                + sprintLengthEndSlider.getValue() + " weeks");
    }

    private void updateSprintNumberRangeLabel() {
        sprintNumberRangeLabel.setText("Sprint Number Range: " + sprintNumberStartSlider.getValue() + " - "
                + sprintNumberEndSlider.getValue());
    }

    private void updateSubmitButtonState() {
        submitButton.setEnabled(sprintLengthStartSlider.getValue() < sprintLengthEndSlider.getValue() &&
                sprintNumberStartSlider.getValue() < sprintNumberEndSlider.getValue());
    }

    public JPanel getPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Auto Fill:"));
        panel.add(toggleButton);
        return panel;
    }
}
/**
 * to add it to backnd logic for selecting a random range from the provided
 * range keep in mind that here startSlider and endSlider are the two sliders
 * that are used to select the range of sprint length and sprint number.
 **/