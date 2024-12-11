package com.nicorp.demo2.tests;

import com.nicorp.demo2.animals.Herbivore;
import com.nicorp.demo2.animals.Rabbit;
import com.nicorp.demo2.island.Island;
import com.nicorp.demo2.island.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HerbivoreTest {

    private Island island;
    private Herbivore herbivore;
    private Location location;

    @BeforeEach
    void setUp() {
        island = new Island(10, 10, 100, Map.of("Rabbit", 1));
        herbivore = new Rabbit("Rabbit 1");
        location = island.getLocation(0, 0);
        location.setHasGrass(true);
        location.addAnimal(herbivore);
    }

    @Test
    void testEat() {
        herbivore.eat(island);
        assertFalse(location.hasGrass());
    }
}