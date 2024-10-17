package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.Player;
import com.groupesan.project.java.scrumsimulator.mainpackage.core.ScrumRole;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;

import java.awt.GridBagLayout;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DemoPane extends JFrame implements BaseComponent {
    private final Player player = new Player("bob", new ScrumRole("demo"));

    public DemoPane() {
        this.init();
        player.doRegister();
    }

    /**
     * Initialization of Demo Pane. Demonstrates creation of User stories, Sprints,
     * and Simulation
     * start.
     */
    public void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Demo");
        setSize(1400, 500);

        GridBagLayout myGridbagLayout = new GridBagLayout();
        JPanel myJpanel = new JPanel();
        myJpanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        myJpanel.setLayout(myGridbagLayout);
        JLabel currentRolelaLabel = new JLabel("Current Role: ");
        JComboBox<String> roleComboBox = new JComboBox<>(new String[] { "Scrum Master", "Developer", "Product Owner" });
        roleComboBox.addActionListener(e -> {
            String selectedItem = (String) roleComboBox.getSelectedItem();
            System.out.println("Selected: " + selectedItem);
            // here add logic to change the role from backend
        });

        JButton sprintsButton = new JButton("Sprints");
        sprintsButton.addActionListener(
                e -> {
                    SprintListPane form = new SprintListPane();
                    form.setVisible(true);
                });

        JButton userStoriesButton = new JButton("Product Backlog (User Stories)");
        userStoriesButton.addActionListener(
                e -> {
                    UserStoryListPane form = new UserStoryListPane();
                    form.setVisible(true);
                });

        SimulationPanel simulationPanel = new SimulationPanel();

        JButton potentialBlockersButton = new JButton("Potential Blockers");
        potentialBlockersButton.addActionListener(
                e -> {
                    PotentialBlockersPane form = new PotentialBlockersPane();
                    form.setVisible(true);
                });

        JButton updateStoryStatusButton = new JButton("Update User Story Status");
        updateStoryStatusButton.addActionListener(
                e -> {
                    UpdateUserStoryPanel form = new UpdateUserStoryPanel();
                    form.setVisible(true);
                });

        JButton simulationButton = new JButton("Add User");
        simulationButton.addActionListener(
                e -> {
                    SimulationPane simulationPane = new SimulationPane();
                    simulationPane.setVisible(true);
                });

        JButton modifySimulationButton = new JButton("Modify Simulation");
        modifySimulationButton.addActionListener(
                e -> {
                    SimulationManager simulationManager = new SimulationManager();
                    ModifySimulationPane modifySimulationPane = new ModifySimulationPane(simulationManager);
                    modifySimulationPane.setVisible(true);
                });

        JButton joinSimulationButton = new JButton("Join Simulation");
        joinSimulationButton.addActionListener(
                e -> {
                    SimulationUI simulationUserInterface = new SimulationUI();
                    simulationUserInterface.setVisible(true);
                });

        JButton simulationSwitchRoleButton = new JButton("Switch Role");
        simulationSwitchRoleButton.addActionListener(
                e -> {
                    SimulationSwitchRolePane feedbackPanelUI = new SimulationSwitchRolePane();
                    feedbackPanelUI.setVisible(true);
                });

        // New button for Variant Simulation UI
        // TODO: Figure out what this is used for because it was initially hidden from
        // view
        JButton variantSimulationUIButton = new JButton("Variant Simulation UI");
        variantSimulationUIButton.addActionListener(
                e -> {
                    VariantSimulationUI variantSimulationUI = new VariantSimulationUI();
                    variantSimulationUI.setVisible(true);
                });

        JButton sprintBacklogsButton = getSprintBacklogsButton();

        new DemoPaneBuilder(myJpanel)
                .addComponent(currentRolelaLabel, 3, 1)
                .addComponent(roleComboBox, 4, 1)
                .addComponent(sprintsButton, 0, 0)
                .addComponent(userStoriesButton, 1, 0)
                .addComponent(simulationPanel, 2, 0)
                .addComponent(sprintBacklogsButton, 3, 0)
                .addComponent(updateStoryStatusButton, 4, 0)
                .addComponent(modifySimulationButton, 5, 0)
                .addComponent(joinSimulationButton, 6, 0)
                .addComponent(simulationButton, 7, 0)
                .addComponent(potentialBlockersButton, 9, 0)
                .addComponent(simulationSwitchRoleButton, 1, 1)
                .addComponent(variantSimulationUIButton, 2, 1)
                .buildPanel();

        add(myJpanel);
    }

    private static JButton getSprintBacklogsButton() {
        JButton sprintBacklogsButton = new JButton("Assign Sprint Backlogs");
        sprintBacklogsButton.addActionListener(
                e -> {
                    if (SimulationStateManager.getInstance().getCurrentSimulation() == null) {
                        JOptionPane.showMessageDialog(null,
                                "Please create and join a simulation before adding user stories to sprint backlog");
                        return;
                    }

                    // Load SprintUIPane
                    SprintBacklogPane sprintBacklogPane = new SprintBacklogPane();
                    sprintBacklogPane.setVisible(true);
                });
        return sprintBacklogsButton;
    }
}
