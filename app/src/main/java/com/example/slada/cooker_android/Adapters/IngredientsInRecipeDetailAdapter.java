package com.example.slada.cooker_android.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


import com.example.slada.cooker_android.Objects.Ingredient;
import com.example.slada.cooker_android.R;

//TODO get rid of this class using abstract class IngredientAdapter if its possible
/**
 * adapter for showing ingredients in detail of recipe
 */
public class IngredientsInRecipeDetailAdapter extends RecyclerView.Adapter<IngredientsInRecipeDetailAdapter.MyViewHolder> {
        ArrayList<Ingredient> ingredients;

        public IngredientsInRecipeDetailAdapter(ArrayList<Ingredient> arrayList){
            ingredients=arrayList;
        }

        @Override
        public IngredientsInRecipeDetailAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_in_recipe_row_item,parent,false);
            IngredientsInRecipeDetailAdapter.MyViewHolder myViewHolder=new IngredientsInRecipeDetailAdapter.MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(IngredientsInRecipeDetailAdapter.MyViewHolder holder, int position) {
            holder.name.setText(ingredients.get(position).getName());
            holder.scale.setText(ingredients.get(position).getScale());
            holder.amount.setText(Double.toString(ingredients.get(position).getAmount()));
        }

        @Override
        public int getItemCount() {
            return ingredients.size();
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder{
            TextView name;
            TextView amount;
            TextView scale;

            public MyViewHolder(View itemView) {
                super(itemView);
                name=(TextView)itemView.findViewById(R.id.ingredient_in_recipe_name);
                amount=(TextView)itemView.findViewById(R.id.ingredient_in_recipe_amount);
                scale=(TextView)itemView.findViewById(R.id.ingredient_in_recipe_scale);
            }
        }
    }

