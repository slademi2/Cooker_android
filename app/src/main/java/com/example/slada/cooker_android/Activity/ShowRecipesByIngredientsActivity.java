package com.example.slada.cooker_android.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Filter;

import com.example.slada.cooker_android.Adapters.RecipeAdapter;
import com.example.slada.cooker_android.Objects.Recipe;
import com.example.slada.cooker_android.R;

import java.util.ArrayList;

public class ShowRecipesByIngredientsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Recipe>recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipes_found_by_ingredients);

        recyclerView=findViewById(R.id.showRecipesByIngredients_recyclerView);
        layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recipes=ShowIngredientsActivity.foundRecipes;


        //TODO filtering for recipes chosen by ingredient
        adapter= new RecipeAdapter(recipes, this) {
            @Override
            public Filter getFilter() {
                return null;
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }
}
