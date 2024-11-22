package com.nicorp.demo2.animals;

import com.nicorp.demo2.IslandSimulation;
import com.nicorp.demo2.island.Location;
import com.nicorp.demo2.config.ConfigReader;
import org.json.JSONObject;

public class Wolf extends Carnivore {

    public Wolf(String name) {
        super(name, 20, 10, 0.1, 10, 1);
        JSONObject config = ConfigReader.getAnimalConfig("Wolf");
        this.killChance = config.getJSONObject("eatProbabilities").getDouble("Rabbit");
        this.weight = config.getInt("weight");
        this.maxHunger = config.getInt("maxHunger");
        this.maxAge = config.getInt("maxAge");
        this.foodForFullSaturation = config.getFloat("foodForFullSaturation");
        this.reproductionChance = config.getFloat("reproductionChance");
    }

    @Override
    protected Animal createChild(String name) {
        return new Wolf(name);
    }
}