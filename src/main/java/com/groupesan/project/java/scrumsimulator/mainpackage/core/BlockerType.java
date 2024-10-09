package com.groupesan.project.java.scrumsimulator.mainpackage.core;

public class BlockerType {

    private String name;
    private int encounterChance;
    private int resolveChance;

    public BlockerType(String name, int encounterChance, int resolveChance) {
        this.name = name;
        this.encounterChance = encounterChance;
        this.resolveChance = resolveChance;
    }

    public String getName() {
        return name;
    }

    public int getEncounterChance() {
        return encounterChance;
    }

    public void setEncounterChance(int encounterChance) {
        this.encounterChance = encounterChance;
    }

    public int getResolveChance() {
        return resolveChance;
    }

    public void setResolveChance(int resolveChance) {
        this.resolveChance = resolveChance;
    }

    public String toString() {
        return "[Blocker] " + name;
    }
}
