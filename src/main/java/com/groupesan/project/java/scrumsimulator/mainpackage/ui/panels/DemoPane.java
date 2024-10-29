package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.*;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.border.EmptyBorder;

public class DemoPane extends JFrame implements BaseComponent {
    private final Player player = new Player("bob", new ScrumRole("demo"));
    private JPanel myJpanel;
    private JButton sprintsButton, userStoriesButton, startSimulationButton, potentialBlockersButton,
            updateStoryStatusButton, simulationButton, modifySimulationButton,
            variantSimulationUIButton, sprintBacklogsButton, newSimulationButton, potentialBlockerSolutionsButton;

    private Map<UserRole, Set<JButton>> roleToButton;
    private JPanel bottomPanel;

    public DemoPane() {
        this.init();
        player.doRegister();
        roleToButton = new HashMap<>(Map.of(
                UserRole.SCRUM_MASTER, new HashSet<>(Set.of(
                        newSimulationButton,
                        sprintBacklogsButton
                // spike activities button
                )),
                UserRole.DEVELOPER, new HashSet<>(Set.of(
                        userStoriesButton
                // spike activities button
                )),
                UserRole.PRODUCT_OWNER, new HashSet<>(Set.of(
                        userStoriesButton)),
                UserRole.SCRUM_ADMIN, new HashSet<>(Set.of(
                        potentialBlockersButton,
                        startSimulationButton))));
    }

    public void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Scrum Simulator");
        setSize(1000, 700);
        setLayout(new BorderLayout(10, 10));

