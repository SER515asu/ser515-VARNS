package com.groupesan.project.java.scrumsimulator.mainpackage.state;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SimulationFileHandler {
    private static File getSimulationJsonFile() {
        URL resource = SimulationFileHandler.class.getClassLoader().getResource("simulation.JSON");
        if (resource == null) {
            throw new IllegalArgumentException("simulation.JSON not found in resources");
        }
        return new File(resource.getFile());
    }

    public static JSONArray getSimulationData() {
        try (FileInputStream fis = new FileInputStream(getSimulationJsonFile())) {
            JSONTokener tokener = new JSONTokener(fis);
            return new JSONArray(tokener);
        } catch (IOException | JSONException e) {
            return new JSONArray();
        }
    }

    public static void updateSimulationData(JSONArray updatedData) {
        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(getSimulationJsonFile()), StandardCharsets.UTF_8)) {
            writer.write(updatedData.toString(4));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error writing to simulation.JSON");
        }
    }
}
