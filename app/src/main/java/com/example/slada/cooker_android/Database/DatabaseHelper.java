package com.example.slada.cooker_android.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.slada.cooker_android.Objects.Ingredient;
import com.example.slada.cooker_android.Objects.Recipe;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * The class which handles all action with internal database.
 * It shall have static methods to control the db (all instances of this class would be the same)
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="cooker.db";
    private static final String RECIPES_TABLE_NAME="recipes";
    private static final String RECIPES_ID="id";
    private static final String RECIPES_NAME="NAME";
    private static final String RECIPES_PROCEDURE="PROCEDURE";
    private static final String RECIPES_TIME="TIME";

    private static final String INGREDIENT_TABLE_NAME="ingredients";
    private static final String INGREDIENT_ID="id";
    private static final String INGREDIENT_NAME="NAME";

    private static final String RECIPE_INGREDIENT_TABLE_NAME="IngredientsInRecipes";
    private static final String RECIPE_INGREDIENT_RECIPE_ID="id_rec";
    private static final String RECIPE_INGREDIENT_INGREDIENT_ID="id_ing";

    private static SQLiteDatabase db_writable;
    private static SQLiteDatabase db_readable;


    private static Context context;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context=context;
    }

    public void init(){
        db_writable=this.getWritableDatabase();
        db_readable=this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE VIRTUAL TABLE recipe_search USING fts4 (id_rec, name)" );

        sqLiteDatabase.execSQL("CREATE VIRTUAL TABLE ingredient_search USING fts4 (id_ing, name)" );


        sqLiteDatabase.execSQL("create table "+ RECIPES_TABLE_NAME
                + " (id_rec INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,procedure TEXT,time INTEGER)" );

        sqLiteDatabase.execSQL("create table "+ INGREDIENT_TABLE_NAME
                + " (id_ing INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT)" );

        //creating recipes to ingredient (many to many) table
        sqLiteDatabase.execSQL("create table "+ RECIPE_INGREDIENT_TABLE_NAME
        + " (id_rec INTEGER,id_ing INTEGER,amount REAL,scale TEXT)" );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+INGREDIENT_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+RECIPES_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+RECIPE_INGREDIENT_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


    /**
     * Insert into DB Table for ingredients which are connected to recipe
      * @param id_rec
     * @param id_ing
     * @param amount
     * @param scale
     * @return
     */
    public static int insertIngredientsInRecipeData(int id_rec,int id_ing, double amount, String scale){
        //IngredientsInRecipes
        SQLiteDatabase db = db_writable;
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_rec", id_rec);
        contentValues.put("id_ing", id_ing);
        contentValues.put("amount", amount);
        contentValues.put("scale", scale);

        long result = db.insert("IngredientsInRecipes", null, contentValues);
        if (result == -1) {
            Toast.makeText(context,"insertIngredients: NotOK: ",Toast.LENGTH_LONG).show();
            return -1;
        }
        Toast.makeText(context,"insertIngredients: OK: ",Toast.LENGTH_LONG).show();
        return 1;
    }

    /**
     * function for inserting new recipe to table recipes
     * @param name
     * @param procedure
     * @param time
     * @return
     */
    public static int insertRecipeData(String name,String procedure,int time) {
        //checkTables();
        SQLiteDatabase db = db_writable;
        ContentValues contentValues = new ContentValues();
        contentValues.put(RECIPES_NAME, name);
        contentValues.put(RECIPES_PROCEDURE, procedure);
        contentValues.put(RECIPES_TIME, time);
        long result = db.insert(RECIPES_TABLE_NAME, null, contentValues);
        if (result == -1) {
            return -1;
        }

        SQLiteDatabase dat = db_readable;
        Cursor cursor = dat.rawQuery("select * from recipes WHERE id_rec=(SELECT MAX(id_rec) from recipes)", null);

       if(!cursor.moveToNext()){
          // Toast.makeText(context,"db: NotOK: "+cursor.getCount(),Toast.LENGTH_LONG).show();
           return -1;
       }

        String dbName;
        dbName = cursor.getString(cursor.getColumnIndex("name"));

        if (!name.equals(dbName)){
            return -1;
        }

        int id = cursor.getInt(0);
        ContentValues val=new ContentValues();
        val.put("id_rec",id);
        val.put("name",name);
        result=db.insert("recipe_search",null,val);
        if (result==-1){
            Toast.makeText(context,"couldn't insert into search table ",Toast.LENGTH_LONG).show();
            return -1;
        }
        return id;
    }


    /**
     * function for listing all recipes in table
     * @return
     */
    public static ArrayList<Recipe> getAllRecipes (){

        SQLiteDatabase db=db_writable;
        Cursor cursor=db.rawQuery("select * from "+RECIPES_TABLE_NAME,null);

        String name;
        String procedure;
        int time,id;
        ArrayList<Recipe> recipes=new ArrayList<>();
        while(cursor.moveToNext()){
            id=cursor.getInt(0);
            name=cursor.getString(1);
            procedure=cursor.getString(2);
            time=cursor.getInt(3);
            //Toast.makeText(mContext, m+": "+lon +" "+lat, Toast.LENGTH_LONG).show();

            recipes.add(new Recipe(id,name,procedure,time));
        }
        return recipes;
    }


    /**
     * provides ingredients which are in specific recipe
     * @param rec_id id of recipe
     * @return
     */
    public static ArrayList<Ingredient>getIngredientsFromRecipe(int rec_id){
        SQLiteDatabase db=db_writable;
        Cursor cursor=db.rawQuery("SELECT ingredients.id_ing, name, amount, scale FROM IngredientsInRecipes INNER JOIN ingredients ON IngredientsInRecipes.id_ing = ingredients.id_ing where IngredientsInRecipes.id_rec="+rec_id,null);

        String name,scale;
        double amount;
        int id;
        ArrayList<Ingredient> ing=new ArrayList<>();
        while(cursor.moveToNext()){
            id=cursor.getInt(0);
            name=cursor.getString(1);
            amount=cursor.getDouble(2);
            scale=cursor.getString(3);
            //Toast.makeText(mContext, m+": "+lon +" "+lat, Toast.LENGTH_LONG).show();

            ing.add(new Ingredient(id,name,amount,scale));
        }
        return ing;
    }


    /**
     * function for inserting new ingredient to table
     * @param name
     * @return
     */
    public static boolean insertIngredientData(String name){
       // checkTables();

        SQLiteDatabase db=db_writable;
        ContentValues contentValues=new ContentValues();
        contentValues.put(INGREDIENT_NAME,name);

        long result=db.insert(INGREDIENT_TABLE_NAME,null,contentValues);
        if (result==-1){
            return false;
        }
        SQLiteDatabase dat = db_readable;
        Cursor cursor = dat.rawQuery("select * from ingredients WHERE id_ing=(SELECT MAX(id_ing) from ingredients)", null);

        if(!cursor.moveToNext()){
            // Toast.makeText(context,"db: NotOK: "+cursor.getCount(),Toast.LENGTH_LONG).show();
            return false;
        }

        String dbName;
        dbName = cursor.getString(cursor.getColumnIndex("name"));

        if (!name.equals(dbName)){
            return false;
        }

        int id = cursor.getInt(0);
        ContentValues val=new ContentValues();
        val.put("id_ing",id);
        val.put("name",name);
        result=db.insert("ingredient_search",null,val);


        return true;
    }

    /**
     * function for listing all ingredients in db
     * @return
     */
    public static ArrayList<Ingredient> getAllIngredients (){
        SQLiteDatabase db=db_writable;
        Cursor cursor=db.rawQuery("select * from "+INGREDIENT_TABLE_NAME,null);

        String name;
        int id;
        ArrayList<Ingredient> ingredients=new ArrayList<>();
        while(cursor.moveToNext()){
            id=cursor.getInt(0);
            name=cursor.getString(1);
            ingredients.add(new Ingredient(id,name));
        }
        return ingredients;
    }

    /**
     * Search in recipes by its name
     * @param name
     * @return
     */
    public static ArrayList<Recipe> searchRecipesByName(String name){
        ArrayList<Recipe> found=new ArrayList<>();

        SQLiteDatabase dat = db_readable;
        Cursor cursor = dat.rawQuery("select * from recipes natural JOIN (select * from recipe_search WHERE name match '*"+name+"*' )", null);

        while (cursor.moveToNext()) {
            Recipe rec=new Recipe(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getInt(3)
            );
            found.add(rec);
            //Toast.makeText(context, "found: " + cursor.getCount(), Toast.LENGTH_LONG).show();
        }
        return found;
    }

    /**
     * Search in ingredients by its name
     * @param name
     * @return
     */
    public static ArrayList<Ingredient> searchIngredientByName(String name) {
        ArrayList<Ingredient>found=new ArrayList<>();
        SQLiteDatabase dat = db_readable;
        Cursor cursor = dat.rawQuery("select * from ingredients natural JOIN (select * from ingredient_search WHERE name match '*"+name+"*' )", null);

        while (cursor.moveToNext()) {
            Ingredient ing=new Ingredient(
                    cursor.getInt(0),
                    cursor.getString(1)
            );
            found.add(ing);
            //Toast.makeText(context, "found: " + cursor.getCount(), Toast.LENGTH_LONG).show();
        }
        return found;
    }

    /**
     * Search in recipes by ingredients it contains
     * @param ingredients
     * @return
     */
    public static ArrayList<Recipe> searchRecipesByIngredients (ArrayList<Ingredient> ingredients){
        ArrayList<TreeSet<Integer>> recipesPerIng = new ArrayList<TreeSet<Integer>>();
        SQLiteDatabase dat = db_readable;
        if(ingredients.size()==0){
            return new ArrayList<>();
        }
        //String s="select * from recipes natural join (SELECT id_rec FROM (SELECT id_rec,id_ing, count(id_rec) as cnt from IngredientsInRecipes where id_ing='1' OR id_ing='2' group by id_rec) WHERE cnt=2)";
        String s="select * from recipes natural join (SELECT id_rec FROM (SELECT id_rec,id_ing, count(id_rec) as cnt from IngredientsInRecipes where ";
        s+="id_ing="+ingredients.get(0).getId()+" ";
        for (int i=0;i<ingredients.size();i++) {
                s+="OR id_ing="+ingredients.get(i).getId()+" ";
        }
        s+="group by id_rec) WHERE cnt="+ingredients.size()+")";

        ArrayList<Recipe> found=new ArrayList<>();
        Cursor cursor = dat.rawQuery(s, null);
        //iteration per every recipe found
        while (cursor.moveToNext()) {
            Recipe rec=new Recipe(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3));
            found.add(rec);
        }
        Toast.makeText(context, "found: " + found.size(), Toast.LENGTH_LONG).show();

        return found;
    }
}

