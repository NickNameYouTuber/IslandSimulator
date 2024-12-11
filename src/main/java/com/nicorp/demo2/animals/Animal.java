package com.nicorp.demo2.animals;

import com.nicorp.demo2.IslandSimulation;
import com.nicorp.demo2.island.Island;
import com.nicorp.demo2.island.Location;
import com.nicorp.demo2.utils.RandomUtils;

import static com.nicorp.demo2.island.Island.incrementBorn;
import static com.nicorp.demo2.island.Island.incrementKilled;

public abstract class Animal {
    protected String name;
    public float age;
    public int maxAge;
    public float hunger;
    public int maxHunger;
    public float weight;
    public float foodForFullSaturation;
    public double reproductionChance;
    public int x;
    public int y;

    public abstract void eat(Island island);

    public Animal(String name, int maxAge, int maxHunger, double reproductionChance, float weight) {
        this.name = name;
        this.maxAge = maxAge;
        this.maxHunger = maxHunger;
        this.reproductionChance = reproductionChance;
        this.age = 0;
        this.hunger = 0;
        this.weight = weight;
    }

    public void eat() {
        // Реализуется в подклассах
    }

    public void reproduce(Island island) {
        if (RandomUtils.nextDouble() < reproductionChance && island.getLocation(x, y).getAnimals().size() < 4 && island.getLocation(x, y).hasAnotherAnimal(this) && hunger <= maxHunger / 2) {
            System.out.println("Animal " + name + " reproduced");
            Animal child = createChild(name + "_" + name.charAt(name.length() - 1));
            incrementBorn(child);
            island.getLocation(x, y).addAnimal(child);
        }
    }

    protected abstract Animal createChild(String name);

    public void move(Island island) {
        int newX = x + RandomUtils.nextInt(3) - 1;
        int newY = y + RandomUtils.nextInt(3) - 1;

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

    public void testMove(Island island) {
        int newX = x + 1;
        int newY = y + 1;
        if (newX >= 0 && newX < island.getRows() && newY >= 0 && newY < island.getCols()) {
            island.getLocation(x, y).removeAnimal(this);
            island.getLocation(newX, newY).addAnimal(this);
            setPosition(newX, newY);
        }
    }

    public void age() {
        age += 1.0f/12.0f;
    }

    public void increaseHunger() {
        hunger+=maxHunger/20.0f;
    }

    public boolean isDead() {
        if (age > maxAge) {
            System.out.println("Animal " + name + " is dead of age");
            die();
            return true;
        }
        if (hunger > maxHunger) {
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
}