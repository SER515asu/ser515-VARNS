package com.groupesan.project.java.scrumsimulator.mainpackage.impl;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.BlockerObject;
import com.groupesan.project.java.scrumsimulator.mainpackage.core.BlockerType;
import com.groupesan.project.java.scrumsimulator.mainpackage.utils.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class BlockerTypeStore {

    private static BlockerTypeStore instance;

    private List<BlockerType> blockers;

    private BlockerTypeStore() {
        blockers = new ArrayList<>(
                List.of(
                        new BlockerType("Dependency", 10, 50),
                        new BlockerType("Bug", 20, 80),
                        new BlockerType("Unknown", 10, 90)));
    }

    public static BlockerTypeStore get() {
        if (instance == null) {
            instance = new BlockerTypeStore();
        }
        return instance;
    }

    public void addBlockerType(BlockerType blocker) {
        blockers.add(blocker);
    }

    public List<BlockerType> getBlockerTypes() {
        return blockers;
    }

    public BlockerType getBlockerType(String name) {
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

    /*
     * Rolls for a blocker to be added to the sprint. The chance of each blocker is
     * determined by the percentChance field of the BlockerType.
     */
    public BlockerObject rollForBlocker() {
        
        for (BlockerType blocker : blockers) {
            int roll = RandomUtils.getInstance().getRandomInt(100);

            System.out.println("Roll: " + roll + " Blocker Chance: " + blocker.getEncounterChance());
            
            if (roll < blocker.getEncounterChance()) {
                return new BlockerObject(blocker);
            }
        }

        return null;
    }
}
