package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.Player;
import com.groupesan.project.java.scrumsimulator.mainpackage.core.ScrumRole;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DemoPane extends JFrame implements BaseComponent {
    private final Player player = new Player("bob", new ScrumRole("demo"));
    private JPanel myJpanel;
    private JButton sprintsButton, userStoriesButton, startSimulationButton, potentialBlockersButton,
            updateStoryStatusButton, simulationButton, modifySimulationButton, joinSimulationButton,
            simulationSwitchRoleButton, variantSimulationUIButton, sprintBacklogsButton;

    public DemoPane() {
        this.init();
        player.doRegister();
    }

    public void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Demo");
        setSize(1400, 500);

        GridBagLayout myGridbagLayout = new GridBagLayout();
        myJpanel = new JPanel();
        myJpanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        myJpanel.setLayout(myGridbagLayout);

        sprintsButton = new JButton("Sprints");
        sprintsButton.addActionListener(
                e -> {
                    setMenuButtonsEnabled(false);
                    setGlassPaneVisible(true);

                    SprintListPane form = new SprintListPane(this);
                    form.setVisible(true);

                    setMenuButtonsEnabled(true);
                    setGlassPaneVisible(false);
                });

        userStoriesButton = new JButton("Product Backlog (User Stories)");
        userStoriesButton.addActionListener(
                e -> {
                    setMenuButtonsEnabled(false);
                    setGlassPaneVisible(true);

                    UserStoryListPane form = new UserStoryListPane(this);
                    form.setVisible(true);

                    setMenuButtonsEnabled(true);
                    setGlassPaneVisible(false);
                });

        startSimulationButton = new JButton("Start Simulation");
        startSimulationButton.addActionListener(
                e -> {
                    setMenuButtonsEnabled(false);
                    setGlassPaneVisible(true);

                    SimulationStateManager.getInstance().startSimulation();

                    setMenuButtonsEnabled(true);
                    setGlassPaneVisible(false);
                });

        potentialBlockersButton = new JButton("Potential Blockers");
        potentialBlockersButton.addActionListener(
                e -> {
                    setMenuButtonsEnabled(false);
                    setGlassPaneVisible(true);

                    PotentialBlockersPane form = new PotentialBlockersPane(this);
                    form.setVisible(true);

                    setMenuButtonsEnabled(true);
                    setGlassPaneVisible(false);
                });

        updateStoryStatusButton = new JButton("Update User Story Status");
        updateStoryStatusButton.addActionListener(
                e -> {
                    setMenuButtonsEnabled(false);
                    setGlassPaneVisible(true);

                    UpdateUserStoryPanel form = new UpdateUserStoryPanel(this);
                    form.setVisible(true);

                    setMenuButtonsEnabled(true);
                    setGlassPaneVisible(false);
                });

        simulationButton = new JButton("Add User");
        simulationButton.addActionListener(
                e -> {
                    setMenuButtonsEnabled(false);
                    setGlassPaneVisible(true);

                    SimulationPane simulationPane = new SimulationPane(this);
                    simulationPane.setVisible(true);

                    setMenuButtonsEnabled(true);
                    setGlassPaneVisible(false);
                });

        modifySimulationButton = new JButton("Modify Simulation");
        modifySimulationButton.addActionListener(
                e -> {
                    setMenuButtonsEnabled(false);
                    setGlassPaneVisible(true);

                    ModifySimulationPane modifySimulationPane = new ModifySimulationPane(this);
                    modifySimulationPane.setVisible(true);

                    setMenuButtonsEnabled(true);
                    setGlassPaneVisible(false);
                });

        joinSimulationButton = new JButton("Join Simulation");
        joinSimulationButton.addActionListener(
                e -> {
                    setMenuButtonsEnabled(false);
                    setGlassPaneVisible(true);

                    SimulationUI simulationUserInterface = new SimulationUI(this);
                    simulationUserInterface.setVisible(true);

                    setMenuButtonsEnabled(true);
                    setGlassPaneVisible(false);
                });

        simulationSwitchRoleButton = new JButton("Switch Role");
        simulationSwitchRoleButton.addActionListener(
                e -> {
                    setMenuButtonsEnabled(false);
                    setGlassPaneVisible(true);

                    SimulationSwitchRolePane feedbackPanelUI = new SimulationSwitchRolePane(this);
                    feedbackPanelUI.setVisible(true);

                    setMenuButtonsEnabled(true);
                    setGlassPaneVisible(false);
                });

        variantSimulationUIButton = new JButton("Variant Simulation UI");
        variantSimulationUIButton.addActionListener(
                e -> {
                    setMenuButtonsEnabled(false);
                    setGlassPaneVisible(true);

                    VariantSimulationUI variantSimulationUI = new VariantSimulationUI(this);
                    variantSimulationUI.setVisible(true);

                    setMenuButtonsEnabled(true);
                    setGlassPaneVisible(false);
                });

        sprintBacklogsButton = new JButton("Assign Sprint Backlogs");
        sprintBacklogsButton.addActionListener(
                e -> {
                    if (SimulationStateManager.getInstance().getCurrentSimulation() == null) {
                        JOptionPane.showMessageDialog(null,
                                "Please create and join a simulation before adding user stories to sprint backlog");
                        return;
                    }

                    setMenuButtonsEnabled(false);
                    setGlassPaneVisible(true);

                    SprintBacklogPane sprintBacklogPane = new SprintBacklogPane(this);
                    sprintBacklogPane.setVisible(true);

                    setMenuButtonsEnabled(true);
                    setGlassPaneVisible(false);
                });

        new DemoPaneBuilder(myJpanel)
                .addComponent(sprintsButton, 0, 0)
                .addComponent(userStoriesButton, 1, 0)
                .addComponent(startSimulationButton, 2, 0)
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

        setupGlassPane();
    }

    private void setupGlassPane() {
        JPanel glassPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0, 100));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        glassPane.setOpaque(false);
        setGlassPane(glassPane);
    }

    private void setGlassPaneVisible(boolean visible) {
        getGlassPane().setVisible(visible);
    }

    private void setMenuButtonsEnabled(boolean enabled) {
        sprintsButton.setEnabled(enabled);
        userStoriesButton.setEnabled(enabled);
        startSimulationButton.setEnabled(enabled);
        potentialBlockersButton.setEnabled(enabled);
        updateStoryStatusButton.setEnabled(enabled);
        simulationButton.setEnabled(enabled);
        modifySimulationButton.setEnabled(enabled);
        joinSimulationButton.setEnabled(enabled);
        simulationSwitchRoleButton.setEnabled(enabled);
        variantSimulationUIButton.setEnabled(enabled);
        sprintBacklogsButton.setEnabled(enabled);
    }
}