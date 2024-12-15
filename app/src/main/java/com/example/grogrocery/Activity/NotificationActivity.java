package com.example.grogrocery.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grogrocery.R;
import com.example.grogrocery.SqliteDatabase.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class NotificationActivity extends AppCompatActivity {

    private ListView orderedProductsListView;
    private TextView acknowledgmentText;
    private DBHelper dbHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification); // Ensure this layout exists

        // Initialize Views
        acknowledgmentText = findViewById(R.id.acknowledgmentText);
        orderedProductsListView = findViewById(R.id.orderedProductsListView);

        // Initialize DBHelper
        dbHelper = new DBHelper(this);

        // Retrieve userId from SharedPreferences
        userId = getUserId();
        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set acknowledgment message
        acknowledgmentText.setText("Thank you for your order! Here are the products you have ordered:");

        // Fetch ordered products
        ArrayList<HashMap<String, Object>> orders = dbHelper.getUserOrders(userId);

        // Process orders to create a list of strings to display
        ArrayList<String> orderedProductsList = new ArrayList<>();
        for (HashMap<String, Object> order : orders) {
            String productName = (String) order.get("item_name");
            String orderDate = (String) order.get("order_date");
            orderedProductsList.add(productName + "\nOrdered on: \t" + orderDate);
        }

        // Check if there are orders
        if (orderedProductsList.isEmpty()) {
            orderedProductsList.add("No orders found.");
        }

        // Set up ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, orderedProductsList);
        orderedProductsListView.setAdapter(adapter);
    }

    // Method to retrieve userId from SharedPreferences
    private int getUserId() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int id = prefs.getInt("user_id", -1); // Default -1 if not found
        Log.d("NotificationActivity", "Retrieved userId from SharedPreferences: " + id);
        return id;
    }
}
