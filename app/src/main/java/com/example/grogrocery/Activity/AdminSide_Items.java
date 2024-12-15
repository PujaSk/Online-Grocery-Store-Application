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

public class AdminSide_Items extends AppCompatActivity {
    EditText name, price, qty, ctgry, description;
    ImageView imgGallery;
    Button btnGallery, save, view;
    Bitmap selectedBitmap;
    DBHelper dbHelper;

    private static final int PICK_IMAGE = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminside_items);

        imgGallery = findViewById(R.id.edtimage);
        btnGallery = findViewById(R.id.btnGallery);
        save = findViewById(R.id.savebtnCat);

        view = findViewById(R.id.viewbtnCat);   // Initialize the view button

        name = findViewById(R.id.eTextName);
        price = findViewById(R.id.eTextPrice);
        qty = findViewById(R.id.eTextQty);
        ctgry = findViewById(R.id.eTextCategoryId);
        description = findViewById(R.id.eTextDescription);

        dbHelper = new DBHelper(this);

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminSide_Items.this, ItemListActivity.class);
                startActivity(intent);
            }
        });

        // You need to set OnClickListener for the save button as well
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem(v);
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

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

    public void saveItem(View view) {
        String sname = name.getText().toString();
        String sprice = price.getText().toString();
        String sqty = qty.getText().toString();
        String sctgry = ctgry.getText().toString();
        String sdescription = description.getText().toString();

        if(sname.equals("")){
            name.setError("Cannot be Empty");
            //return;
        }
        if(sprice.equals("")){
            price.setError("Cannot be Empty");
            //return;
        }
        if(sqty.equals("")){
            qty.setError("Cannot be Empty");
           // return;
        }
        if(sctgry.equals("")){
            ctgry.setError("Cannot be Empty");
            //return;
        }
        if (sdescription.equals("")) {
            description.setError("Cannot be Empty");
            //return;
        }
        // Check if the CategoryId exists
        if (!dbHelper.doesCategoryExist(sctgry)) {
            Toast.makeText(getApplicationContext(), "Category doesn't exist", Toast.LENGTH_SHORT).show();
            return; // Return early, do not proceed with saving the item
        }
        // Image selection validation
        if (selectedBitmap == null) {
            Toast.makeText(getApplicationContext(), "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        } else {
            byte[] imageBytes = getBytesFromBitmap(selectedBitmap);
            boolean registeredSuccess = dbHelper.insertItem(sname, sprice, sqty, imageViewtoBy(imgGallery), sctgry, sdescription);
            if (registeredSuccess) {
                Toast.makeText(getApplicationContext(), "Item Added Successfully", Toast.LENGTH_SHORT).show();
                name.setText("");
                price.setText("");
                qty.setText("");
                ctgry.setText("");
                description.setText("");
                imgGallery.setImageResource(0); // Clear the image view
            } else {
                Toast.makeText(getApplicationContext(),"Failed to Add Item", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private static byte[] imageViewtoBy(ImageView imgGallery) {
        Bitmap bitmap = ((BitmapDrawable) imgGallery.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] bytes = stream.toByteArray();
        return bytes;
    }

}
