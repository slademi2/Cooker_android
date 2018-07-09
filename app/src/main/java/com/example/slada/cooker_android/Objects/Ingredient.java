package com.example.slada.cooker_android.Objects;


import android.support.annotation.NonNull;

/**
 * Object representing Ingredient.
 * The number and names of variables can change!!
 */
public class Ingredient implements Comparable{
    private String mName;
    private int id;
    private double amount;
    private String scale;

    public Ingredient(int id,String name){
        this.id=id;
        this.mName = name;

    }

    public Ingredient( int id, String mName, double amount, String scale) {
        this.mName = mName;
        this.id = id;
        this.amount = amount;
        this.scale = scale;
    }

    public String getName() {
        return mName;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getScale() {
        return scale;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Ingredient sec=(Ingredient)o;
        return Integer.compare(this.id,sec.id);
    }
}
