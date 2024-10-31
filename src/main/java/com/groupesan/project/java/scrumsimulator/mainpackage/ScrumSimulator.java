package com.groupesan.project.java.scrumsimulator.mainpackage;

import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationSingleton;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.App;

public class ScrumSimulator {
    public static void main(String[] args) {
        new App().start();

        Runtime.getRuntime()
                .addShutdownHook(new Thread(() -> SimulationSingleton.getInstance().saveSimulationDetails()));
    }
}
