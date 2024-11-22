package com.nicorp.demo2.animals;

import com.nicorp.demo2.config.ConfigReader;
import org.json.JSONObject;

public class Duck extends Herbivore {

    public Duck(String name) {
        super(name, 10, 5, 5, 0.2);

        JSONObject config = ConfigReader.getAnimalConfig("Duck");
        this.weight = config.getInt("weight");
        this.maxHunger = config.getInt("maxHunger");
        this.maxAge = config.getInt("maxAge");
        this.foodForFullSaturation = config.getFloat("foodForFullSaturation");
        this.reproductionChance = config.getFloat("reproductionChance");
    }

    @Override
    protected Animal createChild(String name) {
        return new Duck(name);
    }
}
