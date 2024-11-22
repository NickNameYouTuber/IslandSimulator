package com.nicorp.demo2.island;

import com.nicorp.demo2.animals.*;
import com.nicorp.demo2.config.ConfigReader;
import com.nicorp.demo2.utils.RandomUtils;
import org.json.JSONObject;

import java.util.*;

public class Island {
    private int rows;
    private int cols;
    private Location[][] grid;
    private int grassAmount;
    public static int totalYears;

    // Use maps to store statistics for all animal types
    private static Map<String, Integer> totalBorn = new HashMap<>();
    private static Map<String, Integer> totalKilled = new HashMap<>();
    private static Map<String, Integer> totalStarved = new HashMap<>();

    public Island(int rows, int cols, int grassAmount, Map<String, Integer> animalAmounts) {
        this.rows = rows;
        this.cols = cols;
        this.grassAmount = grassAmount;
        initializeGrid();
        placeEntities(animalAmounts);
    }

    public void initializeGrid() {
        grid = new Location[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Location(i, j);
            }
        }
    }

    public void placeEntities(Map<String, Integer> animalAmounts) {
        for (int i = 0; i < grassAmount; i++) {
            int x = RandomUtils.nextInt(rows);
            int y = RandomUtils.nextInt(cols);
            grid[x][y].setHasGrass(true);
        }

        for (Map.Entry<String, Integer> entry : animalAmounts.entrySet()) {
            String animalType = entry.getKey();
            int amount = entry.getValue();

            for (int i = 0; i < amount; i++) {
                int x = RandomUtils.nextInt(rows);
                int y = RandomUtils.nextInt(cols);
                Animal animal = createAnimal(animalType, animalType + " " + i);
                grid[x][y].addAnimal(animal);

                // Initialize counts for new animal types
                totalBorn.putIfAbsent(animalType, 0);
                totalKilled.putIfAbsent(animalType, 0);
                totalStarved.putIfAbsent(animalType, 0);
            }
        }
    }

    private Animal createAnimal(String animalType, String name) {
        switch (animalType) {
            case "Rabbit":
                return new Rabbit(name);
            case "Wolf":
                return new Wolf(name);
            case "Sheep":
                return new Sheep(name);
            case "Boa":
                return new Boa(name);
            case "Fox":
                return new Fox(name);
            case "Bear":
                return new Bear(name);
            case "Eagle":
                return new Eagle(name);
            case "Horse":
                return new Horse(name);
            case "Deer":
                return new Deer(name);
            case "Mouse":
                return new Mouse(name);
            case "Goat":
                return new Goat(name);
            case "Boar":
                return new Boar(name);
            case "Buffalo":
                return new Buffalo(name);
            case "Duck":
                return new Duck(name);
            case "Caterpillar":
                return new Caterpillar(name);
            default:
                throw new IllegalArgumentException("Unknown animal type: " + animalType);
        }
    }

    public Location getLocation(int x, int y) {
        return grid[x][y];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public boolean hasAnimals() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!grid[i][j].getAnimals().isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    // Method to increment born count
    public static void incrementBorn(Animal animal) {
        String type = animal.getClass().getSimpleName();
        totalBorn.put(type, totalBorn.get(type) + 1);
    }

    // Method to increment killed count
    public static void incrementKilled(Animal animal) {
        String type = animal.getClass().getSimpleName();
        totalKilled.put(type, totalKilled.get(type) + 1);
    }

    // Method to increment starved count
    public static void incrementStarved(Animal animal) {
        String type = animal.getClass().getSimpleName();
        totalStarved.put(type, totalStarved.get(type) + 1);
    }

    public String getStatistics() {
        StringBuilder stats = new StringBuilder();
        stats.append("Total Years: ").append(totalYears).append("\n");

        stats.append("Total Born:\n");
        for (Map.Entry<String, Integer> entry : totalBorn.entrySet()) {
            stats.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        stats.append("Total Killed:\n");
        for (Map.Entry<String, Integer> entry : totalKilled.entrySet()) {
            stats.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        stats.append("Total Starved:\n");
        for (Map.Entry<String, Integer> entry : totalStarved.entrySet()) {
            stats.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        return stats.toString();
    }
}