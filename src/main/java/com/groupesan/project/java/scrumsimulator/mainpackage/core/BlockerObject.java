package com.groupesan.project.java.scrumsimulator.mainpackage.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager;
import com.groupesan.project.java.scrumsimulator.mainpackage.utils.RandomUtils;

public class BlockerObject {

    private final BlockerType type;

    public enum BlockerState {
        UNRESOLVED,
        SPIKED,
        RESOLVED
    }

    private BlockerState state;

    private BlockerSolution solution;

    public BlockerObject(BlockerType type) {
        this.type = type;
        this.state = BlockerState.UNRESOLVED;
    }

    @JsonCreator
    public BlockerObject(@JsonProperty("solution") BlockerSolution solution,
                         @JsonProperty("type") BlockerType type,
                         @JsonProperty("state") BlockerState state) {
        this.solution = solution;
        this.type = type;
        this.state = state;
    }

    public BlockerType getType() {
        return type;
    }

    public String toString() {
        return "[Blocker] " + type.toString();
    }

    public boolean attemptResolve() {
        int resolveChance = type.getResolveChance();

        if (state == BlockerState.SPIKED) {
            resolveChance /= 2;
        }

        if (RandomUtils.getInstance().getRandomInt(100) < resolveChance) {
            this.solution = SimulationStateManager.getInstance().getRandomBlockerSolution();
            return true;
        } else {

            if (RandomUtils.getInstance().getRandomInt(100) < type.getSpikeChance()) {
                System.out.println("Blocker " + this + " has been spiked.");
                state = BlockerState.SPIKED;
            }

            return false;
        }
    }

    public void resolve() {
        this.state = BlockerState.RESOLVED;
    }

    @JsonIgnore
    public boolean isResolved() {
        return state == BlockerState.RESOLVED;
    }

    public BlockerSolution getSolution() {
        return solution;
    }

    public BlockerState getState() {
        return state;
    }
}
