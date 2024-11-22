package com.nicorp.demo2.config;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;

public class ConfigReader {
    private static JSONObject config;

    static {
        try (InputStream is = ConfigReader.class.getResourceAsStream("/config.json")) {
            JSONTokener tokener = new JSONTokener(is);
            config = new JSONObject(tokener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject getAnimalConfig(String animalName) {
        return config.getJSONObject("animals").getJSONObject(animalName);
    }
}