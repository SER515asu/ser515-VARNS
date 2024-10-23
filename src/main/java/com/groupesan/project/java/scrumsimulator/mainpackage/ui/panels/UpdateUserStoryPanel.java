package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.state.UserStoryStateManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class UpdateUserStoryPanel extends JDialog implements BaseComponent {

    private JFrame parent;

    public UpdateUserStoryPanel(JFrame parent) {
        this.parent = parent;

        this.init();
    }

    @Override
    public void init() {
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        setTitle("Update User Story Status");

        JPanel panel = new JPanel();
        placeComponents(panel);
        add(panel);

        setLocationRelativeTo(null);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userStoryLabel = new JLabel("Select User Story:");
        userStoryLabel.setBounds(10, 20, 120, 25);
        panel.add(userStoryLabel);

        List<String> userStories = UserStoryStateManager.getUserStories();
        JComboBox<String> userStoryComboBox = new JComboBox<>(userStories.toArray(new String[0]));
        userStoryComboBox.setBounds(150, 20, 200, 25);
        panel.add(userStoryComboBox);

        JLabel statusLabel = new JLabel("Select Status:");
        statusLabel.setBounds(10, 50, 120, 25);
        panel.add(statusLabel);

        String[] statusOptions = { "new", "in progress", "ready for test", "completed" };
        JComboBox<String> statusComboBox = new JComboBox<>(statusOptions);
        statusComboBox.setBounds(150, 50, 200, 25);
        panel.add(statusComboBox);

        JButton updateButton = new JButton("Update Status");
        updateButton.setBounds(150, 80, 150, 25);
        panel.add(updateButton);

        updateButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String selectedUserStory = (String) userStoryComboBox.getSelectedItem();
                        String selectedStatus = (String) statusComboBox.getSelectedItem();

                        if (selectedUserStory != null && selectedStatus != null) {
                            UserStoryStateManager.updateUserStoryStatus(
                                    selectedUserStory, selectedStatus);
                            JOptionPane.showMessageDialog(null, "Status updated successfully!");
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(
                                    null, "Please select a User Story and Status");
                        }
                    }
                });
    }
}
