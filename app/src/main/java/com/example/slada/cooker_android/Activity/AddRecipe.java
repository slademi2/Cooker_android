package com.example.slada.cooker_android.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.slada.cooker_android.Adapters.IngredientInRecipeAddAdapter;
import com.example.slada.cooker_android.Database.DatabaseHelper;
import com.example.slada.cooker_android.Objects.Ingredient;
import com.example.slada.cooker_android.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;


/**
 * Activity for adding recipes. Run from main screen "Add recipe"
 */
public class AddRecipe extends AppCompatActivity {
    private String name;
    private String procedure;
    private int time;

    private ArrayList<Ingredient> ingredients;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private Activity activity;

    private ImageView imageView;
    private final int PICK_FROM_GALLERY = 0;

    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        ingredients = DatabaseHelper.getAllIngredients();

        recyclerView = findViewById(R.id.Add_Recipe_RecyclerView);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new IngredientInRecipeAddAdapter(ingredients, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        context = this;
        activity = this;

        //imageView and its onClickListener which will allow to add picture to the recipe
        //TODO implement adding pictures from gallery and camera.
        //TODO resize the pictures maybe wirh Glide
        imageView = findViewById(R.id.Add_Recipe_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Choose photo");
                builder.setMessage("Which way do you want to choose photo")
                        .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                                } else {
                                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                                }
                            }
                        })
                        .setPositiveButton("Take a photo", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO implement "TAKE a photo" option
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PICK_FROM_GALLERY:
                // If request is accepted "grantResults" is not empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    // Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryIntent.setType("imageView/*");
                    startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                } else {
                    //TODO show message that it can not be done without permissions and ask again.
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_FROM_GALLERY:
                //TODO save the picture and put uri to database - link recipe and picture
                if (data != null) {
                    Uri contentURI = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                        //Rescaling  the image
                        bitmap = Bitmap.createScaledBitmap(bitmap, 300, (int) (bitmap.getHeight() * (300.0 / bitmap.getWidth())), true);
                        Toast.makeText(context, "Image Saved!", Toast.LENGTH_SHORT).show();
                        imageView.setImageBitmap(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }

        }
    }

    /**
     * Add Recipe button
     * Function that will add recipe if everything is set
     *
     * @param view just a view
     */
    public void addRecipe(View view) {
        //getting values for new recipe
        EditText textName = findViewById(R.id.Add_Recipe_Name);
        EditText textProcedure = findViewById(R.id.Add_Recipe_Procedure);
        EditText textTime = findViewById(R.id.Add_Recipe_Time);
        boolean loop = false;
        if (textName.getText().toString().isEmpty()) {
            Toast.makeText(this, "You did not fill a name", Toast.LENGTH_SHORT).show();
            loop = true;
        }
        if (textProcedure.getText().toString().isEmpty()) {
            Toast.makeText(this, "You did not fill a procedure", Toast.LENGTH_SHORT).show();
            loop = true;
        }
        if (textTime.getText().toString().isEmpty()) {
            Toast.makeText(this, "You did not fill a time", Toast.LENGTH_SHORT).show();
            loop = true;
        }

        //TODO zeptat se jestli opravdu nechci pridat zadne ingredience? pomoci loop promenne

        if (loop == false) {
            name = textName.getText().toString();
            procedure = textProcedure.getText().toString();
            time = Integer.parseInt(textTime.getText().toString());


            int id_rec = DatabaseHelper.insertRecipeData(name, procedure, time);

            if (id_rec == -1) {
                Toast.makeText(this, "Can not add recipe", Toast.LENGTH_LONG).show();
            } else {
                if (IngredientInRecipeAddAdapter.ingredientsToAdd != null) {
                    for (Ingredient ing : IngredientInRecipeAddAdapter.ingredientsToAdd) {
                        if (DatabaseHelper.insertIngredientsInRecipeData(id_rec, ing.getId(), ing.getAmount(), ing.getScale()) == -1) {
                            Toast.makeText(this, "Can not add ingredients in recipe", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
            finish();
        }
    }
}
