package com.groupesan.project.java.scrumsimulator.mainpackage.utils;

import java.security.SecureRandom;

public class RandomUtils {

    private static final SecureRandom random = new SecureRandom();

    private RandomUtils() {
    }

    public static int getRandomInt(int bound) {
        return random.nextInt(bound);
    }

    public static double getRandomDouble() {
        return random.nextDouble();
    }

    public static boolean getRandomBoolean() {
        return random.nextBoolean();
    }
}
