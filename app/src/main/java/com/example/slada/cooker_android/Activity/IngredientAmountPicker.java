package com.example.slada.cooker_android.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slada.cooker_android.Adapters.IngredientInRecipeAddAdapter;
import com.example.slada.cooker_android.Objects.Ingredient;
import com.example.slada.cooker_android.Objects.Scale;
import com.example.slada.cooker_android.R;

import java.util.ArrayList;

/**
 * dialog for picking amount of ingredients
 */
public class IngredientAmountPicker extends DialogFragment implements AdapterView.OnItemSelectedListener {
    private Ingredient ingredient;
    Context context;
    String scale;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        ingredient= IngredientInRecipeAddAdapter.getCurrentIngredient();
        final View view=inflater.inflate(R.layout.ingredient_amount_picker,null);
        TextView name=view.findViewById(R.id.ingredient_amount_picker_Name);

        context=this.getActivity();
        //spiner for scale choose
        ArrayList<String> scales = Scale.getAll();
        Spinner spinner=view.findViewById(R.id.ingredient_amount_picker_spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> spinner_adapter=new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,scales);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);

        name.setText(ingredient.getName());
        builder.setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Toast.makeText(getActivity(),"clicked: yes",Toast.LENGTH_LONG).show();
                        IngredientInRecipeAddAdapter.holder.parrentLayout.setBackgroundColor(Color.GREEN);
                        TextView amount=(TextView)view.findViewById(R.id.ingredient_amount_picker_Amount);

                        Ingredient ing= IngredientInRecipeAddAdapter.getCurrentIngredient();
                        ing.setAmount(Double.parseDouble(amount.getText().toString()));
                        ing.setScale(scale);
                        IngredientInRecipeAddAdapter.ingredientsToAdd.add(ing);
                        Toast.makeText(getActivity(),"ing: "+ing.getAmount(),Toast.LENGTH_LONG).show();

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        IngredientAmountPicker.this.getDialog().cancel();
                        Ingredient ing= IngredientInRecipeAddAdapter.getCurrentIngredient();
                        IngredientInRecipeAddAdapter.holder.parrentLayout.setBackgroundColor(Color.WHITE);
                        IngredientInRecipeAddAdapter.ingredientsToAdd.remove(ing);
                    }
                });



        return builder.create();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        scale=parent.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        scale=parent.getItemAtPosition(0).toString();
    }
}
