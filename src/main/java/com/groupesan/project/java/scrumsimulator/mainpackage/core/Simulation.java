package com.groupesan.project.java.scrumsimulator.mainpackage.core;

import com.groupesan.project.java.scrumsimulator.mainpackage.impl.*;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.utils.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.swing.JList;

public class Simulation {

    private final UUID simulationId;
    private String simulationName;
    private Teacher teacher;
    private final List<Player> players = new ArrayList<>();
    private int sprintCount;
    private int sprintDuration;
    private final List<Sprint> sprints;
    private long randomSeed;
    private final List<UserStory> userStories;

    public Simulation(UUID simulationId, String simulationName, int sprintCount, int sprintDurationDays, long randomSeed) {
        this.simulationId = simulationId;
        this.simulationName = simulationName;
        this.sprintCount = sprintCount;
        this.sprintDuration = sprintDurationDays;
        this.randomSeed = randomSeed;

        for (int i = 0; i < sprintCount; i++) {
            SprintStore.getInstance()
                    .addSprint(SprintFactory.getSprintFactory().createNewSprint(null, null, sprintDuration));
        }
        this.sprints = SprintStore.getInstance().getSprints();
        this.userStories = new ArrayList<>();
    }

    public Simulation(UUID id, String name, int sprintCount, int sprintDurationDays, List<Sprint> sprints, List<UserStory> userStories) {
        this.simulationId = id;
        this.simulationName = name;
        this.sprintCount = sprintCount;
        this.sprintDuration = sprintDurationDays;
        this.sprints = sprints;
        this.userStories = userStories;
    }

    public UUID getSimulationId() {
        return simulationId;
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
        if (this.sprintCount == sprintCount) return;

        if (sprintCount > this.sprintCount) {
            for (int i = this.sprintCount; i < sprintCount; i++) {
                sprints.add(new Sprint("", "", sprintDuration, sprintCount));
            }
        } else {
            for (int i = sprintCount; i < this.sprintCount; i++) {
                sprints.removeLast();
            }
        }
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

    public Sprint getSprintByString(String sprintString) {
        return sprints.stream().filter(s -> s.toString().equals(sprintString)).toList().getFirst();
    }

    public long getRandomSeed() {
        return randomSeed;
    }

    public void setRandomSeed(long seed) {
        randomSeed = seed;
    }

    public void addUserStories(Sprint sprint, List<UserStory> userStories) {
        if (!userStories.isEmpty()) {
            userStories.forEach(sprint::addUserStory);
        }
    }

    public List<UserStory> getUserStories() {
        return userStories;
    }

    public void addUserStory(UserStory userStory) {
        userStories.add(userStory);
    }

    public void addUserStories(List<UserStory> userStories) {
        this.userStories.addAll(userStories);
    }

    public void removeUserStoryFromSprint(Sprint sprint, String userStory) {
        if (userStory == null || sprint == null)
            return;
        UserStory userStoryToBeRemoved = SimulationStateManager
                .getInstance()
                .getCurrentSimulation()
                .getUserStories()
                .stream()
                .filter(us -> us.toString().equals(userStory))
                .toList()
                .getFirst();
        userStoryToBeRemoved.changeState(UserStory.UserStoryState.UNSELECTED);
        sprint.removeUserStory(userStoryToBeRemoved.getId());
    }

    public void addUserStoryToSprint(Sprint sprint, String userStory) {
        if (userStory == null || sprint == null)
            return;
        UserStory userStoryToBeAdded = SimulationStateManager
                .getInstance()
                .getCurrentSimulation()
                .getUserStories()
                .stream()
                .filter(us -> us.toString().equals(userStory))
                .toList()
                .getFirst();
        userStoryToBeAdded.changeState(UserStory.UserStoryState.ADDED);
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
        List<UserStory> userStoriesList = SimulationStateManager.getInstance().getCurrentSimulation().getUserStories();
        for (UserStory userStory : userStoriesList) {
            userStory.changeState(UserStory.UserStoryState.UNSELECTED);
        }
        for (Sprint sprint : sprints) {
            sprint.clearUserStories();
        }

        int numberOfSprints = sprints.size();

        for (UserStory userStory : userStoriesList) {
            int sprintIndex = RandomUtils.getInstance().getRandomInt(numberOfSprints);
            Sprint sprint = sprints.get(sprintIndex);
            sprint.addUserStory(userStory);
            userStory.changeState(UserStory.UserStoryState.ADDED);
        }
    }
}
