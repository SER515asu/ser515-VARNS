package com.groupesan.project.java.scrumsimulator.mainpackage.core;

import com.groupesan.project.java.scrumsimulator.mainpackage.impl.BlockerSolutionStore;
import com.groupesan.project.java.scrumsimulator.mainpackage.utils.RandomUtils;

public class BlockerObject {

    private BlockerType type;

    private enum BlockerState {
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
            this.solution = BlockerSolutionStore.getInstance().getRandomBlockerSolution();
            return true;
        } else {

            if (RandomUtils.getInstance().getRandomInt(100) < type.getSpikeChance()) {
                System.out.println("Blocker " + this.toString() + " has been spiked.");
                state = BlockerState.SPIKED;
            }

            return false;
        }
    }

    public void resolve() {
        state = BlockerState.RESOLVED;
    }

    public boolean isResolved() {
        return state == BlockerState.RESOLVED;
    }

    public BlockerSolution getSolution() {
        return solution;
    }
}
