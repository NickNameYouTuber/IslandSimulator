package com.nicorp.demo2.animals;

import com.nicorp.demo2.IslandSimulation;
import com.nicorp.demo2.island.Island;
import com.nicorp.demo2.island.Location;

import java.util.Random;

public abstract class Animal {
    protected String name;
    public int age;
    public int maxAge;
    public int hunger;
    public int maxHunger;
    public double reproductionChance;
    protected int x;
    protected int y;

    public Animal(String name, int maxAge, int maxHunger, double reproductionChance) {
        this.name = name;
        this.maxAge = maxAge;
        this.maxHunger = maxHunger;
        this.reproductionChance = reproductionChance;
        this.age = 0;
        this.hunger = 0;
    }

    public void eat() {
        // Реализуется в подклассах
    }

    public void reproduce(Island island) {
        Random random = new Random();
        if (random.nextDouble() < reproductionChance && island.getLocation(x, y).getAnimals().size() < 4 && island.getLocation(x, y).hasAnotherAnimal(this)) {
            System.out.println("Animal " + name + " reproduced");
            Animal child = createChild(name + "_" + name.charAt(name.length() - 1));
            island.getLocation(x, y).addAnimal(child);
        }
    }

    protected abstract Animal createChild(String name);

    public void move(Island island) {
        Random random = new Random();
        int newX = x + random.nextInt(3) - 1;
        int newY = y + random.nextInt(3) - 1;

        if (newX >= 0 && newX < island.getRows() && newY >= 0 && newY < island.getCols()) {
            Location currentLocation = island.getLocation(x, y);
            Location newLocation = island.getLocation(newX, newY);

            if (newLocation.getAnimals().size() < 4) {
                currentLocation.removeAnimal(this);
                newLocation.addAnimal(this);
                setPosition(newX, newY);
            }
        }
    }

    public void age() {
        age++;
    }

    public void increaseHunger() {
        hunger++;
    }

    public boolean isDead() {
        if (age > maxAge) {
            System.out.println("Animal " + name + " is dead of age");
            die();
            return true;
        } else if (hunger > maxHunger) {
            System.out.println("Animal " + name + " is dying of hunger");
            die();
            return true;
        }
        return false;
    }

    public void die() {
        Location location = IslandSimulation.island.getLocation(x, y);
        location.removeAnimal(this);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public Class getAnimalClass() { return this.getClass(); }
}