package com.example.grogrocery.Activity;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grogrocery.R;
import com.example.grogrocery.SqliteDatabase.DBHelper;
import java.util.ArrayList;

public class ItemListActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private ListView listView;
    private ItemAdapter itemAdapter;
    ArrayList<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        dbHelper = new DBHelper(this);
        listView = findViewById(R.id.list_view); // Ensure you have a ListView with this ID in your activity_item_list.xml

        // Fetch all items from the database
        //ArrayList<Item> items = dbHelper.getAllItems();

        // Initialize items as an empty ArrayList
        items = new ArrayList<>();
        // Fetch all items from the database and add them to the list
        items.addAll(dbHelper.getAllItems());

        // Create an ItemAdapter and set it on the ListView
        itemAdapter = new ItemAdapter(this, items);
        listView.setAdapter(itemAdapter);


    }
}