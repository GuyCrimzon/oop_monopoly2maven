package project.services;

import java.util.Random;

public class Randomizer {
    public static int randomInt(int min, int max, int step) {
        Random r = new Random();
        return r.nextInt((max - min) / step) * step + min;
    }
}
