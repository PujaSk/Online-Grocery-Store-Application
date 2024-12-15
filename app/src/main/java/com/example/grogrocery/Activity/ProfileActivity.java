// ProfileActivity.java

package com.example.grogrocery.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grogrocery.Activity.User;
import com.example.grogrocery.R;
import com.example.grogrocery.SqliteDatabase.DBHelper;

public class ProfileActivity extends AppCompatActivity {

    private TextView headingProfile, labelName, labelEmail, labelPhone, labelCity;
    private EditText editTextName, editTextCity;
    private TextView textViewEmail, textViewPhone;
    private Button updateButton;
    private int userId;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile); // Ensure this layout exists

        // Initialize DBHelper
        dbHelper = new DBHelper(this);

        // Initialize Views
        headingProfile = findViewById(R.id.headingProfile);
        labelName = findViewById(R.id.labelName);
        labelEmail = findViewById(R.id.labelEmail);
        labelPhone = findViewById(R.id.labelPhone);
        labelCity = findViewById(R.id.labelCity);
        editTextName = findViewById(R.id.editTextName);
        editTextCity = findViewById(R.id.editTextCity);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewPhone = findViewById(R.id.textViewPhone);
        updateButton = findViewById(R.id.updateButton);

        // Retrieve userId from SharedPreferences
        userId = getUserId();
        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch and display user details
        displayUserDetails(userId);

        // Set up Update Button
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserDetails();
            }
        });
    }

    // Method to retrieve userId from SharedPreferences
    private int getUserId() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int id = prefs.getInt("user_id", -1); // Default -1 if not found
        Log.d("ProfileActivity", "Retrieved userId from SharedPreferences: " + id);
        return id;
    }

    // Method to display user details
    private void displayUserDetails(int userId) {
        User user = dbHelper.getUserDetails(userId);
        if (user != null) {
            editTextName.setText(user.getName());
            editTextCity.setText(user.getCity());
            textViewEmail.setText(user.getEmail());
            textViewPhone.setText(user.getPhone());
        } else {
            Toast.makeText(this, "Failed to fetch user details", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to update user details (name and city)
    private void updateUserDetails() {
        String newName = editTextName.getText().toString().trim();
        String newCity = editTextCity.getText().toString().trim();

        if (TextUtils.isEmpty(newName)) {
            editTextName.setError("Name cannot be empty");
            return;
        }

        if (TextUtils.isEmpty(newCity)) {
            editTextCity.setError("City cannot be empty");
            return;
        }

        boolean isUpdated = dbHelper.updateUserNameAndCity(userId, newName, newCity);
        if (isUpdated) {
            Toast.makeText(this, "Details updated successfully", Toast.LENGTH_SHORT).show();
            // Optionally, refresh the displayed name
        } else {
            Toast.makeText(this, "Failed to update details. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
