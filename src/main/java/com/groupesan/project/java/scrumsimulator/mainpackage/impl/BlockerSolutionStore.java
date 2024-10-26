package com.groupesan.project.java.scrumsimulator.mainpackage.impl;

import java.util.ArrayList;
import java.util.List;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.BlockerSolution;
import com.groupesan.project.java.scrumsimulator.mainpackage.utils.RandomUtils;

public class BlockerSolutionStore {

    private static BlockerSolutionStore instance;

    private List<BlockerSolution> blockerSolutions = new ArrayList<>(List.of(
            new BlockerSolution("Solution 1", 100),
            new BlockerSolution("Solution 2", 100),
            new BlockerSolution("Solution 3", 100),
            new BlockerSolution("Solution 4", 100),
            new BlockerSolution("Solution 5", 100)));

    private BlockerSolutionStore() {
    }

    public static synchronized BlockerSolutionStore getInstance() {
        if (instance == null) {
            instance = new BlockerSolutionStore();
        }
        return instance;
    }

    public BlockerSolution getRandomBlockerSolution() {
        int totalWeight = blockerSolutions.stream().mapToInt(BlockerSolution::getChance).sum();
        int randomValue = RandomUtils.getCurrentSeededInstance().getRandomInt(totalWeight);

        int cumulativeWeight = 0;
        for (BlockerSolution solution : blockerSolutions) {
            cumulativeWeight += solution.getChance();
            if (randomValue < cumulativeWeight) {
                return solution;
            }
        }
        return null;
    }

    public List<BlockerSolution> getBlockerSolutions() {
        return blockerSolutions;
    }

    public BlockerSolution getBlockerSolution(String name) {
        for (BlockerSolution solution : blockerSolutions) {
            if (solution.getName().equals(name)) {
                return solution;
            }
        }
        return null;
    }

    public void addBlockerSolution(BlockerSolution solution) {
        blockerSolutions.add(solution);
    }

    public void removeBlockerSolution(BlockerSolution solution) {
        blockerSolutions.remove(solution);
    }
}
