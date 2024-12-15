package com.example.grogrocery.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.grogrocery.R;
import com.example.grogrocery.SqliteDatabase.DBHelper;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class AdminSide_Category extends AppCompatActivity {

    EditText name,id;
    ImageView imgGallery;
    Button btnGallery, save,update,view;
    Bitmap selectedBitmap;

    DBHelper dbHelper;
    private static final int PICK_IMAGE = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminside_category);

        imgGallery = findViewById(R.id.edtimage);
        btnGallery = findViewById(R.id.btnGallery);
        save = findViewById(R.id.savebtnCat);
        view = findViewById(R.id.viewbtnCat);
        name = findViewById(R.id.eTxtCatName);
        id = findViewById(R.id.eTxtCatID);

        dbHelper = new DBHelper(this);


        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminSide_Category.this, ViewCategoryActivity.class);
                startActivity(intent);
            }
        });


    }



    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }
    //for setting gallery img in imageview
    // Handle the result in onActivityResult()
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                selectedBitmap = BitmapFactory.decodeStream(inputStream);
                imgGallery.setImageBitmap(selectedBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



//Add Category Permanently in the database
    public void saveCat(View view)
    {
        String sid,sname;
        sid = id.getText().toString().trim();
        sname = name.getText().toString().trim();

        if(sid.equals("")){
            id.setError("Cannot be Empty");
        }
        if(sname.equals("")){
            name.setError("Cannot be Empty");
        }
        // Check if image is selected
        if (selectedBitmap == null) {  // Add this check
            Toast.makeText(getApplicationContext(), "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            if(dbHelper.checkCatId(sid)){
                Toast.makeText(getApplicationContext(),"Category ID Already Exists", Toast.LENGTH_SHORT).show();
                return;
            }
            // Convert bitmap to byte array
            byte[] imageBytes = getBytesFromBitmap(selectedBitmap);
            boolean registeredSuccess = dbHelper.insertCat(sid,sname,imageViewtoBy(imgGallery));
            if (registeredSuccess) {
                Toast.makeText(getApplicationContext(), "Category Added Successfully", Toast.LENGTH_SHORT).show();
                id.setText("");
                name.setText("");
                imgGallery.setImageResource(0); // Clear the image view
            }
            else
                Toast.makeText(getApplicationContext(),"Failed to Add Category", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private static byte[] imageViewtoBy(ImageView imgGallery) {
        Bitmap bitmap = ((BitmapDrawable)imgGallery.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50,stream);
        byte[] bytes = stream.toByteArray();
        return bytes;
    }






}

