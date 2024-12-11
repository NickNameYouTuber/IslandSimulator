package com.nicorp.demo2.animals;

import com.nicorp.demo2.IslandSimulation;
import com.nicorp.demo2.island.Island;
import com.nicorp.demo2.island.Location;
import com.nicorp.demo2.utils.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class Carnivore extends Animal {
    protected double killChance;

    public Carnivore(String name, int maxAge, int maxHunger, double reproductionChance, float weight, double killChance) {
        super(name, maxAge, maxHunger, reproductionChance, weight);
        this.killChance = killChance;
    }

    @Override
    public void eat(Island island) {
        hunt(island.getLocation(x, y));
    }

    public void hunt(Location location) {
        if (location == null) return;

        List<Animal> animals = new ArrayList<>(location.getAnimals()); // Create a copy to avoid concurrent modification
        for (Animal animal : animals) {
            if (animal instanceof Herbivore && RandomUtils.nextDouble() < killChance) {
                System.out.println("Carnivore " + name + " killed Herbivore " + animal.getName());
                Island.incrementKilled(animal);
                location.removeAnimal(animal);
                hunger -= animal.weight;
                break;
            }
        }
    }
}