package com.nicorp.demo2.island;

import com.nicorp.demo2.animals.*;
import com.nicorp.demo2.plants.Grass;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Location {
    private int x;
    private int y;
    private CopyOnWriteArrayList<Animal> animals;
    private boolean hasGrass;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
        this.animals = new CopyOnWriteArrayList<>();
        this.hasGrass = false;
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
        animal.setPosition(x, y);
    }

    public void removeAnimal(Animal animal) {
        animals.remove(animal);
    }

    public void setHasGrass(boolean hasGrass) {
        this.hasGrass = hasGrass;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public boolean hasGrass() {
        return hasGrass;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean hasAnotherAnimal(Animal animal) {
        for (Animal otherAnimal : animals) {
            if (otherAnimal.getClass() == animal.getClass() && otherAnimal != animal) {
                return true;
            }
        }
        return false;
    }
}