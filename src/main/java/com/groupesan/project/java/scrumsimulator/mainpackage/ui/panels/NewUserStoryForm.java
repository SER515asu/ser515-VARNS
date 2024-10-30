package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.UserAction;
import com.groupesan.project.java.scrumsimulator.mainpackage.core.UserRolePermissions;
import com.groupesan.project.java.scrumsimulator.mainpackage.core.UserRoleSingleton;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.UserStory;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.UserStoryFactory;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;
import com.groupesan.project.java.scrumsimulator.mainpackage.utils.CustomConstraints;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class NewUserStoryForm extends JFrame implements BaseComponent {

    Double[] pointsList = {1.0, 2.0, 3.0, 5.0, 8.0, 11.0, 19.0, 30.0, 49.0};
    Integer[] businessValuePointsList = {1, 2, 3, 5, 8, 13, 20, 40, 100};
    private final JFrame parent;

    public NewUserStoryForm(JFrame parent) {
        this.parent = parent;
        this.init();
    }

    private JTextField nameField = new JTextField();
    private JTextArea descArea = new JTextArea();
    private JComboBox<Double> pointsCombo = new JComboBox<>(pointsList);
    private JComboBox<Integer> pointsCombobusinessValue = new JComboBox<>(businessValuePointsList);

    public void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("New User Story");
        setSize(400, 300);
        setLocationRelativeTo(parent);

        nameField = new JTextField();
        descArea = new JTextArea();
        pointsCombo = new JComboBox<>(pointsList);
        pointsCombobusinessValue = new JComboBox<>(businessValuePointsList);

        GridBagLayout myGridbagLayout = new GridBagLayout();
        JPanel myJpanel = new JPanel();
        myJpanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        myJpanel.setLayout(myGridbagLayout);

        BorderLayout myBorderLayout = new BorderLayout();

        setLayout(myBorderLayout);

        JLabel nameLabel = new JLabel("Name:");
        myJpanel.add(
                nameLabel,
                new CustomConstraints(
                        0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL));
        myJpanel.add(
                nameField,
                new CustomConstraints(
                        1, 0, GridBagConstraints.EAST, 1.0, 0.0,
                        GridBagConstraints.HORIZONTAL));

        JLabel descLabel = new JLabel("Description:");
        myJpanel.add(
                descLabel,
                new CustomConstraints(
                        0, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL));
        myJpanel.add(
                new JScrollPane(descArea),
                new CustomConstraints(
                        1, 1, GridBagConstraints.EAST, 1.0, 0.3, GridBagConstraints.BOTH));

        if (UserRolePermissions.actionAllowed(UserRoleSingleton.getInstance().getUserRole(), UserAction.ESTIMATE_WORK_EFFORT)) {
            JLabel pointsLabel = new JLabel("Points:");
            myJpanel.add(
                    pointsLabel,
                    new CustomConstraints(
                            0, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL));
            myJpanel.add(
                    pointsCombo,
                    new CustomConstraints(
                            1, 2, GridBagConstraints.EAST, 1.0, 0.0,
                            GridBagConstraints.HORIZONTAL));
        }

        if (UserRolePermissions.actionAllowed(UserRoleSingleton.getInstance().getUserRole(), UserAction.MANAGE_USER_STORES)) {
            JLabel businessValuePointsLabel = new JLabel("Business Value Points:");
            myJpanel.add(
                    businessValuePointsLabel,
                    new CustomConstraints(
                            0, 3, GridBagConstraints.WEST,
                            GridBagConstraints.HORIZONTAL));
            myJpanel.add(
                    pointsCombobusinessValue,
                    new CustomConstraints(
                            1, 3, GridBagConstraints.EAST, 1.0, 0.0,
                            GridBagConstraints.HORIZONTAL));
        }

        JButton cancelButton = new JButton("Cancel");

        cancelButton.addActionListener(e -> dispose());

        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener(
                e -> {
                    UserStory userStory = getUserStoryObject();
                    SimulationStateManager.getInstance().getCurrentSimulation().addUserStory(userStory);
                    dispose();
                });

        myJpanel.add(
                cancelButton,
                new CustomConstraints(0, 4, GridBagConstraints.EAST, GridBagConstraints.NONE));
        myJpanel.add(
                submitButton,
                new CustomConstraints(1, 4, GridBagConstraints.WEST, GridBagConstraints.NONE));

        add(myJpanel);
    }

    public UserStory getUserStoryObject() {
        String name = nameField.getText();
        String description = descArea.getText();
        Double points = (Double) pointsCombo.getSelectedItem();
        Integer businessValuePoints = (Integer) pointsCombobusinessValue.getSelectedItem();

        UserStoryFactory userStoryFactory = UserStoryFactory.getInstance();

        UserStory userStory = userStoryFactory.createNewUserStory(name, description, points,
                businessValuePoints);

        System.out.println(userStory);

        return userStory;
    }
}
