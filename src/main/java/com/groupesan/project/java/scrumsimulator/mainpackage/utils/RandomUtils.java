package com.groupesan.project.java.scrumsimulator.mainpackage.utils;

import java.security.SecureRandom;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.Simulation;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationStateManager;

public class RandomUtils {

    private static RandomUtils instance;
    private final SecureRandom random;

    private RandomUtils(long seed) {
        this.random = new SecureRandom();
        this.random.setSeed(seed);
    }

    public static RandomUtils getInstance(long seed) {
        if (instance == null) {
            instance = new RandomUtils(seed);
        }
        return instance;
    }

    public int getRandomInt(int bound) {
        return random.nextInt(bound);
    }

    public int getRandomInt(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        if (min < 0) {
            throw new IllegalArgumentException("min must be greater than 0");
        }

        if (min == max) {
            return min;
        }

        return random.nextInt(max - min) + min;
    }

    public double getRandomDouble() {
        return random.nextDouble();
    }

    public boolean getRandomBoolean() {
        return random.nextBoolean();
    }

    public static long getRandomSeed() {
        return new SecureRandom().nextLong();
    }

    public static RandomUtils getCurrentSeededInstance() {
        Simulation currentSimulation = SimulationStateManager.getInstance().getCurrentSimulation();

        if (currentSimulation == null) {
            System.out.println("Current simulation is not set, using random seed");
            return getInstance(getRandomSeed());
        }

        return getInstance(SimulationStateManager.getInstance().getCurrentSimulation().getRandomSeed());
    }
}
