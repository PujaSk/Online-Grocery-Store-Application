package com.example.grogrocery.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.grogrocery.R;
import com.google.android.material.navigation.NavigationView;

public class Admin_HomeScreen extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageButton buttonDrawerToggle;
    NavigationView navigationView;
    Button btnItems,btnCategory,viewCategoryBtn,viewItemBtn,logoutbtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_screen);

       // onBackPressed();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        buttonDrawerToggle = (ImageButton) findViewById(R.id.img_menu);
        navigationView = findViewById(R.id.navigationView);

        btnItems = findViewById(R.id.ItemBtn);
        btnCategory = findViewById(R.id.CategoryBtn);
        viewCategoryBtn = findViewById(R.id.ViewCategoryBtn);
        viewItemBtn = findViewById(R.id.ViewItemBtn);
        logoutbtn = findViewById(R.id.Admin_LogOut_Btn);


        buttonDrawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if(itemId == R.id.navViewUser){
                    Toast.makeText(getApplicationContext(), "View User..", Toast.LENGTH_SHORT).show();
                    // Navigate to ViewUsersActivity
                    Intent intent = new Intent(Admin_HomeScreen.this, ViewUsersActivity.class);
                    startActivity(intent);
                }
                if(itemId == R.id.navViewOrders){
                    Intent intent = new Intent(Admin_HomeScreen.this, OrderReportActivity.class);
                    startActivity(intent);
                }
                if(itemId == R.id.navViewReports){
                    Intent intent = new Intent(Admin_HomeScreen.this, ReportActivity.class);
                    startActivity(intent);
                }
                if(itemId == R.id.navLogOut){
                    onBackPressed();
                }
                drawerLayout.close();
                return false;
            }
        });



        btnItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Admin_HomeScreen.this, AdminSide_Items.class);
                startActivity(i);
            }
        });

        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Admin_HomeScreen.this, AdminSide_Category.class);
                startActivity(i);
            }
        });

        viewCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Admin_HomeScreen.this, ViewCategoryActivity.class);
                startActivity(i);
            }
        });

        viewItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Admin_HomeScreen.this, ItemListActivity.class);
                startActivity(i);
            }
        });

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button viewReportBtn = findViewById(R.id.ViewReportBtn);
        viewReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open ReportActivity when "View Reports" button is clicked
                Intent intent = new Intent(Admin_HomeScreen.this, ReportActivity.class);
                startActivity(intent);
            }
        });

    }


    // To Logout of the app
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Admin_HomeScreen.this);
        builder.setTitle("LogOut");
        builder.setMessage("Do You want to Logout?");

        builder.setPositiveButton("Yes", (dialogInterface, which) -> {
            // Clear user session or any related data
            // ...

            // Navigate to the Login screen
            Intent intent = new Intent(Admin_HomeScreen.this, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Optional: Close the current activity
        });

        builder.setNegativeButton("No", (dialogInterface, which) -> dialogInterface.cancel());

        AlertDialog ad = builder.create();
        ad.show();
    }
}