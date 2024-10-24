package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.Player;
import com.groupesan.project.java.scrumsimulator.mainpackage.core.ScrumRole;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;

import javax.swing.*;
import java.awt.*;
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
        setSize(1000, 700);
        setLayout(new BorderLayout(10, 10));

        GridBagLayout myGridbagLayout = new GridBagLayout();
        myJpanel = new JPanel();
        myJpanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        myJpanel.setLayout(myGridbagLayout);

        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        setupButtons();

        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        JPanel rightPanel = createRightPanel();
        add(rightPanel, BorderLayout.EAST);

        setupGlassPane();
        StylePane.applyStyle(this);
    }

    private void setupButtons() {
        sprintsButton = new JButton("Sprints");
        sprintsButton.addActionListener(
                e -> handleButtonAction(new SprintListPane(this)));

        userStoriesButton = new JButton("Product Backlog (User Stories)");
        userStoriesButton.addActionListener(
                e -> handleButtonAction(new UserStoryListPane(this)));

        startSimulationButton = new JButton("Start Simulation");
        startSimulationButton.addActionListener(
                e -> handleButtonAction(new SimulationPane(this)));

        potentialBlockersButton = new JButton("Potential Blockers");
        potentialBlockersButton.addActionListener(
                e -> handleButtonAction(new PotentialBlockersPane(this)));

        updateStoryStatusButton = new JButton("Update User Story Status");
        updateStoryStatusButton.addActionListener(
                e -> handleButtonAction(new UpdateUserStoryPanel(this)));

        simulationButton = new JButton("Add User");
        simulationButton.addActionListener(
                e -> handleButtonAction(new AddUserPane(this)));

        modifySimulationButton = new JButton("Modify Simulation");
        modifySimulationButton.addActionListener(
                e -> handleButtonAction(new ModifySimulationPane(this)));

        joinSimulationButton = new JButton("Join Simulation");
        joinSimulationButton.addActionListener(
                e -> handleButtonAction(new SimulationUI(this)));

        simulationSwitchRoleButton = new JButton("Switch Role");
        simulationSwitchRoleButton.addActionListener(
                e -> handleButtonAction(new SimulationSwitchRolePane(this)));

        variantSimulationUIButton = new JButton("Variant Simulation UI");
        variantSimulationUIButton.addActionListener(
                e -> handleButtonAction(new VariantSimulationUI(this)));

        sprintBacklogsButton = new JButton("Assign Sprint Backlogs");
        sprintBacklogsButton.addActionListener(
                e -> handleButtonAction(new SprintBacklogPane(this)));

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
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel roleLabel = new JLabel("Current Role:");
        JComboBox<String> roleComboBox = new JComboBox<>(new String[] { "Scrum Master", "Developer", "Product Owner" });
        roleComboBox.setPreferredSize(new Dimension(150, 25));
        roleComboBox.addActionListener(e -> {
            String selectedRole = (String) roleComboBox.getSelectedItem();
            System.out.println("Selected role: " + selectedRole);
        });

        panel.add(roleLabel);
        panel.add(roleComboBox);
        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Main Actions"));

        panel.add(createButton("Product Backlog(User Stories)", () -> handleButtonAction(new UserStoryListPane(this))));
        panel.add(createButton("Sprints", () -> handleButtonAction(new SprintListPane(this))));
        panel.add(createButton("Assign Sprint Backlogs", () -> handleButtonAction(new SprintBacklogPane(this))));
        panel.add(createButton("Update User Story Status", () -> handleButtonAction(new UpdateUserStoryPanel(this))));
        panel.add(createButton("Potential Blockers", () -> handleButtonAction(new PotentialBlockersPane(this))));

        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Simulation Controls"));

        panel.add(createButton("Add User", () -> handleButtonAction(new AddUserPane(this))));
        panel.add(createButton("Modify Simulation",
                () -> handleButtonAction(new ModifySimulationPane(this))));
        panel.add(createButton("Join Simulation", () -> handleButtonAction(new SimulationUI(this))));
        panel.add(createButton("Switch Role", () -> handleButtonAction(new SimulationSwitchRolePane(this))));
        panel.add(createButton("Variant Simulation UI", () -> handleButtonAction(new VariantSimulationUI(this))));
        panel.add(createButton("Start Simulation", () -> handleButtonAction(new SimulationPane(this))));

        return panel;
    }

    private JButton createButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.addActionListener(e -> action.run());
        return button;
    }

    private void handleButtonAction(JDialog pane) {
        setMenuButtonsEnabled(false);
        setGlassPaneVisible(true);
        pane.setVisible(true);
        setMenuButtonsEnabled(true);
        setGlassPaneVisible(false);
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
