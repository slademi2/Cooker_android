package com.example.slada.cooker_android.Objects;

/**
 * Represent recipes, the variables and its name can change in future.
 */
public class Recipe {

    private int id;
    private String mName;
    private String mProcedure;
    private int mTime;

    public Recipe(int id,String Name, String Procedure, Integer Time) {
        this.id=id;
        this.mName = Name;
        this.mProcedure = Procedure;
        this.mTime = Time;
    }


    public static int compareByID(Recipe r1, Recipe r2){
        return r1.getId() > r2.getId() ? +1 : r1.getId() < r2.getId() ? -1 : 0;
    }

    public static int compareByName(Recipe r1,Recipe r2){
        return r1.getmName().compareTo(r2.getmName());
    }

    public int getId() {
        return id;
    }

    public String getmName() {
        return mName;
    }


    public String getmProcedure() {
        return mProcedure;
    }


    public Integer getmTime() {
        return mTime;
    }

}
