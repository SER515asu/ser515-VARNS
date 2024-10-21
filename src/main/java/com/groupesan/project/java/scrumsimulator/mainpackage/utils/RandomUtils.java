package com.groupesan.project.java.scrumsimulator.mainpackage.utils;

import java.security.SecureRandom;

public class RandomUtils {

    private static final SecureRandom random = new SecureRandom();

    private RandomUtils() {
    }

    public static int getRandomInt(int bound) {
        return random.nextInt(bound);
    }

    public static int getRandomInt(int min, int max) {

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

    public static double getRandomDouble() {
        return random.nextDouble();
    }

    public static boolean getRandomBoolean() {
        return random.nextBoolean();
    }
}
