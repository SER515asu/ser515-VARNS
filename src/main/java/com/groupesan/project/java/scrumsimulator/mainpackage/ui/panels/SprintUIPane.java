package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.Player;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.Sprint;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.UserStory;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.UserStoryStore;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.UserStorySelectedState;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.UserStoryUnselectedState;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.UserStoryWidget;
import com.groupesan.project.java.scrumsimulator.mainpackage.utils.CustomConstraints;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

/**
 * A pane for displaying sprint actions and functions during a sprint. For now only consists of
 * functionality to add sprints to in progress for aplayer, but will be expanded as more functions
 * are added.
 */
public class SprintUIPane extends JFrame implements BaseComponent {

    public SprintUIPane(Player player) {
        this.currentPlayer = player;
        this.init();
    }

    private final List<UserStoryWidget> widgets = new ArrayList<>();

    private final Player currentPlayer;

    public void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Select UserStories");
        setSize(800, 600);

        GridBagLayout myGridbagLayout = new GridBagLayout();
        JPanel myJpanel = new JPanel();
        myJpanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        myJpanel.setLayout(myGridbagLayout);

        JPanel sprintSelectionPanel = new JPanel();
        sprintSelectionPanel.setLayout(new GridBagLayout());

        JComboBox<String> selectSprintComboBox = new JComboBox<>();
        myJpanel.add(selectSprintComboBox,
                new CustomConstraints(
                        2, 0, GridBagConstraints.WEST, 1.0, 0.2, GridBagConstraints.HORIZONTAL));

        for (Sprint sprint : SimulationStateManager.getInstance().getCurrentSimulation().getSprints()) {
            selectSprintComboBox.addItem(sprint.toString());
        }

        JComboBox<String> selectComboBox = new JComboBox<>();
        myJpanel.add(
                selectComboBox,
                new CustomConstraints(
                        0, 1, GridBagConstraints.WEST, 1.0, 0.2, GridBagConstraints.HORIZONTAL));

        for (UserStory userStory : UserStoryStore.getInstance().getUserStories()) {
            // only display unselected states
            if (userStory.getUserStoryState() instanceof UserStoryUnselectedState) {
                selectComboBox.addItem(userStory.toString());
                widgets.add(new UserStoryWidget(userStory, false));
            }
        }

        JPanel availableSubPanel = new JPanel();
        availableSubPanel.setLayout(new GridBagLayout());
        int i = 0;
        for (UserStoryWidget widget : widgets) {
            availableSubPanel.add(
                    widget,
                    new CustomConstraints(
                            0,
                            i++,
                            GridBagConstraints.WEST,
                            1.0,
                            0.1,
                            GridBagConstraints.HORIZONTAL));
        }
        myJpanel.add(
                new JScrollPane(availableSubPanel),
                new CustomConstraints(
                        0, 0, GridBagConstraints.WEST, 1.0, 0.8, GridBagConstraints.HORIZONTAL));

        JPanel selectedSubPanel = new JPanel();
        selectedSubPanel.setLayout(new GridBagLayout());
        i = 0;

        for (UserStory userStory : UserStoryStore.getInstance().getUserStories()) {
            // only display unselected states
            if (userStory.getUserStoryState() instanceof UserStorySelectedState
                    && currentPlayer.equals(userStory.getOwner())) {
                selectedSubPanel.add(
                        new UserStoryWidget(userStory, false),
                        new CustomConstraints(
                                0,
                                i++,
                                GridBagConstraints.WEST,
                                1.0,
                                0.1,
                                GridBagConstraints.HORIZONTAL));
            }
        }
        myJpanel.add(
                new JScrollPane(selectedSubPanel),
                new CustomConstraints(
                        0, 4, GridBagConstraints.WEST, 1.0, 0.8, GridBagConstraints.HORIZONTAL));

        JLabel warningLabel = new JLabel("");

        myJpanel.add(
                warningLabel,
                new CustomConstraints(
                        0, 2, GridBagConstraints.WEST, 1.0, 0.1, GridBagConstraints.HORIZONTAL));

        JButton SelectUSButton = getjButton(selectComboBox, availableSubPanel, selectedSubPanel);
        myJpanel.add(
                SelectUSButton,
                new CustomConstraints(
                        0, 3, GridBagConstraints.WEST, 1.0, 0.2, GridBagConstraints.HORIZONTAL));

        add(myJpanel);
    }

    private JButton getjButton(JComboBox<String> selectComboBox, JPanel availableSubPanel, JPanel selectedSubPanel) {
        JButton SelectUSButton = new JButton("Select");
        SelectUSButton.addActionListener(
                e -> {
                    for (UserStory userStory : UserStoryStore.getInstance().getUserStories()) {
                        if (userStory.toString().equals(selectComboBox.getSelectedItem())) {
                            userStory.setOwner(currentPlayer);
                            userStory.changeState(new UserStorySelectedState(userStory));
                        }
                    }
                    selectComboBox.removeAllItems();
                    widgets.clear();
                    for (UserStory userStory : UserStoryStore.getInstance().getUserStories()) {
                        // only display unselected states
                        if (userStory.getUserStoryState() instanceof UserStoryUnselectedState) {
                            selectComboBox.addItem(userStory.toString());
                            widgets.add(new UserStoryWidget(userStory, false));
                        }
                    }

                    availableSubPanel.removeAll();
                    int i1 = 0;
                    for (UserStoryWidget widget : widgets) {
                        availableSubPanel.add(
                                widget,
                                new CustomConstraints(
                                        0,
                                        i1++,
                                        GridBagConstraints.WEST,
                                        1.0,
                                        0.1,
                                        GridBagConstraints.HORIZONTAL));
                    }

                    selectedSubPanel.removeAll();
                    i1 = 0;
                    for (UserStory userStory : UserStoryStore.getInstance().getUserStories()) {
                        // only display unselected states
                        if (userStory.getUserStoryState() instanceof UserStorySelectedState
                                && currentPlayer.equals(userStory.getOwner())) {
                            selectedSubPanel.add(
                                    new UserStoryWidget(userStory, false),
                                    new CustomConstraints(
                                            0,
                                            i1++,
                                            GridBagConstraints.WEST,
                                            1.0,
                                            0.1,
                                            GridBagConstraints.HORIZONTAL));
                        }
                    }
                });
        return SelectUSButton;
    }
}
