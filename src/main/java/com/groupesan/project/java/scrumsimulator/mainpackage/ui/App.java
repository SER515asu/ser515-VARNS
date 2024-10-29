package com.groupesan.project.java.scrumsimulator.mainpackage.ui;

import com.formdev.flatlaf.FlatLightLaf;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationSingleton;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels.DemoPane;
import javax.swing.*;

public class App {
    public App() {
    }

    public void start() {
        this.loadTheme();
        SwingUtilities.invokeLater(
                () -> {
                    // Load simulations from data store
                    SimulationSingleton.getInstance();

                    // Load DemoPane
                    DemoPane form = new DemoPane();
                    form.setVisible(true);
                });
    }

    private void loadTheme() {
        try {
            // TODO support setting theme from a configuration file
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
    }
}
