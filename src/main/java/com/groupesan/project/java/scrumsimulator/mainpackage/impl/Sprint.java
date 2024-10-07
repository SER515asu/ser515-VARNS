package com.groupesan.project.java.scrumsimulator.mainpackage.impl;

import java.util.ArrayList;
import java.util.List;

public class Sprint {
    private final ArrayList<UserStory> userStories = new ArrayList<>();
    private final String name;

    private final String description;

    private final int length;

    private int remainingDays;

    private final int id;

    public Sprint(String name, String description, int length, int id) {
        this.name = name;
        this.description = description;
        this.length = length;
        this.remainingDays = length;
        this.id = id;
    }

    public void addUserStory(UserStory us) {
        userStories.add(us);
    }

    public void removeUserStory(UserStory us) {
       userStories.remove(us);
    }

    public List<UserStory> getUserStories() {
        return new ArrayList<>(userStories);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getLength() {
        return length;
    }

    public int getDaysRemaining() {
        return remainingDays;
    }

    public void decrementRemainingDays() {
        if (remainingDays > 0) remainingDays--;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return "Sprint " + this.id + "\n";
    }

    public void clearUserStories() {
        userStories.clear();
    }
}
