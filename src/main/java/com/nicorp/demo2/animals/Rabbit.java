package com.nicorp.demo2.animals;

import com.nicorp.demo2.IslandSimulation;
import com.nicorp.demo2.island.Location;
import com.nicorp.demo2.config.ConfigReader;
import org.json.JSONObject;

public class Rabbit extends Herbivore {

    public Rabbit(String name) {
        super(name, 10, 5, 5, 0.2);
        JSONObject config = ConfigReader.getAnimalConfig("Rabbit");
        this.weight = config.getInt("weight");
        this.maxHunger = config.getInt("maxHunger");
        this.maxAge = config.getInt("maxAge");
        this.foodForFullSaturation = config.getFloat("foodForFullSaturation");
        this.reproductionChance = config.getFloat("reproductionChance");
    }

    public Rabbit(String name, int maxAge, int maxHunger, float reproductionChance, int weight) {
        super(name, maxAge, maxHunger, weight, reproductionChance);
    }

    @Override
    protected Animal createChild(String name) {
        return new Rabbit(name);
    }
}