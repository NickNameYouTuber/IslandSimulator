package com.nicorp.demo2.animals;

import com.nicorp.demo2.IslandSimulation;
import com.nicorp.demo2.island.Island;
import com.nicorp.demo2.island.Location;

public class Rabbit extends Herbivore {
    public Rabbit() {
        super("Rabbit", 10, 5, 0.2);
    }

    public Rabbit(String name) {
        super(name, 10, 5, 0.2);
    }

    @Override
    public void eat() {
        Location location = IslandSimulation.island.getLocation(x, y);
        if (location.hasGrass()) {
            System.out.println("Rabbit " + name + " is eating grass");
            super.eat();
            location.setHasGrass(false);
        }
    }

    @Override
    protected Animal createChild(String name) {
        return new Rabbit(name);
    }
}