package com.groupesan.project.java.scrumsimulator.mainpackage.core;

import com.groupesan.project.java.scrumsimulator.mainpackage.utils.RandomUtils;

public class BlockerObject {

    private BlockerType type;

    private enum BlockerState {
        UNRESOLVED,
        RESOLVED
    }

    private BlockerState state;

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
        if (RandomUtils.getRandomInt(100) < type.getResolveChance()) {
            return true;
        } else {
            return false;
        }
    }

    public void resolve() {
        state = BlockerState.RESOLVED;
    }

    public boolean isResolved() {
        return state == BlockerState.RESOLVED;
    }
}
