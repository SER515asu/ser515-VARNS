package com.groupesan.project.java.scrumsimulator.mainpackage.impl;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.BlockerType;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels.EditBlockerProbabilities;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * The following test class uses a new library AssertJ Swing.
 * Usage references from their official documentation here:
 * <a href="https://joel-costigliola.github.io/assertj/assertj-swing-basics.html">...</a>
 */
public class BlockerTypeStoreTest {

    private List<BlockerType> blockers;

    private FrameFixture window;

    @BeforeAll
    public static void setUpOnce() {
        System.setProperty("java.awt.headless", "false");
        FailOnThreadViolationRepaintManager.install();
    }

    @BeforeEach
    public void setup() {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Skipping GUI test in headless mode.");
            return;
        }
        blockers = BlockerTypeStore.get().getBlockerTypes();
        BlockerType blocker = blockers.getFirst();
        blocker.setName("TestBlocker");
        blocker.setEncounterChance(25);
        blocker.setResolveChance(60);

        EditBlockerProbabilities editBlockerProbabilities =
                GuiActionRunner.execute(() ->
                        new EditBlockerProbabilities(blocker.getName(), blocker.getEncounterChance(), blocker.getResolveChance()));

        window = new FrameFixture(editBlockerProbabilities);
        window.show();
    }

    @Test
    public void editBlockerPropertiesWithForm() {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Skipping GUI test in headless mode.");
            return;
        }

        BlockerType blocker = blockers.getFirst();
        assertEquals(window.textBox("nameField").text(), blocker.getName());
        assertEquals(window.textBox("encounterChanceField").text(), String.valueOf(blocker.getEncounterChance()));
        assertEquals(window.textBox("resolveChanceField").text(), String.valueOf(blocker.getResolveChance()));

        window.textBox("nameField").enterText("New Blocker Name");
        window.textBox("encounterChanceField").enterText("75");
        window.textBox("resolveChanceField").enterText("90");

        window.button("saveButton").click();

        assertEquals("New Blocker Name", blocker.getName());
        assertEquals(75, blocker.getEncounterChance());
        assertEquals(90, blocker.getResolveChance());
    }

    @Test
    public void editBlockerPropertiesWithFormBlocksBadData() {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Skipping GUI test in headless mode.");
            return;
        }

        BlockerType blocker = blockers.getFirst();

        window.textBox("nameField").enterText("Test");
        window.textBox("encounterChanceField").enterText("75test");

        window.button("saveButton").click();

        assertEquals("TestBlocker", blocker.getName());
        assertEquals(25, blocker.getEncounterChance());
        assertEquals(60, blocker.getResolveChance());
    }


    @AfterEach
    protected void onTearDown() {
        if (window != null) {
            window.cleanUp();
        }
    }
}
