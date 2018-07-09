package com.example.slada.cooker_android.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.slada.cooker_android.Adapters.IngredientsInRecipeDetailAdapter;
import com.example.slada.cooker_android.Database.DatabaseHelper;
import com.example.slada.cooker_android.Objects.Ingredient;
import com.example.slada.cooker_android.Objects.Recipe;
import com.example.slada.cooker_android.R;

import java.util.ArrayList;


/**
 * for showing detail of clicked Recipe
 */
public class ShowRecipeDetail extends AppCompatActivity {
    private Recipe recipe;
    private ArrayList<Ingredient> ingredients;
    private TextView name;
    private TextView procedure;
    private TextView time;


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipe_detail);
        //Toast.makeText(getApplicationContext(),"stavajici: " +MainActivity.getCurrentRecipe().getId(),Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),"wow: " +recipe.getmName()+"pocet ing:"+ databaseHelper.getIngredientsFromRecipe(recipe.getId()).size(),Toast.LENGTH_LONG).show();
        recipe=MainActivity.getCurrentRecipe();
        ingredients=DatabaseHelper.getIngredientsFromRecipe(recipe.getId());
       // Toast.makeText(getApplicationContext(),"wow: " +recipe.getmName()+"pocet ing:"+ databaseHelper.getIngredientsFromRecipe(recipe.getId()).size(),Toast.LENGTH_LONG).show();


        name=findViewById(R.id.show_recipe_detail_name);
        procedure=findViewById(R.id.show_recipe_detail_procedure);
        time=findViewById(R.id.show_recipe_detail_time);

        name.setText(recipe.getmName());
        procedure.setText(recipe.getmProcedure());
        time.setText(recipe.getmTime().toString());


        recyclerView=findViewById(R.id.show_recipe_detail_recycler_view);
        layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);


        adapter = new IngredientsInRecipeDetailAdapter(ingredients);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);


    }
}
