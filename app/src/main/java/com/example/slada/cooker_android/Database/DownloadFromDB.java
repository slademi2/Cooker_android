package com.example.slada.cooker_android.Database;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.slada.cooker_android.Objects.IngredientInRecipe;
import com.example.slada.cooker_android.Objects.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


//TODO this class is not currently not used. The app is now working only over local database (DatabaseHelper)
/**
 * Class for downloading Recipes and other stuff from DB on server
 * IT will provide static variables
 * Currently not used
 */
public class DownloadFromDB {
    private static ArrayList<Recipe> recipes=new ArrayList<>();
    private static ArrayList<Recipe> recipesByName=new ArrayList<>();
    private static ArrayList<IngredientInRecipe> ingredientInRecipe =new ArrayList<>();
    private static Context context;
    private static DownloadFromDB instance;
    private static String recipesUrl="http://cooker.sladekmichal.cz/recipes.php";
    private static String ingreduentsInRecipesUrl=" http://cooker.sladekmichal.cz/IngInRec.php";


    private DownloadFromDB (Context ctx){
        context=ctx;
    }


    public static synchronized DownloadFromDB getInstance (Context context){
        if(instance==null){
            instance=new DownloadFromDB(context);
        }
        return instance;
    }

    /**
     * public function for downloading recipes from DB
     */
    public static void downloadRecipes(){
        downloadRecipesFromDB();
        recipesByName.addAll(recipes);
        Collections.sort(recipesByName,new Comparator<Recipe>() {
            @Override
            public int compare(Recipe o1, Recipe o2) {
                return o1.getmName().compareTo(o2.getmName());
            }
        });
    }

    /**
     * public function for downloading ingredients in recipes from DB
     */
    public static void downloadIngredientsInRecipes(){
        downloadIngredientsInRecipesFromDB();
    }


    /**
     * Function for getting downloaded recipes
     * @return ArrayList of recipes
     */
    public static ArrayList<Recipe> getRecipes(){
        if (recipes.isEmpty()){
            downloadRecipesFromDB();
        }
        return recipes;
    }

    public static ArrayList<Recipe> getRecipesByName(){
        return recipesByName;
    }

    /**
     * Inner function which takes care of downloading recipes from DB
     */
    private static void downloadRecipesFromDB() {
        final ArrayList<Recipe>temp=new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, recipesUrl,null ,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("response", response.toString());
                int count = 0;
                while(count<response.length()){
                    try {
                        JSONObject jsonObject = (JSONObject) response.get(count);
                        String name, procedure;
                        int time,id;

                        id=jsonObject.getInt("id");
                        name = jsonObject.getString("name");
                        procedure = jsonObject.getString("procedure");
                        time = jsonObject.getInt("time");

                        Recipe recipe= new Recipe(id,name, procedure, time);
                        recipes.add(recipe);
                        /*addToRecipes(recipe);
                        temp.add(recipe);*/
                        count++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //Toast.makeText(context,"Velikost: " + recipes.size(),Toast.LENGTH_LONG).show();
                //setRecipesList(temp);
                //setRecipesList(recipes);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);

    }

    private static void downloadIngredientsInRecipesFromDB() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, recipesUrl,null ,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("response", response.toString());
                int count = 0;
                while(count<response.length()){
                    try {
                        JSONObject jsonObject = (JSONObject) response.get(count);
                        int id_rec, id_ing;
                        double amount;
                        String scale;

                        id_rec = jsonObject.getInt("id_rec");
                        id_ing = jsonObject.getInt("id_ing");
                        amount = jsonObject.getDouble("amount");
                        scale = jsonObject.getString("scale");

                        IngredientInRecipe tmp= new IngredientInRecipe(id_rec, id_ing, amount,scale);
                        ingredientInRecipe.add(tmp);
                        count++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //Toast.makeText(context,"Velikost: " + recipes.size(),Toast.LENGTH_LONG).show();
                //setRecipesList(temp);
                //setRecipesList(recipes);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);

    }


}
