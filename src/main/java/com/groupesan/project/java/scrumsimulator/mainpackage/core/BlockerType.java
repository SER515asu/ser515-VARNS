package com.groupesan.project.java.scrumsimulator.mainpackage.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BlockerType {

    private String name;
    private int encounterChance;
    private int resolveChance;

    @JsonCreator
    public BlockerType(@JsonProperty("name") String name,
                       @JsonProperty("encounterChance") int encounterChance,
                       @JsonProperty("resolveChance") int resolveChance) {
        this.name = name;
        this.encounterChance = encounterChance;
        this.resolveChance = resolveChance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
