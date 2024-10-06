package com.groupesan.project.java.scrumsimulator.mainpackage.core;

import java.util.ArrayList;
import java.util.List;

public class Simulation {

    private String simulationName;
    private Teacher teacher;
    private final List<Player> players = new ArrayList<>();
    private int sprintCount;
    private int sprintDuration; // In days

    public Simulation(String simulationName, int sprintCount, int sprintDurationWeeks) {
        this.simulationName = simulationName;
        this.sprintCount = sprintCount;
        this.sprintDuration = sprintDurationWeeks * 7;
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

    @Override
    public String toString() {
    String result = "[Simulation] " + getSimulationName() + "\n";
    result += "Sprints: " + sprintCount + "\n";
    result += "Sprint Length: " + sprintDuration + " days\n";
    for (Player player : players) {
        result += player + "\n";
    }
    return result;
}
}
