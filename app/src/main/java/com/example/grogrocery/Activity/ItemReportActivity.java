package com.example.grogrocery.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import com.example.grogrocery.R;
import com.example.grogrocery.SqliteDatabase.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class ItemReportActivity extends AppCompatActivity {
    private GridView gridViewItems;
    private ItemReportAdapter itemAdapter;
    private List<ItemReport> itemList;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_report);

        gridViewItems = findViewById(R.id.gridViewItems);
        itemList = new ArrayList<>();
        dbHelper = new DBHelper(this);

        itemAdapter = new ItemReportAdapter(this, itemList);
        gridViewItems.setAdapter(itemAdapter);

        fetchItems();
    }

    private void fetchItems() {
        List<ItemReport> fetchedItems = dbHelper.fetchItemReports();

        if (fetchedItems.isEmpty()) {
            Toast.makeText(this, "No items found.", Toast.LENGTH_SHORT).show();
        } else {
            itemList.clear();
            itemList.addAll(fetchedItems);
            itemAdapter.notifyDataSetChanged();
        }
    }
}
