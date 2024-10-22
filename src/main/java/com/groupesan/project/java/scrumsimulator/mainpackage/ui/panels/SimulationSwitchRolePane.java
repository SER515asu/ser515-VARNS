package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;

public class SimulationSwitchRolePane extends JDialog implements BaseComponent {

    private JRadioButton developerRadioButton;
    private JRadioButton scrumMasterRadioButton;
    private JRadioButton productOwnerRadioButton;
    private ButtonGroup roleButtonGroup;
    private JButton switchButton;

    private JFrame parent;

    public SimulationSwitchRolePane(JFrame parent) {
        this.parent = parent;

        this.init();
    }

    @Override
    public void init() {
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        setTitle("Simulation Status");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        JLabel label = new JLabel("Roles:");
        panel.add(label);

        developerRadioButton = new JRadioButton("Developer");
        scrumMasterRadioButton = new JRadioButton("Scrum Master");
        productOwnerRadioButton = new JRadioButton("Product Owner");
        roleButtonGroup = new ButtonGroup();
        roleButtonGroup.add(developerRadioButton);
        roleButtonGroup.add(scrumMasterRadioButton);
        roleButtonGroup.add(productOwnerRadioButton);
        panel.add(developerRadioButton);
        panel.add(scrumMasterRadioButton);
        panel.add(productOwnerRadioButton);

        switchButton = new JButton("Switch Role");
        switchButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Logic for join button
                        onSwitchButtonClicked();
                    }
                });

        setLayout(new BorderLayout());
        add(switchButton, BorderLayout.SOUTH);
        add(panel);
    }

    private void onSwitchButtonClicked() {
        if (developerRadioButton.isSelected()) {
            JOptionPane.showMessageDialog(
                    null, "Switched to Developer", "Role Switching", JOptionPane.PLAIN_MESSAGE);
        } else if (scrumMasterRadioButton.isSelected()) {
            JOptionPane.showMessageDialog(
                    null, "Switched to Scrum Master", "Role Switching", JOptionPane.PLAIN_MESSAGE);
        } else if (productOwnerRadioButton.isSelected()) {
            JOptionPane.showMessageDialog(
                    null, "Switched to Product Owner", "Role Switching", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "Failed to Switch Role",
                    "Role Switching Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        roleButtonGroup.clearSelection();
        dispose();
        return;
    }
}
