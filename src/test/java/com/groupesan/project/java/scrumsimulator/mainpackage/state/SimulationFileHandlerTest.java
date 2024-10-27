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

        Assertions.assertEquals(1, allSimulationData.length());
        Assertions.assertEquals("Test Simulation", simulationData.getString("Name"));
        Assertions.assertEquals(14, simulationData.getInt("SprintDuration"));
        Assertions.assertEquals("f801aa89-8ce8-49fc-9f73-bc297eba610b", simulationData.getString("ID"));
        Assertions.assertEquals(1, simulationData.getInt("NumberOfSprints"));
        Assertions.assertEquals(1, simulationData.getJSONArray("Sprints").length());
    }
}
