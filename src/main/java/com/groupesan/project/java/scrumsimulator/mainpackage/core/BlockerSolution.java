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

    public void setName(String name) {
        this.name = name;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }
}
