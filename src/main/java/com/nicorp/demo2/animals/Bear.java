package com.nicorp.demo2.animals;

import com.nicorp.demo2.config.ConfigReader;
import org.json.JSONObject;

public class Bear extends Carnivore {

    public Bear(String name) {
        super(name, 10, 5, 5, 0.2f, 1.0);

        JSONObject config = ConfigReader.getAnimalConfig("Bear");
        this.weight = config.getInt("weight");
        this.maxHunger = config.getInt("maxHunger");
        this.maxAge = config.getInt("maxAge");
        this.foodForFullSaturation = config.getFloat("foodForFullSaturation");
        this.reproductionChance = config.getFloat("reproductionChance");
    }

    @Override
    protected Animal createChild(String name) {
        return new Bear(name);
    }
}
