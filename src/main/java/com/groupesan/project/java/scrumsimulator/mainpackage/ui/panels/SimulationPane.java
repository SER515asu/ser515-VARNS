package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import java.awt.Dialog;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.BlockerObject;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.UserStory;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationListener;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager.SprintStateEnum;
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
                if (simulationStateManager.getState() == SprintStateEnum.RUNNING) {
                    simulationStateManager.stopSimulation();
                }
                dispose();
            }
        });
    }

    @Override
    public void onProgressUpdate(int progressValue, int day, int sprint, int sprintDuration) {
        progressPane.updateProgress(progressValue, day, sprint, sprintDuration);
    }

    @Override
    public void onInProgressUserStory(){
        progressPane.inProgressState();
    }

    @Override
    public void onUserStoryStatusChange(UserStory userStory) {
        progressPane.changeState(userStory);
    }


    @Override
    public void onBlockerDetected(BlockerObject blocker) {
        // This will need to be refactored later. - Suparno
        //progressPane.addBlocker(blocker);

    }

    @Override
    public void onBlockerResolved(BlockerObject blocker) {
        // This will need to be refactored later. - Suparno
        //progressPane.removeBlocker(blocker);
    }

    public void onChangeStoryStatus(UserStory userStory) {
        progressPane.changeState(userStory);
    }

    @Override
    public void onUserStory(UserStory userStory){
        progressPane.addUserStory(userStory);
    }

    @Override
    public void onSimulationStopped() {
        JOptionPane.showMessageDialog(this, "Simulation stopped!");
    }

    @Override
    public void onSimulationStarted() {
        System.out.println("Simulation started");
    }

    @Override
    public void onSprintCompletion() {
        progressPane.resetPanel();
    }

    @Override
    public void dispose() {
        simulationStateManager.removeListener(this);
        super.dispose();
    }
}
