package com.groupesan.project.java.scrumsimulator.mainpackage.impl;

import java.util.ArrayList;
import java.util.List;

public class UserStoryStore {

    private static UserStoryStore userStoryStore;

    /**
     * returns the shared instance of the UserStoryStore which contains all user stories in the
     * system.
     *
     * @return UserStory Singleton instance
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

    public void removeUserStory(UserStory userStory) {
        userStories.remove(userStory);
    }

    public List<UserStory> getUserStories() {
        return new ArrayList<>(userStories);
    }

    public List<UserStory> initializeUserStories() {
        UserStory a = UserStoryFactory.getInstance()
                .createNewUserStory("predefinedUS1", "description1", 1.0, 1);

        UserStory b = UserStoryFactory.getInstance()
                .createNewUserStory("predefinedUS2", "description2", 2.0, 8);

        UserStory c = UserStoryFactory.getInstance()
                .createNewUserStory("predefinedUS3", "description3", 3.0, 13);

        return new ArrayList<>(List.of(a, b, c));
    }
}
