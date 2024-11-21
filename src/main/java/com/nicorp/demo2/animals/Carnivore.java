package com.nicorp.demo2.animals;

import com.nicorp.demo2.island.Island;
import com.nicorp.demo2.island.Location;
import com.nicorp.demo2.utils.RandomUtils;

public abstract class Carnivore extends Animal {
    protected double killChance;

    public Carnivore(String name, int maxAge, int maxHunger, double reproductionChance, int weight, double killChance) {
        super(name, maxAge, maxHunger, reproductionChance, weight);
        this.killChance = killChance;
    }

    @Override
    public void eat() {
        if (hunger > 0) {
            hunger-=3;
        }
    }

    public void hunt(Location location) {
        for (Animal animal : location.getAnimals()) {
            if (animal instanceof Herbivore && RandomUtils.nextDouble() < killChance) {
                System.out.println("Carnivore " + name + " killed Herbivore " + animal.getName());
                Island.incrementAnimalsKilled(animal);
                location.removeAnimal(animal);
                hunger = 0;
                break;
            }
        }
    }
}