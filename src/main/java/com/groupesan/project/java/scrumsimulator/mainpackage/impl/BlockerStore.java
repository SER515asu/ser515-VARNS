package com.groupesan.project.java.scrumsimulator.mainpackage.impl;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.BlockerType;

import java.util.ArrayList;
import java.util.List;

public class BlockerStore {

    private static BlockerStore instance;

    private List<BlockerType> blockers;

    private BlockerStore() {
        blockers = new ArrayList<>();
    }

    public static BlockerStore get() {
        if (instance == null) {
            instance = new BlockerStore();
        }
        return instance;
    }

    public void addBlocker(BlockerType blocker) {
        blockers.add(blocker);
    }

    public List<BlockerType> getBlockers() {
        return blockers;
    }

    public BlockerType getBlocker(String name) {
        for (BlockerType blocker : blockers) {
            if (blocker.getName().equals(name)) {
                return blocker;
            }
        }
        return null;
    }

    public void removeBlocker(BlockerType blocker) {
        blockers.remove(blocker);
    }
    
}
