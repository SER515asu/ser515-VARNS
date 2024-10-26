package com.groupesan.project.java.scrumsimulator.mainpackage.core;

public class BlockerSolution {
    private String name;
    private int chance;

    public BlockerSolution(String name, int chance) {
        this.name = name;
        this.chance = chance;
    }

    public String getName() {
        return name;
    }

    public int getChance() {
        return chance;
    }
}
