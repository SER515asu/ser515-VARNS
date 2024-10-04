package com.groupesan.project.java.scrumsimulator.mainpackage.core;

import java.util.ArrayList;
import java.util.List;

public class Simulation {

    private String simulationName;
    private Teacher teacher;
    private final List<Player> players = new ArrayList<>();
    private int sprintCount;
    private int sprintLength;

    public Simulation(String simulationName, Teacher teacher, int sprintCount, int sprintLength) {
        this.simulationName = simulationName;
        this.teacher = teacher;
        this.sprintCount = sprintCount;
        this.sprintLength = sprintLength;
    }

    public int getSprintLength() {
        return sprintLength;
    }
    
    public void setSprintLength(int sprintLength) {
        this.sprintLength = sprintLength;
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

    @Override
    public String toString() {
    String result = "[Simulation] " + getSimulationName() + "\n";
    result += "Sprints: " + sprintCount + "\n";
    result += "Sprint Length: " + sprintLength + " days\n";
    for (Player player : players) {
        result += player + "\n";
    }
    return result;
}
}
