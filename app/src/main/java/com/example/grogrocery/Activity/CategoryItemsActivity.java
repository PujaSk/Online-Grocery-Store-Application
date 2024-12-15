package com.example.grogrocery.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grogrocery.R;
import com.example.grogrocery.SqliteDatabase.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class CategoryItemsActivity extends AppCompatActivity implements ItemsRecyclerAdapter.OnAddToCartClickListener {

    private RecyclerView itemsRecyclerView;
    private TextView categoryTitleTextView;
    private ItemsRecyclerAdapter itemsAdapter;
    private DBHelper dbHelper;
    private String categoryId;
    private String categoryName;
    private int userId; // User ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_items);

        // Initialize views
        itemsRecyclerView = findViewById(R.id.itemsRecyclerView);
        categoryTitleTextView = findViewById(R.id.categoryTitleTextView);
        dbHelper = new DBHelper(this);

        // Retrieve userId
        userId = getUserId(); // Implement this method based on your app's logic
        Log.d("CategoryItemsActivity", "Retrieved userId: " + userId);
        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        // Get the Intent extras
        if (getIntent() != null) {
            categoryId = getIntent().getStringExtra("CATEGORY_ID");
            categoryName = getIntent().getStringExtra("CATEGORY_NAME");
        }

        // Set the category title
        if (categoryName != null) {
            categoryTitleTextView.setText(categoryName);
        }

        if (categoryId != null) {
            loadItems(categoryId);
        } else {
            Toast.makeText(this, "No Category Selected", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadItems(String categoryId) {
        // Fetch items from DB based on categoryId
        ArrayList<HashMap<String, Object>> itemList = dbHelper.getItemsByCategoryId(categoryId);

        if (itemList.isEmpty()) {
            Toast.makeText(this, "No items found for this category.", Toast.LENGTH_SHORT).show();
        }

        // Set up RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        itemsRecyclerView.setLayoutManager(layoutManager);
        itemsAdapter = new ItemsRecyclerAdapter(this, itemList,this, userId); // Pass listener and userId
        itemsRecyclerView.setAdapter(itemsAdapter);
    }

    // Implement the interface method
    @Override
    public void onAddToCart(int itemId, String itemName, int qty) {
        // Optionally, implement actions like updating a cart badge or notifying the user
        //Toast.makeText(this, "Item added to cart!", Toast.LENGTH_SHORT).show();
    }


    // Retrieve userId from SharedPreferences
    private int getUserId() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int fetchedUserId = prefs.getInt("user_id", -1); // Default -1 if not found
        Log.d("CategoryItemsActivity", "Fetched userId from SharedPreferences: " + fetchedUserId);
        return fetchedUserId;
    }
}