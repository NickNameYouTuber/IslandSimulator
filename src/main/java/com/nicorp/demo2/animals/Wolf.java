package com.nicorp.demo2.animals;

import com.nicorp.demo2.IslandSimulation;
import com.nicorp.demo2.island.Location;
import com.nicorp.demo2.island.Island;

import java.util.Iterator;
import java.util.List;

public class Wolf extends Carnivore {
    public Wolf() {
        super("Wolf", 20, 10, 0.1, 1);
    }

    public Wolf(String name) {
        super(name, 20, 10, 0.1, 1);
    }

    @Override
    public void eat() {
        hunt(IslandSimulation.island.getLocation(x, y));
    }

    @Override
    protected Animal createChild() {
        return new Wolf();
    }
}