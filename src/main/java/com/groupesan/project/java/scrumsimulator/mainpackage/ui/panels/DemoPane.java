package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.Player;
import com.groupesan.project.java.scrumsimulator.mainpackage.core.ScrumRole;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;

import javax.swing.*;

import java.awt.*;

public class DemoPane extends JFrame implements BaseComponent {
    private final Player player = new Player("bob", new ScrumRole("demo"));

    public DemoPane() {
        this.init();
        player.doRegister();
    }

    public void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Demo");
        setSize(1000, 700);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        JPanel rightPanel = createRightPanel();
        add(rightPanel, BorderLayout.EAST);

        StylePane.applyStyle(this);
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

        panel.add(createButton("Product Backlog(User Stories)", () -> new UserStoryListPane().setVisible(true)));
        panel.add(createButton("Sprints", () -> new SprintListPane().setVisible(true)));
        panel.add(createButton("Assign Sprint Backlogs", this::handleSprintBacklogs));
        panel.add(createButton("Update User Story Status", () -> new UpdateUserStoryPanel().setVisible(true)));
        panel.add(createButton("Potential Blockers", () -> new PotentialBlockersPane().setVisible(true)));

        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Simulation Controls"));

        panel.add(createButton("Add User", () -> new SimulationPane().setVisible(true)));
        panel.add(createButton("Modify Simulation",
                () -> new ModifySimulationPane(new SimulationManager()).setVisible(true)));
        panel.add(createButton("Join Simulation", () -> new SimulationUI().setVisible(true)));
        panel.add(createButton("Switch Role", () -> new SimulationSwitchRolePane().setVisible(true)));
        panel.add(createButton("Variant Simulation UI", () -> new VariantSimulationUI().setVisible(true)));
        panel.add(createButton("Start Simulation", () -> new SimulationPanel().setVisible(true)));

        return panel;
    }

    private JButton createButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.addActionListener(e -> action.run());
        return button;
    }

    private void handleSprintBacklogs() {
        if (SimulationStateManager.getInstance().getCurrentSimulation() == null) {
            JOptionPane.showMessageDialog(this,
                    "Please create and join a simulation before adding user stories to sprint backlog",
                    "No Active Simulation",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            new SprintBacklogPane().setVisible(true);
        }
    }
}
