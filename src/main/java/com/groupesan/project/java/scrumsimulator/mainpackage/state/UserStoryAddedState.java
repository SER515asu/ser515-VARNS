package com.groupesan.project.java.scrumsimulator.mainpackage.state;

import com.groupesan.project.java.scrumsimulator.mainpackage.impl.UserStory;

public class UserStoryAddedState extends UserStoryState {
    public UserStoryAddedState(UserStory userStory) {
        super(userStory);
    }

    @Override
    public String onSelect() {
        return "Completed";
    }

    @Override
    public String onComplete() {
        return "Completed";
    }

    @Override
    public String onDelete() {
        userStory.changeState(new UserStoryDeletedState(userStory));
        return "Deleted";
    }
}