package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.impl.Sprint;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.SprintStore;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.UserStory;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.UserStoryStore;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.UserStoryUnselectedState;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.utils.GridBagConstraintsBuilder;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.List;

public class SprintBacklogPane extends JDialog implements BaseComponent {

    String selectedUserStory;
    String selectedSprintUserStory;

    List<UserStory> userStories;

    private JFrame parent;

    public SprintBacklogPane(JFrame parent) {
        this.parent = parent;

        init();
    }

    private void updateUserStories() {
        this.userStories = UserStoryStore.getInstance().getUserStories();
    }

    @Override
    public void init() {
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        Container container = getContentPane();
        container.setLayout(new GridBagLayout());
        this.updateUserStories();

        setTitle("Assign Sprint Backlogs");

        JList<String> userStories = new JList<>();
        redrawUserStoriesList(userStories);
        JScrollPane userStoriesList = new JScrollPane(userStories);
        container.add(
                userStoriesList,
                new GridBagConstraintsBuilder()
                        .setGridX(0)
                        .setGridY(1)
                        .setWeightX(1)
                        .setFill(GridBagConstraints.HORIZONTAL));

        JComboBox<String> selectSprintComboBox = new JComboBox<>();
        container.add(selectSprintComboBox,
                new GridBagConstraintsBuilder()
                        .setGridX(2)
                        .setGridY(0)
                        .setWeightX(1)
                        .setFill(GridBagConstraints.HORIZONTAL));

        if (SimulationStateManager.getInstance().getCurrentSimulation() != null) {
            for (Sprint sprint : SimulationStateManager.getInstance().getCurrentSimulation().getSprints()) {
                selectSprintComboBox.addItem(sprint.toString());
            }
        }

        JList<String> sprintUserStories = new JList<>();
        redrawSprintUserStoriesList(selectSprintComboBox, sprintUserStories);
        JScrollPane sprintUserStoriesList = new JScrollPane(sprintUserStories);
        container.add(
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

        Container middleMenuContainer = new Container();
        middleMenuContainer.setLayout(new GridBagLayout()); // Set layout manager

        JButton moveLeft = getMoveLeft(selectSprintComboBox, userStories, sprintUserStories);
        JButton moveRight = getMoveRight(selectSprintComboBox, userStories, sprintUserStories);

        middleMenuContainer.add(
                moveRight,
                new GridBagConstraintsBuilder()
                        .setGridX(0)
                        .setGridY(0)
                        .setWeightX(1)
                        .setFill(GridBagConstraints.HORIZONTAL));

        middleMenuContainer.add(
                moveLeft,
                new GridBagConstraintsBuilder()
                        .setGridX(0)
                        .setGridY(1)
                        .setWeightX(1)
                        .setFill(GridBagConstraints.HORIZONTAL));

        container.add(
                middleMenuContainer,
                new GridBagConstraintsBuilder()
                        .setGridX(1)
                        .setGridY(1)
                        .setWeightX(1)
                        .setFill(GridBagConstraints.HORIZONTAL));

        JButton randomizeButton = getRandomizeButton(selectSprintComboBox, userStories, sprintUserStories);

        container.add(
                randomizeButton,
                new GridBagConstraintsBuilder()
                        .setGridX(1)
                        .setGridY(3)
                        .setWeightX(1)
                        .setFill(GridBagConstraints.HORIZONTAL));
    }

    private JButton getMoveLeft(JComboBox<String> selectSprintComboBox, JList<String> userStories,
            JList<String> sprintUserStories) {
        JButton moveLeft = new JButton("<");
        moveLeft.addActionListener(
                e -> {
                    SimulationStateManager
                            .getInstance()
                            .getCurrentSimulation()
                            .removeUserStory(SprintStore.getInstance().getSprintByString(
                                    (String) selectSprintComboBox.getSelectedItem()), selectedSprintUserStory);
                    redrawUserStoriesList(userStories);
                    redrawSprintUserStoriesList(selectSprintComboBox, sprintUserStories);
                });
        return moveLeft;
    }

    private JButton getMoveRight(JComboBox<String> selectSprintComboBox, JList<String> userStories,
            JList<String> sprintUserStories) {
        JButton moveRight = new JButton(">");
        moveRight.addActionListener(
                e -> {
                    SimulationStateManager
                            .getInstance()
                            .getCurrentSimulation()
                            .addUserStory(SprintStore.getInstance().getSprintByString(
                                    (String) selectSprintComboBox.getSelectedItem()), selectedUserStory);
                    redrawUserStoriesList(userStories);
                    redrawSprintUserStoriesList(selectSprintComboBox, sprintUserStories);
                });
        return moveRight;
    }

    private JButton getRandomizeButton(JComboBox<String> selectSprintComboBox, JList<String> userStories,
            JList<String> sprintUserStories) {
        JButton randomizeButton = new JButton("Randomize");
        randomizeButton.addActionListener(
                e -> {
                    int response = JOptionPane.showConfirmDialog(this,
                            "Are you sure you want to randomize the sprint backlog?", "Randomize Sprint Backlog",
                            JOptionPane.YES_NO_OPTION);

                    if (response != JOptionPane.YES_OPTION) {
                        return;
                    }

                    SimulationStateManager
                            .getInstance()
                            .getCurrentSimulation()
                            .randomizeSprintBacklog(userStories);
                    redrawUserStoriesList(userStories);
                    redrawSprintUserStoriesList(selectSprintComboBox, sprintUserStories);
                });
        return randomizeButton;
    }

    private void redrawUserStoriesList(JList<String> userStoriesComponent) {

        updateUserStories();

        String[] userStoryNames = this.userStories.stream()
                .filter(userStory -> userStory.getUserStoryState() instanceof UserStoryUnselectedState)
                .map(Objects::toString).toList().toArray(new String[] {});

        userStoriesComponent.setVisibleRowCount(20);

        userStoriesComponent.addListSelectionListener(
                e -> selectedUserStory = userStoriesComponent.getSelectedValue());

        userStoriesComponent.setListData(userStoryNames);
    }

    private void redrawSprintUserStoriesList(JComboBox<String> selectSprintComboBox,
            JList<String> userStoriesComponent) {
        String[] userStories = SprintStore.getInstance()
                .getSprintByString((String) selectSprintComboBox.getSelectedItem())
                .getUserStories()
                .stream()
                .map(Objects::toString)
                .toList()
                .toArray(new String[] {});
        userStoriesComponent.setVisibleRowCount(20);

        userStoriesComponent.addListSelectionListener(
                e -> selectedSprintUserStory = userStoriesComponent.getSelectedValue());
        userStoriesComponent.setListData(userStories);
    }

}
