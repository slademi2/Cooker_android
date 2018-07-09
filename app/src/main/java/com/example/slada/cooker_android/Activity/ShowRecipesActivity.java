package com.example.slada.cooker_android.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Filter;

import com.example.slada.cooker_android.Adapters.RecipeAdapter;
import com.example.slada.cooker_android.Database.DatabaseHelper;
import com.example.slada.cooker_android.Objects.Recipe;
import com.example.slada.cooker_android.R;

import java.util.ArrayList;

public class ShowRecipesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecipeAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Recipe> recipes;
    Context context;

    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipes_recycler);

        //toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.show_recipe_recyclerView_Toolbar);
        setSupportActionBar(myToolbar);

        context=this.getApplicationContext();
        recyclerView=findViewById(R.id.show_recipe_recyclerView);
        layoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        //TODO Download from database somewhere on server
        /*
            recipes=DownloadFromDB.getInstance(context).getRecipes();
            Toast.makeText(context,"Velikost: " + recipes.size(),Toast.LENGTH_LONG).show();
        */
        recipes=new ArrayList<>();
        recipes=DatabaseHelper.getAllRecipes();

        adapter = new RecipeAdapter(DatabaseHelper.getAllRecipes(), context) {
            /**
             * Filtering by using DatabaseHelper.
             * @return
             */
            @Override
            public Filter getFilter() {
                return new Filter() {
                    @Override
                    protected FilterResults performFiltering(CharSequence charSequence) {
                        String charString = charSequence.toString();
                        if(charString.isEmpty()){
                            recipesFiltered=recipes;
                        }else {
                            recipesFiltered=DatabaseHelper.searchRecipesByName(charString);
                        }
                        FilterResults filterResults=new FilterResults();
                        filterResults.values=recipesFiltered;
                        return filterResults;
                    }

                    @Override
                    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                        recipesFiltered=(ArrayList<Recipe>)filterResults.values;
                        notifyDataSetChanged();
                    }
                };
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);


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

}
