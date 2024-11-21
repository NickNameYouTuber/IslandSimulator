package com.nicorp.demo2.animals;

import com.nicorp.demo2.IslandSimulation;
import com.nicorp.demo2.island.Location;
import com.nicorp.demo2.plants.Grass;

public abstract class Herbivore extends Animal {
    public Herbivore(String name, int maxAge, int maxHunger, int weight, double reproductionChance) {
        super(name, maxAge, maxHunger, reproductionChance, weight);
    }

    @Override
    public void eat() {
        if (hunger > 0) {
            hunger-=2;
        }
    }
}