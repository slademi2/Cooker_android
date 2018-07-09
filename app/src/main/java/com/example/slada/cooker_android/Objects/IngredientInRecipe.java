package com.example.slada.cooker_android.Objects;

/**
 * The instances of this class represent relation between ingredients and recipes.
 */
public class IngredientInRecipe {
    private int id_rec;
    private int id_ing;
    private double amount;
    private String scale;

    public IngredientInRecipe(int id_rec, int id_ing, double amount, String scale) {
        this.id_rec = id_rec;
        this.id_ing = id_ing;
        this.amount = amount;
        this.scale = scale;
    }

    public int getId_rec() {
        return id_rec;
    }

    public int getId_ing() {
        return id_ing;
    }

    public double getAmount() {
        return amount;
    }

    public String getScale() {
        return scale;
    }
}
