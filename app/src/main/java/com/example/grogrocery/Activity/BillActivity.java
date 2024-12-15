package com.example.grogrocery.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grogrocery.R;
import com.example.grogrocery.SqliteDatabase.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class BillActivity extends AppCompatActivity {

    private TextView billIdTextView, billDateTextView, totalAmountTextView;
    private RecyclerView billRecyclerView;
    private BillAdapter billAdapter;
    private DBHelper dbHelper;
    private long billId;
    private ArrayList<HashMap<String, Object>> orderItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        // Initialize views
        billIdTextView = findViewById(R.id.billIdTextView);
        billDateTextView = findViewById(R.id.billDateTextView);
        totalAmountTextView = findViewById(R.id.totalAmountTextView);
        billRecyclerView = findViewById(R.id.billRecyclerView);

        dbHelper = new DBHelper(this);

        // Retrieve billId from intent
        billId = getIntent().getLongExtra("bill_id", -1);
        if (billId == -1) {
            Toast.makeText(this, "Invalid Bill ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch bill details
        fetchBillDetails();
    }

    private void fetchBillDetails() {
        // Fetch orders associated with billId
        orderItems = dbHelper.getOrdersByBillId(billId);
        if (orderItems.isEmpty()) {
            Toast.makeText(this, "No orders found for this bill.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch bill details
        HashMap<String, Object> billDetails = dbHelper.getBillDetails(billId);
        if (billDetails == null) {
            Toast.makeText(this, "Failed to retrieve bill details.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Set bill ID and date
        billIdTextView.setText("Bill ID: #" + billId);
        Object billDateObj = billDetails.get("bill_date");
        String billDate = (billDateObj != null) ? billDateObj.toString() : "N/A";
        billDateTextView.setText("Date: " + billDate);
        Object totalAmountObj = billDetails.get("total_amount");
        String totalAmount = (totalAmountObj != null) ? String.format("%.2f", (double) totalAmountObj) : "0.00";
        totalAmountTextView.setText("Total Amount: Rs." + totalAmount);

        // Set up RecyclerView
        billRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        billAdapter = new BillAdapter(orderItems);
        billRecyclerView.setAdapter(billAdapter);
    }
}
