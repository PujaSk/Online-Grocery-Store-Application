package com.example.grogrocery.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.grogrocery.R;
import com.example.grogrocery.SqliteDatabase.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewUsersActivity extends AppCompatActivity {

    ListView userListView;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        userListView = findViewById(R.id.userListView);
        dbHelper = new DBHelper(this);

        // Fetch the user data from the Reg_Master table
        ArrayList<HashMap<String, String>> userList = dbHelper.getAllUsers();

        // Display the data in ListView using a SimpleAdapter
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                userList,
                R.layout.user_list_item,
                new String[]{"cid","name", "email", "phno", "city"},
                new int[]{R.id.txtCid,R.id.txtName, R.id.txtEmail, R.id.txtPhone, R.id.txtCity}
        );

        userListView.setAdapter(adapter);
    }
}
