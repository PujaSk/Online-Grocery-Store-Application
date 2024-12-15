package com.example.grogrocery.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grogrocery.R;
import com.example.grogrocery.SqliteDatabase.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class Cart extends AppCompatActivity implements CartRecyclerAdapter.OnCartItemInteractionListener {

    private ImageView backBtn;
    private TextView emptyText;
    private RecyclerView cartView;
    private TextView totalFeeTxt, deliveryTxt, taxTxt, totalTxt;
    private Button checkOutBtn;
    private CartRecyclerAdapter cartAdapter;
    private DBHelper dbHelper;
    private int userId; // User ID
    private ArrayList<HashMap<String, Object>> cartList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        dbHelper = new DBHelper(this);

        // Initialize views
        backBtn = findViewById(R.id.imageView4);
        emptyText = findViewById(R.id.emptyText);
        cartView = findViewById(R.id.cartView);
        totalFeeTxt = findViewById(R.id.totalFeeTxt);
        deliveryTxt = findViewById(R.id.deliveryTxt);
        taxTxt = findViewById(R.id.taxTxt);
        totalTxt = findViewById(R.id.totalTxt);
        checkOutBtn = findViewById(R.id.checkOutBtn);

        // Get userId
        userId = getUserId(); // Now retrieves from SharedPreferences
        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set up RecyclerView
        cartView.setLayoutManager(new LinearLayoutManager(this));
        cartList = dbHelper.getCartItems(userId);
        cartAdapter = new CartRecyclerAdapter(this, cartList, this);
        cartView.setAdapter(cartAdapter);

        // Set up back button
        backBtn.setOnClickListener(v -> finish());

        // Set up checkout button
        checkOutBtn.setOnClickListener(v -> {
            if (cartList.isEmpty()) {
                Toast.makeText(Cart.this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            long billId = dbHelper.checkoutCart(userId); // Get the billId
            if (billId != -1) {
                Toast.makeText(Cart.this, "Order Registered", Toast.LENGTH_SHORT).show();
                // Open OrderRegisteredActivity and pass the billId
                Intent intent = new Intent(Cart.this, OrderRegisteredActivity.class);
                intent.putExtra("bill_id", billId); // Pass billId
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(Cart.this, "Checkout failed. Please check stock quantities.", Toast.LENGTH_SHORT).show();
            }
        });


        updateTotals();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCart();
    }

    private void loadCart() {
        cartList = dbHelper.getCartItems(userId);
        if (cartList.isEmpty()) {
            cartView.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        } else {
            cartView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);
            cartAdapter.updateCartList(cartList);
            updateTotals();
        }
    }

    private void updateTotals() {
        int subtotal = 0;
        for (HashMap<String, Object> item : cartList) {
            int price = (int) item.get("price");
            int quantity = (int) item.get("quantity");
            subtotal += price * quantity;
        }

        int delivery = 50; // Fixed delivery fee, can be adjusted
        double tax = subtotal * 0.1; // 10% tax
        double total = subtotal + delivery + tax;

        totalFeeTxt.setText("Rs." + subtotal);
        deliveryTxt.setText("Rs." + delivery);
        taxTxt.setText("Rs." + String.format("%.2f", tax));
        totalTxt.setText("Rs." + String.format("%.2f", total));
    }

    @Override
    public void onIncreaseQuantity(int itemId, int newQuantity) {
        // Update quantity in DB
        boolean success = dbHelper.updateCartQuantity(userId, itemId, newQuantity);
        if (success) {
            loadCart();
        } else {
            Toast.makeText(this, "Failed to update quantity", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDecreaseQuantity(int itemId, int newQuantity) {
        // Update quantity in DB
        boolean success = dbHelper.updateCartQuantity(userId, itemId, newQuantity);
        if (success) {
            loadCart();
        } else {
            Toast.makeText(this, "Failed to update quantity", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRemoveItem(int itemId, int position) {
        // Remove the item from the cartList
        cartList.remove(position);

        // Notify the adapter about item removal
        cartAdapter.notifyItemRemoved(position);
        cartAdapter.notifyItemRangeChanged(position, cartList.size());

        // Update the database to remove the item by setting its quantity to 0
        boolean success = dbHelper.updateCartQuantity(userId, itemId, 0);
        if (!success) {
            Toast.makeText(this, "Failed to remove item from cart", Toast.LENGTH_SHORT).show();
        }

        // Refresh the totals
        updateTotals();
    }



    @Override
    public void onOutOfStock(int itemId) {
        Toast.makeText(this, "Item is out of stock", Toast.LENGTH_SHORT).show();
    }

    // Remove the dummy getUserId method and replace it with the following:
    // Retrieve userId from SharedPreferences
    private int getUserId() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int fetchedUserId = prefs.getInt("user_id", -1); // Default -1 if not found
        Log.d("Cart", "Fetched userId from SharedPreferences: " + fetchedUserId);
        return fetchedUserId;
    }
}
