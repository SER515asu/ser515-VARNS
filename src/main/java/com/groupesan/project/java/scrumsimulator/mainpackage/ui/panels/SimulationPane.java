package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.groupesan.project.java.scrumsimulator.mainpackage.core.UserRole;
import com.groupesan.project.java.scrumsimulator.mainpackage.core.UserRoleSingleton;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.BlockerObject;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationListener;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager.SprintStateEnum;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.dialogs.simulation.SimulationProgressPane;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;

public class SimulationPane extends JFrame implements SimulationListener, BaseComponent {

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

        setTitle("Simulation Pane");

        setLayout(new BorderLayout());
        add(createRoleSelector(), BorderLayout.NORTH);
        add(progressPane.getSimPan(), BorderLayout.CENTER);
        
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

    private JComboBox<String> roleComboBox;

    private JPanel createRoleSelector() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel roleLabel = new JLabel("Current Role:");
        roleComboBox = new JComboBox<>(new String[] { "Scrum Administrator", "Scrum Master", "Developer", "Product Owner"});
        
        UserRole currentRole = UserRoleSingleton.getInstance().getUserRole();
        String currentRoleLabel = UserRoleSingleton.getLabelFromUserRole(currentRole);
        roleComboBox.setSelectedItem(currentRoleLabel);
        
        roleComboBox.addActionListener(e -> {
            String selectedRole = (String) roleComboBox.getSelectedItem();
            UserRole role = UserRoleSingleton.getUserRoleValueFromLabel(selectedRole);
            UserRoleSingleton.getInstance().setUserRole(role);
            ((DemoPane)parent).roleComboBox.setSelectedItem(selectedRole);
        });
        
        panel.add(roleLabel);
        panel.add(roleComboBox);
        
        return panel; 
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
        System.out.println("Simulation started");
    }

    @Override
    public void dispose() {
        simulationStateManager.removeListener(this);
        super.dispose();
    }
}
