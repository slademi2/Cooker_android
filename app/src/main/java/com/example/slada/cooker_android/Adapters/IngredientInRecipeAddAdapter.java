package com.example.slada.cooker_android.Adapters;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.slada.cooker_android.Activity.IngredientAmountPicker;
import com.example.slada.cooker_android.Objects.Ingredient;
import com.example.slada.cooker_android.R;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;


//TODO get rid of this class using abstract class IngredientAdapter if its possible
/**
 * adapter for showing ingredients in new Recipes
 */
public class IngredientInRecipeAddAdapter extends RecyclerView.Adapter<IngredientInRecipeAddAdapter.MyViewHolder> implements Filterable {
    ArrayList<Ingredient> ingredients;
    ArrayList<Ingredient> ingredientsFiltered;

    public static Set<Ingredient> ingredientsToAdd=new TreeSet<>();
    private static Ingredient currentIngredient;
    public static IngredientInRecipeAddAdapter.MyViewHolder holder;

    Context context;

    public IngredientInRecipeAddAdapter(ArrayList<Ingredient> arrayList, Context ctx){
        ingredientsToAdd.clear();
        ingredients=arrayList;
        context=ctx;
        ingredientsFiltered=ingredients;
    }

    public static Ingredient getCurrentIngredient() {
        return currentIngredient;
    }

    public static void setCurrentIngredient(Ingredient currentIngredient) {
        IngredientInRecipeAddAdapter.currentIngredient = currentIngredient;
    }



    @Override
    public IngredientInRecipeAddAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_row_item,parent,false);
        IngredientInRecipeAddAdapter.MyViewHolder myViewHolder=new IngredientInRecipeAddAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final IngredientInRecipeAddAdapter.MyViewHolder hold, final int position) {

        hold.name.setText(ingredients.get(position).getName());
        hold.parrentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(context,"clicked: "+position,Toast.LENGTH_LONG).show();
                DialogFragment ingredientAmountPicker = new IngredientAmountPicker();
                setCurrentIngredient(ingredients.get(position));
                FragmentManager manager=((AppCompatActivity) context).getFragmentManager();
                holder=hold;
                ingredientAmountPicker.show(manager, "Pick");
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                return null;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

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
