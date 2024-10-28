package com.groupesan.project.java.scrumsimulator.mainpackage.impl;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.Simulation;
import com.groupesan.project.java.scrumsimulator.mainpackage.core.Teacher;

public class SimulationFactory {
    private static SimulationFactory simulationFactory;

    public static SimulationFactory geSimulationFactory() {
        if (simulationFactory == null) {
            simulationFactory = new SimulationFactory();
        }

        return simulationFactory;
    }

    private SimulationFactory() {}

    public Simulation createNewSimulation(String name, Teacher teacher, int sprintDuration, long randomSeed) {
        Simulation newSimulation = new Simulation(name, 0, sprintDuration, randomSeed);
        newSimulation.setTeacher(teacher);
        return newSimulation;
    }
}
