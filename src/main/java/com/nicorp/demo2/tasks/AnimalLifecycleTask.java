package com.nicorp.demo2.tasks;

import com.nicorp.demo2.IslandSimulation;
import com.nicorp.demo2.animals.Animal;
import com.nicorp.demo2.island.Island;
import com.nicorp.demo2.island.Location;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class AnimalLifecycleTask implements Runnable {
    private final Island island;
    private final IslandSimulation simulation;
    private final ExecutorService animalTaskExecutor;

    public AnimalLifecycleTask(Island island, IslandSimulation simulation, ExecutorService animalTaskExecutor) {
        this.island = island;
        this.simulation = simulation;
        this.animalTaskExecutor = animalTaskExecutor;
    }

    @Override
    public void run() {
        if (!simulation.isSimulationRunning()) {
            return;
        }

        processAnimalLifecycle();
        Platform.runLater(simulation::updateView);

        if (!island.hasAnimals()) {
            simulation.stopSimulation();
            Platform.runLater(simulation::showResultsWindow);
        }
    }

    private void processAnimalLifecycle() {
        Island.totalYears++;
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        // Process each row in parallel
        for (int i = 0; i < island.getRows(); i++) {
            final int row = i;
            CompletableFuture<Void> future = CompletableFuture.runAsync(
                    () -> processRow(row), animalTaskExecutor);
            futures.add(future);
        }

        // Wait for all row processing to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // Process animal movement after all other actions
        moveAnimals();
    }

    private void processRow(int row) {
        for (int col = 0; col < island.getCols(); col++) {
            Location location = island.getLocation(row, col);
            List<Animal> animals = new ArrayList<>(location.getAnimals());

            for (Animal animal : animals) {
                if (simulation.isSimulationRunning()) {
                    animal.eat();
                    animal.reproduce(island);
                    animal.age();
                    animal.increaseHunger();

                    if (animal.isDead()) {
                        location.removeAnimal(animal);
                        Island.incrementStarved(animal);
                    }
                }
            }
        }
    }

    private void moveAnimals() {
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < island.getRows(); i++) {
            for (int j = 0; j < island.getCols(); j++) {
                Location location = island.getLocation(i, j);
                List<Animal> animals = new ArrayList<>(location.getAnimals());

                for (Animal animal : animals) {
                    CompletableFuture<Void> future = CompletableFuture.runAsync(
                            () -> animal.move(island), animalTaskExecutor);
                    futures.add(future);
                }
            }
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }
}