        GridBagLayout myGridbagLayout = new GridBagLayout();
        myJpanel = new JPanel();
        myJpanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        myJpanel.setLayout(myGridbagLayout);

        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        bottomPanel = new JPanel(new BorderLayout(10, 10));
        redrawUIBasedOnRole();
    }

    public void redrawUIBasedOnRole() {
        UserRole role = UserRoleSingleton.getInstance().getUserRole();
        bottomPanel.removeAll();
        repaint();
        setupButtons();

        JPanel centerPanel = createCenterPanel(role);
        bottomPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel rightPanel = createRightPanel(role);
        bottomPanel.add(rightPanel, BorderLayout.EAST);

        setupGlassPane();
        add(bottomPanel);
        StylePane.applyStyle(this);
    }

    private void setupButtons() {
        newSimulationButton = new JButton("New Simulation");
        newSimulationButton.addActionListener(
                e -> handleButtonAction(new NewSimulationPane(this)));

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

        potentialBlockerSolutionsButton = new JButton("Potential Blocker Solutions");
        potentialBlockerSolutionsButton.addActionListener(
                e -> handleButtonAction(new PotentialBlockerSolutionsPane(this)));

        updateStoryStatusButton = new JButton("Update User Story Status");
        updateStoryStatusButton.addActionListener(
                e -> handleButtonAction(new UpdateUserStoryPanel(this)));

        simulationButton = new JButton("Add User");
        simulationButton.addActionListener(
                e -> handleButtonAction(new AddUserPane(this)));

        modifySimulationButton = new JButton("Modify Simulation");
        modifySimulationButton.addActionListener(
                e -> handleButtonAction(new ModifySimulationPane(this)));


        variantSimulationUIButton = new JButton("Variant Simulation UI");
        variantSimulationUIButton.addActionListener(
                e -> handleButtonAction(new VariantSimulationUI(this)));

        sprintBacklogsButton = new JButton("Assign Sprint Backlogs");
        sprintBacklogsButton.addActionListener(
                e -> handleButtonAction(new SprintBacklogPane(this)));

        new DemoPaneBuilder(myJpanel)
                .addComponent(newSimulationButton, 0, 0)
                .addComponent(sprintsButton, 1, 0)
                .addComponent(userStoriesButton, 2, 0)
                .addComponent(startSimulationButton, 3, 0)
                .addComponent(potentialBlockersButton, 4, 0)
                .addComponent(potentialBlockerSolutionsButton, 5, 0)
                .addComponent(updateStoryStatusButton, 6, 0)
                .addComponent(simulationButton, 7, 0)
                .addComponent(modifySimulationButton, 8, 0)
                .addComponent(variantSimulationUIButton, 10, 0)
                .addComponent(sprintBacklogsButton, 11, 0)
                .buildPanel();

        add(myJpanel);
    }

    private JComboBox<String> roleComboBox;

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel roleLabel = new JLabel("Current Role:");
        roleComboBox = new JComboBox<>(new String[] { "Scrum Administrator", "Scrum Master", "Developer", "Product Owner"});
        roleComboBox.setPreferredSize(new Dimension(150, 25));
        UserRoleSingleton.getInstance().setUserRole(UserRole.SCRUM_ADMIN);
        roleComboBox.addActionListener(e -> {
            String selectedRole = (String) roleComboBox.getSelectedItem();
            UserRole role = UserRoleSingleton.getUserRoleValueFromLabel(selectedRole);
            UserRoleSingleton.getInstance().setUserRole(role);
            System.out.println("Selected role: " + UserRoleSingleton.getInstance().getUserRole());
            redrawUIBasedOnRole();
        });

        panel.add(roleLabel);
        panel.add(roleComboBox);
        return panel;
    }

    public void updateRoleSelection(String selectedRole) {
        roleComboBox.setSelectedItem(selectedRole);
    }

    private JPanel createCenterPanel(UserRole role) {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Main Actions"));
    
        switch (role) {
            case SCRUM_MASTER:
                panel.add(createButton("Assign Sprint Backlogs", () -> handleButtonAction(new SprintBacklogPane(this))));
                // TODO: Spike Button Here
                break;
            case DEVELOPER:
                panel.add(createButton("Product Backlog (User Stories)",
                        () -> handleButtonAction(new UserStoryListPane(this))));
                // TODO: Spike Button Here
                break;
            case PRODUCT_OWNER:
                panel.add(createButton("Product Backlog (User Stories)",
                        () -> handleButtonAction(new UserStoryListPane(this))));
                break;
            case SCRUM_ADMIN:
                panel.add(createButton("Assign Sprint Backlogs", () -> handleButtonAction(new SprintBacklogPane(this))));
                // TODO: Spike Button Here
                panel.add(createButton("Product Backlog (User Stories)",
                        () -> handleButtonAction(new UserStoryListPane(this))));
                panel.add(createButton("Potential Blockers", () -> handleButtonAction(new PotentialBlockersPane(this))));
                panel.add(createButton("Potential Blocker Solutions",
                        () -> handleButtonAction(new PotentialBlockerSolutionsPane(this))));
                break;
        }
    
        // TODO: Potentially remove below buttons
        // panel.add(createButton("Sprints", () -> handleButtonAction(new SprintListPane(this))));
        panel.add(createButton("Update User Story Status", () -> handleButtonAction(new UpdateUserStoryPanel(this))));
        return panel;
    }
    

    private JPanel createRightPanel(UserRole role) {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Simulation Controls"));

        // Switch case syntax is not ideal, but it's easier than fixing Checkstyle
        // plugin at this time
        switch (role) {
            case SCRUM_MASTER:
                panel.add(createButton("New Simulation", () -> handleButtonAction(new NewSimulationPane(this))));
                panel.add(createButton("Modify Simulation", () -> handleButtonAction(new ModifySimulationPane(this))));
                panel.add(createButton("Start Simulation", () -> handleButtonAction(new SimulationPane(this))));
                break;
            case DEVELOPER:
                break;
            case PRODUCT_OWNER:
                break;
            case SCRUM_ADMIN:
                panel.add(createButton("New Simulation", () -> handleButtonAction(new NewSimulationPane(this))));
                panel.add(createButton("Modify Simulation", () -> handleButtonAction(new ModifySimulationPane(this))));
                panel.add(createButton("Start Simulation", () -> handleButtonAction(new SimulationPane(this))));
                // TODO: Add Show Simulation History Button here
                break;
        }
        panel.add(createButton("Add User", () -> handleButtonAction(new AddUserPane(this))));
        panel.add(createButton("Variant Simulation UI", () -> handleButtonAction(new VariantSimulationUI(this))));

        return panel;
    }

    private JButton createButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.addActionListener(e -> action.run());
        return button;
    }

    private void handleButtonAction(JFrame pane) {
        setMenuButtonsEnabled(false);
        setGlassPaneVisible(true);
        pane.setVisible(true);
        pane.setAlwaysOnTop(true);
        
        pane.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                setMenuButtonsEnabled(true);
                setGlassPaneVisible(false);
            }
        });
    }

    private void setupGlassPane() {
        JPanel glassPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0, 100));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        glassPane.addMouseListener(new java.awt.event.MouseAdapter() {
        });
        glassPane.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
        });

        glassPane.setOpaque(false);
        setGlassPane(glassPane);
    }

    private void setGlassPaneVisible(boolean visible) {
        getGlassPane().setVisible(visible);
    }

    private void setMenuButtonsEnabled(boolean enabled) {
        newSimulationButton.setEnabled(enabled);
        sprintsButton.setEnabled(enabled);
        userStoriesButton.setEnabled(enabled);
        startSimulationButton.setEnabled(enabled);
        potentialBlockersButton.setEnabled(enabled);
        potentialBlockerSolutionsButton.setEnabled(enabled);
        updateStoryStatusButton.setEnabled(enabled);
        simulationButton.setEnabled(enabled);
        modifySimulationButton.setEnabled(enabled);
        variantSimulationUIButton.setEnabled(enabled);
        sprintBacklogsButton.setEnabled(enabled);
    }
}
