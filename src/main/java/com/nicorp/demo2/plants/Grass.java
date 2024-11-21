package com.nicorp.demo2.plants;

public class Grass {
    private int quantity;
    private int growthRate;

    public Grass(int quantity, int growthRate) {
        this.quantity = quantity;
        this.growthRate = growthRate;
    }

    public void grow() {
        // Рост травы
        quantity += growthRate;
    }

    public void consume(int amount) {
        // Поедание травы
        quantity -= amount;
        if (quantity < 0) quantity = 0;
    }

    public int getQuantity() {
        return quantity;
    }
}