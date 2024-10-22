package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import java.awt.Dialog;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.BlockerObject;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationListener;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.dialogs.simulation.SimulationProgressPane;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;

public class SimulationPane extends JDialog implements SimulationListener, BaseComponent {

    private SimulationProgressPane progressPane;
    private JFrame parent;
    private SimulationStateManager simulationStateManager;

    public SimulationPane(JFrame parent) {
        this.parent = parent;
        this.progressPane = new SimulationProgressPane();
        this.simulationStateManager = SimulationStateManager.getInstance();
        this.init();

        simulationStateManager.addListener(this);
    }

    @Override
    public void init() {
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        setTitle("Simulation Pane");
        add(progressPane.getSimPan());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent evt) {
                simulationStateManager.startSimulation();
            }

            @Override
            public void windowClosing(WindowEvent evt) {
                simulationStateManager.stopSimulation();
                dispose();
            }
        });
    }

    @Override
    public void onProgressUpdate(int progressValue, int day, int sprint, int sprintDuration) {
        progressPane.updateProgress(progressValue, day, sprint, sprintDuration);
    }

    @Override
    public void onBlockerDetected(BlockerObject blocker) {
        progressPane.addBlocker(blocker);
    }

    @Override
    public void onBlockerResolved(BlockerObject blocker) {
        progressPane.removeBlocker(blocker);
    }

    @Override
    public void onSimulationStopped() {
        JOptionPane.showMessageDialog(this, "Simulation stopped!");
    }

    @Override
    public void onSimulationStarted() {
        JOptionPane.showMessageDialog(this, "Simulation started!");
    }

    @Override
    public void dispose() {
        simulationStateManager.removeListener(this);
        super.dispose();
    }
}
