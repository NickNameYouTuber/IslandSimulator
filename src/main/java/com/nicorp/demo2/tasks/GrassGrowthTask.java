package com.nicorp.demo2.tasks;

import com.nicorp.demo2.IslandSimulation;
import com.nicorp.demo2.island.Island;
import javafx.application.Platform;

import java.util.Random;

public class GrassGrowthTask implements Runnable {
    private final Island island;
    private final IslandSimulation simulation;
    private final Random random = new Random();
    private static final int GRASS_GROWTH_PER_CYCLE = 10;

    public GrassGrowthTask(Island island, IslandSimulation simulation) {
        this.island = island;
        this.simulation = simulation;
    }

    @Override
    public void run() {
        if (!simulation.isSimulationRunning()) {
            return;
        }

        for (int i = 0; i < GRASS_GROWTH_PER_CYCLE; i++) {
            int x = random.nextInt(island.getRows());
            int y = random.nextInt(island.getCols());
            island.getLocation(x, y).setHasGrass(true);
        }

        Platform.runLater(simulation::updateView);
    }
}