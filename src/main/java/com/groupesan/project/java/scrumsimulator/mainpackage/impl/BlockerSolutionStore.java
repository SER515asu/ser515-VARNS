package com.groupesan.project.java.scrumsimulator.mainpackage.impl;

import java.util.List;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.BlockerSolution;
import com.groupesan.project.java.scrumsimulator.mainpackage.utils.RandomUtils;

public class BlockerSolutionStore {

    private static final List<BlockerSolution> blockerSolutions = List.of(
            new BlockerSolution("Solution 1", 100),
            new BlockerSolution("Solution 2", 100),
            new BlockerSolution("Solution 3", 100),
            new BlockerSolution("Solution 4", 100),
            new BlockerSolution("Solution 5", 100));

    private BlockerSolutionStore() {
    }

    public static BlockerSolution getRandomBlockerSolution() {
        int totalWeight = blockerSolutions.stream().mapToInt(BlockerSolution::getChance).sum();
        int randomValue = RandomUtils.getRandomInt(totalWeight);

        int cumulativeWeight = 0;
        for (BlockerSolution solution : blockerSolutions) {
            cumulativeWeight += solution.getChance();
            if (randomValue < cumulativeWeight) {
                return solution;
            }
        }
        return null;
    }

    public static List<BlockerSolution> getBlockerSolutions() {
        return blockerSolutions;
    }

    public static BlockerSolution getBlockerSolution(String name) {
        for (BlockerSolution solution : blockerSolutions) {
            if (solution.getName().equals(name)) {
                return solution;
            }
        }
        return null;
    }

    public static void addBlockerSolution(BlockerSolution solution) {
        blockerSolutions.add(solution);
    }

    public static void removeBlockerSolution(BlockerSolution solution) {
        blockerSolutions.remove(solution);
    }
}
