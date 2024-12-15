package com.example.grogrocery.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grogrocery.R;
import com.example.grogrocery.SqliteDatabase.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeScreen extends AppCompatActivity implements CategoryRecyclerAdapter.OnItemClickListener {

    private TextView textView10; // TextView to display the user's name
    private int userId; // User ID
    private SearchView sv;
    private RecyclerView categoriesRecyclerView, PopularView;   // Add PopularView RecyclerView
    private DBHelper dbHelper;
    private CategoryRecyclerAdapter categoryAdapter;
    private PopularProductAdapter popularAdapter; // Adapter for PopularView
    private ImageView imageView_Bell; // ImageView for the bell icon

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        bottomNavigation();

        // Initialize views
        textView10 = findViewById(R.id.textView10);
        PopularView = findViewById(R.id.PopularView);
        sv = findViewById(R.id.searchView);
        categoriesRecyclerView = findViewById(R.id.categoriesRecyclerView);
        imageView_Bell = findViewById(R.id.imageView_Bell); // Initialize the bell ImageView

        dbHelper = new DBHelper(this);

        // Retrieve userId from SharedPreferences
        userId = getUserId();
        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch and display user's name
        displayUserName(userId);

        // Load category records from the database
        ArrayList<HashMap<String, Object>> categoryList = dbHelper.getAllCategories();

        // Set up RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        categoriesRecyclerView.setLayoutManager(layoutManager);
        categoryAdapter = new CategoryRecyclerAdapter(this, categoryList, this); // 'this' refers to OnItemClickListener
        categoriesRecyclerView.setAdapter(categoryAdapter);

        // Load popular products
        loadPopularProducts();

        // Set up SearchView
        setupSearchView();

        // Set up click listener for the bell icon
        setupBellIcon();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh user name when activity resumes
        displayUserName(userId);
    }

    private void bottomNavigation() {
        LinearLayout cartbtn = findViewById(R.id.CartBtn);
        LinearLayout orderbtn = findViewById(R.id.OrderBtn);
        LinearLayout profilebtn = findViewById(R.id.ProfileBtn);

        cartbtn.setOnClickListener(view -> {
            Intent i = new Intent(HomeScreen.this, Cart.class);
            startActivity(i);
        });
        orderbtn.setOnClickListener(view -> {
            Intent i = new Intent(HomeScreen.this, Order.class);
            startActivity(i);
        });
        profilebtn.setOnClickListener(view -> {
            Intent i = new Intent(HomeScreen.this, ProfileActivity.class);
            startActivity(i);
        });
    }

    // Handle category click
    @Override
    public void onItemClick(String categoryId, String categoryName) {
        Intent intent = new Intent(HomeScreen.this, CategoryItemsActivity.class);
        intent.putExtra("CATEGORY_ID", categoryId);
        intent.putExtra("CATEGORY_NAME", categoryName);
        startActivity(intent);
    }

    // To Logout of the app
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);
        builder.setTitle("LogOut");
        builder.setMessage("Do You want to Logout?");

        builder.setPositiveButton("Yes", (dialogInterface, which) -> finish());

        builder.setNegativeButton("No", (dialogInterface, which) -> dialogInterface.cancel());

        AlertDialog ad = builder.create();
        ad.show();
    }


    private void displayUserName(int userId) {
        ArrayList<HashMap<String, String>> userList = dbHelper.getAllUsers();
        String userName = "User"; // Default name

        for (HashMap<String, String> user : userList) {
            int cid = Integer.parseInt(user.get("cid"));
            if (cid == userId) {
                userName = user.get("name");
                break;
            }
        }
        textView10.setText(userName);
        Log.d("HomeScreen", "Displayed userName: " + userName);
    }

    private int getUserId() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int id = prefs.getInt("user_id", -1); // Default -1 if not found
        Log.d("HomeScreen", "Retrieved userId from SharedPreferences: " + id);
        return id;
    }

    private void loadPopularProducts() {
        // Fetch popular items from DB
        ArrayList<Item> popularItems = dbHelper.getAllItems(); // Replace with a method that fetches popular items if available

        if (popularItems.isEmpty()) {
            Toast.makeText(this, "No popular items found.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Set up PopularView RecyclerView
        popularAdapter = new PopularProductAdapter(this, popularItems, userId);
        LinearLayoutManager popularLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        PopularView.setLayoutManager(popularLayoutManager);
        PopularView.setAdapter(popularAdapter);
    }

    private void setupSearchView() {
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true; // Indicate that the query has been handled
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    // If search query is cleared, show popular products again
                    loadPopularProducts();
                } else {
                    performSearch(newText);
                }
                return true;
            }
        });
    }

    private void performSearch(String query) {
        ArrayList<Item> searchResults = dbHelper.searchItems(query);

        if (searchResults.isEmpty()) {
            Toast.makeText(this, "No results found for \"" + query + "\"", Toast.LENGTH_SHORT).show();
        }

        // Update the adapter with search results
        popularAdapter.updateItemList(searchResults);
    }

    private void setupBellIcon() {
        imageView_Bell.setOnClickListener(view -> {
            Intent intent = new Intent(HomeScreen.this, NotificationActivity.class);
            startActivity(intent);
        });
    }

}