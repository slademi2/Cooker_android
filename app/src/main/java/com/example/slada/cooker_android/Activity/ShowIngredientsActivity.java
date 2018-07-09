package com.example.slada.cooker_android.Activity;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.slada.cooker_android.Adapters.IngredientAdapter;
import com.example.slada.cooker_android.Database.DatabaseHelper;
import com.example.slada.cooker_android.Objects.Ingredient;
import com.example.slada.cooker_android.Objects.Recipe;
import com.example.slada.cooker_android.R;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * Triggered by ShowIngredients button
 */
public class ShowIngredientsActivity extends AppCompatActivity {
    private static ArrayList<Ingredient> chosenIngredients=new ArrayList<>();
    private static Set<Ingredient>chosenIngredientsSet;
    public static ArrayList<Recipe> foundRecipes;

    private RecyclerView recyclerView;
    private IngredientAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private RecyclerView recyclerView_chosen;
    public static IngredientAdapter adapter_chosen;
    private RecyclerView.LayoutManager layoutManager_chosen;

    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ingredients);

        //variables
        chosenIngredients=new ArrayList<>();
        chosenIngredientsSet=new TreeSet<>();
        foundRecipes=new ArrayList<>();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.show_ingredient_Toolbar);
        setSupportActionBar(myToolbar);

        recyclerView=findViewById(R.id.showIngredients_recyclerView);
        layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new IngredientAdapter( this) {
            @Override
            public void onBindViewHolder(final MyViewHolder hold, final int position) {
                hold.name.setText(ingredientsFiltered.get(position).getName());
                hold.parrentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int size=ShowIngredientsActivity.chosenIngredientsSet.size();
                        ShowIngredientsActivity.chosenIngredientsSet.add(ingredientsFiltered.get(position));
                        if (size!=ShowIngredientsActivity.chosenIngredientsSet.size()){
                            //Toast.makeText(context,"adding ingredient",Toast.LENGTH_LONG).show();
                            ShowIngredientsActivity.chosenIngredients.add(ingredientsFiltered.get(position));
                            adapter_chosen.update();
                            ShowIngredientsActivity.adapter_chosen.notifyDataSetChanged();
                        }else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Ingredient already chosen");
                            builder.setMessage("This ingredient is already chosen")
                                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //Toast.makeText(context,"clicked: yes",Toast.LENGTH_LONG).show();
                                            //ShowIngredientsActivity.chosenIngredients.remove(position);
                                            //notifyItemRemoved(position);
                                        }
                                    });
                            // Create the AlertDialog object and return it
                            AlertDialog dialog=builder.create();
                            dialog.show();
                        }
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        recyclerView_chosen=findViewById(R.id.showIngredients_chosenRecyclerView);
        layoutManager_chosen=new LinearLayoutManager(getApplicationContext());
        recyclerView_chosen.setLayoutManager(layoutManager_chosen);

        adapter_chosen = new IngredientAdapter(chosenIngredients, this) {
            @Override
            public void onBindViewHolder(final MyViewHolder hold,final int position) {
                hold.name.setText(chosenIngredients.get(hold.getAdapterPosition()).getName());
            }
        };

        recyclerView_chosen.setAdapter(adapter_chosen);
        recyclerView_chosen.setHasFixedSize(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show_recipes_menu, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    public void findRecipesByIngredients (View view){
        //empty
        if (chosenIngredientsSet.size()==0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("You didn't choose ingredients");
            builder.setMessage("You didn't choose ingredients")
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog dialog=builder.create();
            dialog.show();
        }

        ShowIngredientsActivity.foundRecipes=DatabaseHelper.searchRecipesByIngredients(chosenIngredients);
        Intent intent =new Intent(getApplicationContext(),ShowRecipesByIngredientsActivity.class);
        startActivity(intent);
    }
}
