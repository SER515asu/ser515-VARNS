package com.groupesan.project.java.scrumsimulator.mainpackage.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.ScrumIdentifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserStoryTest {
    private UserStory myUserStory;

    @BeforeEach
    public void setup() {
        myUserStory = UserStoryFactory.getInstance()
                .createNewUserStory("predefinedUS1", "description1", 1.0, 1);
    }

    @Test
    public void testUserStoryRegistered() {
        ScrumIdentifier id = myUserStory.getId();

        assertNotNull(id);
    }
}
