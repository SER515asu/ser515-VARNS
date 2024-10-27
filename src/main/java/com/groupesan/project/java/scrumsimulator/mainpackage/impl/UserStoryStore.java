package com.groupesan.project.java.scrumsimulator.mainpackage.impl;

import java.util.ArrayList;
import java.util.List;

public class UserStoryStore {

    private static UserStoryStore userStoryStore;

    /**
     * returns the shared instance of the UserStoryStore which contains all user stories in the
     * system.
     *
     * @return
     */
    public static UserStoryStore getInstance() {
        if (userStoryStore == null) {
            userStoryStore = new UserStoryStore();
        }
        return userStoryStore;
    }

    private final List<UserStory> userStories;

    private UserStoryStore() {
        userStories = new ArrayList<>();
    }

    public void addUserStory(UserStory userStory) {
        userStory.setLabel("US #%d".formatted(userStories.size()));
        userStories.add(userStory);
    }

    public void addAllUserStories(List<UserStory> userStories) {
       userStories.forEach(this::addUserStory);
    }

    public void removeUserStory(UserStory userStory) {
        userStories.remove(userStory);
    }

    public List<UserStory> getUserStories() {
        return new ArrayList<>(userStories);
    }
}
