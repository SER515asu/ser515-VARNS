package com.groupesan.project.java.scrumsimulator.mainpackage.core;

import com.groupesan.project.java.scrumsimulator.mainpackage.impl.Sprint;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.SprintFactory;

import java.util.ArrayList;
import java.util.List;

public class Simulation {

    private String simulationName;
    private Teacher teacher;
    private final List<Player> players = new ArrayList<>();
    private int sprintCount;
    private int sprintDuration; // In days
    private final List<Sprint> sprints = new ArrayList<>();

    public Simulation(String simulationName, int sprintCount, int sprintDurationWeeks) {
        this.simulationName = simulationName;
        this.sprintCount = sprintCount;
        this.sprintDuration = sprintDurationWeeks * 7;

        for (int i = 0; i < sprintCount; i++) {
            sprints.add(SprintFactory.getSprintFactory().createNewSprint(null, null, sprintDuration));
        }
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

    @Override
    public String toString() {
        String result = "[Simulation] " + getSimulationName() + "\n";
        result += "Sprints: " + sprintCount + "\n";
        for (Player player : players) {
            result += player + "\n";
        }
        return result;
    }
}
