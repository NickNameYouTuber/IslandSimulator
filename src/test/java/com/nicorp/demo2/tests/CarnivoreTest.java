package com.nicorp.demo2.tests;

import com.nicorp.demo2.animals.Carnivore;
import com.nicorp.demo2.animals.Rabbit;
import com.nicorp.demo2.animals.Wolf;
import com.nicorp.demo2.island.Island;
import com.nicorp.demo2.island.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CarnivoreTest {

    private Island island;
    private Carnivore carnivore;
    private Location location;

    @BeforeEach
    void setUp() {
        island = new Island(10, 10, 100, Map.of("Wolf", 1, "Rabbit", 1));
        carnivore = new Wolf("Wolf 1", 10, 10, 1, 10, 1);
        location = island.getLocation(0, 0);
        location.addAnimal(carnivore);
        location.addAnimal(new Rabbit("Rabbit 1", 10, 10, 1, 10));
    }

    @Test
    void testHunt() {
        carnivore.eat(island);
        assertEquals(1, location.getAnimals().size());
    }
}