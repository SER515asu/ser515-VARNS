package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.Player;
import com.groupesan.project.java.scrumsimulator.mainpackage.core.ScrumRole;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import java.awt.*;

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
        setSize(1000, 700);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        JPanel rightPanel = createRightPanel();
        add(rightPanel, BorderLayout.EAST);

        applyStyle(this);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel roleLabel = new JLabel("Current Role:");
        JComboBox<String> roleComboBox = new JComboBox<>(new String[] { "Scrum Master", "Developer", "Product Owner" });
        roleComboBox.setPreferredSize(new Dimension(150, 25));
        roleComboBox.addActionListener(e -> {
            String selectedRole = (String) roleComboBox.getSelectedItem();
            System.out.println("Selected role: " + selectedRole);
            // Add logic to change the role in the backend
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

    // TODO: Figure out what this is used for because it was initially hidden from
    // view

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

    private void applyStyle(Container container) {
        for (Component comp : container.getComponents()) {

            comp.setFont(new Font("Arial", Font.PLAIN, 20));

            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                button.setBackground(new Color(70, 130, 180));
                button.setForeground(Color.WHITE);
                button.setFocusPainted(false);
                button.setFont(new Font("Arial", Font.BOLD, 20));
            } else if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                panel.setBackground(new Color(240, 240, 240));
                applyStyle(panel);
            } else if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                label.setForeground(new Color(50, 50, 50));
            } else if (comp instanceof JTextField || comp instanceof JTextArea) {
                JTextComponent textComp = (JTextComponent) comp;
                textComp.setBackground(Color.WHITE);
                textComp.setForeground(new Color(50, 50, 50));
                textComp.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            } else if (comp instanceof JComboBox) {
                JComboBox<?> comboBox = (JComboBox<?>) comp;
                comboBox.setBackground(Color.WHITE);
                comboBox.setForeground(new Color(50, 50, 50));
                Dimension size = comboBox.getPreferredSize();
                size.width = 220;
                comboBox.setPreferredSize(size);
            } else if (comp instanceof JCheckBox || comp instanceof JRadioButton) {
                AbstractButton abstractButton = (AbstractButton) comp;
                abstractButton.setBackground(new Color(240, 240, 240));
                abstractButton.setForeground(new Color(50, 50, 50));
            } else if (comp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) comp;
                scrollPane.getViewport().setBackground(Color.WHITE);
                applyStyle(scrollPane.getViewport());
            } else if (comp instanceof Container) {
                applyStyle((Container) comp);
            }
            if (comp instanceof JComponent) {
                ((JComponent) comp).setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DemoPane().setVisible(true));
    }
}
