package com.example.slada.cooker_android.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slada.cooker_android.Database.DatabaseHelper;
import com.example.slada.cooker_android.Objects.Ingredient;
import com.example.slada.cooker_android.R;

import java.util.ArrayList;


/**
 * Abstract class of RecyclerView Adapter used for showing ingredients in recyclerView
 * Implements filterable so there can be implemented searching specific ingredient under
 * button "Ingredients"
 */
public abstract class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.MyViewHolder> implements Filterable {
    private ArrayList<Ingredient> ingredients; //All of ingredients which should be shown
    //TODO get rid of this static variable ? same issue as in RecipeAdapter
    public static ArrayList<Ingredient> ingredientsFiltered;
    private ArrayList<Ingredient> ingredientsToAdd=new ArrayList<>(); //ingredients to add into recipe
    public static Context context;
    //is this class used for basic use or for chosen ingredients?
    private boolean basic;
    private int size;

    public IngredientAdapter(Context ctx) {
        basic=true;
        //ingredientsToAdd.clear();
        ingredients = DatabaseHelper.getAllIngredients();
        context = ctx;
        ingredientsFiltered = new ArrayList<Ingredient>(ingredients);
    }

    /**
     * for adapter which shows only chosen ingredients
     * @param ing
     * @param ctx
     */
    public IngredientAdapter(ArrayList<Ingredient> ing, Context ctx){
        basic=false;
        ingredients=new ArrayList<Ingredient>(ing);
        context=ctx;
        Toast.makeText(context, "size4: " + ingredients.size(), Toast.LENGTH_LONG).show();
        size=0;
    }


    @Override
    public IngredientAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_row_item,parent,false);
        IngredientAdapter.MyViewHolder myViewHolder=new IngredientAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public abstract void onBindViewHolder(final IngredientAdapter.MyViewHolder hold, final int position);


    public void update(){
        size++;
    }

    @Override
    public int getItemCount() {
        if (basic==true) {
            return ingredientsFiltered.size();
        }else {
            return size;
        }

    }

    /**
     * Filtering performed by using DatabaseHelper class
     * @return
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString=charSequence.toString();
                if(charString.isEmpty()){
                    ingredientsFiltered=ingredients;
                }else {
                    //Searching ingredients by name using SQL query
                    ingredientsFiltered=DatabaseHelper.searchIngredientByName(charString);
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=ingredientsFiltered;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                ingredientsFiltered=(ArrayList<Ingredient>)filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public LinearLayout parrentLayout;


        public MyViewHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.Ingredient_Row_Name);
            parrentLayout=itemView.findViewById(R.id.Ingredient_Row_Parrent_layout);
        }
    }
}
