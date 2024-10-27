package com.groupesan.project.java.scrumsimulator.mainpackage.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.groupesan.project.java.scrumsimulator.mainpackage.core.ScrumIdentifier;

public class UserStoryIdentifier extends ScrumIdentifier {
    @JsonCreator
    public UserStoryIdentifier(@JsonProperty("value") int value) {
        super(value);
    }

    public String toString() {
        return "US #" + this.id;
    }
}
