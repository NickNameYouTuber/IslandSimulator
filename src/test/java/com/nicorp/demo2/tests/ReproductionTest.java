package com.nicorp.demo2.tests;

import com.nicorp.demo2.animals.Animal;
import com.nicorp.demo2.animals.Rabbit;
import com.nicorp.demo2.island.Island;
import com.nicorp.demo2.island.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReproductionTest {

    private Island island;
    private Animal animal;
    private Location location;

    @BeforeEach
    void setUp() {
        island = new Island(10, 10, 100, Map.of("Rabbit", 2));
        animal = new Rabbit("Rabbit 1");
        location = island.getLocation(0, 0);
        location.addAnimal(animal);
        location.addAnimal(new Rabbit("Rabbit 2"));
    }

    @Test
    void testReproduce() {
        int initialSize = location.getAnimals().size();
        animal.reproduce(island);
        assertTrue(location.getAnimals().size() >= initialSize);
    }
}