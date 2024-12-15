package com.example.grogrocery.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.grogrocery.R;
import com.example.grogrocery.SqliteDatabase.DBHelper;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewCategoryActivity extends AppCompatActivity {

    ListView categoryListView;
    DBHelper dbHelper;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imgViewForUpdate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);

        categoryListView = findViewById(R.id.categoryListView);
        dbHelper = new DBHelper(this);

        // Load category records from the database
        ArrayList<HashMap<String, Object>> categoryList = dbHelper.getAllCategories();

        // Log the size of the category list to debug
        Log.d("ViewCategoryActivity", "Category List Size: " + categoryList.size());

        // Prepare the custom adapter
        CategoryAdapter adapter = new CategoryAdapter(this, categoryList);

        // Set the adapter to the ListView
        categoryListView.setAdapter(adapter);


        categoryListView.setOnItemClickListener((parent, view, position, id) -> {
            // Get the selected category details
            HashMap<String, Object> selectedCategory = (HashMap<String, Object>) parent.getItemAtPosition(position);

            // Extract Category ID and Name
            String categoryId = (String) selectedCategory.get("id");
            String categoryName = (String) selectedCategory.get("name");

            // Convert image bytes to Bitmap (if needed)
            byte[] imageBytes = (byte[]) selectedCategory.get("avatar");
            Bitmap categoryImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

            // Open the dialog for updating the category
            showUpdateDialog(categoryId, categoryName, categoryImage);
        });

    }



    private void showUpdateDialog(String categoryId, String categoryName, Bitmap categoryImage) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.update_dialog_category, null);
        dialogBuilder.setView(dialogView);

        // Get references to the EditText and ImageView in the dialog
        EditText edtId = dialogView.findViewById(R.id.updateId);
        EditText edtName = dialogView.findViewById(R.id.updateName);
        imgViewForUpdate = dialogView.findViewById(R.id.updateImageView);
        ImageView imgView = dialogView.findViewById(R.id.updateImageView);
        Button btnUpdateImage = dialogView.findViewById(R.id.btnUpdateImage);

        // Set initial values
        edtId.setText(categoryId);
        edtName.setText(categoryName);
        imgView.setImageBitmap(categoryImage);

        // Set up button to select a new image
        btnUpdateImage.setOnClickListener(v -> {
            // Logic to open the gallery and select a new image
            openGalleryForUpdate();
        });

        dialogBuilder.setTitle("Update Category");
        dialogBuilder.setPositiveButton("Update", (dialog, which) -> {
            // Get updated values from the dialog
            String updatedId = edtId.getText().toString();
            String updatedName = edtName.getText().toString();

            // Retrieve the updated image (make sure the global imgViewForUpdate has the new image)
            Bitmap updatedImage = ((BitmapDrawable) imgViewForUpdate.getDrawable()).getBitmap();

            // Update the category in the database
            updateCategoryInDatabase(updatedId, updatedName, updatedImage);
        });


        dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void updateCategoryInDatabase(String id, String name, Bitmap image) {
        // Convert the Bitmap to a byte array
        byte[] imageBytes = getBytesFromBitmap(image);

        // Debugging: Log the byte array length to check if the image is updated
        Log.d("UpdateCategory", "Image byte array length: " + imageBytes.length);

        // Update the category in the database
        boolean updateSuccess = dbHelper.updateCategory(id, name, imageBytes);

        if (updateSuccess) {
            Toast.makeText(getApplicationContext(), "Category Updated Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Failed to Update Category", Toast.LENGTH_SHORT).show();
        }

        // Refresh the ListView after updating
        ArrayList<HashMap<String, Object>> updatedCategoryList = dbHelper.getAllCategories();
        CategoryAdapter adapter = new CategoryAdapter(this, updatedCategoryList);
        categoryListView.setAdapter(adapter);
    }


    private byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void openGalleryForUpdate() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                // Get the image from the Uri and decode it
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                // Set the selected image in the global ImageView reference (imgViewForUpdate)
                if (imgViewForUpdate != null) {
                    imgViewForUpdate.setImageBitmap(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




}
