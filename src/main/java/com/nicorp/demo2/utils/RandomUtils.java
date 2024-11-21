package com.nicorp.demo2.utils;

import java.util.Random;

public class RandomUtils {
    private static final Random random = new Random();

    public static boolean roll(int probability) {
        return random.nextInt(100) < probability;
    }

    public static int randomInt(int max) {
        return random.nextInt(max);
    }
}
