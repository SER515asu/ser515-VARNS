package com.groupesan.project.java.scrumsimulator.mainpackage.core;

public class BlockerType {

    private String name;
    private int percentChance;

    public BlockerType(String name, int percentChance) {
        this.name = name;
        this.percentChance = percentChance;
    }

    public String getName() {
        return name;
    }

    public int getPercentChance() {
        return percentChance;
    }

    public void setPercentChance(int percentChance) {
        this.percentChance = percentChance;
    }

    public String toString() {
        return "[Blocker] " + name;
    }
}
