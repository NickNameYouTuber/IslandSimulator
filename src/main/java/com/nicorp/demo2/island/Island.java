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
    private int totalYears;
    private static int totalRabbitsBorn;
    private static int totalWolvesBorn;
    private static int totalRabbitsKilled;
    private static int totalWolvesKilled;
    private int totalRabbitsStarved;
    private int totalWolvesStarved;

    public Island(int rows, int cols, int grassAmount, int rabbitAmount, int wolfAmount, int sheepAmount) {
        this.rows = rows;
        this.cols = cols;
        this.grassAmount = grassAmount;
        initializeGrid();
        placeEntities(rabbitAmount, wolfAmount, sheepAmount);
    }

    public void initializeGrid() {
        grid = new Location[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Location(i, j);
            }
        }
    }

    public void placeEntities(int rabbitAmount, int wolfAmount, int sheepAmount) {
        JSONObject rabbitConfig = ConfigReader.getAnimalConfig("Rabbit");
        JSONObject wolfConfig = ConfigReader.getAnimalConfig("Wolf");

        for (int i = 0; i < grassAmount; i++) {
            int x = RandomUtils.nextInt(rows);
            int y = RandomUtils.nextInt(cols);
            grid[x][y].setHasGrass(true);
        }

        for (int i = 0; i < rabbitAmount; i++) {
            int x = RandomUtils.nextInt(rows);
            int y = RandomUtils.nextInt(cols);
            grid[x][y].addAnimal(new Rabbit("Rabbit " + i));
        }

        for (int i = 0; i < wolfAmount; i++) {
            int x = RandomUtils.nextInt(rows);
            int y = RandomUtils.nextInt(cols);
            grid[x][y].addAnimal(new Wolf("Wolf " + i));
        }

        for (int i = 0; i < sheepAmount; i++) {
            int x = RandomUtils.nextInt(rows);
            int y = RandomUtils.nextInt(cols);
            grid[x][y].addAnimal(new Sheep("Sheep " + i));
        }
    }

    public void updateGrid() {
        totalYears++;
        updateAnimals();
        moveAnimals();
    }

    private void updateAnimals() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Location location = grid[i][j];
                List<Animal> animals = new ArrayList<>(location.getAnimals());
                for (Animal animal : animals) {
                    animal.eat();
                    animal.reproduce(this);
                    animal.age();
                    animal.increaseHunger();
                    if (animal.isDead()) {
                        location.removeAnimal(animal);
                        if (animal instanceof Rabbit) {
                            totalRabbitsStarved++;
                        } else if (animal instanceof Wolf) {
                            totalWolvesStarved++;
                        }
                    }
                }
            }
        }
    }

    private void moveAnimals() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Location location = grid[i][j];
                List<Animal> animals = new ArrayList<>(location.getAnimals());
                for (Animal animal : animals) {
                    animal.move(this);
                }
            }
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

    public static void incrementAnimalsKilled(Animal animal) {
        if (animal instanceof Rabbit) {
            totalRabbitsKilled++;
        } else if (animal instanceof Wolf) {
            totalWolvesKilled++;
        }
    }

    public static void incrementAnimalsBorn(Animal animal) {
        if (animal instanceof Rabbit) {
            totalRabbitsBorn++;
        } else if (animal instanceof Wolf) {
            totalWolvesBorn++;
        }
    }

    public String getStatistics() {
        return "Total Years: " + totalYears +
                "\nTotal Rabbits Born: " + totalRabbitsBorn +
                "\nTotal Wolves Born: " + totalWolvesBorn +
                "\nTotal Rabbits Killed: " + totalRabbitsKilled +
                "\nTotal Wolves Killed: " + totalWolvesKilled +
                "\nTotal Rabbits Starved: " + totalRabbitsStarved +
                "\nTotal Wolves Starved: " + totalWolvesStarved;
    }
}