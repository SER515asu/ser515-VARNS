package com.groupesan.project.java.scrumsimulator.mainpackage.impl;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.BlockerType;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels.EditBlockerProbabilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels.EditBlockerProbabilities.getEncounterResolveProbabilities;
import static org.junit.jupiter.api.Assertions.*;


public class BlockerTypeStoreTest {

    private List<BlockerType> blockers;

    @BeforeEach
    public void setup() {
        blockers = BlockerTypeStore.get().getBlockerTypes();
        BlockerType blocker = blockers.getFirst();
        blocker.setName("TestBlocker");
        blocker.setEncounterChance(25);
        blocker.setResolveChance(60);
    }

    @Test
    public void editBlockerProperties() {
        BlockerType blocker = blockers.getFirst();
        assertEquals("TestBlocker", blocker.getName());
        assertEquals(25, blocker.getEncounterChance());
        assertEquals(60, blocker.getResolveChance());

        JTextField encounterChanceField = new JTextField("75");
        JTextField resolveChanceField = new JTextField("90");

        EditBlockerProbabilities.EncounterResolveProbabilities result = getEncounterResolveProbabilities(encounterChanceField, resolveChanceField);

        assertNotNull(result);
        assertEquals(75, result.newEncounterChance());
        assertEquals(90, result.newResolveChance());
    }

    @Test
    public void editBlockerPropertiesWithBadEncounterData() {
        JTextField encounterChanceField = new JTextField("75test");
        JTextField resolveChanceField = new JTextField("90");
        EditBlockerProbabilities.EncounterResolveProbabilities result = null;

        try {
            result = getEncounterResolveProbabilities(encounterChanceField, resolveChanceField);
        } catch (HeadlessException e) {
            // Expected error
        }
        assertNull(result);
    }

    @Test
    public void editBlockerPropertiesWithBadResolveData() {
        JTextField encounterChanceField = new JTextField("75");
        JTextField resolveChanceField = new JTextField("90test");
        EditBlockerProbabilities.EncounterResolveProbabilities result = null;

        try {
            result = getEncounterResolveProbabilities(encounterChanceField, resolveChanceField);
        } catch (HeadlessException e) {
            // Expected error
        }
        assertNull(result);
    }

    @Test
    public void editBlockerPropertiesWithBadEncounterRange() {
        JTextField encounterChanceField = new JTextField("150");
        JTextField resolveChanceField = new JTextField("90");
        EditBlockerProbabilities.EncounterResolveProbabilities result = null;

        try {
            result = getEncounterResolveProbabilities(encounterChanceField, resolveChanceField);
        } catch (HeadlessException e) {
            // Expected error
        }
        assertNull(result);
    }

    @Test
    public void editBlockerPropertiesWithBadResolveRange() {
        JTextField encounterChanceField = new JTextField("50");
        JTextField resolveChanceField = new JTextField("-1");
        EditBlockerProbabilities.EncounterResolveProbabilities result = null;

        try {
            result = getEncounterResolveProbabilities(encounterChanceField, resolveChanceField);
        } catch (HeadlessException e) {
            // Expected error
        }

        assertNull(result);
    }
}
