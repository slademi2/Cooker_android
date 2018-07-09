package com.example.slada.cooker_android.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slada.cooker_android.Database.DatabaseHelper;
import com.example.slada.cooker_android.Objects.Recipe;
import com.example.slada.cooker_android.R;

public class MainActivity extends AppCompatActivity {

    public TextView textView;
    //url of webServer
    private String serverUrl="http://cooker.sladekmichal.cz";
    //Recipe of which detail is currently shown
    public static Recipe currentRecipe;
    //just providing initialization for databasehelper
    private DatabaseHelper databaseHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        textView=findViewById(R.id.textView);

        //Initializing databaseHelper (db_readable,db_writable);
        databaseHelper=new DatabaseHelper(this);
        databaseHelper.init();
    }

    public static Recipe getCurrentRecipe() {
        return currentRecipe;
    }

    public static void setCurrentRecipe(Recipe currentRecipe) {
        MainActivity.currentRecipe = currentRecipe;
    }



    //Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    //TODO Has too be implemented
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent;
        switch (item.getItemId()) {
            //TODO menu item where we choose recipes by ingredients
            case R.id.menu_item_ingredients:

                break;
            //TODO menu item where we choose recipes by categories like pizza, pastas, pork, etc.
            case R.id.menu_item_categories:

                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Recipes button
     * @param view
     */
    public void showRecipes(View view){
        //Toast.makeText(this,"Recipes-Clicked",Toast.LENGTH_LONG).show();
        Intent intent=new Intent(getApplicationContext(),ShowRecipesActivity.class);
        startActivity(intent);
    }

    /**
     * Add Recipes button
     * @param view
     */
    public void addNewRecipe(View view){
        Toast.makeText(this,"Add Recipe-Clicked",Toast.LENGTH_LONG).show();
        Intent intent =new Intent(getApplicationContext(),AddRecipe.class);
        startActivity(intent);
    }

    /**
     * Ingredients button
     * @param view
     */
    public void showIngredients(View view){
        //Toast.makeText(this,"showIngredients-Clicked",Toast.LENGTH_LONG).show();
        Intent intent =new Intent(getApplicationContext(),ShowIngredientsActivity.class);
        startActivity(intent);
    }

    /**
     * Add ingredient button
     * @param view
     */
    public void addNewIngredient(View view){
        Toast.makeText(this,"Add Ingredient-Clicked",Toast.LENGTH_LONG).show();
        Intent intent =new Intent(getApplicationContext(),AddIngredient.class);
        startActivity(intent);
    }

    /**
     * Only shows recipes now
     * @param view
     */
    public void connectToDb(View view){
        //TODO Will connect to the distant database.
        Toast.makeText(this,"Not supported yet.",Toast.LENGTH_LONG).show();
        Intent intent=new Intent(getApplicationContext(),ShowRecipesActivity.class);
        startActivity(intent);

    }
}
