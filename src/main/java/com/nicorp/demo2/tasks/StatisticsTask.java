package com.nicorp.demo2.tasks;

import com.nicorp.demo2.animals.Animal;
import com.nicorp.demo2.island.Island;
import com.nicorp.demo2.island.Location;

import java.util.HashMap;
import java.util.Map;

public class StatisticsTask implements Runnable {
    private final Island island;

    public StatisticsTask(Island island) {
        this.island = island;
    }

    @Override
    public void run() {
        Map<String, Integer> animalCounts = new HashMap<>();

        for (int i = 0; i < island.getRows(); i++) {
            for (int j = 0; j < island.getCols(); j++) {
                Location location = island.getLocation(i, j);
                for (Animal animal : location.getAnimals()) {
                    String type = animal.getClass().getSimpleName();
                    animalCounts.merge(type, 1, Integer::sum);
                }
            }
        }

        // Print statistics
        System.out.println("\nCurrent Island Statistics:");
        System.out.println("-------------------------");
        animalCounts.forEach((type, count) ->
                System.out.printf("%s: %d%n", type, count));
        System.out.println("-------------------------\n");
    }
}