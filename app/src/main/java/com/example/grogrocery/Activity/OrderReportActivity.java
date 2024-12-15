package com.example.grogrocery.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import com.example.grogrocery.R;
import com.example.grogrocery.SqliteDatabase.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class OrderReportActivity extends AppCompatActivity {
    private GridView gridViewOrders;
    private OrderReportAdapter orderAdapter;
    private List<OrderReport> orderList;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_report);

        gridViewOrders = findViewById(R.id.gridViewOrders);
        orderList = new ArrayList<>();
        dbHelper = new DBHelper(this);

        // Initialize the adapter with the empty list
        orderAdapter = new OrderReportAdapter(this, orderList);
        gridViewOrders.setAdapter(orderAdapter);

        // Fetch data from the database
        fetchOrders();
    }

    private void fetchOrders() {
        // Use dbHelper to fetch order reports
        List<OrderReport> fetchedOrders = dbHelper.fetchOrderReports();

        if (fetchedOrders.isEmpty()) {
            Toast.makeText(this, "No orders found.", Toast.LENGTH_SHORT).show();
        } else {
            orderList.clear();
            orderList.addAll(fetchedOrders);
            orderAdapter.notifyDataSetChanged();
        }
    }
}
