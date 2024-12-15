package com.example.grogrocery.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.Toast;

import com.example.grogrocery.R;
import com.example.grogrocery.SqliteDatabase.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class BillReportActivity extends AppCompatActivity {
    private RecyclerView recyclerViewBills;
    private BillReportRecyclerAdapter billAdapter;
    private List<Bill> billList;
    private DBHelper dbHelper; // Declare DBHelper instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_report);

        // Initialize UI components and variables
        recyclerViewBills = findViewById(R.id.recyclerViewBills);
        billList = new ArrayList<>();
        dbHelper = new DBHelper(this); // Initialize DBHelper

        // Set LayoutManager
        recyclerViewBills.setLayoutManager(new GridLayoutManager(this, 1)); // 1 column for table-like rows

        // Initialize the adapter with the current (empty) billList
        billAdapter = new BillReportRecyclerAdapter(this, billList);
        recyclerViewBills.setAdapter(billAdapter); // Set the adapter to RecyclerView

        // Fetch data from the database and update the adapter
        fetchBills();
    }

    private void fetchBills() {
        // Fetch bills from DBHelper
        List<Bill> fetchedBills = dbHelper.fetchBills();

        if (fetchedBills.isEmpty()) {
            Toast.makeText(this, "No bills found.", Toast.LENGTH_SHORT).show();
        } else {
            billList.clear(); // Clear existing data
            billList.addAll(fetchedBills); // Add new data
            billAdapter.notifyDataSetChanged(); // Notify adapter about data changes
        }
    }
}
