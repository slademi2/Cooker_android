package com.example.slada.cooker_android.Objects;

import java.util.ArrayList;

public enum Scale {
    KILO("Kg"), GRAM("g"), LITER("l"), DECIlIER("dl"), MILLILITRE("ml");

    private String show;
    private Scale(String show){
        this.show=show;
    }

    public String description() {
        return show;
    }

    public static ArrayList<String> getAll() {
        ArrayList<String> all=new ArrayList<>();
        all.add(Scale.KILO.description());
        all.add(Scale.GRAM.description());
        all.add(Scale.LITER.description());
        all.add(Scale.DECIlIER.description());
        all.add(Scale.MILLILITRE.description());

        return all;
    }
}
