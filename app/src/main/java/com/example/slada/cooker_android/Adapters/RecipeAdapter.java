package com.example.slada.cooker_android.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.slada.cooker_android.Activity.MainActivity;
import com.example.slada.cooker_android.Objects.Recipe;
import com.example.slada.cooker_android.R;
import com.example.slada.cooker_android.Activity.ShowRecipeDetail;

import java.util.ArrayList;


/**
 * Abstract class of RecyclerView Adapter used for showing Recipes in recyclerView
 * Implements filterable so there can be implemented searching for specific Recipe under
 * button "Recipes"
 */
public abstract class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder> implements Filterable {

    private ArrayList<Recipe> recipes; //all recipes downloaded from database

    //TODO get rid of this static variable ?
    /**
     * recipes which has been filtered by getFilter();
     * It is static because I need to alter it when implementing getFilter() method when RecipeAdapter instance is made
     */
    public static ArrayList<Recipe> recipesFiltered;
    private Context context;

    /**
     * @param arrayList all recipes which should be shown in ReciclerView
     * @param ctx
     */
    public RecipeAdapter(ArrayList<Recipe> arrayList, Context ctx){
        recipes=arrayList;
        context=ctx;
        recipesFiltered=recipes;
    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_row_item,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        //TODO There is still default image when recipes are shown..
        //TODO need to implement saving picture from camera when recipe is added.
        String image="https://ms2.ostium.cz/instance/web-recepty/jHOY5G3f/h389w574t.jpg";
        //glide
        Glide.with(context)
                .asBitmap()
                .load(image)
                .into(holder.image);

        //recipe info
        holder.name.setText(recipesFiltered.get(position).getmName());
        //Will maybe be used
        /*
            holder.procedure.setText(recipesFiltered.get(position).getmProcedure());
            holder.time.setText(recipesFiltered.get(position).getmTime().toString());
        */
        //The Recipe detail is shown when recipe is tapped
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(context,"clicked: "+position,Toast.LENGTH_LONG).show();
                Recipe recipe=recipesFiltered.get(position);
                MainActivity.setCurrentRecipe(recipe);
                //Show tapped recipe
                Intent intent =new Intent(context,ShowRecipeDetail.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipesFiltered.size();
    }

    /**
     * Has to bee implemented when RecipeAdapter instance is made
     * provides filtering of recipes
     * @return
     */
    @Override
    public abstract Filter getFilter();

    /**
     * Metadata of every line in RecyclerView
     * Contains imageView for image of recipe and TextView for name.
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        LinearLayout parentLayout;
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.Recipe_Row_Name);
            parentLayout=itemView.findViewById(R.id.Recipe_Row_ParrentLayout);
            image=itemView.findViewById(R.id.Recipe_Row_image);
        }
    }
}
