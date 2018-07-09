package com.example.slada.cooker_android.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.slada.cooker_android.Database.DatabaseHelper;
import com.example.slada.cooker_android.R;

/**
 * Is triggered by button AddIngredient
 */
public class AddIngredient extends AppCompatActivity {
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);
    }

    // buttons activity
    public void addIngredient (View view){
        EditText textName=findViewById(R.id.Add_Ingredient_Name);
        name=textName.getText().toString();

        if (name.isEmpty()){
            Toast.makeText(this,"Can not add ingredient without name",Toast.LENGTH_LONG).show();
        }else {
            if (DatabaseHelper.insertIngredientData(name) == false) {
                Toast.makeText(this, "Can not add ingredient", Toast.LENGTH_LONG).show();
            }
        }
        finish();
    }
}
