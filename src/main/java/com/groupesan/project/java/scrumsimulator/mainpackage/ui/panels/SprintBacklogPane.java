package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.impl.Sprint;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.SprintStore;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.UserStoryStore;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.UserStoryUnselectedState;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.utils.GridBagConstraintsBuilder;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class SprintBacklogPane extends JFrame {

    String selectedUserStory;
    String selectedSprintUserStory;

    public SprintBacklogPane() {
        setBounds(100, 100, 200, 200);
        Container container = getContentPane();
        container.setLayout(new GridBagLayout());
        init(container);
    }

    public void init(Container myContainer) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Allocate Sprint Backlog");
        setSize(800, 600);

        JList<String> userStories = new JList<>();
        redrawUserStoriesList(userStories);
        JScrollPane userStoriesList = new JScrollPane(userStories);
        myContainer.add(
                userStoriesList,
                new GridBagConstraintsBuilder()
                        .setGridX(0)
                        .setGridY(1)
                        .setWeightX(1)
                        .setFill(GridBagConstraints.HORIZONTAL));

        JComboBox<String> selectSprintComboBox = new JComboBox<>();
        myContainer.add(selectSprintComboBox,
                new GridBagConstraintsBuilder()
                        .setGridX(2)
                        .setGridY(0)
                        .setWeightX(1)
                        .setFill(GridBagConstraints.HORIZONTAL));

        for (Sprint sprint : SimulationStateManager.getInstance().getCurrentSimulation().getSprints()) {
            selectSprintComboBox.addItem(sprint.toString());
        }

        JList<String> sprintUserStories = new JList<>();
        redrawSprintUserStoriesList(selectSprintComboBox, sprintUserStories);
        JScrollPane sprintUserStoriesList = new JScrollPane(sprintUserStories);
        myContainer.add(
                sprintUserStoriesList,
                new GridBagConstraintsBuilder()
                        .setGridX(2)
                        .setGridY(1)
                        .setWeightX(1)
                        .setFill(GridBagConstraints.HORIZONTAL));

        selectSprintComboBox.addActionListener(e -> {
            redrawUserStoriesList(userStories);
            redrawSprintUserStoriesList(selectSprintComboBox, sprintUserStories);
        });

        JButton moveLeft = getMoveLeft(selectSprintComboBox, userStories, sprintUserStories);
        JButton moveRight = getMoveRight(selectSprintComboBox, userStories, sprintUserStories);

        myContainer.add(
                moveLeft,
                new GridBagConstraintsBuilder()
                        .setGridX(1)
                        .setGridY(1)
                        .setWeightX(1)
                        .setFill(GridBagConstraints.HORIZONTAL));
        myContainer.add(
                moveRight,
                new GridBagConstraintsBuilder()
                        .setGridX(1)
                        .setGridY(2)
                        .setWeightX(1)
                        .setFill(GridBagConstraints.HORIZONTAL));

    }

    private JButton getMoveLeft(JComboBox<String> selectSprintComboBox, JList<String> userStories, JList<String> sprintUserStories) {
        JButton moveLeft = new JButton("<");
        moveLeft.addActionListener(
                e -> {
                    SimulationStateManager
                            .getInstance()
                            .getCurrentSimulation()
                            .removeUserStory(SprintStore.getInstance().getSprintByString((String) selectSprintComboBox.getSelectedItem()), selectedSprintUserStory);
                    redrawUserStoriesList(userStories);
                    redrawSprintUserStoriesList(selectSprintComboBox, sprintUserStories);
                }
        );
        return moveLeft;
    }

    private JButton getMoveRight(JComboBox<String> selectSprintComboBox, JList<String> userStories, JList<String> sprintUserStories) {
        JButton moveRight = new JButton(">");
        moveRight.addActionListener(
                e -> {
                    SimulationStateManager
                            .getInstance()
                            .getCurrentSimulation()
                            .addUserStory(SprintStore.getInstance().getSprintByString((String) selectSprintComboBox.getSelectedItem()), selectedUserStory);
                    redrawUserStoriesList(userStories);
                    redrawSprintUserStoriesList(selectSprintComboBox, sprintUserStories);
                }
        );
        return moveRight;
    }

    private void redrawUserStoriesList(JList<String> userStoriesComponent) {
        String[] userStories = UserStoryStore.getInstance().getUserStories()
                .stream()
                .filter(userStory -> userStory.getUserStoryState() instanceof UserStoryUnselectedState)
                .map(Objects::toString)
                .toList()
                .toArray(new String[]{});
        userStoriesComponent.setVisibleRowCount(20);

        userStoriesComponent.addListSelectionListener(
                e -> selectedUserStory = userStoriesComponent.getSelectedValue()
        );

       userStoriesComponent.setListData(userStories);
    }

    private void redrawSprintUserStoriesList(JComboBox<String> selectSprintComboBox, JList<String> userStoriesComponent) {
        String[] userStories = SprintStore.getInstance()
                .getSprintByString((String) selectSprintComboBox.getSelectedItem())
                .getUserStories()
                .stream()
                .map(Objects::toString)
                .toList()
                .toArray(new String[]{});
        userStoriesComponent.setVisibleRowCount(20);

        userStoriesComponent.addListSelectionListener(
                e -> selectedSprintUserStory = userStoriesComponent.getSelectedValue()
        );
        userStoriesComponent.setListData(userStories);
    }

}