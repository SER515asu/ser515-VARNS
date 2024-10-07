package com.groupesan.project.java.scrumsimulator.mainpackage.core;

import com.groupesan.project.java.scrumsimulator.mainpackage.impl.*;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.UserStoryAddedState;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.UserStoryUnselectedState;
import com.groupesan.project.java.scrumsimulator.mainpackage.utils.RandomUtils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JList;

public class Simulation {

    private String simulationName;
    private Teacher teacher;
    private final List<Player> players = new ArrayList<>();
    private int sprintCount;
    private int sprintDuration; // In days
    private final List<Sprint> sprints;

    public Simulation(String simulationName, int sprintCount, int sprintDurationWeeks) {
        this.simulationName = simulationName;
        this.sprintCount = sprintCount;
        this.sprintDuration = sprintDurationWeeks * 7;

        for (int i = 0; i < sprintCount; i++) {
            SprintStore.getInstance()
                    .addSprint(SprintFactory.getSprintFactory().createNewSprint(null, null, sprintDuration));
        }
        this.sprints = SprintStore.getInstance().getSprints();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public boolean removePlayer(Player player) {
        return players.remove(player);
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher newTeacher) {
        teacher = newTeacher;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public void setSimulationName(String simulationName) {
        this.simulationName = simulationName;
    }

    public void setSprintCount(int sprintCount) {
        this.sprintCount = sprintCount;
    }

    public int getSprintCount() {
        return sprintCount;
    }

    public void setSprintDuration(int sprintDuration) {
        this.sprintDuration = sprintDuration;
    }

    public int getSprintDuration() {
        return sprintDuration;
    }

    public List<Sprint> getSprints() {
        return this.sprints;
    }

    public void addUserStories(Sprint sprint, List<UserStory> userStories) {
        if (!userStories.isEmpty()) {
            userStories.forEach(sprint::addUserStory);
        }
    }

    public void removeUserStory(Sprint sprint, String userStory) {
        if (userStory == null || sprint == null)
            return;
        UserStory userStoryToBeRemoved = UserStoryStore
                .getInstance()
                .getUserStories()
                .stream()
                .filter(us -> us.toString().equals(userStory))
                .toList()
                .getFirst();
        userStoryToBeRemoved.changeState(new UserStoryUnselectedState(userStoryToBeRemoved));
        sprint.removeUserStory(userStoryToBeRemoved);
    }

    public void addUserStory(Sprint sprint, String userStory) {
        if (userStory == null || sprint == null)
            return;
        UserStory userStoryToBeAdded = UserStoryStore
                .getInstance()
                .getUserStories()
                .stream()
                .filter(us -> us.toString().equals(userStory))
                .toList()
                .getFirst();
        userStoryToBeAdded.changeState(new UserStoryAddedState(userStoryToBeAdded));
        sprint.addUserStory(userStoryToBeAdded);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[Simulation] " + getSimulationName() + "\n");
        result.append("Sprints: ").append(sprintCount).append("\n");
        result.append("Sprint Length: ").append(sprintDuration).append(" days\n");
        for (Player player : players) {
            result.append(player).append("\n");
        }
        return result.toString();
    }

    public void randomizeSprintBacklog(JList<String> userStories) {
        List<UserStory> userStoriesList = UserStoryStore.getInstance().getUserStories();
        for (UserStory userStory : userStoriesList) {
            userStory.changeState(new UserStoryUnselectedState(userStory));
        }
        for (Sprint sprint : sprints) {
            sprint.clearUserStories();
        }

        int numberOfSprints = sprints.size();

        for (UserStory userStory : userStoriesList) {
            int sprintIndex = RandomUtils.getRandomInt(numberOfSprints);
            Sprint sprint = sprints.get(sprintIndex);
            sprint.addUserStory(userStory);
            userStory.changeState(new UserStoryAddedState(userStory));
        }
    }
}
