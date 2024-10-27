package com.groupesan.project.java.scrumsimulator.mainpackage.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.groupesan.project.java.scrumsimulator.mainpackage.core.BlockerObject;
import com.groupesan.project.java.scrumsimulator.mainpackage.core.Player;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.UserStoryState;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.UserStoryUnselectedState;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserStory {
    @JsonProperty
    private UUID id;

    @JsonIgnore
    private String label;

    @JsonProperty
    private String name;

    @JsonProperty
    private String description;

    @JsonProperty
    private double pointValue;

    @JsonIgnore
    private UserStoryState state;

    @JsonIgnore
    private Player owner;

    @JsonProperty
    private int businessValuePoint;

    @JsonProperty
    private List<BlockerObject> blockers = new ArrayList<>();

    // private ArrayList<Task> tasks; TODO: implement tasks

    /**
     * Creates a user story. Leaves the description as an empty string.
     *
     * @param name       the name for the user story
     * @param pointValue the point value for the story as a way of estimating
     *                   required effort.
     */
    public UserStory(String name, double pointValue, int businessValuePoint) {
        this.name = name;
        this.description = "";
        this.pointValue = pointValue;
        this.businessValuePoint = businessValuePoint; // added buisness value point
        this.state = new UserStoryUnselectedState(this);
        // added buisness value point to the constructor
    }

    /**
     * Creates a user story.
     *
     * @param name        the name for the user story
     * @param description the description for the user story for better
     *                    understanding of the
     *                    requirements.
     * @param pointValue  the point value for the story as a way of estimating
     *                    required effort.
     */
    public UserStory(String name, String description, double pointValue, int businessValuePoint) {
        this.name = name;
        this.description = description;
        this.pointValue = pointValue;
        this.businessValuePoint = businessValuePoint; // added buisness value point
        this.state = new UserStoryUnselectedState(this);
        this.id = UUID.randomUUID();
    }

    @JsonCreator
    public UserStory(@JsonProperty("businessValuePoint") int businessValuePoint,
                     @JsonProperty("pointValue") int pointValue,
                     @JsonProperty("name") String name,
                     @JsonProperty("description") String description,
                     @JsonProperty("blockers") List<BlockerObject> blockers,
                     @JsonProperty("id") UUID id) {
        this.businessValuePoint = businessValuePoint;
        this.pointValue = pointValue;
        this.name = name;
        this.description = description;
        this.blockers = blockers;
        this.id = id;
    }

    /**
     * Gets the identifier for this UserStory.
     *
     * @return The UUID for this user story
     */
    public UUID getId() {
        return id;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    @JsonIgnore
    public String getLabel() {
        return label;
    }

    /**
     * Get the name for this UserStory
     *
     * @return the name of this UserStory as a string
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the User Story to the specified string
     *
     * @param name the string to set the name to
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the description text of this UserStory
     *
     * @return the description of this UserStory as a string.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the Description of the User Story to the specified string
     *
     * @param description the string to set the description to
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the point value of this UserStory
     *
     * @return the point value of this UserStory as a double
     */
    public double getPointValue() {
        return pointValue;
    }

    /**
     * Set the point value of the User Story to the specified value
     *
     * @param pointValue the point value as a double. Usually an element of the
     *                   fibonacci sequence.
     */
    public void setPointValue(double pointValue) {
        this.pointValue = pointValue;
    }

    /**
     * returns this user story's ID and name as text in the following format: US #3
     * - foo
     *
     * @return a string of the following format: "US #3 - foo"
     */
    @Override
    public String toString() {
        return "US - " + name;
    }

    // State Management, need Player class to implement final selection logic
    /**
     * Change the state of this UserStory. Usually called when a Player picks up the
     * task or
     * finishes it.
     *
     * @param state the new UserStoryState for this UserStory
     */
    public void changeState(UserStoryState state) {
        this.state = state;
    }

    /**
     * Get the UserStoryState of this UserStory. Unselected, completed, etc.
     *
     * @return a UserStoryState object containing the state for this UserStory
     */
    @JsonIgnore
    public UserStoryState getUserStoryState() {
        return state;
    }

    /**
     * Sets the owner of this UserStory to the specified player. This should be
     * called whenever a
     * Player picks up this task and assigns themselves to it.
     *
     * @param player the Player object who is assigned to this UserStory
     */
    public void setOwner(Player player) {
        this.owner = player;
    }

    /**
     * Get the owner of this UserStory
     *
     * @return a Player object representing the owner of this UserStory
     */
    public Player getOwner() {
        return this.owner;
    }

    // added buisness value point getter and setter
    public int getBusinessValuePoint() {
        return businessValuePoint;
    }

    public void setBusinessValuePoint(int businessValuePoint) {
        this.businessValuePoint = businessValuePoint;
    }

    @JsonIgnore
    public boolean isBlocked() {
        for (BlockerObject blocker : blockers) {
            if (!blocker.isResolved()) {
                return true;
            }
        }

        return false;
    }

    // using this to add blocker to the user story
    public void setBlocker(BlockerObject blocker) {
        blockers.add(blocker);
    }

    public List<BlockerObject> getBlockers() {
        return blockers;
    }

    public void resolveBlockers() {
        if (blockers.size() == 0) {
            return;
        }

        for (BlockerObject blocker : blockers) {

            if(blocker.isResolved()) {
                continue;
            }

            if (blocker.attemptResolve()) {
                blocker.resolve();
                System.out.println("Blocker resolved: " + blocker.getType().getName());
            }
        }
    }
}
