package com.nicorp.demo2.island;

import com.nicorp.demo2.animals.*;
import com.nicorp.demo2.plants.Grass;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.Random;

public class Island {
    private int rows;
    private int cols;
    private Location[][] grid;
    private int grassAmount;
    private int totalYears;
    private int totalRabbitsBorn;
    private int totalWolvesBorn;
    private static int totalRabbitsKilled;
    private int totalWolvesKilled;
    private int totalRabbitsStarved;
    private int totalWolvesStarved;

    public Island(int rows, int cols, int grassAmount, int rabbitAmount, int wolfAmount) {
        this.rows = rows;
        this.cols = cols;
        this.grassAmount = grassAmount;
        initializeGrid();
        placeEntities(rabbitAmount, wolfAmount);
    }

    public void initializeGrid() {
        grid = new Location[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Location(i, j);
            }
        }
    }

    public void placeEntities(int rabbitAmount, int wolfAmount) {
        Random random = new Random();
        for (int i = 0; i < grassAmount; i++) {
            int x = random.nextInt(rows);
            int y = random.nextInt(cols);
            grid[x][y].setHasGrass(true);
        }

        // Пример размещения животных (например, 10 кроликов и 5 волков)
        for (int i = 0; i < rabbitAmount; i++) {
            int x = random.nextInt(rows);
            int y = random.nextInt(cols);
            grid[x][y].addAnimal(new Rabbit("Rabbit " + i));
        }

        for (int i = 0; i < wolfAmount; i++) {
            int x = random.nextInt(rows);
            int y = random.nextInt(cols);
            grid[x][y].addAnimal(new Wolf("Wolf " + i));
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

    public void incrementRabbitsBorn() {
        totalRabbitsBorn++;
    }

    public void incrementWolvesBorn() {
        totalWolvesBorn++;
    }

    public static void incrementRabbitsKilled() {
        totalRabbitsKilled++;
    }

    public void incrementWolvesKilled() {
        totalWolvesKilled++;
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