package com.groupesan.project.java.scrumsimulator.mainpackage.state;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SimulationFileHandlerTest {

    @Test
    public void testLoadingFile() {
        JSONArray allSimulationData = SimulationFileHandler.getSimulationData();
        JSONObject simulationData = allSimulationData.getJSONObject(0);

        Assertions.assertEquals(1, allSimulationData.length(), "Only 1 simulation should be present");
        Assertions.assertEquals("Test Simulation", simulationData.getString("Name"), "Name should be Test Simulation but was %s".formatted(simulationData.getString("Name")));
        Assertions.assertEquals("f801aa89-8ce8-49fc-9f73-bc297eba610b", simulationData.getString("ID"), "ID should be f801aa89-8ce8-49fc-9f73-bc297eba610b but was %s".formatted(simulationData.getString("ID")));
        Assertions.assertEquals(1, simulationData.getJSONArray("Sprints").length(), "Only 1 Sprint should be present");
    }
}
