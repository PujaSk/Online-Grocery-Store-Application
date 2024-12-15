// Order.java
package com.example.grogrocery.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.grogrocery.R;
import com.example.grogrocery.SqliteDatabase.DBHelper;
import java.util.ArrayList;
import java.util.HashMap;

public class Order extends AppCompatActivity {

    private RecyclerView orderRecyclerView;
    private OrderRecyclerAdapter orderAdapter;
    private DBHelper dbHelper;
    private int userId; // User ID
    private ArrayList<HashMap<String, Object>> orderList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        dbHelper = new DBHelper(this);

        // Initialize RecyclerView
        orderRecyclerView = findViewById(R.id.orderRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        orderRecyclerView.setLayoutManager(layoutManager);

        // Add divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(orderRecyclerView.getContext(),
                layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_order));
        orderRecyclerView.addItemDecoration(dividerItemDecoration);

        // Get userId
        userId = getUserId(); // Correctly retrieves from SharedPreferences

        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch and display orders
        loadOrders();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders();
    }

    private void loadOrders() {
        orderList = dbHelper.getUserOrders(userId);

        if (orderList.isEmpty()) {
            Toast.makeText(this, "No orders found.", Toast.LENGTH_SHORT).show();
        } else {
            // Create or update adapter
            if (orderAdapter == null) {
                orderAdapter = new OrderRecyclerAdapter(this, orderList);
                orderRecyclerView.setAdapter(orderAdapter);
            } else {
                // Update existing adapter with new data
                orderAdapter.updateOrderList(orderList);
            }
        }
    }

    // Retrieve user ID from SharedPreferences
    private int getUserId() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return prefs.getInt("user_id", -1); // Default -1 if not found
    }

}
