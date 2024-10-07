package com.groupesan.project.java.scrumsimulator.mainpackage.ui.dialogs.simulation;

import javax.swing.*;

public class SimulationProgressPane {
    private JPanel simPan;
    private JLabel jimPan;
    private JLabel currentProgressValue;
    private JProgressBar jimProg;

    public SimulationProgressPane() {
        simPan = new JPanel();
        jimPan = new JLabel();
        currentProgressValue = new JLabel();
        jimProg = new JProgressBar();

        simPan.add(jimPan);
        simPan.add(currentProgressValue);
        simPan.add(jimProg);
    }

    public JPanel getSimPan() {
        return simPan;
    }

    public void updateProgress(int progressValue, int day, int sprint, int sprintDuration) {
        jimPan.setText("Running simulation for day " + day + " of " + sprintDuration + " of sprint " + sprint);
        currentProgressValue.setText("Progress: " + progressValue + "%");
        jimProg.setValue(progressValue);
    }

    public void resetProgress() {
        currentProgressValue.setText("Progress: 0%");
        jimProg.setValue(0);
    }
}
