package com.nicorp.demo2.tests;

import com.nicorp.demo2.animals.Animal;
import com.nicorp.demo2.animals.Rabbit;
import com.nicorp.demo2.island.Island;
import com.nicorp.demo2.island.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {

    private Island island;
    private Animal animal;
    private Location initialLocation;

    @BeforeEach
    void setUp() {
        island = new Island(10, 10, 100, Map.of("Rabbit", 1));
        animal = new Rabbit("Rabbit 1");
        initialLocation = island.getLocation(0, 0);
        initialLocation.addAnimal(animal);
    }

    @Test
    void testMove() {
        int initialX = animal.x;
        int initialY = animal.y;
        animal.testMove(island);
        assertNotEquals(initialX, animal.x);
        assertNotEquals(initialY, animal.y);
    }
}