package com.example.grogrocery.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grogrocery.R;

public class OrderRegisteredActivity extends AppCompatActivity {

    private TextView confirmationText;
    private Button continueShoppingBtn;
    private TextView goToBillActivityText;
    private long billId; // Store the bill ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_registered);

        // Initialize views
        confirmationText = findViewById(R.id.confirmationText);
        continueShoppingBtn = findViewById(R.id.continueShoppingBtn);
        goToBillActivityText = findViewById(R.id.goToBillActivityText);

        // Set confirmation message
        confirmationText.setText("Your order has been successfully registered!");

        // Retrieve billId from intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("bill_id")) {
            billId = intent.getLongExtra("bill_id", -1);
        } else {
            billId = -1;
        }

        // Handle continue shopping button
        continueShoppingBtn.setOnClickListener(v -> {
            // Navigate back to HomeScreen or any other desired activity
            Intent homeIntent = new Intent(OrderRegisteredActivity.this, HomeScreen.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
            finish();
        });

        // Handle "View Bill" TextView click
        goToBillActivityText.setOnClickListener(v -> {
            if (billId != -1) {
                Intent billIntent = new Intent(OrderRegisteredActivity.this, BillActivity.class);
                billIntent.putExtra("bill_id", billId);
                startActivity(billIntent);
            } else {
                Toast.makeText(this, "Bill ID not found.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
