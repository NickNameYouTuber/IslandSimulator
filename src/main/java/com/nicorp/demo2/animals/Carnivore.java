package com.nicorp.demo2.animals;

import com.nicorp.demo2.IslandSimulation;
import com.nicorp.demo2.island.Island;
import com.nicorp.demo2.island.Location;

import java.util.List;

import java.util.List;
import java.util.Random;

import java.util.Random;

public abstract class Carnivore extends Animal {
    protected double killChance;

    public Carnivore(String name, int maxAge, int maxHunger, double reproductionChance, double killChance) {
        super(name, maxAge, maxHunger, reproductionChance);
        this.killChance = killChance;
    }

    @Override
    public void eat() {
        if (hunger > 0) {
            hunger--;
        }
    }

    public void hunt(Location location) {
        Random random = new Random();
        for (Animal animal : location.getAnimals()) {
            if (animal instanceof Herbivore && random.nextDouble() < killChance) {
                System.out.println("Carnivore " + name + " killed Herbivore " + animal.getName());
                Island.incrementRabbitsKilled();
                location.removeAnimal(animal);
                hunger = 0;
                break;
            }
        }
    }
}